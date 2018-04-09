package com.mylocarson.medmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.activities.MedicationDetailsActivity;
import com.mylocarson.medmanager.models.Reminder;
import com.mylocarson.medmanager.utils.Constants;

import java.util.ArrayList;

/**
 * Created by user on 05/04/2018.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Reminder> reminderArrayList;

    public ReminderAdapter(Context context, ArrayList<Reminder> reminders) {
        this.mContext = context;
        this.reminderArrayList = reminders;
    }

    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.reminder_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder holder, int position) {
        final Reminder reminder = reminderArrayList.get(position);
        holder.medicationName.setText(reminder.getMedicationName());
        String frequencyString = reminder.getFrequency() == 1 ? Integer.toString(reminder.getFrequency()) + " time" :
                Integer.toString(reminder.getFrequency())+ " times";

        holder.interval.setText(frequencyString);
        holder.startDate.setText(reminder.getStartDate());
        holder.endDate.setText(reminder.getEndDate());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MedicationDetailsActivity.class);

                Bundle data = new Bundle();
                data.putString(Constants.REMINDER_ID,reminder.getId());
                data.putString(Constants.MEDICATION_ID,reminder.getMedicationID());

                intent.putExtras(data);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reminderArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView medicationName, interval,startDate,endDate;
        public View view;
        public ViewHolder(View itemView) {
            super(itemView);

            medicationName = itemView.findViewById(R.id.medicationName);
            interval = itemView.findViewById(R.id.medInterval);
            startDate = itemView.findViewById(R.id.medStartDate);
            endDate = itemView.findViewById(R.id.medEndDate);
            view = itemView;
        }
    }
}
