package com.anvaishy.easymedc_user_app.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.anvaishy.easymedc_user_app.Model.Document;
import com.anvaishy.easymedc_user_app.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DocumentUploadList extends AppCompatActivity {
    ListView DocumentListView;
    ArrayList<Document> dataModalArrayList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload_list);

        // below line is use to initialize our variables
        DocumentListView = findViewById(R.id.docList);
        dataModalArrayList = new ArrayList<>();

        // initializing our variable for firebase
        // firestore and getting its instance.
        db = FirebaseFirestore.getInstance();

        // here we are calling a method
        // to load data in our list view.
        loadDatainListview();
    }

    private void loadDatainListview() {
        // below line is use to get data from Firebase
        // firestore using collection in android.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String emailID = user.getEmail();
        Log.e("User",emailID);
        db.collection("Users").document(emailID).collection("Document").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are hiding
                            // our progress bar and adding our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                // after getting this list we are passing
                                // that list to our object class.

                                String docname = (String) d.get("name");
                                String docurl = (String) d.get("link");
                                Log.e("Data", docname + " " + docurl + "\n");
                                Document dataModal = new Document(docname, docurl);
                                // after getting data from Firebase we are
                                // storing that data in our array list
                                dataModalArrayList.add(dataModal);
                            }
                            // after that we are passing our array list to our adapter class.
                            DocumentAdapter adapter = new DocumentAdapter(DocumentUploadList.this, dataModalArrayList);

                            // after passing this array list to our adapter
                            // class we are setting our adapter to our list view.
                            Log.e("URL",dataModalArrayList.get(0).getName());
                            DocumentListView.setAdapter(adapter);


                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(DocumentUploadList.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(e -> {
                    // we are displaying a toast message
                    // when we get any error from Firebase.
                    Toast.makeText(DocumentUploadList.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                });
    }

    public void goToSelectFile(View view) {
        Intent intent = new Intent(this,SingleDocumentUpload.class);
        startActivity(intent);
    }
    public void selector(String url){

    }
}