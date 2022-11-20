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
import com.anvaishy.easymedc_user_app.model.MedicalPassRequestUser;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MedicalPassAdapter extends FirestoreRecyclerAdapter<MedicalPassRequestUser, MedicalPassAdapter.MedicalViewHolder> {
    public MedicalPassAdapter(@NonNull FirestoreRecyclerOptions<MedicalPassRequestUser> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MedicalViewHolder holder, int pos, @NonNull MedicalPassRequestUser request) {
        Log.d("ok1", request.getDescription());
        holder.description.setText(request.getDescription());
        holder.description.append("\n");
        holder.description.append(request.getArrival().toDate().toString());
        holder.description.append("\n");
        holder.description.append(request.getDepart().toDate().toString());
        holder.arrival.setText(request.getArrival().toDate().toString());
        holder.depart.setText(request.getDepart().toDate().toString());
        holder.arrival.setTextColor(Color.BLACK);
        holder.depart.setTextColor(Color.BLACK);
    }

    @NonNull
    @Override
    public MedicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_pass_request_card, parent, false);
        return new MedicalPassAdapter.MedicalViewHolder(view);
    }

    class MedicalViewHolder extends RecyclerView.ViewHolder {
        TextView description, arrival, depart;
        public MedicalViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.med_pass_desc);
            arrival = itemView.findViewById(R.id.arrival_time);
            depart = itemView.findViewById(R.id.depart_time);
        }
    }
}
