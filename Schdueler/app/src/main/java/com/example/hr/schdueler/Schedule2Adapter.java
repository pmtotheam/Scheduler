package com.example.hr.schdueler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by H$R on 10/25/2018.
 */

public class Schedule2Adapter extends RecyclerView.Adapter<Schedule2Adapter.Schedule2Viewholder> {
    private ArrayList<Schedule> schedules;

    public Schedule2Adapter(ArrayList<Schedule> schedules) {this.schedules = schedules;}
    public class Schedule2Viewholder extends RecyclerView.ViewHolder{
        public TextView dayHolderView,fromHolderView,toHolderView;
        public Schedule2Viewholder(View itemView) {
            super(itemView);
            dayHolderView=(TextView)itemView.findViewById(R.id.row_day_schedule2);
            fromHolderView=(TextView)itemView.findViewById(R.id.row_from_schedule2);
            toHolderView=(TextView)itemView.findViewById(R.id.row_to_schedule2);
        }
    }

    @Override
    public Schedule2Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule2_list_row,
                parent,false);
        return new Schedule2Viewholder(view);
    }

    @Override
    public void onBindViewHolder(Schedule2Viewholder holder, int position) {
        Schedule schedule=schedules.get(position);
        holder.dayHolderView.setText(schedule.getDay());
        holder.fromHolderView.setText("From: "+schedule.getFrom());
        holder.toHolderView.setText("To: "+schedule.getTo());
    }

    @Override
    public int getItemCount() {return schedules.size();}
}
