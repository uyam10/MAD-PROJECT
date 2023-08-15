package com.example.weatherjano;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class settings extends Fragment {

    Switch simpleSwitch;
    String statusSwitch1;
    Button submit;
    SharedPreferences sharedPref;
    String switchcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        // initiate a Switch
        simpleSwitch = (Switch) view.findViewById(R.id.switch1);
        submit = (Button) view.findViewById(R.id.submitbutton);

        sharedPref= getActivity().getSharedPreferences("Switch",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        switchcon= sharedPref.getString("switch","");
        if(switchcon.equals("ON"))
        {
            simpleSwitch.setChecked(true);
        }
        else
        {
            simpleSwitch.setChecked(false);
        }
        submit.setOnClickListener(view1 -> {

            if (simpleSwitch.isChecked()) {
                statusSwitch1 = simpleSwitch.getTextOn().toString();
                System.err.println(statusSwitch1);
                editor.putString("switch", statusSwitch1);
                editor.apply();
                Toast.makeText(getActivity(), "Value successfully changed", Toast.LENGTH_SHORT).show();
            }
            else {
                statusSwitch1 = simpleSwitch.getTextOff().toString();
                System.err.println(statusSwitch1);
                editor.putString("switch", statusSwitch1);
                editor.apply();
                Toast.makeText(getActivity(), "Value successfully changed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}