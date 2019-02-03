package com.example.hr.schdueler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by H$R on 10/21/2018.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private ArrayList<Schedule> mSchedules;

    public ScheduleAdapter(ArrayList<Schedule> mSchedules) {
        this.mSchedules = mSchedules;
    }
    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        public TextView dayHolderView,fromHolderView,toHolderView;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            dayHolderView=(TextView)itemView.findViewById(R.id.row_day);
            fromHolderView=(TextView)itemView.findViewById(R.id.row_from);
            toHolderView=(TextView)itemView.findViewById(R.id.row_to);
        }
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_row,
                parent,false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        Schedule schedule=mSchedules.get(position);
        holder.dayHolderView.setText(schedule.getDay());
        holder.fromHolderView.setText("From: "+schedule.getFrom()+schedule.getAmpmFrom());
        holder.toHolderView.setText("To: "+schedule.getTo()+schedule.getAmpmTo());
    }

    @Override
    public int getItemCount() {
        return mSchedules.size();
    }
}
