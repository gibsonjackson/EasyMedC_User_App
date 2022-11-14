package com.anvaishy.easymedc_user_app.View;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anvaishy.easymedc_user_app.Model.Document;
import com.anvaishy.easymedc_user_app.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DocumentAdapter extends FirestoreRecyclerAdapter<
        Document, DocumentAdapter.personsViewholder> {

public DocumentAdapter(
@NonNull FirestoreRecyclerOptions<Document> options)
        {
        super(options);
        }
@Override
protected void
        onBindViewHolder(@NonNull personsViewholder holder,
        int position, @NonNull Document model)
        {
        holder.firstname.setText(model.getDocname());
        System.out.print(model.getDocname()+" "+model.getDocurl());
        holder.lastname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        }
@NonNull
@Override
public personsViewholder
        onCreateViewHolder(@NonNull ViewGroup parent,
        int viewType)
        {
        View view
        = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.documentcard, parent, false);
        return new DocumentAdapter.personsViewholder(view);
        }
class personsViewholder extends RecyclerView.ViewHolder {
    TextView firstname;
    Button lastname;
    public personsViewholder(@NonNull View itemView)
    {
        super(itemView);
        firstname = itemView.findViewById(R.id.docname);
        lastname = itemView.findViewById(R.id.docurl);
    }
}
}