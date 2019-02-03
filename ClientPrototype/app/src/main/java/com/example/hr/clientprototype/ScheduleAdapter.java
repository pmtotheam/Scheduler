package com.example.hr.clientprototype;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by H$R on 10/25/2018.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewholder>  {
    private ArrayList<Schedule> schedules;

    public ScheduleAdapter(ArrayList<Schedule> schedules) {this.schedules = schedules;}
    public class ScheduleViewholder extends RecyclerView.ViewHolder{
        public TextView dayHolderView,fromHolderView,toHolderView;
        public ScheduleViewholder(View itemView) {
            super(itemView);
            dayHolderView=(TextView)itemView.findViewById(R.id.row_day_schedule);
            fromHolderView=(TextView)itemView.findViewById(R.id.row_from_schedule);
            toHolderView=(TextView)itemView.findViewById(R.id.row_to_schedule);
        }
    }
    @Override
    public ScheduleViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_row,
                parent,false);
        return new ScheduleViewholder(view);
    }
    @Override
    public void onBindViewHolder(ScheduleViewholder holder, int position) {
        Schedule schedule=schedules.get(position);
        holder.dayHolderView.setText(schedule.getDay());
        holder.fromHolderView.setText("From: "+schedule.getFrom());
        holder.toHolderView.setText("To: "+schedule.getTo());
    }
    @Override
    public int getItemCount() {
        return schedules.size();
    }
}
