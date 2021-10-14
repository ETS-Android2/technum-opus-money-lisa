package com.technumopus.moneylisa.controller.addIncome;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.controller.addtrans.AddTransactionFragment;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.TransactionBean;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddIncomeFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private OnFragmentItemSelectedListener listener;
    DatabaseHelper dao;
    EditText etAddIncTitle, etAddIncDetails, etAmountAddInc, etDateAddInc;//, etRecDateAddInc;
    Spinner spCurrencyAddInc;//, spRecFreqAddInc;
    ImageView recDropImgAddInc;
    Button delButton;

    TextView recDate;
    Spinner recFreq;
    EditText datePicker, datePickerRec;
    Calendar myCalendar, myCalendarRec;
    DatePickerDialog.OnDateSetListener date, dateRec;


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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
        System.out.println("Inside AddIncomeFragment :: onCreateView :: ");
        final View root = inflater.inflate( R.layout.add_income_fragment, container, false);

        recDate=(TextView) root.findViewById( R.id.et_recdate_addinc );
        recFreq=(Spinner) root.findViewById( R.id.sp_recfreq_addinc );
        recDropImgAddInc=(ImageView) root.findViewById( R.id.rec_drop_img_addinc );

        etAddIncTitle=(EditText)root.findViewById( R.id.et_addinc_title );
        etAddIncDetails=(EditText)root.findViewById( R.id.et_addinc_details );
        etAmountAddInc=(EditText)root.findViewById( R.id.et_amount_addinc );
        etDateAddInc=(EditText)root.findViewById( R.id.et_date_addinc );
        spCurrencyAddInc=(Spinner) root.findViewById( R.id.sp_currency_addinc );

        recDate.setVisibility( EditText.INVISIBLE );
        recFreq.setVisibility( EditText.INVISIBLE );
        recDropImgAddInc.setVisibility( ImageView.INVISIBLE );

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_addincome);

        myCalendar = Calendar.getInstance();
        datePicker= (EditText) root.findViewById(R.id.et_date_addinc);
        date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set( Calendar.YEAR, year );
                myCalendar.set(Calendar.MONTH, month);
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


        myCalendarRec = Calendar.getInstance();

        datePickerRec= (EditText) root.findViewById(R.id.et_recdate_addinc);
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

        dao = new DatabaseHelper(getContext());

        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) root.findViewById(R.id.navigationViewIncome);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item){
                        switch (item.getItemId()){
                            case R.id.addinc_mi_recpop:
                                if(recDate.getVisibility() == TextView.VISIBLE){
                                    recDate.setVisibility(TextView.INVISIBLE);
                                    recFreq.setVisibility(TextView.INVISIBLE);
                                    recDropImgAddInc.setVisibility( TextView.INVISIBLE );
                                }else{
                                    recDate.setVisibility(TextView.VISIBLE);
                                    recFreq.setVisibility(TextView.VISIBLE);
                                    recDropImgAddInc.setVisibility( TextView.VISIBLE );
                                }
                                return true;
                            case R.id.addinc_mi_cancel:
                                listener.onButtonSelectedAddInc(R.id.addinc_mi_cancel);
                                return true;
                            case R.id.addinc_mi_save:
                                boolean validate = true;
                                if(etAddIncTitle.getText().toString().trim().length()==0){
                                    etAddIncTitle.setText("");
                                    etAddIncTitle.setHint("Title is required");
                                    etAddIncTitle.setHintTextColor(getResources().getColor(R.color.indian_red));
                                    validate=false;
                                }if(etAmountAddInc.getText().toString().trim().length()==0){
                                etAmountAddInc.setHint("Income is required");
                                etAmountAddInc.setHintTextColor(getResources().getColor(R.color.indian_red));
                                validate=false;
                            }if(etAddIncTitle.getText().toString().trim().length()>50){
                                etAddIncTitle.setText("");
                                etAddIncTitle.setHint("Max 50 characters");
                                etAddIncTitle.setHintTextColor(getResources().getColor(R.color.indian_red));
                                validate=false;
                            }if(etAmountAddInc.getText().toString().trim().length()>10){
                                etAmountAddInc.setHint("Max limit 10 digits");
                                etAmountAddInc.setHintTextColor(getResources().getColor(R.color.indian_red));
                                validate=false;
                            }
                            Pattern p = Pattern.compile("[^a-z0-9öÖÜß ]", Pattern.CASE_INSENSITIVE);
                            Matcher m = p.matcher(etAddIncTitle.getText());
                            if(m.find()){
                                etAddIncTitle.setText("");
                                etAddIncTitle.setHint("No special characters allowed");
                                etAddIncTitle.setHintTextColor(getResources().getColor(R.color.indian_red));
                                validate=false;
                            }
                                System.out.println("validate flag  :: Add Income"+validate);
                            if (validate == true){
                                TransactionBean transactionBean;
                                try{
                                    boolean recBool;
                                    if (recDate.getVisibility() == TextView.VISIBLE) {
                                        recBool = true;

                                    }
                                    else {
                                        recBool = false;
                                    }
                                    transactionBean=new TransactionBean();
                                    transactionBean.set_title( etAddIncTitle.getText().toString() );
                                    transactionBean.set_amount(Float.parseFloat(etAmountAddInc.getText().toString()));
                                    transactionBean.setTxnType( "income" );
                                    transactionBean.set_currency(spCurrencyAddInc.getSelectedItem().toString());
                                    transactionBean.setTxnDetails(etAddIncDetails.getText().toString().trim());
                                    transactionBean.set_rec(recBool);
                                    if(recBool){
                                        try{
                                            transactionBean.set_recFrequency(recFreq.getSelectedItem().toString());
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        if(recDate.getText().length()>4)
                                            transactionBean.set_recStartDate(recDate.getText().toString());
                                        else
                                            transactionBean.set_recStartDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

                                        transactionBean.setNxtRecDate(nextDateCalculator(transactionBean.get_recStartDate(), transactionBean.get_recFrequency()));
                                    }

                                    System.out.println("Transaction bean before inserting -- 1 --  :: Add Income :: inside try"+transactionBean.toString());


                                    if (etDateAddInc.getText().length() > 4)
                                        transactionBean.set_dateTrans(etDateAddInc.getText().toString());
                                    else
                                        transactionBean.set_dateTrans(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

                                    transactionBean.set_tDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                                    transactionBean.set_tTime(new SimpleDateFormat("HH-mm-ss", Locale.getDefault()).format(new Date()));

                                    try {
                                        System.out.println("Transaction bean before inserting :: Add Income :: inside try"+transactionBean.toString());
                                        System.out.println("***************** calling dao for insert :: Add income:: ");
                                        dao.insertTxn( transactionBean );
                                        Toast.makeText( getContext(), "Income added successfully", Toast.LENGTH_SHORT ).show();
                                        listener.onButtonSelectedAddInc( R.id.addinc_mi_save );
                                    }catch (Exception e) {
                                        System.out.println("***************** Inside exception of insert :: Add income:: ");
                                        Toast.makeText( getContext(), "Problem adding income", Toast.LENGTH_SHORT ).show();
                                        System.out.println( e.getMessage() );
                                        e.printStackTrace();
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                }
        );
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
        System.out.println("******************** inside onClick of add income :: Implement this method");
    //    listener.onButtonSelectedAddInc( R.id. );

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedAddInc(int id);
    }
}
