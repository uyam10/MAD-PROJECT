package com.example.weatherjano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class date extends Fragment {

    CalendarView calender;
    Spinner spinner;
    String cityname;
    Button getweatherinfo;
    TextView pname, temp,weatherdes,maxtemp,mintemp;
    ImageView weatherimg;
    CardView cardView;
    ProgressDialog progressDialog;
    String tempunit;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date, container, false);
        // use findViewById() to get the
        // CalendarView and TextView
        calender = view.findViewById(R.id.calender);
        spinner = view.findViewById(R.id.static_spinner);
        getweatherinfo= view.findViewById(R.id.getweather);

        pname= view.findViewById(R.id.pname);
        temp = view.findViewById(R.id.curtemp);
        weatherdes = view.findViewById(R.id.desweather);
        maxtemp = view.findViewById(R.id.maxtempp);
        mintemp = view.findViewById(R.id.mintempp);
        weatherimg = view.findViewById(R.id.imgweather);
        cardView = view.findViewById(R.id.maincontent);
        cardView.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading city Data");

        sharedPreferences= getActivity().getSharedPreferences("Switch",Context.MODE_PRIVATE);
        tempunit= sharedPreferences.getString("switch","");

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getActivity(), R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(staticAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                cityname= (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // Add Listener in calendar
        calender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            // In this Listener have one method
            // and in this method we will
            // get the value of DAYS, MONTH, YEARS
            public void onSelectedDayChange(
                    @NonNull CalendarView view, int year,int month,int dayOfMonth) {

                // Store the value of date with
                // format in String type Variable
                // Add 1 in month because month
                // index is start with 0
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                // set this date in TextView for Display
                System.err.println(Date);
            }
        });

        getweatherinfo.setOnClickListener(view1 -> {
            progressDialog.show();
            new Handler().post(new Runnable() {
                @Override
                public void run() {

                    getWeatherInfo();
                }
            });

        });

        return view;
    }

    public void getWeatherInfo() {
        cardView.setVisibility(View.VISIBLE);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String uname = sharedPref.getString("username","");
        System.err.println(">>>>>>>>>>>>>>>>>  "+ uname);
        OkHttpClient client = new OkHttpClient();
        String ou_response;
        System.err.println(cityname);
        Request request = new Request.Builder()
//        api.openweathermap.org/data/2.5/weather?q="+cityname+",IN&appid=a62d425c25ae4413b28bfb832b82ca7e
                .url("https://api.openweathermap.org/data/2.5/weather?q="+cityname+"&appid=a62d425c25ae4413b28bfb832b82ca7e")
//                .url("https://api.openweathermap.org/data/2.5/onecall?lat=26.25&lon=29.325&appid=a62d425c25ae4413b28bfb832b82ca7e")
                .get()
                .addHeader("cityweather", "a62d425c25ae4413b28bfb832b82ca7e")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            ou_response = response.body().string().trim();
            JSONObject result = new JSONObject(ou_response);
            System.err.println(result);

            String name= result.getString("name");
            pname.setText(name);
            JSONArray weather= result.getJSONArray("weather");
            //gettong array at 0 index
            JSONObject o = weather.getJSONObject(0);
            weatherdes.setText(o.getString("description"));
            //setting Icon
            String iconUrl = "http://openweathermap.org/img/w/" + o.getString("icon") + ".png";
            Glide.with(getActivity()).load(iconUrl).placeholder(R.drawable.img).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(weatherimg);
            JSONObject main= result.getJSONObject("main");
            if(tempunit.equals("ON"))
            {
                Double temperature= Double.valueOf(main.getString("temp"));
                temperature = temperature- 273.15;
                long tmp = Math.round((temperature * 9/5) + 32);
                temp.setText(tmp +" \u2109");
                Double mintempp= Double.valueOf(main.getString("temp_min"));
                mintempp=mintempp - 273.15;
                long mintmp = Math.round((mintempp * 9/5) + 32);
                mintemp.setText(String.valueOf(mintmp)+" \u2109");
                Double maxtempp= Double.valueOf(main.getString("temp_max"));
                maxtempp=maxtempp - 273.15;
                long maxtmp = Math.round((maxtempp * 9/5) + 32);
                maxtemp.setText(String.valueOf(maxtmp)+" \u2109");
            }
            else {
                Double temperature= Double.valueOf(main.getString("temp"));
                temperature = temperature- 273.15;
                long tmp = Math.round(temperature);
                temp.setText(tmp +" \u2103");
                Double mintempp= Double.valueOf(main.getString("temp_min"));
                mintempp=mintempp - 273.15;
                long mintmp = Math.round(mintempp);
                mintemp.setText(String.valueOf(mintmp)+" \u2103");
                Double maxtempp= Double.valueOf(main.getString("temp_max"));
                maxtempp=maxtempp - 273.15;
                long maxtmp = Math.round(maxtempp);
                maxtemp.setText(String.valueOf(maxtmp)+" \u2103");
            }
            progressDialog.hide();

        } catch (JSONException e) {
            progressDialog.hide();
            e.printStackTrace();
        } catch (IOException e) {
            progressDialog.hide();
            e.printStackTrace();
        }
    }
}