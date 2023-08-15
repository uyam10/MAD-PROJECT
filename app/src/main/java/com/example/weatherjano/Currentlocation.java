package com.example.weatherjano;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Currentlocation extends Fragment {

    int flag = 0;
    Location mLastLocation, location;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    ImageView weatherimg;
    TextView username, placename,temp,weatherinfo,mintemp,maxtemp,feeltemp,windspeed,winddirection,
            desweatherinfo,pressure,humidity,visiblity, sunrise,sunset;
     ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    String tempunit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.currentlocation,container,false);

        placename= view.findViewById(R.id.placename);
        temp= view.findViewById(R.id.temp);
        weatherinfo= view.findViewById(R.id.cloudinfo);
        mintemp= view.findViewById(R.id.mintemp);
        maxtemp= view.findViewById(R.id.maxtemp);
        feeltemp= view.findViewById(R.id.windfeelslike);
        windspeed= view.findViewById(R.id.windspeed);
        winddirection= view.findViewById(R.id.winddirection);
        desweatherinfo= view.findViewById(R.id.weathetinfo);
        pressure= view.findViewById(R.id.pressure);
        humidity= view.findViewById(R.id.humidity);
        visiblity= view.findViewById(R.id.visiblity);
        sunrise= view.findViewById(R.id.sunrise);
        sunset= view.findViewById(R.id.sunset);
        weatherimg= view.findViewById(R.id.imageView2);
        username= view.findViewById(R.id.uname);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Refershing Weather Data ");

        progressDialog.show();

        sharedPreferences= getActivity().getSharedPreferences("Switch",Context.MODE_PRIVATE);
        username.setText("Hello, "+sharedPreferences.getString("username",""));
        tempunit= sharedPreferences.getString("switch","");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //click event over Bottom bar menu item
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getLastLocation();
            }
        });
        return view;
    }

    public void getWeatherInfo(double latitude, double longitude)  {
        OkHttpClient client = new OkHttpClient();
        String ou_response;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latitude", String.valueOf(latitude));
        editor.putString("longitude", String.valueOf(longitude));
        editor.commit();
        Request request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?lat="+ latitude+"&lon="+longitude+"&appid=a62d425c25ae4413b28bfb832b82ca7e")
                .get()
                .addHeader("weatherjano", "a62d425c25ae4413b28bfb832b82ca7e")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();

            ou_response = response.body().string().trim();

        JSONObject result = new JSONObject(ou_response);
        System.err.println(result);
            String name= result.getString("name");
            JSONArray weather= result.getJSONArray("weather");
            //gettong array at 0 index
            JSONObject o = weather.getJSONObject(0);
             weatherinfo.setText(o.getString("main"));
             desweatherinfo.setText(o.getString("description"));
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
            //getting value from main JSON Object
            JSONObject main= result.getJSONObject("main");
            if(tempunit.equals("ON"))
            {
                Double temperature= Double.valueOf(main.getString("temp"));
                temperature = temperature- 273.15;
                long tmp = Math.round((temperature * 9/5) + 32);
                temp.setText(tmp +" \u2109");
                Double feeltempp= Double.valueOf(main.getString("feels_like"));
                feeltempp= feeltempp - 273.15;
                long feeltmp = Math.round((feeltempp * 9/5) + 32);
                feeltemp.setText(String.valueOf(feeltmp));
                Double mintempp= Double.valueOf(main.getString("temp_min"));
                mintempp=mintempp - 273.15;
                long mintmp = Math.round((mintempp * 9/5) + 32);
                mintemp.setText(String.valueOf(mintmp));
                Double maxtempp= Double.valueOf(main.getString("temp_max"));
                maxtempp=maxtempp - 273.15;
                long maxtmp = Math.round((maxtempp * 9/5) + 32);
                maxtemp.setText(String.valueOf(maxtmp));
            }
            else {
                Double temperature= Double.valueOf(main.getString("temp"));
                temperature = temperature- 273.15;
                long tmp = Math.round(temperature);
                temp.setText(tmp +" \u2103");
                Double feeltempp= Double.valueOf(main.getString("feels_like"));
                feeltempp= feeltempp - 273.15;
                long feeltmp = Math.round(feeltempp);
                feeltemp.setText(String.valueOf(feeltmp));
                Double mintempp= Double.valueOf(main.getString("temp_min"));
                mintempp=mintempp - 273.15;
                long mintmp = Math.round(mintempp);
                mintemp.setText(String.valueOf(mintmp));
                Double maxtempp= Double.valueOf(main.getString("temp_max"));
                maxtempp=maxtempp - 273.15;
                long maxtmp = Math.round(maxtempp);
                maxtemp.setText(String.valueOf(maxtmp));
            }

            pressure.setText((main.getString("pressure")).concat(" hPa"));
            humidity.setText(main.getString("humidity").concat("%"));
            int visi= Integer.parseInt(result.getString("visibility")) /1000;
            visiblity.setText(visi+ " KM");
            JSONObject wind= result.getJSONObject("wind");
            windspeed.setText(wind.getString("speed")+ " m/s");

            Double degree= Double.valueOf(wind.getString("deg"));
            if (degree >0 && degree <90)
            {
                winddirection.setText("NE");
            }
            else if(degree <270 && degree > 180)
            {
                winddirection.setText("SW");
            }
            else if(degree>90 && degree <180)
            {
                winddirection.setText("SE");
            }
            else if(degree <360 && degree >270)
            {
                winddirection.setText("NW");
            }
            placename.setText(name);
            JSONObject sys= result.getJSONObject("sys");
            String sunrisedata= sys.getString("sunrise");
            String sunsetdata= sys.getString("sunset");
            long dv = Long.valueOf(sunrisedata)*1000;// its need to be in milisecond
            Date df = new java.util.Date(dv);
            String vv = new SimpleDateFormat("MM dd, yyyy hh:mma", Locale.forLanguageTag("IN")).format(df);
            sunrise.setText(vv);
            long ss = Long.valueOf(sunsetdata)*1000;// its need to be in milisecond
            Date dss = new java.util.Date(ss);
            String ssv = new SimpleDateFormat("MM dd, yyyy hh:mma", Locale.forLanguageTag("IN")).format(dss);
            sunset.setText(ssv);
            progressDialog.hide();

        } catch (IOException e) {
            progressDialog.hide();
            e.printStackTrace();
        } catch (JSONException e) {
            progressDialog.hide();
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        location = task.getResult();

                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            Log.e("latitude ", location.getLatitude() + "  1");
                            Log.e("Longitude", location.getLongitude() + "  1");
                            flag = 1;
                            getWeatherInfo(location.getLatitude(),location.getLongitude());

                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        Log.e(">>>>>>>>>>>>>","new location");
        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            mLastLocation = locationResult.getLastLocation();
            System.err.println("Latitude >>>: " + mLastLocation.getLatitude() + "");
            System.err.println("Longitude>>>: " + mLastLocation.getLongitude() + "");
            flag=2;
            LatLng latLng= new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
            System.err.println(latLng);
            getWeatherInfo(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
     public void onResume() {
        super.onResume();
        getLastLocation();
        Log.d("lifecycle >>>>>>","onResume invoked");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("lifecycle >>>>>>>","onPause invoked");
    }
}
