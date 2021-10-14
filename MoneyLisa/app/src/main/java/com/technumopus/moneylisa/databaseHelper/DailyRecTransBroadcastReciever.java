package com.technumopus.moneylisa.databaseHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;

import com.technumopus.moneylisa.model.TransactionBean;

import java.text.ParseException;
import java.util.List;
import java.util.ListIterator;

public class DailyRecTransBroadcastReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper dao = new DatabaseHelper(context);
        List<TransactionBean> transactionBeanList;
        transactionBeanList = dao.fetchTodayRecTransactions();
        ListIterator iter = transactionBeanList.listIterator();
//        while(iter.hasNext()){
//            System.out.println("********************* jkcheck4 ************************");
//            System.out.println(iter.next().getNxtRecDate().toString);
//            System.out.println("********************* jkcheck4 ************************");
//        }
        for (int i=0; i<transactionBeanList.size(); i++) {
            if (transactionBeanList.get(i).get_rec() == true) {
                System.out.println("********************* jkcheck4 check nextrecdate************************");
                System.out.println(transactionBeanList.get(i).getNxtRecDate());
                transactionBeanList.get(i).set_recStartDate(transactionBeanList.get(i).getNxtRecDate());
                try {
                    transactionBeanList.get(i).setNxtRecDate(nextDateCalculator(transactionBeanList.get(i).get_recStartDate(), transactionBeanList.get(i).get_recFrequency()));
                } catch (ParseException e) {
                }
                dao.editTxn(transactionBeanList.get(i));
            }
        }
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
}
