package com.technumopus.moneylisa.controller.alltrans;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technumopus.moneylisa.MainActivity;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.model.TransactionBean;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.controller.addtrans.AddTransactionFragment;

import java.util.List;

public class AlltransFragment extends Fragment implements View.OnClickListener{

    private OnFragmentItemSelectedListener listener;
    DatabaseHelper dao;
    Button delButton;
    private boolean isRecursiveCall;
    public String filterQuery;

    public AlltransFragment() {
    }
    public AlltransFragment(boolean isRecursiveCall) {
        this.isRecursiveCall=isRecursiveCall;
        System.out.println("*************************** AlltransFragment::AlltransFragment  isRecursiveCall:"+isRecursiveCall);
    }


    public AlltransFragment(int contentLayoutId) {
        super( contentLayoutId );
    }

    public AlltransFragment(String filterQuery) {
        this.filterQuery = filterQuery;
    }
/*
    public AlltransFragment(int contentLayoutId, String filterQuery) {
        super( contentLayoutId );
        this.filterQuery = filterQuery;
    }*/


    // Recycler View
    private RecyclerView recyclerView;



    public List showTransactionListView(DatabaseHelper dao, String filterQuery, boolean isRecursiveCall){
        if(MainActivity.REC_FRAGMENT_CALL=="FROM_REC"){
            if(filterQuery!=null){
                List<TransactionBean> allTransactionList=dao.fetchRecTransactions(filterQuery+" and RECURR_FLAG='1'" + " AND " + DatabaseHelper.COL_USER_NAME + " = " + "\"" + DatabaseHelper.CURRENT_USER + "\"" + " AND " + DatabaseHelper.COL_USER_PROFILE + " = " + "\"" + DatabaseHelper.CURRENT_USER_PROFILE + "\""+" AND "+DatabaseHelper.COL_TXN_TYPE+" = 'expense'");
                return allTransactionList;
            }else{
                List<TransactionBean> allTransactionList=dao.fetchRecTransactions("SELECT * FROM TRANSACTION_DETAILS where RECURR_FLAG='1'" + " AND " + DatabaseHelper.COL_USER_NAME + " = " + "\"" + DatabaseHelper.CURRENT_USER + "\"" + " AND " + DatabaseHelper.COL_USER_PROFILE + " = " + "\"" + DatabaseHelper.CURRENT_USER_PROFILE + "\""+" AND "+DatabaseHelper.COL_TXN_TYPE+" = 'expense'");
                return allTransactionList;
            }
        }else{
            if(filterQuery!=null){
                List<TransactionBean> allTransactionList=dao.fetchFilteredTransactions(filterQuery + " AND " + DatabaseHelper.COL_USER_NAME + " = " + "\"" + DatabaseHelper.CURRENT_USER + "\"" + " AND " + DatabaseHelper.COL_USER_PROFILE + " = " + "\"" + DatabaseHelper.CURRENT_USER_PROFILE + "\"");
                return allTransactionList;
            }else{
                List<TransactionBean> allTransactionList=dao.fetchAllTransactions();
                return allTransactionList;
            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("***************************** AlltransFragment : On create view ");
        View root = inflater.inflate(R.layout.fragment_alltrans, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView= root.findViewById( R.id.rv_alltrans );
        recyclerView.setLayoutManager( linearLayoutManager );
        dao = new DatabaseHelper( getContext() );
        System.out.println("************************* before showTransactionListView ::  isRecursiveCall:"+isRecursiveCall);
        List<TransactionBean> transactionBeanList = showTransactionListView( dao , filterQuery, isRecursiveCall);
/*        ListIterator iter = transactionBeanList.listIterator();
        while(iter.hasNext()){
            System.out.println("********************* transaction bean starts ************************");
            System.out.println(iter.next().toString());
            System.out.println("********************* transaction bean ends ************************");

        }*/
        AlltransAdapter alltransAdapter = new AlltransAdapter( transactionBeanList );
        recyclerView.setAdapter( alltransAdapter );
        alltransAdapter.notifyDataSetChanged();

        delButton = (Button)getActivity().findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.VISIBLE);
        delButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_filter, 0);
        delButton.setOnClickListener(this);

        alltransAdapter.setOnItemClickListener(new AlltransAdapter.ClickListener() {
            @Override
            public void onItemClick(TransactionBean transactionBean, View v) {
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                AddTransactionFragment addtransFragment = new AddTransactionFragment();
                if (transactionBean.get_rec() == false) {
                    addtransFragment.text_addtrans_bl_recbool = false;
                }
                else {
                    addtransFragment.text_addtrans_bl_recbool = true;
                }
                addtransFragment.CALL_FOR = "EDIT";
                addtransFragment.populateTransaction(transactionBean);
                System.out.println("================================================checkitout");
                System.out.println(transactionBean.get_cat());
                fragmentTransaction.replace(R.id.nav_host_fragment,addtransFragment);
                MainActivity.addtransFragment_temp = addtransFragment;
                fragmentTransaction.commit();
//                Toast.makeText( v.getContext(), transactionBean.get_title(), Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onItemLongClick(TransactionBean transactionBean, View v) {
//                v.setBackgroundColor(getResources().getColor(R.color.light_pink));
                Toast.makeText( v.getContext(), transactionBean.get_title(), Toast.LENGTH_SHORT ).show();
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if(MainActivity.REC_FRAGMENT_CALL=="FROM_REC")
            toolbar.setTitle(R.string.menu_recursive);
        else
            toolbar.setTitle(R.string.menu_alltrans);

//        Button btn = (Button)root.findViewById(R.id.btAlltransToFilops);
//        btn.setOnClickListener(this);
//
//        Button btn2 = (Button)root.findViewById(R.id.btCancelAlltransToHome);
//        btn2.setOnClickListener(this);

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
            case R.id.toolbar_del_button:
                listener.onButtonSelectedAlltrans(R.id.toolbar_del_button);
                break;
//            case R.id.btCancelAlltransToHome:
//                listener.onButtonSelectedAlltrans(R.id.btCancelAlltransToHome);
//                break;
            default:
                break;
        }
    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedAlltrans(int id);
    }

}
