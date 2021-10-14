package com.technumopus.moneylisa.controller.alltrans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.technumopus.moneylisa.R;
import com.technumopus.moneylisa.model.TransactionBean;

import java.util.List;
import java.util.ListIterator;


public class AlltransAdapter extends RecyclerView.Adapter<AlltransAdapter.ViewHolder> {

    private List<TransactionBean> transactionBeanList;
    private static ClickListener clickListener;


    public AlltransAdapter(List<TransactionBean> transactionBeanList) {
        this.transactionBeanList = transactionBeanList;
        ListIterator iter = transactionBeanList.listIterator();
        while(iter.hasNext()){
            System.out.println("********************* AlltransAdapter : AlltransAdapter :  bean starts ************************");
            System.out.println(iter.next().toString());
            System.out.println("********************* AlltransAdapter :  bean ends ************************");

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.all_transaction_layout, viewGroup, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.setData( transactionBeanList.get( position ) );
    }

    @Override
    public int getItemCount() {
        return transactionBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView amount;
        private TextView title;
        private TextView txnDate;
//        private TextView details;
        private TextView currency;
        private ImageView recur;
        private ImageView contact;
        private ImageView catImg;
//        private TextView cat;
        private int tid;

        TransactionBean transactionBean;

        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

            amount=itemView.findViewById( R.id.tv_amount_fetch  );
            title=itemView.findViewById(R.id.tv_trans_title);
            txnDate =itemView.findViewById(R.id.tv_date_added);
//            details=itemView.findViewById(R.id.tv_txn_dtls);
            currency=itemView.findViewById( R.id.tv_currency_fetch );
            recur=itemView.findViewById( R.id.iv_recur );
            contact=itemView.findViewById( R.id.iv_contact );
            catImg=itemView.findViewById( R.id.iv_cat_icon );
//            cat=itemView.findViewById( R.id.tv_cat);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            name = (TextView) itemView.findViewById(R.id.tv_trans_title);
        }
        private void setData(TransactionBean transactionBean){
            this.transactionBean = transactionBean;
            this.tid = transactionBean.getTid();
            amount.setText( Float.toString(transactionBean.get_amount() ));
            title.setText( transactionBean.get_title() );
            txnDate.setText( transactionBean.get_dateTrans() );
//            details.setText( transactionBean.getTxnDetails() );
            currency.setText( transactionBean.get_currency() );
//            cat.setText( transactionBean.get_cat() );
            if (transactionBean.get_rec() == true) {
                recur.setVisibility(ImageView.VISIBLE);
            } else {
                recur.setVisibility(ImageView.INVISIBLE);
            }
            try {
                switch(transactionBean.get_cat()) {
                    case "Grocery":
                        catImg.setImageResource(R.drawable.ic_cat_grocery);
                        break;
                    case "Travel":
                        catImg.setImageResource(R.drawable.ic_cat_travel);
                        break;
                    case "Utility Bill":
                        catImg.setImageResource(R.drawable.ic_cat_utilitybills);
                        break;
                    case "Entertainment":
                        catImg.setImageResource(R.drawable.ic_cat_entertainment);
                        break;
                    case "Dine Out":
                        catImg.setImageResource(R.drawable.ic_cat_dineout);
                        break;
                    case "Rent":
                        catImg.setImageResource(R.drawable.ic_cat_rent);
                        break;
                    case "Loan":
                        catImg.setImageResource(R.drawable.ic_cat_loan);
                        break;
                    case "Education":
                        catImg.setImageResource(R.drawable.ic_cat_education);
                        break;
                    case "Fuel":
                        catImg.setImageResource(R.drawable.ic_cat_gasoline);
                        break;
                    case "Gift":
                        catImg.setImageResource(R.drawable.ic_cat_gift);
                        break;
                    case "Health Care":
                        catImg.setImageResource(R.drawable.ic_cat_health);
                        break;
                    case "Cab":
                        catImg.setImageResource(R.drawable.ic_cat_taxi);
                        break;
                    default:
                        catImg.setImageResource(R.drawable.ic_cat_miscellaneous);
                }
            }catch(Exception e){
            }

        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(this.transactionBean, v);
//            clickListener.onClick(v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(this.transactionBean, v);
//            longclickListener.onLongClick(v);
            return false;
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        AlltransAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(TransactionBean transactionBean, View v);
        void onItemLongClick(TransactionBean transactionBean, View v);
    }
}
