package com.technumopus.moneylisa.controller.filterOptions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.model.CategoryBean;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class TxnFilterFragment extends Fragment implements View.OnClickListener{

    private OnFragmentItemSelectedListener listener;
    private Spinner categorySpinner;
    private Spinner monthSpinner;
    private Spinner methOfPmntSpinner;
    DatabaseHelper dao;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<CategoryBean> categoryBeanList = new ArrayList<>(  );
        List<String> categoryList = new ArrayList<String>(  );
        View root = inflater.inflate(R.layout.txn_filter_fragment, container, false);
        monthSpinner= (Spinner) root.findViewById( R.id.sp_month_filter );
        categorySpinner= (Spinner) root.findViewById( R.id.sp_category_filter );
        methOfPmntSpinner= (Spinner) root.findViewById( R.id.sp_meth_pay_filter );

        dao = new DatabaseHelper( getContext() );
        categoryBeanList=dao.fetchAllCategories();
        System.out.println("********************* All categories are : \n"+categoryBeanList.toString());
        ListIterator<CategoryBean> iterator = categoryBeanList.listIterator();
        while(iterator.hasNext()){
            categoryList.add( iterator.next().getCatName() );
        }
        System.out.println("********************* categoryList: "+categoryList);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>( getContext(), R.layout.support_simple_spinner_dropdown_item, categoryList );
        arrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        categorySpinner.setAdapter( arrayAdapter );

        List<String> monthList = dao.fetchAllMonths();
        System.out.println("*************************** Month list is : \n"+monthList);
        ArrayAdapter<String> arrayAdapterMonth = new ArrayAdapter<String>( getContext(), R.layout.support_simple_spinner_dropdown_item, monthList );
        arrayAdapterMonth.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        monthSpinner.setAdapter( arrayAdapterMonth );

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_filops);

        Button btn_cancel = (Button)root.findViewById(R.id.btCancelFilopsToAlltrans);
        btn_cancel.setOnClickListener(this);
        Button btn_apply = root.findViewById( R.id.btn_applyFilter );
        btn_apply.setOnClickListener( this );
        System.out.println("******************* TxnFilterFragment : Block 1 : After btn_apply.setOnClickListener : ");

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("******************* TxnFilterFragment : Block 2 : onAttach");
        if(context instanceof OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;

        }else {
            throw new ClassCastException(context.toString() + " must implement listener");
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println("******************* TxnFilterFragment : Block 3 : onClick;");

        switch (v.getId()) {
            case R.id.btCancelFilopsToAlltrans:
                listener.onButtonSelectedFilops(R.id.btCancelFilopsToAlltrans);
                break;
            case R.id.btn_applyFilter:
                String filterQuery=getSelectedFilters( getView() );
                listener.onButtonSelectedFilops(R.id.btn_applyFilter, filterQuery);

            default:
                break;
        }
    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedFilops(int id);
        public void onButtonSelectedFilops(int id, String filterQuery);

    }

    public String getSelectedFilters(View v){
        String monthVal= (String) monthSpinner.getSelectedItem();
        String categoryVal= (String) categorySpinner.getSelectedItem();
        String methOfPaymentVal= (String) methOfPmntSpinner.getSelectedItem();
        return buildFilterQuery( monthVal,categoryVal,methOfPaymentVal );
    }

    private String buildFilterQuery(String monthValue, String categoryValue, String methOfPaymentValue ){
        String baseQuery="SELECT * FROM TRANSACTION_DETAILS WHERE TXN_TYPE= 'expense' ";
        String returnQuery;
        /*
        * Case 1. None of them are selected
        *
        * Case 2. Month
        * Case 3. Category
        * Case 4. method of payment
        *
        * Case 5. Month + Category
        * Case 6. Month + MOP
        * Case 7. Category + MOP
        *
        * Case 8. Month + Category + MOP
        *
        * */

        // Case 1
        if(monthValue == "Month" && categoryValue == "Category" &&  methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery;
            // Case 2, 3, 4
        else if(monthValue != "Month" && categoryValue == "Category" &&  methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery+" AND TXN_DATE like '%"+monthValue+"'";
        else if(monthValue == "Month" && categoryValue != "Category" &&  methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery+" AND CATEGORY ='"+categoryValue+"'";
        else if(monthValue == "Month" && categoryValue == "Category" &&  !methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery+" AND TXN_METHOD_OF_PMNT ='"+methOfPaymentValue+"'";
            // Case 5, 6, 7
        else if(monthValue != "Month" && categoryValue != "Category" &&  methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery+" AND TXN_DATE like '%"+monthValue+"' AND CATEGORY ='"+categoryValue+"'";
        else if(monthValue != "Month" && categoryValue == "Category" &&  !methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery+" AND TXN_DATE like '%"+monthValue+"' AND TXN_METHOD_OF_PMNT ='"+methOfPaymentValue+"'";
        else if(monthValue == "Month" && categoryValue != "Category" &&  !methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery+" AND TXN_METHOD_OF_PMNT ='"+methOfPaymentValue+"' AND CATEGORY ='"+categoryValue+"'";
            // Case 8
        else if(monthValue != "Month" && categoryValue != "Category" &&  !methOfPaymentValue.equals("Mode of Payment"))
            returnQuery =  baseQuery+" AND TXN_DATE like '%"+monthValue+"' AND CATEGORY ='"+categoryValue+"' AND TXN_METHOD_OF_PMNT ='"+methOfPaymentValue+"'";
        else
            returnQuery = baseQuery;

        System.out.println("**************************** returnQuery : \n"+returnQuery);
        return returnQuery;
    }

}
