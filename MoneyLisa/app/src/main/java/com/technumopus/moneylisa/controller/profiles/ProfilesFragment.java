package com.technumopus.moneylisa.controller.profiles;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technumopus.moneylisa.MainActivity;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.controller.home.CumulativeAmtAdapter;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.TransactionBean;
import com.technumopus.moneylisa.model.UserBean;

import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class ProfilesFragment extends Fragment implements View.OnClickListener{

    private OnFragmentItemSelectedListener listener;
    private RecyclerView recyclerView;
    DatabaseHelper dao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profiles, container, false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        UserBean currentUserBean = new UserBean();
        List<UserBean> userBeanList=databaseHelper.fetchAllUsers();
        for (int i=0; i<userBeanList.size(); i++) {
            if (userBeanList.get(i).get_user_name().toLowerCase().equals(DatabaseHelper.CURRENT_USER.toLowerCase())) {
                MainActivity.CURRENT_USER_BEAN = userBeanList.get(i);
            }
        }


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_profiles);

        ImageButton btProfileHome = (ImageButton)root.findViewById(R.id.btProfileHome);
        btProfileHome.setOnClickListener(this);

        ImageButton btProfileBusiness = (ImageButton)root.findViewById(R.id.btProfileBusiness);
        btProfileBusiness.setOnClickListener(this);

        ImageButton btProfileTravel = (ImageButton)root.findViewById(R.id.btProfileTravel);
        btProfileTravel.setOnClickListener(this);




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
            case R.id.btProfileHome:
                DatabaseHelper.CURRENT_USER_PROFILE = "home";
                listener.onButtonSelectedProfiles(R.id.btProfileHome);
                break;
            case R.id.btProfileBusiness:
                DatabaseHelper.CURRENT_USER_PROFILE = "business";
                listener.onButtonSelectedProfiles(R.id.btProfileBusiness);
                break;
            case R.id.btProfileTravel:
                DatabaseHelper.CURRENT_USER_PROFILE = "travel";
                listener.onButtonSelectedProfiles(R.id.btProfileTravel);
                break;
            default:
                break;
        }
    }

    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedProfiles(int id);
    }

}
