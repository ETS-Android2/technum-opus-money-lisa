package com.technumopus.moneylisa.databaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.Nullable;

import com.technumopus.moneylisa.model.CategoryBean;
import com.technumopus.moneylisa.model.ThresholdBean;
import com.technumopus.moneylisa.model.TransactionBean;
import com.technumopus.moneylisa.model.UserBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TBL_TRANSACTION_DETAILS = "TRANSACTION_DETAILS";
    public static final String COL_TXN_ID = "TXN_ID";
    public static final String COL_TXN_TITLE = "TXN_TITLE";
    public static final String COL_TXN_AMOUNT = "TXN_AMOUNT";
    public static final String COL_TXN_CURRENCY = "TXN_CURRENCY";
    public static final String COL_TXN_METHOD_OF_PMNT = "TXN_METHOD_OF_PMNT";
    public static final String COL_TXN_DETAILS = "TXN_DETAILS";
    public static final String COL_TXN_NOTES = "TXN_NOTES";
    public static final String COL_TXN_TYPE = "TXN_TYPE";
    public static final String COL_RECURR_FLAG = "RECURR_FLAG";
    public static final String COL_RECURR_START_DATE = "RECURR_START_DATE";
    public static final String COL_RECURR_FREQ = "RECURR_FREQ";
    public static final String COL_ENTRY_DATE = "ENTRY_DATE";
    public static final String COL_ENTRY_TIME = "ENTRY_TIME";
    public static final String COL_TXN_DATE = "TXN_DATE";
    public static final String COL_CATEGORY = "CATEGORY";
    public static final String COL_BORROWED = "BORROWED_FROM"; // For Transaction Table //jk1806
    public static final String TBL_CATEGORY_DTLS = "CATEGORY_DTLS";
    public static final String COL_CAT_NAME = "CAT_NAME";               // For category table
    public static final String COL_CAT_ICON_VECTOR = "CAT_ICON_VECTOR";
    public static final String COL_REC_NEXT_DATE = "REC_NEXT_DATE";
    public static final String COL_USER_PROFILE = "USER_PROFILE";
    public static final String TBL_USER_DATA = "USER_DATA";
    public static final String COL_USER_ID = "USER_ID";
    public static final String COL_USER_NAME = "USER_NAME";
    public static final String COL_USER_EMAIL = "USER_EMAIL";
    public static final String COL_USER_PASSWORD = "USER_PASSWORD";
    public static final String COL_USER_DEFAULT_CURRENCY = "USER_DEFAULT_CURRENCY";
    public static final String COL_USER_THRESHOLD = "USER_THRESHOLD";
    public static final String TBL_USER_THRESHOLD_DETAILS = "USER_THRESHOLD_DETAILS";
    public static final String COL_THRES_AMOUNT = "THRES_AMOUNT";


    public static String CURRENT_USER = "default";
    public static String CURRENT_USER_PROFILE = "home";

    public DatabaseHelper(@Nullable Context context) {
        super( context, "moneylisadb.db", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_create_category= "CREATE TABLE " + TBL_CATEGORY_DTLS + " (" + COL_CAT_NAME + " TEXT PRIMARY KEY , " + COL_USER_NAME + " TEXT)";
        db.execSQL(query_create_category);
        String insertCategoryQuery=("INSERT INTO CATEGORY_DTLS (CAT_NAME, USER_NAME) VALUES ('Grocery','default'),('Travel','default'),('Utility Bill','default'),('Entertainment','default'),('Dine Out','default'),('Rent','default'),('Loan','default'),('Miscellaneous','default'),('Education','default'),('Fuel','default'),('Gift','default'),('Health Care','default'),('Cab','default');");
        db.execSQL( insertCategoryQuery );
        String query_create_txn= "CREATE TABLE " + TBL_TRANSACTION_DETAILS + " (" + COL_TXN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TXN_TITLE + " TEXT, " + COL_TXN_AMOUNT + " REAL, " + COL_TXN_CURRENCY + " TEXT, " + COL_TXN_METHOD_OF_PMNT + " TEXT, " + COL_TXN_DETAILS + " TEXT, " + COL_TXN_NOTES + " TEXT, " + COL_TXN_TYPE + " TEXT, " + COL_RECURR_FLAG + " BOOL, " + COL_RECURR_START_DATE + " TEXT, " + COL_RECURR_FREQ + " TEXT, " + COL_ENTRY_DATE + " TEXT, " + COL_ENTRY_TIME + " TEXT, " + COL_TXN_DATE + " TEXT, " + COL_REC_NEXT_DATE + " TEXT, " + COL_CATEGORY +" TEXT, " + COL_USER_NAME + " TEXT, " + COL_USER_PROFILE + " TEXT, " + COL_BORROWED + " TEXT)"; //jk1806
        db.execSQL(query_create_txn);
        String query_create_userdata= "CREATE TABLE " + TBL_USER_DATA + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_USER_EMAIL + " TEXT, " + COL_USER_NAME + " TEXT, " + COL_USER_PASSWORD + " TEXT, " + COL_USER_DEFAULT_CURRENCY + " TEXT, " + COL_USER_THRESHOLD + " REAL)";
        db.execSQL(query_create_userdata);
        String insertDefaultUserQuery=("INSERT INTO USER_DATA (USER_NAME, USER_EMAIL, USER_PASSWORD, USER_DEFAULT_CURRENCY, USER_THRESHOLD) VALUES ('default','default','default','EUR',5);");
        db.execSQL( insertDefaultUserQuery );
        String createThresholdQuery= "CREATE TABLE " + TBL_USER_THRESHOLD_DETAILS + " ("+ COL_TXN_CURRENCY + " TEXT PRIMARY KEY, " + COL_THRES_AMOUNT + " REAL );";
        System.out.println("****************************** createThresholdQuery :"+createThresholdQuery);
        db.execSQL(createThresholdQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertTxn(TransactionBean transactionBean){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COL_TXN_TITLE, transactionBean.get_title() );
        contentValues.put( COL_TXN_AMOUNT, transactionBean.get_amount() );
        contentValues.put( COL_TXN_CURRENCY, transactionBean.get_currency() );
        contentValues.put( COL_TXN_METHOD_OF_PMNT, transactionBean.get_mop() );
        contentValues.put( COL_TXN_DETAILS, transactionBean.getTxnDetails() ); // create variable
        contentValues.put( COL_TXN_NOTES, transactionBean.get_notes() );
        contentValues.put( COL_TXN_TYPE, transactionBean.getTxnType() );
        contentValues.put( COL_RECURR_FLAG, transactionBean.get_rec() );
        contentValues.put( COL_RECURR_START_DATE, transactionBean.get_recStartDate() );
        contentValues.put( COL_RECURR_FREQ, transactionBean.get_recFrequency() );
        contentValues.put( COL_ENTRY_DATE, transactionBean.get_tDate() );
        contentValues.put( COL_ENTRY_TIME, transactionBean.get_tTime() );
        contentValues.put( COL_TXN_DATE, transactionBean.get_dateTrans() );
        contentValues.put( COL_CATEGORY, transactionBean.get_cat() );
        contentValues.put( COL_REC_NEXT_DATE, transactionBean.getNxtRecDate() );
        contentValues.put( COL_USER_NAME, CURRENT_USER );
        contentValues.put( COL_USER_PROFILE, CURRENT_USER_PROFILE );
        contentValues.put( COL_BORROWED, transactionBean.get_borrowedFrom() ); //jk1806

        long insert = sqLiteDatabase.insert(TBL_TRANSACTION_DETAILS, null, contentValues);
        if(insert==-1)
            return false;
        else
            return true;
    }

    public boolean editTxn(TransactionBean transactionBean) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COL_TXN_TITLE, transactionBean.get_title() );
        contentValues.put( COL_TXN_AMOUNT, transactionBean.get_amount() );
        contentValues.put( COL_TXN_CURRENCY, transactionBean.get_currency() );
        contentValues.put( COL_TXN_METHOD_OF_PMNT, transactionBean.get_mop() );
        contentValues.put( COL_TXN_DETAILS, transactionBean.getTxnDetails() ); // create variable
        contentValues.put( COL_TXN_NOTES, transactionBean.get_notes() );
        contentValues.put( COL_TXN_TYPE, transactionBean.getTxnType() );
        contentValues.put( COL_RECURR_FLAG, transactionBean.get_rec() );
        contentValues.put( COL_RECURR_START_DATE, transactionBean.get_recStartDate() );
        contentValues.put( COL_RECURR_FREQ, transactionBean.get_recFrequency() );
        contentValues.put( COL_ENTRY_DATE, transactionBean.get_tDate() );
        contentValues.put( COL_ENTRY_TIME, transactionBean.get_tTime() );
        contentValues.put( COL_TXN_DATE, transactionBean.get_dateTrans() );
        contentValues.put( COL_CATEGORY, transactionBean.get_cat() );
        contentValues.put( COL_REC_NEXT_DATE, transactionBean.getNxtRecDate() );
        contentValues.put( COL_BORROWED, transactionBean.get_borrowedFrom() ); //jk1806
        long insert = sqLiteDatabase.update(TBL_TRANSACTION_DETAILS, contentValues, COL_TXN_ID + " = " + transactionBean.getTid(), null);
        sqLiteDatabase.close();
        if(insert==-1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean delTxn(int tid) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        long insert = sqLiteDatabase.delete(TBL_TRANSACTION_DETAILS, COL_TXN_ID + " = " + tid, null);
        sqLiteDatabase.close();
        if(insert==-1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean insertCategory(CategoryBean categoryBean){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COL_CAT_NAME, categoryBean.getCatName()  );
        contentValues.put( COL_USER_NAME, CURRENT_USER  );
    //    contentValues.put( COL_CAT_ICON_VECTOR, categoryBean.getCatVector() );

        long insert = sqLiteDatabase.insert(TBL_CATEGORY_DTLS, null, contentValues);
        if(insert==-1)
            return false;
        else
            return true;
    }

    public List<TransactionBean> fetchAllTransactions(){
        List<TransactionBean> allTransactionList = new ArrayList<>();
        String fetchAllQuery="SELECT * FROM "+TBL_TRANSACTION_DETAILS + " WHERE " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " AND " + COL_USER_PROFILE + " = " + "\"" + CURRENT_USER_PROFILE + "\""+" AND "+COL_TXN_TYPE+" = 'expense'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( fetchAllQuery, null );
        if(cursor.moveToFirst()){
            do{
                TransactionBean retrievedTxn=new TransactionBean(  );
                retrievedTxn.setTid(cursor.getInt( 0 ));
                retrievedTxn.set_title(cursor.getString( 1 ));
                retrievedTxn.set_amount( cursor.getFloat( 2 ) );
                retrievedTxn.set_currency( cursor.getString( 3 ) );
                retrievedTxn.set_mop( cursor.getString( 4 ) );
                retrievedTxn.setTxnDetails( cursor.getString( 5 ) );
                retrievedTxn.set_notes( cursor.getString( 6 ) );
                retrievedTxn.setTxnType( cursor.getString( 7 ) );
                System.out.println("******************** fetching boolean "+cursor.getString(8));
                boolean temp_rec=false;
                if (cursor.getString(8).equals("1")){
                    temp_rec = true;
                } else{
                    temp_rec = false;
                }
                retrievedTxn.set_rec( temp_rec );
                retrievedTxn.set_recStartDate( cursor.getString( 9 ) );
                retrievedTxn.set_recFrequency( cursor.getString( 10 ) );
                retrievedTxn.set_tDate( cursor.getString( 11 ) );
                retrievedTxn.set_tTime( cursor.getString( 12 ) );
                retrievedTxn.set_dateTrans( cursor.getString( 13 ) );
                retrievedTxn.setNxtRecDate( cursor.getString( 14 ) );
                retrievedTxn.set_cat( cursor.getString( 15 ) );
                retrievedTxn.set_borrowedFrom( cursor.getString( 18 ) ); //jk1806

                allTransactionList.add( retrievedTxn );

/*
                System.out.println("***************** Validate fetch values here");
                System.out.println(retrievedTxn.toString());
*/

            }while(cursor.moveToNext());
        }else{
            TransactionBean elseTxnBean=new TransactionBean();
            elseTxnBean.set_title( "No Transactions To Show" );
            allTransactionList.add( elseTxnBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return allTransactionList;
    }

    public List<TransactionBean> fetchFilteredTransactions(String filterQuery) {
        List<TransactionBean> filteredTransactionList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( filterQuery, null );
        if(cursor.moveToFirst()){
            do{
                TransactionBean retrievedTxn=new TransactionBean(  );
                retrievedTxn.setTid(cursor.getInt( 0 ));
                retrievedTxn.set_title(cursor.getString( 1 ));
                retrievedTxn.set_amount( cursor.getFloat( 2 ) );
                retrievedTxn.set_currency( cursor.getString( 3 ) );
                retrievedTxn.set_mop( cursor.getString( 4 ) );
                retrievedTxn.setTxnDetails( cursor.getString( 5 ) );
                retrievedTxn.set_notes( cursor.getString( 6 ) );
                retrievedTxn.setTxnType( cursor.getString( 7 ) );
                System.out.println("******************** fetching boolean "+cursor.getString(8));
                retrievedTxn.set_rec( Boolean.parseBoolean( cursor.getString(8) ) );
                retrievedTxn.set_recStartDate( cursor.getString( 9 ) );
                retrievedTxn.set_recFrequency( cursor.getString( 10 ) );
                retrievedTxn.set_tDate( cursor.getString( 11 ) );
                retrievedTxn.set_tTime( cursor.getString( 12 ) );
                retrievedTxn.set_dateTrans( cursor.getString( 13 ) );
                retrievedTxn.setNxtRecDate( cursor.getString( 14 ) );
                retrievedTxn.set_cat( cursor.getString( 15 ) );
                retrievedTxn.set_borrowedFrom( cursor.getString( 18 ) ); //jk1806

                filteredTransactionList.add( retrievedTxn );

/*
                System.out.println("***************** Validate fetch values here");
                System.out.println(retrievedTxn.toString());
*/

            }while(cursor.moveToNext());
        }else{
            TransactionBean elseTxnBean=new TransactionBean();
            elseTxnBean.set_title( "No Transactions To Show" );
            filteredTransactionList.add( elseTxnBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return filteredTransactionList;

    }


    public List<CategoryBean> fetchAllCategories(){
        List<CategoryBean> categoryBeanList = new ArrayList<>(  );
        categoryBeanList.add(0,  new CategoryBean( "Category" ) );
        String fetchALLQuery="SELECT * FROM "+TBL_CATEGORY_DTLS + " WHERE " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " OR " + COL_USER_NAME + " = " + "\"" + "default" + "\"";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( fetchALLQuery, null );
        if(cursor.moveToFirst()){
            do{
                CategoryBean retrievedCategory=new CategoryBean(  );
                retrievedCategory.setCatName( cursor.getString( 0 ) );
                categoryBeanList.add( retrievedCategory );
            }while(cursor.moveToNext());
        }else{
            CategoryBean elseCategoryBean = new CategoryBean("Error fetching categories.");
            categoryBeanList.add( elseCategoryBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return categoryBeanList;
    }

    public List<String> fetchAllMonths(){
        List<String> monthList = new ArrayList<>(  );
        monthList.add( 0, "Month" );
        //     String fetchQuery="SELECT DISTINCT "+COL_ENTRY_DATE+ " FROM "+TBL_TRANSACTION_DETAILS;

        //      String fetchQuery="SELECT CASE month WHEN '01' THEN 'January' WHEN '02' THEN 'Febuary' WHEN '03' THEN 'March' WHEN '04' THEN 'April' WHEN '05' THEN 'May' WHEN '06' THEN 'June' WHEN '07' THEN 'July' WHEN '08' THEN 'August' WHEN '09' THEN 'September' WHEN '10' THEN 'October' WHEN '11' THEN 'November' WHEN '12' THEN 'December' END || ' ' || year AS dates FROM (SELECT DISTINCT strftime('%m', ENTRY_DATE) month, strftime('%Y', ENTRY_DATE) year FROM TRANSACTION_DETAILS) ORDER BY year, month;";

        String fetchQuery="SELECT DISTINCT SUBSTR("+COL_TXN_DATE+ ", 4) FROM "+TBL_TRANSACTION_DETAILS + " WHERE " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " AND " + COL_USER_PROFILE + " = " + "\"" + CURRENT_USER_PROFILE + "\""+" AND "+COL_TXN_TYPE+" = 'expense'";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( fetchQuery, null );
        if(cursor.moveToFirst()){
            do{
                String dateFetched=cursor.getString( 0 );
                monthList.add( dateFetched );
            }while(cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return  monthList;
    }

    public List<String> fetchAllCategories1() {
        List<String> allCategoryList = new ArrayList<>();
        String fetchAllQuery = "SELECT * FROM " + TBL_CATEGORY_DTLS + " WHERE " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " OR " + COL_USER_NAME + " = " + "\"" + "default" + "\"";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(fetchAllQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String retrievedCat = new String();
                // retrievedCat.setCategoryId(cursor.getInt(0));
                allCategoryList.add(cursor.getString(0));

                // allCategoryList.add(retrievedCat);

/*
                System.out.println("******* Validate fetch values here");
                System.out.println(retrievedTxn.toString());
*/

            } while (cursor.moveToNext());

/*            while (cursor.isAfterLast() == false) {
                cursor.getString(1);
                cursor.moveToNext();
            }*/
        }
        cursor.close();
        sqLiteDatabase.close();
        return allCategoryList;
    }

    public List<TransactionBean> fetchTodayRecTransactions(){
        String todayDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<TransactionBean> allTransactionList = new ArrayList<>();
        String fetchAllQuery="SELECT * FROM "+TBL_TRANSACTION_DETAILS + " WHERE " + COL_REC_NEXT_DATE + " = " + "\"" + todayDate + "\""+" AND "+COL_TXN_TYPE+" = 'expense'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( fetchAllQuery, null );
        if(cursor.moveToFirst()){
            do{
                TransactionBean retrievedTxn=new TransactionBean(  );
                retrievedTxn.setTid(cursor.getInt( 0 ));
                retrievedTxn.set_title(cursor.getString( 1 ));
                retrievedTxn.set_amount( cursor.getFloat( 2 ) );
                retrievedTxn.set_currency( cursor.getString( 3 ) );
                retrievedTxn.set_mop( cursor.getString( 4 ) );
                retrievedTxn.setTxnDetails( cursor.getString( 5 ) );
                retrievedTxn.set_notes( cursor.getString( 6 ) );
                retrievedTxn.setTxnType( cursor.getString( 7 ) );
                boolean temp_rec=false;
                if (cursor.getString(8).equals("1")){
                    temp_rec = true;
                } else{
                    temp_rec = false;
                }
                retrievedTxn.set_rec( temp_rec );
                retrievedTxn.set_recStartDate( cursor.getString( 9 ) );
                retrievedTxn.set_recFrequency( cursor.getString( 10 ) );
                retrievedTxn.set_tDate( cursor.getString( 11 ) );
                retrievedTxn.set_tTime( cursor.getString( 12 ) );
                retrievedTxn.set_dateTrans( cursor.getString( 13 ) );
                retrievedTxn.setNxtRecDate( cursor.getString( 14 ) );
                retrievedTxn.set_cat( cursor.getString( 15 ) );
                retrievedTxn.set_borrowedFrom( cursor.getString( 18 ) ); //jk1806

                allTransactionList.add( retrievedTxn );

/*
                System.out.println("***************** Validate fetch values here");
                System.out.println(retrievedTxn.toString());
*/

            }while(cursor.moveToNext());
        }else{
            TransactionBean elseTxnBean=new TransactionBean();
            elseTxnBean.set_title( "No Transactions To Show" );
            allTransactionList.add( elseTxnBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return allTransactionList;
    }

    public List<TransactionBean> fetchCurrMonthTxn(String thisMonth, String span){
        String[] array = thisMonth.split("\\|", -1);
        List<TransactionBean> cumulativeMonthlyList = new ArrayList<>();
        Map<String, Float> thresholdMap=new HashMap<>(  );
        String query = "SELECT "+COL_TXN_CURRENCY+ " , SUM("+COL_TXN_AMOUNT+") FROM "+TBL_TRANSACTION_DETAILS+" WHERE SUBSTR("+COL_TXN_DATE+ ", 4) = '"+thisMonth+"' AND " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " AND " + COL_USER_PROFILE + " = " + "\"" + CURRENT_USER_PROFILE + "\"" +" AND "+COL_TXN_TYPE+" = 'expense'"+ " GROUP BY "+COL_TXN_CURRENCY+" ORDER BY "+COL_TXN_CURRENCY+" DESC";
        if (span.equals("month")) {
            query="SELECT "+COL_TXN_CURRENCY+ " , SUM("+COL_TXN_AMOUNT+") FROM "+TBL_TRANSACTION_DETAILS+" WHERE SUBSTR("+COL_TXN_DATE+ ", 4) = '"+thisMonth+"' AND " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " AND " + COL_USER_PROFILE + " = " + "\"" + CURRENT_USER_PROFILE + "\"" +" AND "+COL_TXN_TYPE+" = 'expense'"+ " GROUP BY "+COL_TXN_CURRENCY+" ORDER BY "+COL_TXN_CURRENCY+" DESC";
        } else if (span.equals("today")) {
            query="SELECT "+COL_TXN_CURRENCY+ " , SUM("+COL_TXN_AMOUNT+") FROM "+TBL_TRANSACTION_DETAILS+" WHERE SUBSTR("+COL_TXN_DATE+ ", 1) = '"+thisMonth+"' AND " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " AND " + COL_USER_PROFILE + " = " + "\"" + CURRENT_USER_PROFILE + "\"" +" AND "+COL_TXN_TYPE+" = 'expense'"+ " GROUP BY "+COL_TXN_CURRENCY+" ORDER BY "+COL_TXN_CURRENCY+" DESC";
        } else if (span.equals("week")) {
            query="SELECT "+COL_TXN_CURRENCY+ " , SUM("+COL_TXN_AMOUNT+") FROM "+TBL_TRANSACTION_DETAILS+" WHERE ("+ COL_TXN_DATE+ " = '"+array[1]+"' OR "+ COL_TXN_DATE+ " = '"+array[2]+"' OR "+ COL_TXN_DATE+ " = '"+array[3]+"' OR "+ COL_TXN_DATE+ " = '"+array[4]+"' OR "+ COL_TXN_DATE+ " = '"+array[5]+"' OR "+ COL_TXN_DATE+ " = '"+array[6]+"' OR "+ COL_TXN_DATE+ " = '"+array[7]+ "') AND " + COL_USER_NAME + " = " + "\"" + CURRENT_USER + "\"" + " AND " + COL_USER_PROFILE + " = " + "\"" + CURRENT_USER_PROFILE + "\"" +" AND "+COL_TXN_TYPE+" = 'expense'"+ " GROUP BY "+COL_TXN_CURRENCY+" ORDER BY "+COL_TXN_CURRENCY+" DESC";
            System.out.println("=========== sql query =========");
            System.out.println(query);
        }
        String queryThreshold="SELECT * FROM "+TBL_USER_THRESHOLD_DETAILS;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( query, null );
        Cursor cursorThreshold=sqLiteDatabase.rawQuery( queryThreshold, null );

        if(cursorThreshold.moveToFirst()){
            do{
                thresholdMap.put( cursorThreshold.getString( 0 ), cursorThreshold.getFloat( 1 ) );
            }while(cursorThreshold.moveToNext());
        }


        if(cursor.moveToFirst()){
            do{
                TransactionBean retrievedTxn=new TransactionBean(  );
                retrievedTxn.set_currency( cursor.getString( 0 ) );
                retrievedTxn.set_amount( cursor.getFloat( 1 ) );
                try {
                    retrievedTxn.setThresholdAmt( thresholdMap.get( retrievedTxn.get_currency() ) );
                }catch (Exception e){
                    System.out.println("********************* Inside Exception :: DAO :: fetchCurrMonthTxn :: retrievedTxn.setThresholdAmt :: Currency not found.. Apply logic here:"+e);
                }
                cumulativeMonthlyList.add( retrievedTxn );
            }while(cursor.moveToNext());
        }else{
            TransactionBean elseTxnBean=new TransactionBean();
            elseTxnBean.set_currency("EUR");
            elseTxnBean.set_amount(0);
            cumulativeMonthlyList.add( elseTxnBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return  cumulativeMonthlyList;
    }

    public List<TransactionBean> fetchRecTransactions(String filterQuery) {
        List<TransactionBean> filteredTransactionList = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( filterQuery, null );
        if(cursor.moveToFirst()){
            do{
                TransactionBean retrievedTxn=new TransactionBean(  );
                retrievedTxn.setTid(cursor.getInt( 0 ));
                retrievedTxn.set_title(cursor.getString( 1 ));
                retrievedTxn.set_amount( cursor.getFloat( 2 ) );
                retrievedTxn.set_currency( cursor.getString( 3 ) );
                retrievedTxn.set_mop( cursor.getString( 4 ) );
                retrievedTxn.setTxnDetails( cursor.getString( 5 ) );
                retrievedTxn.set_notes( cursor.getString( 6 ) );
                retrievedTxn.setTxnType( cursor.getString( 7 ) );
                System.out.println("******************** fetching boolean "+cursor.getString(8));
                retrievedTxn.set_rec( Boolean.parseBoolean( cursor.getString(8) ) );
                retrievedTxn.set_recStartDate( cursor.getString( 9 ) );
                retrievedTxn.set_recFrequency( cursor.getString( 10 ) );
                retrievedTxn.set_tDate( cursor.getString( 11 ) );
                retrievedTxn.set_tTime( cursor.getString( 12 ) );
                retrievedTxn.set_dateTrans( cursor.getString( 13 ) );
                retrievedTxn.setNxtRecDate( cursor.getString( 14 ) );
                retrievedTxn.set_cat( cursor.getString( 15 ) );
                retrievedTxn.set_borrowedFrom( cursor.getString( 18 ) ); //jk1806

                filteredTransactionList.add( retrievedTxn );

/*
                System.out.println("***************** Validate fetch values here");
                System.out.println(retrievedTxn.toString());
*/

            }while(cursor.moveToNext());
        }else{
            TransactionBean elseTxnBean=new TransactionBean();
            elseTxnBean.set_title( "No Transactions To Show" );
            filteredTransactionList.add( elseTxnBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return filteredTransactionList;

    }

    public boolean insertUser(UserBean userBean){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COL_USER_NAME, userBean.get_user_name() );
        contentValues.put( COL_USER_EMAIL, userBean.get_user_email() );
        contentValues.put( COL_USER_PASSWORD, userBean.get_user_password() );

        long insert = sqLiteDatabase.insert(TBL_USER_DATA, null, contentValues);
        if(insert==-1)
            return false;
        else
            return true;
    }

    public boolean insertUserThreshold(float threshold){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String runQuery="UPDATE "+TBL_USER_DATA+" SET "+COL_USER_THRESHOLD+" = "+threshold+" WHERE "+COL_USER_NAME+"='"+CURRENT_USER+"'" ;
        sqLiteDatabase.execSQL(runQuery);
        sqLiteDatabase.close();
        return true;
    }

    public boolean insertUserDefaultCurrency(String defaultCurrency){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String runQuery="UPDATE "+TBL_USER_DATA+" SET "+COL_USER_DEFAULT_CURRENCY+"='"+defaultCurrency+"'"+" WHERE "+COL_USER_NAME+"='"+CURRENT_USER+"'" ;
        sqLiteDatabase.execSQL(runQuery);
        sqLiteDatabase.close();
        return true;
    }

    public List<UserBean> fetchAllUsers(){
        List<UserBean> userBeanList = new ArrayList<>(  );
        String fetchALLQuery="SELECT * FROM "+TBL_USER_DATA;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( fetchALLQuery, null );
        if(cursor.moveToFirst()){
            do{
                UserBean retrievedUser=new UserBean(  );
                retrievedUser.set_user_email( cursor.getString( 1 ) );
                retrievedUser.set_user_name( cursor.getString( 2 ) );
                retrievedUser.set_user_password( cursor.getString( 3) );
                retrievedUser.set_default_currency( cursor.getString( 4) );
                retrievedUser.set_user_threshold( cursor.getFloat( 5) );
                userBeanList.add( retrievedUser );
            }while(cursor.moveToNext());
        }else{
            UserBean userBean = new UserBean("error","error");
            userBeanList.add( userBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return userBeanList;
    }

    public void setThresholdDB(String currency, float amount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String checkQuery="Select count(*) FROM "+ TBL_USER_THRESHOLD_DETAILS+" WHERE "+COL_TXN_CURRENCY+"='"+currency+"'";
        String runQuery="";
        Cursor checkCursor=sqLiteDatabase.rawQuery(checkQuery, null);
        int valuePresent=0;
        if(checkCursor.moveToFirst()){
            valuePresent=checkCursor.getInt( 0 );
        }while (checkCursor.moveToNext());

        if(valuePresent==0){
            runQuery="INSERT INTO "+TBL_USER_THRESHOLD_DETAILS+" VALUES ('"+currency+"', "+amount+")";
        }if(valuePresent>0){
            runQuery="UPDATE "+TBL_USER_THRESHOLD_DETAILS+" SET "+COL_THRES_AMOUNT+" = "+amount+" WHERE "+COL_TXN_CURRENCY+"='"+currency+"'" ;
        }
        sqLiteDatabase.execSQL(runQuery);
    }

    public List<ThresholdBean> getThresholdEntries() {
        List<ThresholdBean> thresholdBeanList = new ArrayList<>(  );

        String query= "SELECT * FROM USER_THRESHOLD_DETAILS ORDER BY TXN_CURRENCY";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery( query, null );

        if(cursor.moveToFirst()) {
            do {
                ThresholdBean thresholdBean = new ThresholdBean();
                thresholdBean.setCurrency( cursor.getString( 0 ) );
                thresholdBean.setAmount( cursor.getFloat( 1 ) );

                thresholdBeanList.add( thresholdBean );
            } while (cursor.moveToNext());
        }else{
            ThresholdBean thresholdBean=new ThresholdBean();
            thresholdBean.setCurrency( "" );
            thresholdBeanList.add( thresholdBean );
        }
        cursor.close();
        sqLiteDatabase.close();
        return thresholdBeanList;
    }


}
