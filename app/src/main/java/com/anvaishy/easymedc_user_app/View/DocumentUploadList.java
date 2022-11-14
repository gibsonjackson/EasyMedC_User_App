package com.anvaishy.easymedc_user_app.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.anvaishy.easymedc_user_app.ItemClickListener;
import com.anvaishy.easymedc_user_app.Model.Document;
import com.anvaishy.easymedc_user_app.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DocumentUploadList extends AppCompatActivity implements ItemClickListener {
    DocumentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload_list);
        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Medical History");
        RecyclerView recyclerView = findViewById(R.id.docrecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String emailID = firebaseUser.getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("Users").document(emailID)
                .collection("Document");
        FirestoreRecyclerOptions<Document> options = new FirestoreRecyclerOptions.Builder<Document>()
                .setQuery(query, Document.class)
                .build();
        adapter = new DocumentAdapter(options);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View view, int position) {

    }
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
    public void goToSelectFile(View view) {
        Intent intent = new Intent(this,SingleDocumentUpload.class);
        startActivity(intent);
    }
}