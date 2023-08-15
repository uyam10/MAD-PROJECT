package com.example.weatherjano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherjano.Adapter.MyRecyclerViewAdapter;
import com.example.weatherjano.model.Daily;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class sevendaysweather extends Fragment {

    private RecyclerView recyclerView;
    MyRecyclerViewAdapter myRecyclerViewAdapter;
    View view;
    ProgressDialog progressDialog;
    SharedPreferences sharedPref;
    TextView uname;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.sevendaysweather, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Getting 7 Days Weather Data ");
        progressDialog.show();
        uname= view.findViewById(R.id.sevenusername);

        sharedPref= getActivity().getSharedPreferences("Switch",Context.MODE_PRIVATE);
        uname.setText("Hello, "+sharedPref.getString("username",""));


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                getWeatherInfo();
            }
        });
        return view;
    }

    public void getWeatherInfo() {
         //saving data
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Switch",Context.MODE_PRIVATE);
        Double latitude = Double.valueOf(sharedPref.getString("latitude", ""));
        Double longitude = Double.valueOf(sharedPref.getString("longitude", ""));

        OkHttpClient client = new OkHttpClient();
        String ou_response;
        Request request = new Request.Builder()
               .url("https://api.openweathermap.org/data/2.5/onecall?lat="+ latitude+"&lon="+longitude+"&exclude=minutely,hourly,current&appid=a62d425c25ae4413b28bfb832b82ca7e")
                .get()
                .addHeader("cityweather", "a62d425c25ae4413b28bfb832b82ca7e")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            ou_response = response.body().string().trim();
            JSONObject result = new JSONObject(ou_response);

            JSONArray dailyarray= result.getJSONArray("daily");
            System.err.println(result);
            //creating 3 ArrayList variables
            ArrayList<Daily> data = new ArrayList<>();

            for (int i =0; i<dailyarray.length(); i++){

                //getting all data in date json object
                JSONObject date = dailyarray.getJSONObject(i);
                JSONArray weatherarray = date.getJSONArray("weather");
                JSONObject weatherobj= weatherarray.getJSONObject(0);
                //adding value to ArrayList
                data.add(new Daily(date.getInt("dt"),date.getJSONObject("temp").getDouble("day"),
                        date.getJSONObject("temp").getDouble("min"),date.getJSONObject("temp").getDouble("max"),
                        weatherobj.getString("description"),weatherobj.getString("icon")));

            }
            generateDataList(data);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void generateDataList(ArrayList<Daily> List) {
        recyclerView = view.findViewById(R.id.recycleview);
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(),List);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myRecyclerViewAdapter);
        progressDialog.hide();
    }
}