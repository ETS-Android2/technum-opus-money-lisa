package com.technumopus.moneylisa.controller.threshold;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.ThresholdBean;

import java.util.List;

public class ThresholdFragment extends Fragment implements View.OnClickListener {

    EditText et_threshold;
    Button bt_save;
    Spinner sp_currency;
    DatabaseHelper dao;
    RecyclerView recyclerView;
    private OnFragmentItemSelectedListener listener;

    public ThresholdFragment() {
 //       et_threshold=null;
        System.out.println(" ******************** inside ThresholdFragment : ThresholdFragment ");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dao=new DatabaseHelper( getContext() );
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_threshold);

        List<ThresholdBean> thresholdBeanList=dao.getThresholdEntries();
        System.out.println("****************************** ThresholdFragment :: onCreateView :: thresholdBeanList ::"+thresholdBeanList.toString());

        View root = inflater.inflate( R.layout.threshold_fragment, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getContext() );
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
        recyclerView=root.findViewById( R.id.rv_thres_list );
        recyclerView.setLayoutManager( linearLayoutManager );

        ViewThresholdAdapter viewThresholdAdapter= new ViewThresholdAdapter( thresholdBeanList );
        recyclerView.setAdapter( viewThresholdAdapter );
        viewThresholdAdapter.notifyDataSetChanged();

        et_threshold=(EditText) root.findViewById(R.id.et_set_threshold_amount);
        sp_currency=(Spinner) root.findViewById( R.id.sp_thres_curr );
        System.out.println("******************** inside onCreateView : et_threshold:"+et_threshold.getText());
        bt_save=(Button) root.findViewById(R.id.bt_set_threshold);

        bt_save.setOnClickListener( this );
        return root;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_set_threshold) {
            boolean validate = true;
            if (TextUtils.isEmpty(et_threshold.getText())) {
                et_threshold.setHint("Amount is required");
                et_threshold.setHintTextColor(getResources().getColor(R.color.indian_red));
                validate = false;
            }
            if (et_threshold.getText().length() > 10) {
                et_threshold.setText("");
                et_threshold.setHint("Max limit 10 digits");
                et_threshold.setHintTextColor(getResources().getColor(R.color.indian_red));
                validate = false;
            }
            if (validate == true) {
                System.out.println(" ******************** inside ThresholdFragment : onClick ");
                float thresholdAmt = Float.parseFloat(et_threshold.getText().toString());
                String currency = sp_currency.getSelectedItem().toString();
                System.out.println("******************** inside onClick : thresholdAmt:" + thresholdAmt + " :: currency :: " + currency);
                //    dao=new DatabaseHelper( getContext() );
                dao.setThresholdDB(currency, thresholdAmt);
                //     dao.setThresholdDB(  );
                listener.onButtonSelectedThreshold(R.id.bt_set_threshold);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentItemSelectedListener){
            listener = (OnFragmentItemSelectedListener) context;

        }else {
            throw new ClassCastException(context.toString());
        }
    }


    public interface  OnFragmentItemSelectedListener{
        public void onButtonSelectedThreshold(int id);
    }
}
