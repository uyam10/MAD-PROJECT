package com.example.weatherjano;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    BottomAppBar bottomAppBar;
    BottomNavigationView bottNavView;
    private FragmentManager fm;
    private Fragment fragment;
    EditText username;
    CardView cardView;
    String uname;
    Button btnsave;
    FragmentTransaction t;
    SharedPreferences sharedPref;
    String saveuname;
    ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //ini the bottomappbar
        bottomAppBar = findViewById(R.id.bottom_appbar);
        //ini the bottomnav bar
        bottNavView= findViewById(R.id.bottom_navigation);
        username= findViewById(R.id.username);
        cardView= findViewById(R.id.cardview);
        btnsave =findViewById(R.id.savename);
        sharedPref= getSharedPreferences("Switch",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        //checking for internet connnection
        connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( connectivityManager.getActiveNetworkInfo() != null && connectivityManager.
                getActiveNetworkInfo().isConnected() )
        {
            saveuname= sharedPref.getString("username","");

            fm = getSupportFragmentManager();
            t = fm.beginTransaction();
            fragment = new Currentlocation();
            if(saveuname.equals(""))
            {
                cardView.setVisibility(View.VISIBLE);
            }
            else {
                uname=saveuname;
                cardView.setVisibility(View.INVISIBLE);
                //checking for null
                if (savedInstanceState == null) {
                    t.replace(R.id.maincontent, fragment);
                    t.commit();
                } else {
                    fragment = (Fragment) fm.findFragmentById(R.id.maincontent);
                }
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
        }


        btnsave.setOnClickListener(view -> {
            //getting username in uname
            uname= username.getText().toString();
            if(uname.equals(""))  // checking for null
            {
                Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            }
            else
            {
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                editor.putString("username",uname).apply();
                cardView.setVisibility(View.INVISIBLE);

                //checking for null
                if (savedInstanceState == null) {
                    t.replace(R.id.maincontent, fragment);
                    t.commit();
                } else {
                    fragment = (Fragment) fm.findFragmentById(R.id.maincontent);
                }
            }
        });

        bottNavView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //getting id from itens.getid()
            case R.id.navigation_current:
                if(uname == null || uname.equals(""))
                {
                    bottNavView.getMenu().getItem(0).setCheckable(false);
                    Toast.makeText(MainActivity.this, "please enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    bottNavView.getMenu().getItem(0).setCheckable(true);
                    FragmentTransaction t = fm.beginTransaction();
                    fragment = new Currentlocation();
                    t.replace(R.id.maincontent, fragment);
                    t.commit();
                }

               return true;

            case R.id.navigation_date:
                if(uname == null || uname.equals(""))
                {
                    bottNavView.getMenu().getItem(1).setCheckable(false);
                    Toast.makeText(MainActivity.this, "please enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    bottNavView.getMenu().getItem(1).setCheckable(true);
                    FragmentTransaction date = fm.beginTransaction();
                    fragment = new date();
                    date.replace(R.id.maincontent, fragment);
                    date.commit();
                }
                return true;

            case R.id.navigation_7days:
                if(uname == null || uname.equals(""))
                {
                    bottNavView.getMenu().getItem(2).setCheckable(false);
                    Toast.makeText(MainActivity.this, "please enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    bottNavView.getMenu().getItem(2).setCheckable(true);
                    FragmentTransaction daysweather = fm.beginTransaction();
                    fragment = new sevendaysweather();
                    daysweather.replace(R.id.maincontent, fragment);
                    daysweather.commit();
                }
                return true;

            case R.id.navigation_setting:
                if(uname == null || uname.equals(""))
                {
                    bottNavView.getMenu().getItem(3).setCheckable(false);
                    Toast.makeText(MainActivity.this, "please enter Your Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    bottNavView.getMenu().getItem(3).setCheckable(true);
                    FragmentTransaction settings = fm.beginTransaction();
                    fragment = new settings();
                    settings.replace(R.id.maincontent, fragment);
                    settings.commit();
                }
                return true;
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
    }
}