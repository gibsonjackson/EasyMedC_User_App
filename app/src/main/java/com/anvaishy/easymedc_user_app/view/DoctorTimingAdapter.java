package com.anvaishy.easymedc_user_app.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.model.Doctor;
import com.anvaishy.easymedc_user_app.model.Document;

import java.util.ArrayList;

public class DoctorTimingAdapter extends ArrayAdapter<Doctor> {

    public DoctorTimingAdapter(@NonNull Context context, ArrayList<Doctor> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.doctor_card, parent, false);
        }

        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        Doctor dataModal = getItem(position);

        // initializing our UI components of list view item.
        TextView name = listitemView.findViewById(R.id.name);
        TextView spec = listitemView.findViewById(R.id.spec);

        TextView start = listitemView.findViewById(R.id.start);
        TextView end = listitemView.findViewById(R.id.end);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        name.setText(dataModal.getName());
        spec.setText(dataModal.getSpecialisation());
        start.setText(dataModal.getStartTime());
        end.setText(dataModal.getEndTime());
        return listitemView;
    }
}
