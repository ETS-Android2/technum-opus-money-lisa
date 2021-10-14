package com.technumopus.moneylisa.controller.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.technumopus.moneylisa.MainActivity;
import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.model.TransactionBean;
import com.technumopus.moneylisa.model.UserBean;

import java.util.List;

public class CumulativeAmtAdapter extends RecyclerView.Adapter<CumulativeAmtAdapter.Viewholder> {

    private List<TransactionBean> transactionBeanList;

    public CumulativeAmtAdapter(List<TransactionBean> transactionBeanList) {
        this.transactionBeanList = transactionBeanList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_cumulative_amt_layout, viewGroup, false);
        return new Viewholder( view  );
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        viewholder.setData( transactionBeanList.get( position ) );
    }

    @Override
    public int getItemCount() {
        return transactionBeanList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private TextView amount;
        private TextView currency;
        private TextView usage;
        private TextView usageText;
        TransactionBean transactionBean;
//        private ProgressBar progressBarThreshold;
        private CircularProgressBar circularProgressBar;

        public Viewholder(@NonNull View itemView) {
            super( itemView );
            amount=itemView.findViewById( R.id.tv_amount_home );
            currency=itemView.findViewById( R.id.tv_currency_home );
            usage=itemView.findViewById( R.id.et_usage );
            usageText=itemView.findViewById( R.id.et_usage_text );
//            progressBarThreshold=itemView.findViewById( R.id.pb_threshold );
            circularProgressBar = (CircularProgressBar)itemView.findViewById(R.id.pb_threshold);
        }
        private void setData(TransactionBean transactionBean){

            this.transactionBean = transactionBean;
            amount.setText( Float.toString(transactionBean.get_amount() ));
//            currency.setText( transactionBean.get_currency() );
            currency.setText(MainActivity.CURRENT_USER_BEAN.get_default_currency());
//            float thresAmt=transactionBean.getThresholdAmt();
            float thresAmt= MainActivity.CURRENT_USER_BEAN.get_user_threshold();
            float totalamount=transactionBean.get_amount();
            int progressPercent=0;
            if(thresAmt!=0) {
                progressPercent=(int)totalamount*100/(int)thresAmt;
                usage.setText(String.valueOf(progressPercent) + "%");
            } else {
//                amount.setText( "No expenses yet in " + transactionBean.get_currency());
//                amount.setTextSize(18);
//                currency.setText("");
                usage.setText("");
                usageText.setText("");
                circularProgressBar.setVisibility(circularProgressBar.INVISIBLE);
            }
            System.out.println("********************* Threshold Amount: "+thresAmt+" And the totalamount is :"+totalamount);
//            progressBarThreshold.setProgress( progressPercent );
            if (progressPercent<33) {
//                circularProgressBar.setColor(R.color.yellow_green);
                circularProgressBar.setColor(itemView.getContext().getColor(R.color.yellow_green));
            } else if (progressPercent > 33 && progressPercent < 66) {
//                circularProgressBar.setColor(R.color.dark_orange);
                circularProgressBar.setColor(itemView.getContext().getColor(R.color.dark_orange));
            } else {
//                circularProgressBar.setColor(R.color.indian_red);
                circularProgressBar.setColor(itemView.getContext().getColor(R.color.indian_red));
            }
            circularProgressBar.setProgressWithAnimation(progressPercent, 1500);
        }
    }
}
