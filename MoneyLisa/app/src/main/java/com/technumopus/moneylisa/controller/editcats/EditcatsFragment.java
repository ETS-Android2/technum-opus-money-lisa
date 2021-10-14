package com.technumopus.moneylisa.controller.editcats;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.model.CategoryBean;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.TransactionBean;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import com.example.moneylisa.providers.ExpensesContract.Categories;

public class EditcatsFragment extends Fragment implements View.OnClickListener{

    private OnFragmentItemSelectedListener listener;
    EditText bean_catName;
    Button btndone;
    DatabaseHelper dao;
    Spinner bean_addtrans_sp_cat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_editcats, container, false);
        bean_catName =(EditText)root.findViewById( R.id.category_edit_text);
        btndone =(Button) root.findViewById( R.id.button_cat_done);
        // bean_addtrans_sp_cat =(Spinner)root.findViewById( R.id.addtrans_sp_cat);
        // bean_addtrans_sp_cat.setOnItemSelectedListener(this);
        // loadSpinnerData();


        btndone.setOnClickListener(this);
        dao = new DatabaseHelper(getContext());
        CategoryBean categoryBean;
        System.out.println(bean_catName.getText().toString());

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_editcats);

        System.out.println("========================== making trans bean 1===========================");

        btndone.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validate = true;
                if (bean_catName.getText().toString().trim().length() == 0) {
                    bean_catName.setText("");
                    validate = false;
                }
                if (TextUtils.isEmpty(bean_catName.getText())) {
                    bean_catName.setHint("Required");
                    bean_catName.setHintTextColor(getResources().getColor(R.color.indian_red));
                    validate = false;
                }
                if (bean_catName.getText().length() > 50) {
                    bean_catName.setText("");
                    bean_catName.setHint("Max 50 characters");
                    bean_catName.setHintTextColor(getResources().getColor(R.color.indian_red));
                    validate = false;
                }
                Pattern p = Pattern.compile("[^a-z0-9öÖÜß ]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(bean_catName.getText());
                if (m.find()) {
                    bean_catName.setHint("No special characters allowed");
                    bean_catName.setHintTextColor(getResources().getColor(R.color.indian_red));
                    bean_catName.setText("");
                    validate = false;
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                List<CategoryBean> categoryBeansList=databaseHelper.fetchAllCategories();
                for (int i=0; i<categoryBeansList.size(); i++) {
                    if (categoryBeansList.get(i).getCatName().toString().trim().toLowerCase().equals(bean_catName.getText().toString().trim().toLowerCase())) {
                        bean_catName.setHint("Already exists");
                        bean_catName.setHintTextColor(getResources().getColor(R.color.indian_red));
                        bean_catName.setText("");
                        validate = false;
                        break;
                    }
                }
                if (validate == true) {
                    CategoryBean categoryBean;
                    try{
                        categoryBean= new CategoryBean( bean_catName.getText().toString(), bean_catName.getText().toString() );
                        boolean isInserted = dao.insertCategory( categoryBean );
                        if(isInserted)
                            Toast.makeText( getContext(), "Category added successfully", Toast.LENGTH_LONG ).show();
                        else
                            Toast.makeText( getContext(), "Problem adding category", Toast.LENGTH_LONG ).show();
                        listener.onButtonSelectedEditcats(R.id.button_cat_done);
                    }catch(Exception e){
                        categoryBean= new CategoryBean( bean_catName.getText().toString(), bean_catName.getText().toString() );
                    }
                }
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

    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedEditcats(int id);
    }


}


