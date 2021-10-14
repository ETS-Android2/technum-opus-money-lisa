package com.technumopus.moneylisa.controller.home;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.technumopus.moneylisa.MainActivity;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.model.TransactionBean;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.UserBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener, TabLayout.OnTabSelectedListener {

    private OnFragmentItemSelectedListener listener;
    private RecyclerView recyclerView;
    DatabaseHelper dao;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView= root.findViewById(R.id.rv_cumulative_amt_home);
        recyclerView.setLayoutManager( linearLayoutManager );

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        UserBean currentUserBean = new UserBean();
        List<UserBean> userBeanList=databaseHelper.fetchAllUsers();
        for (int i=0; i<userBeanList.size(); i++) {
            if (userBeanList.get(i).get_user_name().toLowerCase().equals(DatabaseHelper.CURRENT_USER.toLowerCase())) {
                MainActivity.CURRENT_USER_BEAN = userBeanList.get(i);
            }
        }

//        dao = new DatabaseHelper( getContext() );
//        String todaysDate= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//        String todaysMonth=todaysDate.substring( 3 );
//        System.out.println("******************** todays month is :"+todaysMonth);
//        System.out.println("******************** todays date is :"+todaysDate);
//        List<TransactionBean> transactionBeanList = dao.fetchCurrMonthTxn( todaysMonth , "month");
//
//        ListIterator iterator= transactionBeanList.listIterator();
//        while(iterator.hasNext()){
//            System.out.println("********************* Cumulative  bean starts ************************");
//            System.out.println(iterator.next().toString());
//            System.out.println("********************* Cumulative bean Ends ************************");
//        }
//
//
//        CumulativeAmtAdapter cumulativeAmtAdapter = new CumulativeAmtAdapter( transactionBeanList );
//        recyclerView.setAdapter( cumulativeAmtAdapter );
//        cumulativeAmtAdapter.notifyDataSetChanged();


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_home);

        FloatingActionButton btn = (FloatingActionButton)root.findViewById(R.id.btHomeToAddtrans);
        btn.setOnClickListener(this);

        FloatingActionButton btn2 = (FloatingActionButton)root.findViewById(R.id.btHomeToAlltrans);
        btn2.setOnClickListener(this);

        tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(this);
        TabLayout.Tab tab = tabLayout.getTabAt(2);
        tab.select();

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
            case R.id.btHomeToAddtrans:
                listener.onButtonSelectedHome(R.id.btHomeToAddtrans);
                break;
            case R.id.btHomeToAlltrans:
                listener.onButtonSelectedHome(R.id.btHomeToAlltrans);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        String todaysDate;
        List<TransactionBean> transactionBeanList = new ArrayList<>();
        List<TransactionBean> transactionBeanListTemp;
        CumulativeAmtAdapter cumulativeAmtAdapter;
        switch(tab.getPosition()) {
            case 0:
//                Toast.makeText( getContext(), "Today", Toast.LENGTH_SHORT ).show();

                dao = new DatabaseHelper( getContext() );
                todaysDate= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                transactionBeanList = dao.fetchCurrMonthTxn( todaysDate , "today");
                cumulativeAmtAdapter = new CumulativeAmtAdapter( transactionBeanList );
                recyclerView.setAdapter( cumulativeAmtAdapter );
                cumulativeAmtAdapter.notifyDataSetChanged();
                break;
            case 1:
                System.out.println("===========================================================================================");
//                Toast.makeText( getContext(), "This Week", Toast.LENGTH_SHORT ).show();
                dao = new DatabaseHelper( getContext() );
                Calendar calendar = Calendar.getInstance();
                int offset = calendar.get(Calendar.DAY_OF_WEEK) - 2;

                calendar.add(Calendar.DATE, -offset);
                String dateList = "";
                for(int i = 0; i < 7; i++){
                    System.out.println(calendar.getTime());
                    todaysDate= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar);
                    dateList = dateList + "|" + todaysDate;
//                    transactionBeanListTemp = dao.fetchCurrMonthTxn( dateList , "week");
//                    System.out.println(todaysDate);
//                    for (int j=0; j<transactionBeanListTemp.size(); j++) {
//                        if (transactionBeanListTemp.get(j).get_amount() != 0) {
//                            transactionBeanList.add(transactionBeanListTemp.get(j));
//                        }
//                    }
                    calendar.add(Calendar.DATE, 1);
                }
                cumulativeAmtAdapter = new CumulativeAmtAdapter( dao.fetchCurrMonthTxn( dateList , "week") );
                recyclerView.setAdapter( cumulativeAmtAdapter );
                cumulativeAmtAdapter.notifyDataSetChanged();
                System.out.println(transactionBeanList.size());
                ListIterator iterator2= transactionBeanList.listIterator();
                while(iterator2.hasNext()){
                    System.out.println("********************* Cumulative  bean starts ************************");
                    System.out.println(iterator2.next().toString());
                    System.out.println("********************* Cumulative bean Ends ************************");
                }
                System.out.println("===========================================================================================");
                break;
            case 2:
//                Toast.makeText( getContext(), "This Month", Toast.LENGTH_SHORT ).show();

                dao = new DatabaseHelper( getContext() );
                todaysDate= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String todaysMonth=todaysDate.substring( 3 );
                transactionBeanList = dao.fetchCurrMonthTxn( todaysMonth , "month");
                cumulativeAmtAdapter = new CumulativeAmtAdapter( transactionBeanList );
                recyclerView.setAdapter( cumulativeAmtAdapter );
                cumulativeAmtAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedHome(int id);
    }

}
