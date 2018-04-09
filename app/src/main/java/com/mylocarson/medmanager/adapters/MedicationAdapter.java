package com.mylocarson.medmanager.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mylocarson.medmanager.R;
import com.mylocarson.medmanager.models.Medication;
import com.mylocarson.medmanager.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by user on 05/04/2018.
 */

public class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.ViewHolder> {
    ArrayList<Medication> medicationArrayList;
    Context mContext;
    public MedicationAdapter(Context context, ArrayList<Medication> medicationArrayList) {
        this.mContext = context;
        this.medicationArrayList = medicationArrayList;
    }

    @Override
    public MedicationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.medication_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedicationAdapter.ViewHolder holder, int position) {
        final Medication medication = medicationArrayList.get(position);
        holder.medName.setText(medication.getName());
        holder.medDescription.setText(medication.getDescription());
        holder.dateCreated.setText(Utilities.convertDate(medication.getDateCreated()));

//        holder.view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), MedicationDetailsActivity.class);
//                intent.putExtra(Constants.MEDICATION_ID, medication.getId());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return medicationArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView medName,medDescription,dateCreated;
        public View view;
        private ViewHolder(View itemView) {
            super(itemView);

            medName = itemView.findViewById(R.id.medicationName);
            medDescription = itemView.findViewById(R.id.medicationDetails);
            dateCreated = itemView.findViewById(R.id.time);
            view = itemView;

        }
    }
}
