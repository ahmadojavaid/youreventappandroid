package com.jobesk.yea.Utils;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.google.android.gms.common.util.Strings;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jobesk.yea.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalClass {


    private static GlobalClass singleton;

    private static boolean isLoading = false;
    private static CustomDialog dialog;

    private static String PREF_NAME = "DefaultPref";

    public static void showToast(Context context, String messageText) {

        Toast.makeText(context, messageText, Toast.LENGTH_LONG).show();

    }

    public static void darkenStatusBar(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(color);
        }

    }

    public static void ShowToast(Context context, String msg) {


        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getToken() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.d("TokenID", refreshedToken);
        return refreshedToken;
    }

    public static boolean isOnline(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void moreApps(Activity activity) {


//        Hi+Logix
//        Hi%20Logix
//        Uri uri = Uri.parse("market://search?q=JDeeStudio&c=apps");

        Uri uri = Uri.parse("market://search?q=pub:\"VitalApps\"");
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }

    }

//    public static void shareApp(Activity activity, String msg) {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
//
//        activity.startActivity(Intent.createChooser(shareIntent, "Share " + activity.getString(R.string.app_name)));
//    }
//
//    public static void rateApp(Activity activity) {
//        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
//        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//        try {
//            activity.startActivity(goToMarket);
//        } catch (ActivityNotFoundException e) {
//            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
//        }
//    }

//    public static String getToken(){
//
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//        Log.d("TokenID",refreshedToken);
//        return refreshedToken;
//    }

    public static boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // hides keyboard
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void putPref(String key, String value, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }


    public static String getPref(String key, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void clearPref(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }


    public static void showLoading(Activity activity) {
        if (!isLoading) {
            isLoading = true;
            dialog = new CustomDialog(activity);
            dialog.setContentView(R.layout.custom_dialog_loading);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    public static void dismissLoading() {
        try {
            if (isLoading) {
                isLoading = false;
                dialog.dismiss();
            }
        } catch (Exception e) {
            isLoading = true;
        }
    }


    // Converting File to Base64.encode String type using Method
    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile = "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[50240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile = output.toString();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    public static String parseDate(String timedate, Context context) {
        TimeSince timeSince = new TimeSince();

        String result = "";
        try {
            DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = readFormat.parse(timedate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = "";
            if (date != null) {
                formattedDate = writeFormat.format(date);
            }
            Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(formattedDate);
            long timeInMilliSeconds = startDate.getTime();
            timeInMilliSeconds = timeInMilliSeconds + (3600000 * 5);
            result = timeSince.getTimeAgo(timeInMilliSeconds, context);

        } catch (
                ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";


        try {
            if (dateString.equalsIgnoreCase("")) {

                return result;

            }


            SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
            SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
            Date date = null;
            try {
                date = formatterOld.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                result = formatterNew.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();

            result = "";
        }

        return result;
    }


    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

}