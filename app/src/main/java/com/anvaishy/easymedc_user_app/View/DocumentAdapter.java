package com.anvaishy.easymedc_user_app.View;

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

import com.anvaishy.easymedc_user_app.Model.Document;
import com.anvaishy.easymedc_user_app.R;
import com.anvaishy.easymedc_user_app.View.DocumentUploadList;
import java.util.ArrayList;

public class DocumentAdapter  extends ArrayAdapter<Document> {

    public DocumentAdapter(@NonNull Context context, ArrayList<Document> dataModalArrayList) {
        super(context, 0, dataModalArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.documentcard, parent, false);
        }

        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        Document dataModal = getItem(position);

        // initializing our UI components of list view item.
        TextView nameTV = listitemView.findViewById(R.id.docname);
        Button courseIV = listitemView.findViewById(R.id.docurl);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        nameTV.setText(dataModal.getName());

        // below line is use to add item click listener
        // for our item of list view.
        courseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = dataModal.getLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));

                Toast.makeText(getContext(), "Downloading : " + dataModal.getName(), Toast.LENGTH_SHORT).show();
                getContext().startActivity(i);
            }
        });
        return listitemView;
    }
}