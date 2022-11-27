package com.anvaishy.easymedc_user_app.view;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.model.Doctor;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DoctorTimingAdapter extends FirestoreRecyclerAdapter<Doctor, DoctorTimingAdapter.DoctorViewHolder> {
    public DoctorTimingAdapter(@NonNull FirestoreRecyclerOptions<Doctor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DoctorViewHolder holder, int pos, @NonNull Doctor request) {
        Log.e("r",request.getName());
        holder.name.setText(request.getName());
        holder.spec.setText(request.getSpecialisation());
        holder.arrival.setText(request.getStartTime());
        holder.depart.setText(request.getEndTime());
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_card, parent, false);
        return new DoctorTimingAdapter.DoctorViewHolder(view);
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView name,spec, arrival, depart;
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            arrival = itemView.findViewById(R.id.start);
            depart = itemView.findViewById(R.id.end);
            spec = itemView.findViewById(R.id.spec);
        }
    }
}
