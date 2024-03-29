package com.anvaishy.easymedc_user_app.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
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

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull MedicalViewHolder holder, int pos, @NonNull MedicalPassRequestUser request) {
        Log.d("ok1", request.getDescription());
        holder.description.setText(request.getDescription());
        StringBuilder arrivalTime = new StringBuilder(request.getArrival().toDate().toString());
        arrivalTime.delete(20, 30);
        StringBuilder departTime = new StringBuilder(request.getDepart().toDate().toString());
        departTime.delete(20, 30);
        holder.arrival.setText(arrivalTime.toString());
        holder.depart.setText(departTime.toString());
        holder.arrival.setTextColor(Color.BLACK);
        holder.depart.setTextColor(Color.BLACK);
        if(request.getStatus()==2){
            holder.status.setCardBackgroundColor(Color.rgb(228, 59, 59));
            holder.desc.setCardBackgroundColor(Color.rgb(224, 1, 1));
            holder.description.setTextColor(Color.rgb(224, 227, 231));
            holder.arrival.setTextColor(Color.rgb(224, 227, 231));
            holder.depart.setTextColor(Color.rgb(224, 227, 231));
        }
        else if(request.getStatus()==1){
            holder.status.setCardBackgroundColor(Color.rgb(72, 198, 57));
            holder.desc.setCardBackgroundColor(Color.rgb(64, 180, 50));
            holder.description.setTextColor(Color.BLACK);
            holder.arrival.setTextColor(Color.BLACK);
            holder.depart.setTextColor(Color.BLACK);
        }
        else if (request.getStatus() == 0) {
            holder.status.setCardBackgroundColor(Color.rgb(224, 227, 231));
            holder.desc.setCardBackgroundColor(Color.rgb(215, 218, 222));
            holder.description.setTextColor(Color.BLACK);
            holder.arrival.setTextColor(Color.BLACK);
            holder.depart.setTextColor(Color.BLACK);
        }
    }

    @NonNull
    @Override
    public MedicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medical_pass_request_card, parent, false);
        return new MedicalPassAdapter.MedicalViewHolder(view);
    }

    class MedicalViewHolder extends RecyclerView.ViewHolder {
        TextView description, arrival, depart;
        CardView status;
        CardView desc;
        public MedicalViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.med_pass_desc);
            arrival = itemView.findViewById(R.id.arrival_time);
            depart = itemView.findViewById(R.id.depart_time);
            status = itemView.findViewById(R.id.requestCard);
            desc = itemView.findViewById(R.id.desc_card);
        }
    }
}
