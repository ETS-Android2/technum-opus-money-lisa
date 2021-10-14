package com.technumopus.moneylisa;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.technumopus.moneylisa.controller.addIncome.AddIncomeFragment;
import com.technumopus.moneylisa.controller.currency.CurrencyFragment;
import com.technumopus.moneylisa.controller.help.helpfragment;
import com.technumopus.moneylisa.controller.profiles.ProfilesFragment;
import com.technumopus.moneylisa.controller.settings.SettingsFragment;
import com.technumopus.moneylisa.controller.threshold.ThresholdFragment;
import com.technumopus.moneylisa.databaseHelper.DatabaseHelper;
import com.technumopus.moneylisa.databaseHelper.DailyRecTransExecuter;
import com.technumopus.moneylisa.controller.addtrans.AddTransactionFragment;
import com.technumopus.moneylisa.controller.alltrans.AlltransFragment;
import com.technumopus.moneylisa.controller.blank.BlankFragment;
import com.technumopus.moneylisa.controller.editcats.EditcatsFragment;
import com.technumopus.moneylisa.controller.filterOptions.TxnFilterFragment;
import com.technumopus.moneylisa.controller.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.technumopus.moneylisa.model.UserBean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentItemSelectedListener,
        HomeFragment.OnFragmentItemSelectedListener,
        AddTransactionFragment.OnFragmentItemSelectedListener,
        EditcatsFragment.OnFragmentItemSelectedListener,
        AlltransFragment.OnFragmentItemSelectedListener,
        TxnFilterFragment.OnFragmentItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener,
        ThresholdFragment.OnFragmentItemSelectedListener,
        ProfilesFragment.OnFragmentItemSelectedListener,
        AddIncomeFragment.OnFragmentItemSelectedListener,
        SettingsFragment.OnFragmentItemSelectedListener,
        CurrencyFragment.OnFragmentItemSelectedListener{

    public static String CATS_BACK_BUTTON = "NAV";
    public static String REC_FRAGMENT_CALL="";
    public static String CALL_FROM_SETTINGS="YES";
    public static UserBean CURRENT_USER_BEAN;

    public static SharedPreferences mPrefs;

    public static AddTransactionFragment addtransFragment_temp;

    private AppBarConfiguration mAppBarConfiguration;
     DatabaseHelper dao;

    SharedPreferences prefs = null;

    BlankFragment blankFragment;
    ProfilesFragment profilesFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    public static final int MY_PERMISSIONS_REQUEST_GET_CONTACT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("***************************** check");
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dao=new DatabaseHelper(MainActivity.this);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.menu_blank);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        mPrefs = getSharedPreferences("label", 0);
        String user = mPrefs.getString("user", "default");
//        Toast.makeText(MainActivity.this, user, Toast.LENGTH_SHORT).show();

        if (user.equals("default")) {
            blankFragment = new BlankFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment,blankFragment);
            fragmentTransaction.commit();// add the fragment
        } else {
            DatabaseHelper.CURRENT_USER = user;
            DatabaseHelper.CURRENT_USER_PROFILE = "home";
            profilesFragment = new ProfilesFragment();
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.nav_host_fragment,profilesFragment);
            fragmentTransaction.commit();// add the fragment
        }


        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        DailyRecTransExecuter dailyRecTransExecuter;
        dailyRecTransExecuter = new DailyRecTransExecuter(this);
        dailyRecTransExecuter.setSchedule();

        // -------------------------------------------------------------------------------
        // requesting for contacts permission
        // -------------------------------------------------------------------------------

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_GET_CONTACT);
            }
        } else {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
//            Toast.makeText( this, "First Run", Toast.LENGTH_SHORT ).show();
            System.out.println("================================First Run");
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        closeDrawer();
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
//        if(menuItem.getItemId() == R.id.nav_blank){
//            loadFragment(new BlankFragment());
//        }
        if(menuItem.getItemId() == R.id.nav_home){
            loadFragment(new HomeFragment());
        }
        if(menuItem.getItemId() == R.id.nav_addtrans){
            loadFragment(new AddTransactionFragment());
        }
        if(menuItem.getItemId() == R.id.nav_add_income){
            loadFragment(new AddIncomeFragment());
        }
        if(menuItem.getItemId() == R.id.nav_alltrans){
            MainActivity.REC_FRAGMENT_CALL="";
            loadFragment(new AlltransFragment());
        }

        if(menuItem.getItemId() == R.id.nav_editcats){
            MainActivity.CATS_BACK_BUTTON = "NAV";
            loadFragment(new EditcatsFragment());
        }
        if(menuItem.getItemId() == R.id.nav_rec_exp){
            MainActivity.REC_FRAGMENT_CALL="FROM_REC";
            loadFragment(new AlltransFragment( true ));
        }
        if(menuItem.getItemId() == R.id.nav_logout){
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Are you sure you want to log out?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            DatabaseHelper.CURRENT_USER = "default";
                            DatabaseHelper.CURRENT_USER_PROFILE = "default";
                            Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                            loadFragment(new BlankFragment());
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        if(menuItem.getItemId() == R.id.nav_threshold){
            CALL_FROM_SETTINGS = "NO";
            loadFragment(new ThresholdFragment());
        }
        if(menuItem.getItemId() == R.id.nav_profiles){
            loadFragment(new ProfilesFragment());
        }
        if(menuItem.getItemId() == R.id.nav_settings){
            loadFragment(new SettingsFragment());
        }
        if(menuItem.getItemId() == R.id.nav_help){
            loadFragment(new helpfragment());
        }
//        if(menuItem.getItemId() == R.id.nav_spendanalysis){
//            loadFragment(new SpendAnalysisFragment());
//        }
//        if(menuItem.getItemId() == R.id.nav_edit_income){
//            loadFragment(new EditIncomeFragment());
//        }
        return true;
    }

    private void loadFragment(Fragment secondFragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,secondFragment);
        fragmentTransaction.commit();
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onButtonSelectedBlank(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.blank_bt_signin) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new ProfilesFragment());
        }
        if (id == R.id.blank_bt_signup) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new BlankFragment());
        }
        if (id == R.id.blank_bt_skip) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new ProfilesFragment());
        }
        if (id == R.id.blank_bt_cancel_signup) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new BlankFragment());
        }
        fragmentTransaction.commit();
    }
    @Override
    public void onButtonSelectedHome(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        MainActivity.REC_FRAGMENT_CALL="";
        if (id == R.id.btHomeToAddtrans) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new AddTransactionFragment());
        }
        if (id == R.id.btHomeToAlltrans) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new AlltransFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedAddtrans(int id, String callFor) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.addtrans_bt_editcats) {

            fragmentTransaction.replace(R.id.nav_host_fragment,new EditcatsFragment());
        }
        if (id == R.id.addtrans_mi_cancel) {
            if (callFor.equals("ADD")){
                fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            }
            if (callFor.equals("EDIT")){
                fragmentTransaction.replace(R.id.nav_host_fragment,new AlltransFragment());
            }
        }
        if (id == R.id.addtrans_mi_save) {
            if (callFor.equals("ADD")){
                fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            }
            if (callFor.equals("EDIT")){
                fragmentTransaction.replace(R.id.nav_host_fragment,new AlltransFragment());
            }
        }
        if (id == R.id.toolbar_del_button) {
            if (callFor.equals("ADD")){
                fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            }
            if (callFor.equals("EDIT")){
                fragmentTransaction.replace(R.id.nav_host_fragment,new AlltransFragment());
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedAddInc(int id) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(id == R.id.addinc_mi_save){
            fragmentTransaction.replace( R.id.nav_host_fragment, new HomeFragment() );
        }if(id == R.id.addinc_mi_cancel){
            fragmentTransaction.replace( R.id.nav_host_fragment, new HomeFragment() );
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onButtonSelectedEditcats(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.button_cat_done) {
            if (CATS_BACK_BUTTON.equals("NAV")) {
                fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            }
            if (CATS_BACK_BUTTON.equals("FRG")) {
                fragmentTransaction.replace(R.id.nav_host_fragment,addtransFragment_temp);
            }
            if (MainActivity.CATS_BACK_BUTTON.equals("EDT")) {
                fragmentTransaction.replace(R.id.nav_host_fragment,addtransFragment_temp);
            }
        }
        CATS_BACK_BUTTON = "NAV";
        fragmentTransaction.commit();
    }


    @Override
    public void onButtonSelectedAlltrans(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
   //     REC_FRAGMENT_CALL="";
        if (id == R.id.toolbar_del_button) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new TxnFilterFragment());
        }
//        if (id == R.id.btCancelAlltransToHome) {
//            fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
//        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedFilops(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.btCancelFilopsToAlltrans) {
            fragmentTransaction.replace(R.id.nav_host_fragment,new AlltransFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedFilops(int id, String filterQuery) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        System.out.println("******************* MainActivity : onButtonSelectedFilops with Query Argument : Block Main : before condition; and filterQuery is \n" + filterQuery);
        if(id == R.id.btn_applyFilter){
            fragmentTransaction.replace(R.id.nav_host_fragment, new AlltransFragment(filterQuery));
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedProfiles(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(id == R.id.btProfileHome){
            fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
        }
        if(id == R.id.btProfileBusiness){
            fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
        }
        if(id == R.id.btProfileTravel){
            fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedSettings(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(id == R.id.settings_bt_save){
            CALL_FROM_SETTINGS = "YES";
            fragmentTransaction.replace(R.id.nav_host_fragment, new HomeFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedCurrency(int id) {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(id == R.id.bt_currency_def_save){
            fragmentTransaction.replace(R.id.nav_host_fragment, new SettingsFragment());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onButtonSelectedThreshold(int id) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(id==R.id.bt_set_threshold){
            fragmentTransaction.replace( R.id.nav_host_fragment, new ThresholdFragment() );
        }
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        Button delButton = (Button)this.findViewById( R.id.toolbar_del_button);
        delButton.setVisibility(Button.INVISIBLE);
        try {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            if (toolbar.getTitle().equals(getString(R.string.menu_home))) {
                super.onBackPressed();
            }
            if (toolbar.getTitle().equals(getString(R.string.menu_blank))) {
                super.onBackPressed();
            }
            if (toolbar.getTitle().equals(getString(R.string.menu_profiles))) {
                super.onBackPressed();
            }
            if (toolbar.getTitle().equals(getString(R.string.menu_addtrans))) {
                fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            }
            if (toolbar.getTitle().equals("Edit Expense")) {
                fragmentTransaction.replace(R.id.nav_host_fragment,new AlltransFragment());
            }
            if (toolbar.getTitle().equals(getString(R.string.menu_alltrans))) {
    //            REC_FRAGMENT_CALL="";
                fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            }
            if(toolbar.getTitle().equals(getString( R.string.menu_recursive ))){
                System.out.println("********************* Inside onbackpressed : getTitle : ");
                fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
            }
            if (toolbar.getTitle().equals(getString(R.string.menu_filops))) {
                fragmentTransaction.replace(R.id.nav_host_fragment,new AlltransFragment());
            }
            if (toolbar.getTitle().equals(getString(R.string.menu_editcats))) {
                if (MainActivity.CATS_BACK_BUTTON.equals("NAV")) {
                    fragmentTransaction.replace(R.id.nav_host_fragment,new HomeFragment());
                }
                if (MainActivity.CATS_BACK_BUTTON.equals("FRG")) {
                    fragmentTransaction.replace(R.id.nav_host_fragment,addtransFragment_temp);
                }
                if (MainActivity.CATS_BACK_BUTTON.equals("EDT")) {
                    fragmentTransaction.replace(R.id.nav_host_fragment,addtransFragment_temp);
                }
                CATS_BACK_BUTTON = "NAV";
            }

            if(toolbar.getTitle().equals(getString(R.string.menu_threshold))){
                System.out.println("********************* Inside onbackpressed : getTitle : Threshold");
                if (CALL_FROM_SETTINGS=="YES") {
                    fragmentTransaction.replace( R.id.nav_host_fragment, new SettingsFragment() );
                }
                if (CALL_FROM_SETTINGS=="NO") {
                    fragmentTransaction.replace( R.id.nav_host_fragment, new HomeFragment() );
                }
            }
            if(toolbar.getTitle().equals(getString(R.string.menu_settings))){
                fragmentTransaction.replace( R.id.nav_host_fragment, new HomeFragment() );
            }
            if(toolbar.getTitle().equals(getString(R.string.menu_currency))){
                fragmentTransaction.replace( R.id.nav_host_fragment, new SettingsFragment() );
            }
            if(toolbar.getTitle().equals(getString(R.string.menu_help))){
                fragmentTransaction.replace( R.id.nav_host_fragment, new HomeFragment() );
            }
            fragmentTransaction.commit();
        }catch(Exception e){
            super.onBackPressed();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            System.out.println(requestCode);
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_GET_CONTACT:
                    // handle contact results
//                    Bundle extras = data.getExtras();
//                    Set keys = extras.keySet();
//                    Iterator iterate = keys.iterator();
//                    while (iterate.hasNext()) {
//                        String key = iterate.next();
//                        Log.v(DEBUG_TAG, key + "[" + extras.get(key) + "]");
//                    }
                    Uri result = data.getData();
                    // get the contact id from the Uri
                    String id = result.getLastPathSegment();
                    // query for everything email
                    String email = "";
                    String name = "";
                    String number = "";
                    Cursor cursorEmail = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[]{id}, null);
                    Cursor cursorName = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ "=?", new String[]{id}, null);
                    Cursor cursorNumber = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ "=?", new String[]{id}, null);
                    if (cursorEmail.moveToFirst()) {
                        int emailIdx = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                        email = cursorEmail.getString(emailIdx);
                    }
                    if (cursorName.moveToFirst()) {
                        int nameIdx = cursorName.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        name = cursorName.getString(nameIdx);
                    }
                    if (cursorNumber.moveToFirst()) {
                        int numberIdx = cursorNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        number = cursorNumber.getString(numberIdx);
                    }
//                    System.out.println("================ jkcheck12");
//                    System.out.println(email);
//                    System.out.println(name);
//                    System.out.println(number);
                    EditText et = findViewById(R.id.addtrans_et_borrowed);
                    et.setText(name + " : " + number + " : " + email);
                    break;
            }

        } else {
            // gracefully handle failure

        }
    }


}
