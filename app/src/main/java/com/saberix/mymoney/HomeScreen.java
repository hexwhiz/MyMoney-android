package com.saberix.mymoney;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

public class HomeScreen extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    private NavigationBarView navigationBarView;
    WalletFragment walletFragment=new WalletFragment();
    PayFragment payFragment=new PayFragment();
    AirplaneModeChangeReceiver airplaneModeChangeReceiver = new AirplaneModeChangeReceiver();

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        txt=findViewById(R.id.welcome_message);
        String nameOfUser=MyMoneyTools.getDataFromFileAt(1,
                ""+SaveSharedPreferences.getUserName(this)+".bin",
                this);
        if(nameOfUser==null){
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
            finish();
        }
        txt.setText("Welcome 😀\n"+nameOfUser);

        navigationBarView=findViewById(R.id.bottomNavigationView);
        navigationBarView.setOnItemSelectedListener(this);
        navigationBarView.getMenu().getItem(0).setCheckable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.walletoption){
            navigationBarView.getMenu().getItem(0).setCheckable(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,walletFragment).commit();
            return true;
        }
        if(item.getItemId()==R.id.payoption){
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,payFragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutoption){
            new AlertDialog.Builder(HomeScreen.this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SaveSharedPreferences.clearUserName(getApplicationContext());
                            finishAffinity();
                            Intent intent=new Intent(HomeScreen.this,MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();
            return true;
        }
        if(item.getItemId()==R.id.aboutoption){
            Intent intent=new Intent(HomeScreen.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }
        if(item.getItemId()==R.id.settingoption){
            Intent intent=new Intent(HomeScreen.this,ProfileSetting.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(HomeScreen.this)
                    .setTitle("Confirm")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {finish();}
                    })
                    .setNegativeButton("No",null)
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        registerReceiver(airplaneModeChangeReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(airplaneModeChangeReceiver);
    }

}