package customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditText_fira_sans_medium extends EditText {

    public EditText_fira_sans_medium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditText_fira_sans_medium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText_fira_sans_medium(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/FiraSans-Medium.otf");
            setTypeface(tf);
        }
    }

}