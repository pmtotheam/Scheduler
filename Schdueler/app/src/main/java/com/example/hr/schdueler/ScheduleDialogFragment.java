package com.example.hr.schdueler;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by H$R on 10/20/2018.
 */

public class ScheduleDialogFragment extends DialogFragment {
    public interface OnInputListner{//sending data to activity
        void sendInput(Schedule schedule);
    }
    public OnInputListner mInputListner;
    private Spinner spinnerDay,spinnerFrom,spinnerTo,spinnerAmPm1,spinnerAmPm2;
    private Button buttonAdd;

    private String stringDay,stringFrom,stringTo,stringAmPm1,stringAmPm2;

    private Resources res;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.schedule_fragment_view,container,false);
        intialise(view);
        settingOnItemSelected();
        getDialog().setTitle(res.getString(R.string.add));
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Schedule schedule=new Schedule();
                schedule.setDay(stringDay);
                schedule.setFrom(stringFrom);
                schedule.setTo(stringTo);
                schedule.setAmpmFrom(stringAmPm1);
                schedule.setAmpmTo(stringAmPm2);
                mInputListner.sendInput(schedule);
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        //Resizing of Dialogfragment
        ViewGroup.LayoutParams params=getDialog().getWindow().getAttributes();
        params.width= WindowManager.LayoutParams.WRAP_CONTENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    private void intialise(View view){
        res=getResources();
        buttonAdd=(Button)view.findViewById(R.id.add_button_fragment);
        spinnerDay=(Spinner) view.findViewById(R.id.day_spinner);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                res.getStringArray(R.array.days_array));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(arrayAdapter1);

        spinnerFrom=(Spinner)view.findViewById(R.id.from_time_spinner);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                res.getStringArray(R.array.time_array));
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(arrayAdapter2);

        spinnerTo=(Spinner)view.findViewById(R.id.to_time_spinner);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                res.getStringArray(R.array.time_array));
        arrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(arrayAdapter3);

        spinnerAmPm1=(Spinner)view.findViewById(R.id.ampm_spinner1);
        ArrayAdapter<String> arrayAdapter4 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                res.getStringArray(R.array.ampm_array));
        arrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmPm1.setAdapter(arrayAdapter4);

        spinnerAmPm2=(Spinner)view.findViewById(R.id.ampm_spinner2);
        ArrayAdapter<String> arrayAdapter5 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                res.getStringArray(R.array.ampm_array));
        arrayAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAmPm2.setAdapter(arrayAdapter5);
    }
    private void settingOnItemSelected(){
        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringDay=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerAmPm2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringAmPm2=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerAmPm1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringAmPm1=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringFrom=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringTo=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mInputListner=(OnInputListner)getActivity();
    }
}
