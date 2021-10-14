package com.technumopus.moneylisa.controller.blank;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.technumopus.moneylisa.MainActivity;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.CategoryBean;
import com.technumopus.moneylisa.model.UserBean;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlankFragment extends Fragment implements View.OnClickListener{

    private OnFragmentItemSelectedListener listener;

    EditText blank_et_email, blank_et_username, blank_et_password;
    Button blank_bt_signin, blank_bt_signup, blank_bt_skip, blank_bt_cancel_signup;
    UserBean userBean;
    DatabaseHelper databaseHelper;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_blank, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_blank);

//        NavigationView navigationView = (NavigationView) root.findViewById(R.id.nav_view);
//        DrawerLayout drawerLayout = root.findViewById(R.id.drawer_layout);
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Button btn = (Button)root.findViewById(R.id.blank_bt_signin);
        btn.setOnClickListener(this);

        blank_et_email = (EditText)root.findViewById(R.id.blank_et_email);
        blank_et_username = (EditText)root.findViewById(R.id.blank_et_username);
        blank_et_password = (EditText)root.findViewById(R.id.blank_et_password);
        blank_bt_signin = (Button)root.findViewById(R.id.blank_bt_signin);
        blank_bt_signin.setOnClickListener(this);
        blank_bt_signup = (Button)root.findViewById(R.id.blank_bt_signup);
        blank_bt_signup.setOnClickListener(this);
        blank_bt_skip = (Button)root.findViewById(R.id.blank_bt_skip);
        blank_bt_skip.setOnClickListener(this);
        blank_bt_cancel_signup = (Button)root.findViewById(R.id.blank_bt_cancel_signup);
        blank_bt_cancel_signup.setOnClickListener(this);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        float dpWidth  = outMetrics.widthPixels / density;
//        float pxWidth = outMetrics.widthPixels;
        int buttonsWidth = (int)(((dpWidth - 54)/3)*density);
        blank_bt_signin.setWidth(buttonsWidth);
        blank_bt_signup.setWidth(buttonsWidth);
        blank_bt_skip.setWidth(buttonsWidth);
        blank_bt_cancel_signup.setWidth(buttonsWidth);

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;

        }else {
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blank_bt_signin:
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                List<UserBean> userBeanList=databaseHelper.fetchAllUsers();
                boolean userFound = false;
                for (int i=0; i<userBeanList.size(); i++) {
                    if (userBeanList.get(i).get_user_email().toLowerCase().equals(blank_et_username.getText().toString().toLowerCase()) ||
                            userBeanList.get(i).get_user_name().toLowerCase().equals(blank_et_username.getText().toString().toLowerCase())) {
                        if (userBeanList.get(i).get_user_password().equals(blank_et_password.getText().toString())) {
                            userFound = true;
                            Toast.makeText( getContext(), "Logged in successfully", Toast.LENGTH_LONG ).show();
                            DatabaseHelper.CURRENT_USER = userBeanList.get(i).get_user_name();
                            SharedPreferences.Editor mEditor = MainActivity.mPrefs.edit();
                            mEditor.putString("user", DatabaseHelper.CURRENT_USER).commit();
                            listener.onButtonSelectedBlank(R.id.blank_bt_signin);
                            break;
                        }
                    }
                }
                if (userFound == false) {
                    Toast.makeText( getContext(), "Wrong credentials", Toast.LENGTH_LONG ).show();
                }
                break;
            case R.id.blank_bt_signup:
//                blank_bt_signin.getBackground().setColorFilter(getResources().getColor(R.color.light_grey), PorterDuff.Mode.MULTIPLY);
//                blank_bt_skip.getBackground().setColorFilter(getResources().getColor(R.color.light_grey), PorterDuff.Mode.MULTIPLY);
                if (blank_et_email.getVisibility() == EditText.VISIBLE) {
                    boolean validate = true;
                    // email validation
                    if (blank_et_email.getText().toString().trim().length() == 0) {
                        blank_et_email.setText("");
                        validate = false;
                    }
                    if (TextUtils.isEmpty(blank_et_email.getText())) {
                        blank_et_email.setHint("Required");
                        blank_et_email.setHintTextColor(getResources().getColor(R.color.indian_red));
                        validate = false;
                    }
                    if (blank_et_email.getText().length() > 50) {
                        blank_et_email.setText("");
                        blank_et_email.setHint("Max 50 characters");
                        blank_et_email.setHintTextColor(getResources().getColor(R.color.indian_red));
                        validate = false;
                    }

                    Pattern p = Pattern.compile("[^a-z0-9öÖÜß_@.-]", Pattern.CASE_INSENSITIVE);
                    Matcher m = p.matcher(blank_et_email.getText());
                    if (m.find()) {
                        blank_et_email.setHint("No special characters and spaces allowed");
                        blank_et_email.setHintTextColor(getResources().getColor(R.color.indian_red));
                        blank_et_email.setText("");
                        validate = false;
                    }
                    // password validation
                    if (blank_et_password.getText().toString().trim().length() == 0) {
                        blank_et_password.setText("");
                        validate = false;
                    }
                    if (TextUtils.isEmpty(blank_et_password.getText())) {
                        blank_et_password.setHint("Required");
                        blank_et_password.setHintTextColor(getResources().getColor(R.color.indian_red));
                        validate = false;
                    }
                    if (blank_et_password.getText().length() < 7) {
                        blank_et_password.setText("");
                        blank_et_password.setHint("Min 7 characters");
                        blank_et_password.setHintTextColor(getResources().getColor(R.color.indian_red));
                        validate = false;
                    }
                    // username validation
                    if (blank_et_username.getText().toString().trim().length() == 0) {
                        blank_et_username.setText("");
                        validate = false;
                    }
                    if (TextUtils.isEmpty(blank_et_username.getText())) {
                        blank_et_username.setHint("Required");
                        blank_et_username.setHintTextColor(getResources().getColor(R.color.indian_red));
                        validate = false;
                    }

                    if (blank_et_username.getText().length() > 20) {
                        blank_et_username.setText("");
                        blank_et_username.setHint("Max 50 characters");
                        blank_et_username.setHintTextColor(getResources().getColor(R.color.indian_red));
                        validate = false;
                    }

                    p = Pattern.compile("[^a-z0-9öÖÜß_/-]", Pattern.CASE_INSENSITIVE);
                    m = p.matcher(blank_et_username.getText());
                    if (m.find()) {
                        blank_et_username.setHint("No special characters and spaces allowed");
                        blank_et_username.setHintTextColor(getResources().getColor(R.color.indian_red));
                        blank_et_username.setText("");
                        validate = false;
                    }
                    // checking uniqueness
                    DatabaseHelper databaseHelper2 = new DatabaseHelper(getContext());
                    List<UserBean> userBeanList2=databaseHelper2.fetchAllUsers();
                    for (int i=0; i<userBeanList2.size(); i++) {
                        if (userBeanList2.get(i).get_user_email().toLowerCase().equals(blank_et_email.getText().toString().toLowerCase())) {
                            blank_et_email.setText("");
                            blank_et_email.setHint("Already exists");
                            blank_et_email.setHintTextColor(getResources().getColor(R.color.indian_red));
                            validate = false;
                        }
                        if (userBeanList2.get(i).get_user_name().toLowerCase().equals(blank_et_username.getText().toString().toLowerCase())) {
                            blank_et_username.setText("");
                            blank_et_username.setHint("Already exists");
                            blank_et_username.setHintTextColor(getResources().getColor(R.color.indian_red));
                            validate = false;
                        }
                    }
                    // validation condition
                    if (validate == true) {
                        userBean = new UserBean();
                        userBean.set_user_email(blank_et_email.getText().toString());
                        userBean.set_user_name(blank_et_username.getText().toString());
                        userBean.set_user_password(blank_et_password.getText().toString());
                        databaseHelper = new DatabaseHelper(getContext());
                        databaseHelper.insertUser(userBean);
                        Toast.makeText( getContext(), "Registered successfully", Toast.LENGTH_LONG ).show();
                        DatabaseHelper.CURRENT_USER = userBean.get_user_name();
                        SharedPreferences.Editor mEditor = MainActivity.mPrefs.edit();
                        mEditor.putString("user", DatabaseHelper.CURRENT_USER).commit();
                        listener.onButtonSelectedBlank(R.id.blank_bt_signup);
                    }
                } else {
                    blank_et_email.setVisibility(EditText.VISIBLE);
                    blank_bt_signin.setVisibility((Button.INVISIBLE));
                    blank_bt_skip.setVisibility((Button.INVISIBLE));
                    blank_bt_cancel_signup.setVisibility((Button.VISIBLE));
                    blank_et_username.setHint("Username");
                }
                break;
            case R.id.blank_bt_skip:
                DatabaseHelper.CURRENT_USER = "default";
                SharedPreferences.Editor mEditor = MainActivity.mPrefs.edit();
                mEditor.putString("user", DatabaseHelper.CURRENT_USER).commit();
                listener.onButtonSelectedBlank(R.id.blank_bt_skip);
                break;
            case R.id.blank_bt_cancel_signup:
                blank_et_email.setVisibility(EditText.INVISIBLE);
                blank_bt_signin.setVisibility((Button.VISIBLE));
                blank_bt_skip.setVisibility((Button.VISIBLE));
                blank_bt_cancel_signup.setVisibility((Button.INVISIBLE));
                blank_et_username.setHint("Username/Email");
                listener.onButtonSelectedBlank(R.id.blank_bt_cancel_signup);
                break;
            default:
                break;
        }
    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedBlank(int id);
    }

}
