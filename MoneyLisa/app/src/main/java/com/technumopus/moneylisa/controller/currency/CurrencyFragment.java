package com.technumopus.moneylisa.controller.currency;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private OnFragmentItemSelectedListener listener;
    DatabaseHelper dao;
    private Spinner currSpinner;
    private EditText currCustom;
    private Button currButtonSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_currency, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_currency);

        currSpinner = (Spinner) root.findViewById(R.id.sp_currency_def);
        currSpinner.setOnItemSelectedListener(this);
        currCustom = (EditText) root.findViewById(R.id.et_currency_def);
        currButtonSave = (Button) root.findViewById(R.id.bt_currency_def_save);
        currButtonSave.setOnClickListener(this);

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
            case R.id.bt_currency_def_save:
                boolean validate = true;
                if (currCustom.getText().toString().trim().length() != 3) {
                    currCustom.setText("");
                    currCustom.setHint("Enter 3 Alphabetic Letters");
                    currCustom.setHintTextColor(getResources().getColor(R.color.indian_red));
                    validate = false;
                }
                Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(currCustom.getText());
                if (m.find()) {
                    currCustom.setHint("Enter 3 Alphabetic Letters");
                    currCustom.setHintTextColor(getResources().getColor(R.color.indian_red));
                    currCustom.setText("");
                    validate = false;
                }
                if (validate == true) {

                    Toast.makeText( getContext(), "Default Currency Set", Toast.LENGTH_SHORT ).show();
                    listener.onButtonSelectedCurrency(R.id.bt_currency_def_save);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currCustom.setText(currSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedCurrency(int id);
    }

}
