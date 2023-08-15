package com.example.weatherjano.Adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weatherjano.model.Daily;
import com.example.weatherjano.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {

    private List<Daily> dataList;
    private Context context;
    String tempunit;

    public MyRecyclerViewAdapter(Context context,List<Daily> dataList){
        this.context = context;
        this.dataList = dataList;

    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        TextView date, temp,weatherdes,maxtemp,mintemp;
        ImageView weatherimg;
        SharedPreferences sharedPref;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            date= mView.findViewById(R.id.pname);
            temp = mView.findViewById(R.id.curtemp);
            weatherdes = mView.findViewById(R.id.desweather);
            maxtemp = mView.findViewById(R.id.maxtempp);
            mintemp = mView.findViewById(R.id.mintempp);
            weatherimg = mView.findViewById(R.id.imgweather);
            sharedPref= context.getSharedPreferences("Switch",Context.MODE_PRIVATE);
            tempunit= sharedPref.getString("switch","");

        }
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        if(tempunit.equals("ON"))
        {
            Double temperature= dataList.get(position).getDay();
            temperature = temperature- 273.15;
            long tmp = Math.round((temperature * 9/5) + 32);
            holder.temp.setText(tmp +" \u2109");
            Double mintempp= dataList.get(position).getMin();
            mintempp=mintempp - 273.15;
            long mintmp = Math.round((mintempp * 9/5) + 32);
            holder.mintemp.setText(String.valueOf(mintmp)+" \u2109");
            Double maxtempp= dataList.get(position).getMax();
            maxtempp=maxtempp - 273.15;
            long maxtmp = Math.round((maxtempp * 9/5) + 32);
            holder.maxtemp.setText(String.valueOf(maxtmp)+" \u2109");
        }
        else{
            Double temperature= dataList.get(position).getDay();
            temperature = temperature- 273.15;
            long tmp = Math.round(temperature);
            holder.temp.setText(tmp +" \u2103");
            Double mintempp= dataList.get(position).getMin();
            mintempp=mintempp - 273.15;
            long mintmp = Math.round(mintempp);
            holder.mintemp.setText(String.valueOf(mintmp)+" \u2103");
            Double maxtempp= dataList.get(position).getMax();
            maxtempp=maxtempp - 273.15;
            long maxtmp = Math.round(maxtempp);
            holder.maxtemp.setText(String.valueOf(maxtmp)+" \u2103");
        }

        long ss = Long.valueOf( dataList.get(position).getDt())*1000;// its need to be in milisecond
        Date dss = new java.util.Date(ss);
        String ssv = new SimpleDateFormat("dd-MM-yyyy", Locale.forLanguageTag("IN")).format(dss);
        holder.date.setText(ssv);
        holder.weatherdes.setText(dataList.get(position).getDescription());

        String iconUrl = "http://openweathermap.org/img/w/" + dataList.get(position).getIcon() + ".png";
        Glide.with(context).load(iconUrl)
                .error(R.drawable.cloudback)
                .into(holder.weatherimg);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}

