package com.showhidepasswordview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vramz on 10/13/16.
 */

/**
 * This is the reusable custom class to which will create EditText view with SHOW/HIDE link.
 * ShowHidePasswordView is the attribute array where MaxLength,imeOptions,SHOW/HIDE... will be set like
 *
 * app:showPassword="SHOW"
 * app:hidePassword="HIDE"
 * android:maxLength="10" were  android:maxLength will be present in ShowHidePasswordView array, because this is custom view we
 * need to declare android:maxLength in ShowHidePasswordView array and set it from the layout where you are using this view.
 *
 */

public class ShowHidePasswordView extends RelativeLayout {
    private boolean mIsShowingPassword;
    private boolean mEnabled;
    private boolean mShowButton;
    private int mImeOptions = -1;
    private int textMaxlength ;
    private int inputType ;
    private String showPasswordText;
    private String hidePasswordText;
    private EditText editText;
    private TextView showhidetextview;


    public ShowHidePasswordView(Context context) {
        super(context);
        init(null);
    }

    public ShowHidePasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public ShowHidePasswordView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    /**
     * Initialize EditText view with SHOW/HIDE link.
     * @param attrs like MaxLength,ImeOptions.. will be passed to he constructor from the layout to this view.
     */
    private void init(AttributeSet attrs) {

        //inflate layout
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.show_hide_password, this, true);

        //pass attributes to EditText, make clearable
        editText = (EditText) findViewById(R.id.showHidePasswordET);
        mEnabled = true;
        mShowButton = true;
        if (attrs != null){
            TypedArray attrsArray = getContext().obtainStyledAttributes(attrs,R.styleable.ShowHidePasswordView);

            mEnabled = attrsArray.getBoolean(R.styleable.ShowHidePasswordView_android_enabled , true);
            mShowButton = attrsArray.getBoolean(R.styleable.ShowHidePasswordView_showButton, true);
            mImeOptions = attrsArray.getInteger(R.styleable.ShowHidePasswordView_android_imeOptions, -1);
            textMaxlength = attrsArray.getInteger(R.styleable.ShowHidePasswordView_android_maxLength,0);
            inputType = attrsArray.getInteger(R.styleable.ShowHidePasswordView_android_inputType,0);
            showPasswordText = attrsArray.getString(R.styleable.ShowHidePasswordView_showPassword);
            hidePasswordText = attrsArray.getString(R.styleable.ShowHidePasswordView_hidePassword);
            attrsArray.recycle();

        }
        if (mEnabled) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (mShowButton) {
                        if(s.length() > 0){
                            showhidetextview.setVisibility(RelativeLayout.VISIBLE);
                            if(!mIsShowingPassword){
                                showhidetextview.setText(showPasswordText);
                            }else {
                                showhidetextview.setText(hidePasswordText);
                            }
                        }
                        else {
                            mIsShowingPassword = false;
                            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            showhidetextview.setVisibility(RelativeLayout.GONE);
                        }
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {}
            });
        } else {
            editText.setEnabled(false);
        }
        showhidetextview = (TextView)findViewById(R.id.show_hideTV);
        showhidetextview.setVisibility(RelativeLayout.GONE);
        showhidetextview.setOnTouchListener(mOnTouchListener);
        //To set MAX length for the EditText view
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(textMaxlength) });
        if (mImeOptions > -1) {
            editText.setImeOptions(mImeOptions);
        }
    }

    /**
     * Set OnClickListener, making EditText unfocusable
     * @param listener
     */
    @Override
    public void setOnClickListener(OnClickListener listener) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setOnClickListener(listener);
    }


    /**
     * If the user entered Password is hidden, then onclick of SHOW link will make the password will be show.
     */
    public void showPassword() {
        mIsShowingPassword = false;
        editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText.setSelection(editText.getText().length(),editText.getText().length());
        showhidetextview.setText(showPasswordText);
    }

    /**
     * If the user entered password visible to the user, then onclick of HIDE link password will not be visible.
     */
    public void hidePassword() {
        mIsShowingPassword = true;
        editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editText.setSelection(editText.getText().length(),editText.getText().length());
        showhidetextview.setText(hidePasswordText);

    }

    OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (mIsShowingPassword) {
                        showPassword();
                    } else {
                        hidePassword();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }

            return false;
        }
    };
}


