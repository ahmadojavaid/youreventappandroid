package com.jobesk.yea.SponsorArea.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jobesk.yea.AttendeArea.Adapter.AdapterSpinnerTitles;
import com.jobesk.yea.AttendeArea.EventBuses.AttendeeEventBus;
import com.jobesk.yea.AttendeArea.EventBuses.EventBusOpenFilter;
import com.jobesk.yea.AttendeArea.Models.AttendeModel;
import com.jobesk.yea.AttendeArea.Models.SpeakersModel;
import com.jobesk.yea.AttendeArea.Models.SpinnerTitleModel;
import com.jobesk.yea.R;
import com.jobesk.yea.SponsorArea.Adapters.SAttendeesAdapter;
import com.jobesk.yea.SponsorArea.DrawerActivitySponsor;
import com.jobesk.yea.Utils.GlobalClass;
import com.jobesk.yea.Utils.Urls;
import com.jobesk.yea.Utils.WebReq;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cz.msebera.android.httpclient.Header;

public class FragSAttendees extends Fragment implements View.OnClickListener {

    private TextView toolbar_title_tv;
    private SAttendeesAdapter mAdapter;
    private RecyclerView recyclerView;
    private Activity activity;
    private ArrayList<AttendeModel> arrayList = new ArrayList<>();
    private ArrayList<AttendeModel> arrayListTemp = new ArrayList<>();
    private ImageView meun_img;
    private String TAG = "FragSAttendees";
    private String userIDCurrent;
    private String searchKeyWord = "";
    private AttendeModel model;
    private int statusBarColor_int, appMainColor_int, btnColor_int;
    private LinearLayout toolbar_header;
    private ImageView logo_toolbar;
    //    private TextView done_tv;
    private LinearLayout filter_container;
    private ImageView closeFilter_img, filter_img;
    private int statusDrawerOpen = 0;
    private TextView filter_by_f_name, filter_by_sur_name, filter_by_job_title, filter_by_company;
    private RelativeLayout search_container;
    private String searchBy = "";
    private EditText search_et;
    private String keywordSearch;
    private ImageView image_f_name, image_surname, image_job_title, image_company;
    private int imageStatus_first_name = 0;
    private int imageStatus_sur_name = 0;
    private int imageStatus_job_name = 0;
    private int imageStatus_company_name = 0;
    private Spinner spinner_job_titles, spinner;
    private ArrayList<SpinnerTitleModel> companyList = new ArrayList<>();
    private ArrayList<SpinnerTitleModel> titlesList = new ArrayList<>();
    private TextView apply_filter;
    private ArrayList<AttendeModel> searchList;
    private String searchStatus = "0";
    private ArrayList<AttendeModel> contactListFiltered;
    private TextView clear_filter;
    private AdapterSpinnerTitles myAdapterCompany, myAdapterTitlesSpinner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_a_attendees, container, false);


        activity = (DrawerActivitySponsor) rootView.getContext();

        userIDCurrent = GlobalClass.getPref("userID", activity);
        meun_img = rootView.findViewById(R.id.meun_img);
        meun_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerActivitySponsor.openDrawer();

            }
        });

        toolbar_title_tv = rootView.findViewById(R.id.toolbar_title_tv);
        toolbar_title_tv.setText(getActivity().getResources().getString(R.string.attendees));


        clear_filter = rootView.findViewById(R.id.clear_filter);
        clear_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                image_f_name.setBackgroundResource(0);
                image_surname.setBackgroundResource(0);
                image_job_title.setBackgroundResource(0);
                image_company.setBackgroundResource(0);

                imageStatus_first_name = 0;
                imageStatus_sur_name = 0;
                imageStatus_job_name = 0;
                imageStatus_company_name = 0;
                searchStatus = "0";


                for (int i = 0; i < companyList.size(); i++) {


                    if (i == 0) {


                        companyList.get(0).setChecked("0");
                        companyList.get(0).setName(getActivity().getResources().getString(R.string.company_name));
                        companyList.get(0).setSelectedText(getActivity().getResources().getString(R.string.company_name));
                        companyList.get(0).setId("000000");
                        companyList.get(0).setChecked("0");


                        titlesList.get(0).setChecked("0");
                        titlesList.get(0).setName(getActivity().getResources().getString(R.string.job_title));
                        titlesList.get(0).setSelectedText(getActivity().getResources().getString(R.string.job_title));
                        titlesList.get(0).setId("000000");
                        titlesList.get(0).setChecked("0");

                    }


                    companyList.get(i).setChecked("0");
                    titlesList.get(i).setChecked("0");


                }


                myAdapterCompany.notifyDataSetChanged();
                myAdapterTitlesSpinner.notifyDataSetChanged();


                arrayList = new ArrayList<AttendeModel>(arrayListTemp);
                mAdapter = new SAttendeesAdapter(activity, arrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);


//                filter_container.setVisibility(View.GONE);
//                statusDrawerOpen = 0;


            }
        });


        filter_container = rootView.findViewById(R.id.filter_container);
        filter_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        spinner = (Spinner) rootView.findViewById(R.id.spinner_titles);
        spinner_job_titles = (Spinner) rootView.findViewById(R.id.spinner_job_titles);

        filter_by_f_name = rootView.findViewById(R.id.filter_by_f_name);
        filter_by_sur_name = rootView.findViewById(R.id.filter_by_sur_name);
        filter_by_job_title = rootView.findViewById(R.id.filter_by_job_title);
        filter_by_company = rootView.findViewById(R.id.filter_by_company);

        filter_by_f_name.setOnClickListener(this);
        filter_by_sur_name.setOnClickListener(this);
        filter_by_job_title.setOnClickListener(this);
        filter_by_company.setOnClickListener(this);


        filter_img = rootView.findViewById(R.id.filter_img);
        filter_img.setOnClickListener(this);


        closeFilter_img = rootView.findViewById(R.id.closeFilter_img);
        closeFilter_img.setOnClickListener(this);


        apply_filter = rootView.findViewById(R.id.apply_filter);
        apply_filter.setOnClickListener(this);


        image_f_name = rootView.findViewById(R.id.image_f_name);
        image_surname = rootView.findViewById(R.id.image_surname);
        image_job_title = rootView.findViewById(R.id.image_job_title);
        image_company = rootView.findViewById(R.id.image_company);


        search_container = rootView.findViewById(R.id.search_container);
        search_et = rootView.findViewById(R.id.search_et);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mAdapter = new SAttendeesAdapter(activity, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        String statusBarColor = GlobalClass.getPref("statusBarColor", getActivity());
        String appMainColor = GlobalClass.getPref("appMainColor", getActivity());
        String btnColor = GlobalClass.getPref("btnColor", getActivity());
        String appLogo = GlobalClass.getPref("appLogo", getActivity());


        logo_toolbar = rootView.findViewById(R.id.logo_toolbar);
        toolbar_header = rootView.findViewById(R.id.toolbar_header);
        if (!appMainColor.equalsIgnoreCase("")) {

            statusBarColor_int = Color.parseColor(statusBarColor);
            appMainColor_int = Color.parseColor(appMainColor);
            btnColor_int = Color.parseColor(btnColor);

            meun_img.setColorFilter(btnColor_int);
            closeFilter_img.setColorFilter(appMainColor_int);


            apply_filter.setBackgroundColor(appMainColor_int);
            clear_filter.setBackgroundColor(appMainColor_int);

            toolbar_header.setBackgroundResource(0);
            toolbar_header.setBackgroundColor(appMainColor_int);


            String imageLogoPath = Urls.BASE_URL_IMAGE + appLogo;

            Picasso.with(activity)
                    .load(imageLogoPath)
                    .fit().centerInside()
                    .into(logo_toolbar);


            search_container.setBackgroundColor(btnColor_int);


        }


        search_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {


                    keywordSearch = search_et.getText().toString().trim();
                    if (keywordSearch.equalsIgnoreCase("")) {

                        Toast.makeText(activity, activity.getApplicationContext().getResources().getString(R.string.enter_search_title), Toast.LENGTH_SHORT).show();


                    } else {


                        filter(keywordSearch);


                    }


                    return true;
                }
                return false;
            }
        });
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.length() == 0) {
                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                } else {


                    filter(s.toString().trim());
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        getAttendees();

        return rootView;
    }


    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<AttendeModel> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (AttendeModel s : arrayList) {

            String value = s.getName().toLowerCase() + " " + s.getSurName().toLowerCase().trim();

            if (value.contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
//        mAdapter.filterList(filterdNames);

        if (filterdNames.size() == 0) {
            Toast.makeText(activity, getActivity().getResources().getString(R.string.no_attendees_found_with_this_name), Toast.LENGTH_SHORT).show();
        }

        mAdapter = new SAttendeesAdapter(activity, filterdNames);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }

    private void makeSpinnerCompany() {

        myAdapterCompany = new AdapterSpinnerTitles(activity, companyList);
        spinner.setAdapter(myAdapterCompany);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                myAdapterCompany.notifyDataSetChanged();

                spinner.setPrompt("Select an item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("noting", "asdf");
            }


        });

    }

    private void makeSpinnerTitles() {

        myAdapterTitlesSpinner = new AdapterSpinnerTitles(activity, titlesList);
        spinner_job_titles.setAdapter(myAdapterTitlesSpinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                myAdapterTitlesSpinner.notifyDataSetChanged();

                spinner.setPrompt("Select an item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AttendeeEventBus event) {
        searchKeyWord = event.getKeyword();

        Log.d("keywordTitleSearch", searchKeyWord);


        getAttendees();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusOpenFilter event) {


        String statusDrawerOpen = event.getStatus();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.filter_img:

                GlobalClass.hideKeyboard(activity);

                if (statusDrawerOpen == 0) {

                    filter_container.setVisibility(View.VISIBLE);
                    statusDrawerOpen = 1;

                } else {
                    filter_container.setVisibility(View.GONE);
                    statusDrawerOpen = 0;
                }


//                image_f_name.setBackgroundResource(0);
//                image_surname.setBackgroundResource(0);
//                image_job_title.setBackgroundResource(0);
//                image_company.setBackgroundResource(0);
//
//                imageStatus_first_name = 0;
//                imageStatus_sur_name = 0;
//                imageStatus_job_name = 0;
//                imageStatus_company_name = 0;


                break;

            case R.id.closeFilter_img:

                GlobalClass.hideKeyboard(activity);

                filter_container.setVisibility(View.GONE);
                statusDrawerOpen = 0;


                break;


            //Filters searchs
            case R.id.filter_by_f_name:


                if (imageStatus_first_name == 0) {

                    searchStatus = "1";


                    image_f_name.setBackgroundResource(R.drawable.ic_asending);
                    image_surname.setBackgroundResource(0);
                    image_job_title.setBackgroundResource(0);
                    image_company.setBackgroundResource(0);

                    imageStatus_first_name = 1;
                    imageStatus_sur_name = 0;
                    imageStatus_job_name = 0;
                    imageStatus_company_name = 0;

                } else if (imageStatus_first_name == 1) {

                    searchStatus = "2";

                    image_f_name.setBackgroundResource(R.drawable.ic_decending);
                    image_surname.setBackgroundResource(0);
                    image_job_title.setBackgroundResource(0);
                    image_company.setBackgroundResource(0);

                    imageStatus_first_name = 0;
                    imageStatus_sur_name = 0;
                    imageStatus_job_name = 0;
                    imageStatus_company_name = 0;


                }


                searchBy = "firstname";


                break;
            case R.id.filter_by_sur_name:


                if (imageStatus_sur_name == 0) {

                    searchStatus = "3";

                    image_f_name.setBackgroundResource(0);
                    image_surname.setBackgroundResource(R.drawable.ic_asending);
                    image_job_title.setBackgroundResource(0);
                    image_company.setBackgroundResource(0);

                    imageStatus_first_name = 0;
                    imageStatus_sur_name = 1;
                    imageStatus_job_name = 0;
                    imageStatus_company_name = 0;

                } else if (imageStatus_sur_name == 1) {

                    searchStatus = "4";

                    image_f_name.setBackgroundResource(0);
                    image_surname.setBackgroundResource(R.drawable.ic_decending);
                    image_job_title.setBackgroundResource(0);
                    image_company.setBackgroundResource(0);

                    imageStatus_first_name = 0;
                    imageStatus_sur_name = 0;
                    imageStatus_job_name = 0;
                    imageStatus_company_name = 0;


                }


                searchBy = "surname";

                break;
            case R.id.filter_by_job_title:


                if (imageStatus_job_name == 0) {

                    searchStatus = "5";

                    image_f_name.setBackgroundResource(0);
                    image_surname.setBackgroundResource(0);
                    image_job_title.setBackgroundResource(R.drawable.ic_asending);
                    image_company.setBackgroundResource(0);


                    imageStatus_first_name = 0;
                    imageStatus_sur_name = 0;
                    imageStatus_job_name = 1;
                    imageStatus_company_name = 0;

                } else if (imageStatus_job_name == 1) {

                    searchStatus = "6";


                    image_f_name.setBackgroundResource(0);
                    image_surname.setBackgroundResource(0);
                    image_job_title.setBackgroundResource(R.drawable.ic_decending);
                    image_company.setBackgroundResource(0);

                    imageStatus_first_name = 0;
                    imageStatus_sur_name = 0;
                    imageStatus_job_name = 0;
                    imageStatus_company_name = 0;


                }


                searchBy = "jobTitle";

                break;
            case R.id.filter_by_company:


                if (imageStatus_company_name == 0) {

                    searchStatus = "7";

                    image_f_name.setBackgroundResource(0);
                    image_surname.setBackgroundResource(0);
                    image_job_title.setBackgroundResource(0);
                    image_company.setBackgroundResource(R.drawable.ic_asending);

                    imageStatus_first_name = 0;
                    imageStatus_sur_name = 0;
                    imageStatus_job_name = 0;
                    imageStatus_company_name = 1;

                } else if (imageStatus_company_name == 1) {

                    searchStatus = "8";

                    image_f_name.setBackgroundResource(0);
                    image_surname.setBackgroundResource(0);
                    image_job_title.setBackgroundResource(0);
                    image_company.setBackgroundResource(R.drawable.ic_decending);


                    imageStatus_first_name = 0;
                    imageStatus_sur_name = 0;
                    imageStatus_job_name = 0;
                    imageStatus_company_name = 0;

                }


                searchBy = "company";

                break;


            case R.id.apply_filter:


                arrayList = new ArrayList<AttendeModel>(arrayListTemp);


                if (searchStatus.equalsIgnoreCase("0")) {


                }


                remaingTwoChecks();


                if (searchStatus.equalsIgnoreCase("1")) {
                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);

                }
                if (searchStatus.equalsIgnoreCase("2")) {

                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o2.getName().compareToIgnoreCase(o1.getName());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }


                //for Surname

                if (searchStatus.equalsIgnoreCase("3")) {

                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o1.getSurName().compareToIgnoreCase(o2.getSurName());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }


                if (searchStatus.equalsIgnoreCase("4")) {

                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o2.getSurName().compareToIgnoreCase(o1.getSurName());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }


                // for job title

                if (searchStatus.equalsIgnoreCase("5")) {

                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o1.getJobTitle().compareToIgnoreCase(o2.getJobTitle());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }


                if (searchStatus.equalsIgnoreCase("6")) {

                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o2.getJobTitle().compareToIgnoreCase(o1.getJobTitle());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }

                //For company name

                if (searchStatus.equalsIgnoreCase("7")) {

                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o1.getCompanyName().compareToIgnoreCase(o2.getCompanyName());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }


                if (searchStatus.equalsIgnoreCase("8")) {

                    Collections.sort(arrayList, new Comparator<AttendeModel>() {
                        @Override
                        public int compare(AttendeModel o1, AttendeModel o2) {


                            return o2.getCompanyName().compareToIgnoreCase(o1.getCompanyName());
                        }

                        @Override
                        public boolean equals(Object obj) {
                            return false;
                        }
                    });


                    mAdapter = new SAttendeesAdapter(activity, arrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                }


                break;


        }
    }

    private void remaingTwoChecks() {
        int checkedTitle = 0;
        searchList = new ArrayList<AttendeModel>();
        for (int i = 0; i < titlesList.size(); i++) {


            String checked = titlesList.get(i).getChecked();
            if (checked.equalsIgnoreCase("1")) {
                checkedTitle = 1;
                Log.d("checkedTitles", titlesList.get(i).getName());

                final String checkedName = titlesList.get(i).getName();


                for (int j = 0; j < arrayList.size(); j++) {
                    if (arrayList.get(j).getJobTitle().contains(checkedName)) {


                        AttendeModel model = arrayList.get(j);
                        searchList.add(model);


                    }
                }


            }


        }


        if (checkedTitle == 1) {


            if (arrayList.size() > 0) {
                arrayList.clear();

            }
            mAdapter.notifyDataSetChanged();
            arrayList = new ArrayList<AttendeModel>(searchList);


            mAdapter = new SAttendeesAdapter(activity, arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);


        }


        int checkedTitle2 = 0;
        searchList = new ArrayList<AttendeModel>();
        for (int i = 0; i < companyList.size(); i++) {


            String checked = companyList.get(i).getChecked();
            if (checked.equalsIgnoreCase("1")) {
                checkedTitle2 = 1;
                Log.d("checkedCompanies", companyList.get(i).getName());

                final String checkedName = companyList.get(i).getName();


                for (int j = 0; j < arrayList.size(); j++) {
                    if (arrayList.get(j).getCompanyName().contains(checkedName)) {


                        AttendeModel model = arrayList.get(j);
                        searchList.add(model);


                    }
                }


            }

        }
        if (checkedTitle2 == 1) {


            if (arrayList.size() > 0) {
                arrayList.clear();

            }
            mAdapter.notifyDataSetChanged();
            arrayList = new ArrayList<AttendeModel>(searchList);


            mAdapter = new SAttendeesAdapter(activity, arrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);


        }


        mAdapter = new SAttendeesAdapter(activity, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        filter_container.setVisibility(View.GONE);
        statusDrawerOpen = 0;
    }


    private void SearchingFilters(String searchKey) {

        ArrayList<AttendeModel> myList;
        switch (searchKey) {

            case "firstname":
                myList = new ArrayList<AttendeModel>();
                for (AttendeModel myPoint : myList) {
                    if (myPoint.getName() != null && myPoint.getName().contains(keywordSearch)) {
                        //Process data do whatever you want
                        System.out.println("Found it!");
                    }
                }

                break;
            case "surname":

                myList = new ArrayList<AttendeModel>();
                for (AttendeModel myPoint : myList) {
                    if (myPoint.getName() != null && myPoint.getName().contains(keywordSearch)) {
                        //Process data do whatever you want
                        System.out.println("Found it!");
                    }
                }

                break;
            case "jobTitle":

                myList = new ArrayList<AttendeModel>();
                for (AttendeModel myPoint : myList) {
                    if (myPoint.getName() != null && myPoint.getName().contains(keywordSearch)) {
                        //Process data do whatever you want
                        System.out.println("Found it!");
                    }
                }

                break;
            case "company":


                myList = new ArrayList<AttendeModel>();
                for (AttendeModel myPoint : myList) {
                    if (myPoint.getName() != null && myPoint.getName().contains(keywordSearch)) {
                        //Process data do whatever you want
                        System.out.println("Found it!");
                    }
                }
                break;


        }


        if (arrayListTemp.size() > 0) {

            arrayListTemp.clear();
        }
        mAdapter.notifyDataSetChanged();


    }


    private void getAttendees() {

        if (GlobalClass.isOnline(getActivity()) == true) {


            if (searchKeyWord.equalsIgnoreCase("")) {
                RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);
                String userID = GlobalClass.getPref("userID", activity);

                WebReq.get(getActivity(), "attendees?userId=" + userID, mParams, new MyTextHttpResponseHandler());

            } else {
                RequestParams mParams = new RequestParams();
//            mParams.put("postId", email);
                String userID = GlobalClass.getPref("userID", activity);
                WebReq.get(getActivity(), "searchAttendee?attendeeName=" + searchKeyWord + "&userId=" + userID, mParams, new MyTextHttpResponseHandler());
            }


        } else {
            Toast.makeText(activity, getActivity().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }


    }

    private class MyTextHttpResponseHandler extends JsonHttpResponseHandler {
        MyTextHttpResponseHandler() {
        }

        @Override
        public void onStart() {
            super.onStart();

            GlobalClass.showLoading(activity);
            Log.d(TAG, "onStart");

        }

        @Override
        public void onFinish() {
            super.onFinish();

            GlobalClass.dismissLoading();
            Log.d(TAG, "onFinish");

        }

        @Override
        public void onFailure(int mStatusCode, Header[] headers, Throwable mThrow, JSONObject e) {

            Log.d(TAG, "OnFailure" + e);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);

            Log.d(TAG, responseString);
            GlobalClass.dismissLoading();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, final JSONObject mResponse) {

            GlobalClass.dismissLoading();
            Log.d(TAG, mResponse.toString() + "Respo");
            if (mResponse != null && mResponse.length() != 0) {
                try {
                    String status = mResponse.getString("statusCode");

                    if (status.equals("1")) {


                        if (arrayList.size() > 0) {

                            arrayList.clear();
                        }
                        if (arrayListTemp.size() > 0) {

                            arrayListTemp.clear();
                        }


                        mAdapter.notifyDataSetChanged();


//                        model = new AttendeModel();
//                        model.setType(SpeakersModel.TYEPE_SEARCH);
//                        model.setIsFollow("0");
//                        arrayList.add(model);

                        JSONArray jsonArray = mResponse.getJSONArray("Result");

                        if (jsonArray.length() > 0) {

                        } else {


                            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.no_attendees_found), Toast.LENGTH_SHORT).show();
                            return;

                        }


                        SpinnerTitleModel spinerModel = new SpinnerTitleModel();
                        spinerModel.setName(getActivity().getResources().getString(R.string.company_name));
                        spinerModel.setSelectedText(getActivity().getResources().getString(R.string.company_name));
                        spinerModel.setId("000000");
                        spinerModel.setChecked("0");
                        companyList.add(spinerModel);


                        SpinnerTitleModel spinerMode2 = new SpinnerTitleModel();
                        spinerMode2.setName(getActivity().getResources().getString(R.string.job_title));
                        spinerMode2.setSelectedText(getActivity().getResources().getString(R.string.job_title));
                        spinerMode2.setId("000000");
                        spinerMode2.setChecked("0");
                        titlesList.add(spinerMode2);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String name = jsonObject.getString("name");
                            String image = jsonObject.getString("profileImage");
                            String email = jsonObject.getString("email");
                            String ID = jsonObject.getString("id");
                            String companyName = jsonObject.getString("companyName");
                            String jobTitle = jsonObject.getString("jobTitle");
                            String id = jsonObject.getString("id");
                            String isfollowed = jsonObject.getString("isfollowed");
//                            String Surname = jsonObject.getString("Surname");


                            AttendeModel model = new AttendeModel();
                            model.setName(name);
                            model.setImage(image);
                            model.setEmail(email);
                            model.setID(ID);
                            model.setCompanyName(companyName);
                            model.setJobTitle(jobTitle);
                            model.setType(SpeakersModel.TYPE_USER);
                            model.setID(id);
                            model.setIsFollow(isfollowed);
//                            model.setSurName(Surname);
                            if (!ID.equalsIgnoreCase(userIDCurrent)) {
                                arrayList.add(model);
                                arrayListTemp.add(model);

                            }

                            SpinnerTitleModel spinnerModel = new SpinnerTitleModel();
                            spinnerModel.setName(companyName);
                            spinnerModel.setId(id);
                            spinnerModel.setChecked("0");
                            companyList.add(spinnerModel);


                            SpinnerTitleModel spinnerModel2 = new SpinnerTitleModel();
                            spinnerModel2.setName(jobTitle);
                            spinnerModel2.setId(id);
                            spinnerModel2.setChecked("0");
                            titlesList.add(spinnerModel2);

                        }


                        makeSpinnerCompany();
                        makeSpinnerTitles();


                        mAdapter.notifyDataSetChanged();

                    } else {
                        String message = mResponse.getString("statusMessage");
                        Toast.makeText(activity, "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}
