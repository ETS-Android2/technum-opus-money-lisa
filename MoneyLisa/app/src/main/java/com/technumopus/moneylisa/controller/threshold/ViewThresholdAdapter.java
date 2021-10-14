package com.technumopus.moneylisa.controller.threshold;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.model.ThresholdBean;

import java.util.List;

public class ViewThresholdAdapter extends RecyclerView.Adapter<ViewThresholdAdapter.ViewHolder> {

    private List<ThresholdBean> thresholdBeanList;
    public ViewThresholdAdapter(List<ThresholdBean> thresholdBeanList) {
        this.thresholdBeanList = thresholdBeanList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.threshold_layout, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.setData( thresholdBeanList.get( position ) );
    }

    @Override
    public int getItemCount() {
        return thresholdBeanList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView amount;
        private TextView currency;
        private ThresholdBean thresholdBean;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            amount=itemView.findViewById( R.id.tv_amt_thr_layout );
            currency=itemView.findViewById( R.id.tv_curr_thr_layout );

        }

        public void setData(ThresholdBean thresholdBean){
            System.out.println("************************** ViewThresholdAdapter :: ViewHolder :: setData :: thresholdBean::"+thresholdBean.toString());
            this.thresholdBean=thresholdBean;
            currency.setText( thresholdBean.getCurrency() );
            if (currency.getText().toString().equals("")) {
                amount.setText("");
            } else {
                amount.setText(Float.toString( thresholdBean.getAmount() ));
            }
        }
    }

    }
