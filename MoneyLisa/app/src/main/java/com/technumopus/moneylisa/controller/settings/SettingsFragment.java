package com.technumopus.moneylisa.controller.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technumopus.moneylisa.MainActivity;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.UserBean;

import java.util.Arrays;
import java.util.List;

public class SettingsFragment extends Fragment implements View.OnClickListener{

    private OnFragmentItemSelectedListener listener;
    private EditText threshold;
    private Spinner default_currency;
    private Button buttonSave;
    DatabaseHelper databaseHelper;

//    public static String CURRENT_USER_THRESHOLD="YES";
//    public static float CURRENT_USER_DEFAULT_CURRENCY="YES";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_settings);

        threshold = (EditText) root.findViewById(R.id.settings_et_threshold);
        default_currency = (Spinner) root.findViewById(R.id.settings_sp_default_currency);
        buttonSave = (Button) root.findViewById(R.id.settings_bt_save);
        buttonSave.setOnClickListener(this);

        databaseHelper = new DatabaseHelper(getContext());
        UserBean currentUserBean = new UserBean();
        List<UserBean> userBeanList=databaseHelper.fetchAllUsers();
        for (int i=0; i<userBeanList.size(); i++) {
            if (userBeanList.get(i).get_user_name().toLowerCase().equals(DatabaseHelper.CURRENT_USER.toLowerCase())) {
                currentUserBean = userBeanList.get(i);
            }
        }
        threshold.setText(Float.toString(currentUserBean.get_user_threshold()));
        String[] resArray;
        resArray = getResources().getStringArray(R.array.Currencies);
        default_currency.setSelection(Arrays.asList(resArray).indexOf(currentUserBean.get_default_currency()));

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
            case R.id.settings_bt_save:
                databaseHelper = new DatabaseHelper(getContext());
                databaseHelper.insertUserThreshold(Float.parseFloat(threshold.getText().toString()));
                databaseHelper.insertUserDefaultCurrency(default_currency.getSelectedItem().toString());
                Toast.makeText( getContext(), "Settings saved successfully", Toast.LENGTH_SHORT ).show();
                listener.onButtonSelectedSettings(R.id.settings_bt_save);
                break;
            default:
                break;
        }
    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedSettings(int id);
    }

}
