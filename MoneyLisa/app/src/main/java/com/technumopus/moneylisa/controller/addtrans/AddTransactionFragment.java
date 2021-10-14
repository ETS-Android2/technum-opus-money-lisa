package com.technumopus.moneylisa.controller.addtrans;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.technumopus.moneylisa.MainActivity;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.model.TransactionBean;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTransactionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public String CALL_FOR = "ADD";
    private int tid = -100;

    private OnFragmentItemSelectedListener listener;
    DatabaseHelper dao;
    EditText bean_addtrans_et_title, bean_addtrans_et_details, bean_addtrans_et_amount, bean_addtrans_et_date, bean_addtrans_et_recdate, bean_addtrans_et_recnotes, bean_addtrans_et_borrowed;
    Spinner bean_addtrans_sp_currency, bean_addtrans_sp_cat, bean_addtrans_sp_mop, bean_addtrans_sp_recfreq;
    ImageView bean_addtrans_iv_recdropimg;
    boolean bean_addtrans_bl_recbool;

    TextView recDate;
    Spinner recFreq;
    TextView recNotes;
    ImageView recDropImg;
    Boolean recVisible = Boolean.FALSE;

    EditText datePicker, datePickerRec;
    Calendar myCalendar, myCalendarRec;
    DatePickerDialog.OnDateSetListener date, dateRec;

    TransactionBean transactionBean2;

    String text_addtrans_et_title;
    String text_addtrans_et_details;
    String text_addtrans_et_amount;
    String text_addtrans_sp_currency;
    String text_addtrans_sp_cat;
    String text_addtrans_sp_mop;
    String text_addtrans_et_date;
    String text_addtrans_sp_recfreq;
    String text_addtrans_et_recdate;
    String text_addtrans_et_recnotes;
    String text_addtrans_et_borrowed;
    public boolean text_addtrans_bl_recbool = false;



    Button delButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_addtrans, container, false);
        recDate = (TextView)root.findViewById(R.id.addtrans_et_recdate);
        recFreq = (Spinner)root.findViewById(R.id.addtrans_sp_recfreq);
        recNotes = (TextView)root.findViewById(R.id.addtrans_et_recnotes);
        recDropImg = (ImageView) root.findViewById(R.id.rec_drop_img);
        recDate.setVisibility(TextView.INVISIBLE);
        recFreq.setVisibility(TextView.INVISIBLE);
        recNotes.setVisibility(TextView.INVISIBLE);
        recDropImg.setVisibility(TextView.INVISIBLE);

        initView(root);

        bean_addtrans_et_title =(EditText)root.findViewById( R.id.addtrans_et_title);
        bean_addtrans_et_details =(EditText)root.findViewById( R.id.addtrans_et_details);
        bean_addtrans_et_amount =(EditText)root.findViewById( R.id.addtrans_et_amount);
        bean_addtrans_sp_currency =(Spinner)root.findViewById( R.id.addtrans_sp_currency);
        bean_addtrans_sp_cat =(Spinner)root.findViewById( R.id.addtrans_sp_cat);
        bean_addtrans_sp_mop =(Spinner)root.findViewById( R.id.addtrans_sp_mop);
        bean_addtrans_et_date =(EditText)root.findViewById( R.id.addtrans_et_date);
        bean_addtrans_sp_recfreq =(Spinner)root.findViewById( R.id.addtrans_sp_recfreq);
        bean_addtrans_et_recdate =(EditText)root.findViewById( R.id.addtrans_et_recdate);
        bean_addtrans_et_recnotes = (EditText)root.findViewById( R.id.addtrans_et_recnotes);
        bean_addtrans_et_borrowed = (EditText)root.findViewById( R.id.addtrans_et_borrowed);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_addtrans);

        delButton = (Button)getActivity().findViewById( R.id.toolbar_del_button);
        if (CALL_FOR.equals("EDIT")){ // "ADD"
            delButton.setVisibility(Button.VISIBLE);
            delButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0);
            toolbar.setTitle("Edit Expense");
            delButton.setOnClickListener(this);
            CALL_FOR.equals("ADD");
        }

        Button btn = (Button)root.findViewById(R.id.addtrans_bt_editcats);

        loadSpinnerData("");
//        System.out.println("=========================== checkitout");
//        System.out.println(bean_addtrans_sp_cat.getSelectedItem().toString());
        btn.setOnClickListener(this);



        // Date Picker - Start

        myCalendar = Calendar.getInstance();

        datePicker= (EditText) root.findViewById(R.id.addtrans_et_date);
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datePicker.setOnClickListener(new EditText.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Date Picker - End

        // Date Picker - Start

        myCalendarRec = Calendar.getInstance();

        datePickerRec= (EditText) root.findViewById(R.id.addtrans_et_recdate);
        dateRec = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendarRec.set(Calendar.YEAR, year);
                myCalendarRec.set(Calendar.MONTH, monthOfYear);
                myCalendarRec.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelRec();
            }

        };

        datePickerRec.setOnClickListener(new EditText.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), dateRec, myCalendarRec
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Date Picker - End

        bean_addtrans_et_borrowed = (EditText) root.findViewById(R.id.addtrans_et_borrowed);
        bean_addtrans_et_borrowed.setOnClickListener(this);


        System.out.println(getCurrencyFactor("USD",MainActivity.CURRENT_USER_BEAN.get_default_currency()));

        dao = new DatabaseHelper(getContext());

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) root.findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addtrans_mi_recpop:
                                if (recDate.getVisibility() == TextView.VISIBLE) {
                                    recDate.setVisibility(TextView.INVISIBLE);
                                    recFreq.setVisibility(TextView.INVISIBLE);
                                    recNotes.setVisibility(TextView.INVISIBLE);
                                    recDropImg.setVisibility(TextView.INVISIBLE);
                                }
                                else {
                                    recDate.setVisibility(TextView.VISIBLE);
                                    recFreq.setVisibility(TextView.VISIBLE);
                                    recNotes.setVisibility(TextView.VISIBLE);
                                    recDropImg.setVisibility(TextView.VISIBLE);
                                }
                                return true;
                            case R.id.addtrans_mi_cancel:
                                delButton.setVisibility(Button.INVISIBLE);
                                listener.onButtonSelectedAddtrans(R.id.addtrans_mi_cancel, CALL_FOR);
                                return true;
                            case R.id.addtrans_mi_save:
                                boolean validate = true;
                                if (bean_addtrans_et_title.getText().toString().trim().length() == 0) {
                                    bean_addtrans_et_title.setText("");
                                    validate = false;
                                }
                                if (TextUtils.isEmpty(bean_addtrans_et_title.getText())) {
                                    bean_addtrans_et_title.setHint("Title is required");
                                    bean_addtrans_et_title.setHintTextColor(getResources().getColor(R.color.indian_red));
                                    validate = false;
                                }
                                if (TextUtils.isEmpty(bean_addtrans_et_amount.getText())) {
                                    bean_addtrans_et_amount.setHint("Amount is required");
                                    bean_addtrans_et_amount.setHintTextColor(getResources().getColor(R.color.indian_red));
                                    validate = false;
                                }
                                if (bean_addtrans_et_title.getText().length() > 50) {
                                    bean_addtrans_et_title.setText("");
                                    bean_addtrans_et_title.setHint("Max 50 characters");
                                    bean_addtrans_et_title.setHintTextColor(getResources().getColor(R.color.indian_red));
                                    validate = false;
                                }
                                if (bean_addtrans_et_amount.getText().length() > 10) {
                                    bean_addtrans_et_amount.setText("");
                                    bean_addtrans_et_amount.setHint("Max limit 10 digits");
                                    bean_addtrans_et_amount.setHintTextColor(getResources().getColor(R.color.indian_red));
                                    validate = false;
                                }
                                Pattern p = Pattern.compile("[^a-z0-9öÖÜß ]", Pattern.CASE_INSENSITIVE);
                                Matcher m = p.matcher(bean_addtrans_et_title.getText());
                                if (m.find()) {
                                    bean_addtrans_et_title.setHint("No special characters allowed");
                                    bean_addtrans_et_title.setHintTextColor(getResources().getColor(R.color.indian_red));
                                    bean_addtrans_et_title.setText("");
                                    validate = false;
                                }
                                if (validate == true) {
                                    TransactionBean transactionBean;
                                    try{
                                        boolean recBool;
                                        if (recDate.getVisibility() == TextView.VISIBLE) {
                                            recBool = true;
                                        }
                                        else {
                                            recBool = false;
                                        }
                                        float amountMultFactor = getCurrencyFactor(bean_addtrans_sp_currency.getSelectedItem().toString(),MainActivity.CURRENT_USER_BEAN.get_default_currency());
                                        float finalAmount = Float.parseFloat(bean_addtrans_et_amount.getText().toString());
                                        if (amountMultFactor != -99) {
                                            finalAmount =  Math.round(finalAmount * amountMultFactor * 10d) / (float)10d;
                                        }
                                        transactionBean= new TransactionBean(bean_addtrans_et_title.getText().toString(), finalAmount, "expense" );
                                        transactionBean.set_dateTrans(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                                        transactionBean.set_tDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                                        transactionBean.set_tTime(new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date()));
                                        try {
                                            transactionBean.setTxnDetails(bean_addtrans_et_details.getText().toString());
                                            transactionBean.set_currency(MainActivity.CURRENT_USER_BEAN.get_default_currency());
                                            transactionBean.set_cat(bean_addtrans_sp_cat.getSelectedItem().toString());
                                            transactionBean.set_mop(bean_addtrans_sp_mop.getSelectedItem().toString());
                                            transactionBean.set_borrowedFrom(bean_addtrans_et_borrowed.getText().toString());
                                        }catch(Exception e){
                                        }
                                        try {
                                            if (bean_addtrans_et_date.getText().length() > 4) {
                                                transactionBean.set_dateTrans(bean_addtrans_et_date.getText().toString());
                                            } else {
                                                transactionBean.set_dateTrans(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                                            }
                                            transactionBean.set_tDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                                            transactionBean.set_tTime(new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date()));
                                        }catch(Exception e){
                                        }
                                        try {
                                            transactionBean.set_rec(recBool);
                                            transactionBean.set_recFrequency(bean_addtrans_sp_recfreq.getSelectedItem().toString());
                                            transactionBean.set_notes(bean_addtrans_et_recnotes.getText().toString());
                                        }catch(Exception e){
                                        }
                                        try {
                                            transactionBean.set_recStartDate(bean_addtrans_et_recdate.getText().toString());
                                            transactionBean.setNxtRecDate(nextDateCalculator(bean_addtrans_et_recdate.getText().toString(),bean_addtrans_sp_recfreq.getSelectedItem().toString()));
                                        }catch(Exception e){
                                        }
                                        Toast.makeText( getContext(), "Added successfully", Toast.LENGTH_SHORT ).show();
                                        System.out.println("************************ Inserting in Database **************************");
                                        if (CALL_FOR.equals("ADD")) {
                                            dao.insertTxn(transactionBean);
                                        }
                                        if (CALL_FOR.equals("EDIT")) {
                                            transactionBean.setTid(tid);
                                            dao.editTxn(transactionBean);
                                        }
                                        delButton.setVisibility(Button.INVISIBLE);
                                        listener.onButtonSelectedAddtrans(R.id.addtrans_mi_save, CALL_FOR);

                                    }catch(Exception e){
                                        e.printStackTrace();
                                        Toast.makeText( getContext(), "Problem adding the entry", Toast.LENGTH_SHORT ).show();
                                    }
                                }
                                return true;
                            }
                            return false;
                    }
                });
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
            case R.id.addtrans_bt_editcats:
                if (CALL_FOR.equals("EDIT")){
                    MainActivity.addtransFragment_temp = this;
                    MainActivity.CATS_BACK_BUTTON = "EDT";
                }
                if (CALL_FOR.equals("ADD")){
                    MainActivity.addtransFragment_temp = this;
                    MainActivity.CATS_BACK_BUTTON = "FRG";
                }
                listener.onButtonSelectedAddtrans(R.id.addtrans_bt_editcats, CALL_FOR);
                break;
            case R.id.toolbar_del_button:
                boolean isDeleted = dao.delTxn( tid );
                System.out.println("************************ is deleted flag"+isDeleted);
                if(isDeleted)
                    Toast.makeText( getContext(), "Deleted successfully", Toast.LENGTH_LONG ).show();
                else
                    Toast.makeText( getContext(), "Problem deleting the entry", Toast.LENGTH_LONG ).show();
                delButton.setVisibility(Button.INVISIBLE);
                listener.onButtonSelectedAddtrans(R.id.toolbar_del_button, CALL_FOR);
                break;
            case R.id.addtrans_et_borrowed:
                doLaunchContactPicker(v);
                break;
            default:
                break;
        }
    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedAddtrans(int id, String callFor);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datePicker.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabelRec() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        datePickerRec.setText(sdf.format(myCalendarRec.getTime()));
    }

    public String nextDateCalculator(String dt, String freq) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
        c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (freq.equals("Repeat Daily")) {
            c.add(Calendar.DATE, 1);
        }
        if (freq.equals("Repeat Weekly")) {
            c.add(Calendar.DATE, 7);
        }
        if (freq.equals("Repeat Monthly")) {
            c.add(Calendar.MONTH, 1);
        }
        if (freq.equals("Repeat Yearly")) {
            c.add(Calendar.YEAR, 1);
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String output = sdf1.format(c.getTime());
        return output;
    }

    public void populateTransaction(TransactionBean transactionBean) {
        try {
            text_addtrans_et_title = transactionBean.get_title();
            text_addtrans_et_details = transactionBean.getTxnDetails();
            text_addtrans_et_amount = Float.toString(transactionBean.get_amount());
        }catch(Exception e){
        }
        try {
            text_addtrans_et_date = transactionBean.get_dateTrans();
        }catch(Exception e){
        }
        try {
            text_addtrans_sp_currency = transactionBean.get_currency();
            text_addtrans_sp_cat = transactionBean.get_cat();
            text_addtrans_sp_mop = transactionBean.get_mop();
            text_addtrans_et_borrowed = transactionBean.get_borrowedFrom();
        }catch(Exception e){
        }
        try {
            text_addtrans_sp_recfreq = transactionBean.get_recFrequency();
            text_addtrans_et_recdate = transactionBean.get_recStartDate();
            text_addtrans_et_recnotes = transactionBean.get_notes();
            text_addtrans_bl_recbool = transactionBean.get_rec();
            tid = transactionBean.getTid();
        }catch(Exception e){
        }
    }

    private void initView(View v) {
        bean_addtrans_et_title = (EditText) v.findViewById(R.id.addtrans_et_title);
        bean_addtrans_et_details =(EditText)v.findViewById( R.id.addtrans_et_details);
        bean_addtrans_et_amount =(EditText)v.findViewById( R.id.addtrans_et_amount);
        bean_addtrans_sp_currency =(Spinner)v.findViewById( R.id.addtrans_sp_currency);
        bean_addtrans_sp_cat =(Spinner)v.findViewById( R.id.addtrans_sp_cat);
        bean_addtrans_sp_mop =(Spinner)v.findViewById( R.id.addtrans_sp_mop);
        bean_addtrans_et_date =(EditText)v.findViewById( R.id.addtrans_et_date);
        bean_addtrans_sp_recfreq =(Spinner)v.findViewById( R.id.addtrans_sp_recfreq);
        bean_addtrans_et_recdate =(EditText)v.findViewById( R.id.addtrans_et_recdate);
        bean_addtrans_et_recnotes = (EditText)v.findViewById( R.id.addtrans_et_recnotes);
        bean_addtrans_iv_recdropimg = (ImageView) v.findViewById( R.id.rec_drop_img);
        bean_addtrans_et_borrowed =(EditText)v.findViewById( R.id.addtrans_et_borrowed);

        String[] resArray;
        bean_addtrans_et_title.setText(text_addtrans_et_title);
        bean_addtrans_et_details.setText(text_addtrans_et_details);
        bean_addtrans_et_amount.setText(text_addtrans_et_amount);
        bean_addtrans_et_borrowed.setText(text_addtrans_et_borrowed);
        resArray = getResources().getStringArray(R.array.Currencies);
        bean_addtrans_sp_currency.setSelection(Arrays.asList(resArray).indexOf(text_addtrans_sp_currency));
//        resArray = getResources().getStringArray(R.array.Categories);
//        bean_addtrans_sp_cat.setSelection(Arrays.asList(resArray).indexOf(text_addtrans_sp_cat));
        loadSpinnerData(text_addtrans_sp_cat);
        resArray = getResources().getStringArray(R.array.ModeOfPayments);
        bean_addtrans_sp_mop.setSelection(Arrays.asList(resArray).indexOf(text_addtrans_sp_mop));
        bean_addtrans_et_date.setText(text_addtrans_et_date);
        resArray = getResources().getStringArray(R.array.RecursiveTransactions);
        bean_addtrans_sp_recfreq.setSelection(Arrays.asList(resArray).indexOf(text_addtrans_sp_recfreq));
        bean_addtrans_et_recdate.setText(text_addtrans_et_recdate);
        bean_addtrans_et_recnotes.setText(text_addtrans_et_recnotes);
        if (text_addtrans_bl_recbool == false) {
            bean_addtrans_sp_recfreq.setVisibility(TextView.INVISIBLE);
            bean_addtrans_et_recdate.setVisibility(TextView.INVISIBLE);
            bean_addtrans_et_recnotes.setVisibility(TextView.INVISIBLE);
            bean_addtrans_iv_recdropimg.setVisibility(TextView.INVISIBLE);
        }
        else {
            bean_addtrans_sp_recfreq.setVisibility(TextView.VISIBLE);
            bean_addtrans_et_recdate.setVisibility(TextView.VISIBLE);
            bean_addtrans_et_recnotes.setVisibility(TextView.VISIBLE);
            bean_addtrans_iv_recdropimg.setVisibility(TextView.VISIBLE);
        }
    }

    public void loadSpinnerData(String value) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<String> lables = db.fetchAllCategories1();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lables);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String temp = "";
        if (value == "") {
            try {
                temp = bean_addtrans_sp_cat.getSelectedItem().toString();
            } catch(Exception e){
            }
        } else {
            temp = value;
        }
        bean_addtrans_sp_cat.setAdapter(dataAdapter);
        try {
            bean_addtrans_sp_cat.setSelection(lables.indexOf(temp));
        } catch(Exception e){
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


    public void doLaunchContactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        getActivity().startActivityForResult(contactPickerIntent, MainActivity.MY_PERMISSIONS_REQUEST_GET_CONTACT);
    }

    public float getCurrencyFactor(String currencyFrom, String currencyTo){

//        System.out.println("===================== currency1");
        String str = "abc";
        try {
            URL url = new URL("https://free.currconv.com/api/v7/convert?q=" + currencyFrom + "_" + currencyTo + "&compact=ultra&apiKey=a0a23e6878183f262040");
//            System.out.println("===================== currency2");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
//            System.out.println("===================== currency3");
//            while (null != (str = br.readLine())) {
////                System.out.println("===================== currency4");
//                System.out.println(str);
////                Toast.makeText( getContext(), str, Toast.LENGTH_SHORT ).show();
//            }
            str = br.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        str = str.replaceAll("[^\\d.]", "");
//        System.out.println("===================== currency5");
//        System.out.println(str);
        float finalValue = -99;
        try {
            finalValue = Float.parseFloat(str);
//            System.out.println(finalValue);
        } catch (Exception ex) {

        }

        return finalValue;
    }

}
