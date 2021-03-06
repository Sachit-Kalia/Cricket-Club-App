package com.example.cricketclubapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FullNetStats extends AppCompatActivity {
    EditText date;
    List<String> names = new ArrayList<>();
    List<String> id = new ArrayList<>();
    Spinner spinner;
    ArrayAdapter players;
    String selectedName,selectedId;
    Button getStats;
    TextView challenge;
    TableLayout allStats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_net_stats);
        Intent intent = getIntent();
        final String getDate = intent.getStringExtra("date");
        spinner = findViewById(R.id.spinner6);
        getStats = findViewById(R.id.button6);
        allStats = findViewById(R.id.table_main);
        challenge = findViewById(R.id.challenge);
        players = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,names);
        FirebaseFirestore.getInstance().collection("users").orderBy("username").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> data = queryDocumentSnapshots.getDocuments();
                for(int i=0;i<data.size();i++)
                {
                    names.add(data.get(i).get("username").toString());
                    id.add(data.get(i).getId());
                }
                players.notifyDataSetChanged();
                spinner.setAdapter(players);
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                selectedName = names.get(position);
                selectedId = id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                challenge.setText("");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                Date chosenDate = null;
                try {
                    chosenDate = simpleDateFormat.parse(String.valueOf(getDate));
                    FirebaseFirestore.getInstance().collection("stats").document(getDate).collection(selectedId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                           List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                            allStats.removeAllViews();
                            TableRow row = new TableRow(getBaseContext());
                            TextView tv = new TextView(getBaseContext());
                            tv.setText("S. No.");
                            tv.setTextColor(getResources().getColor(R.color.statusBarWhite));
                            tv.setGravity(Gravity.CENTER);
                            tv.setPadding(10,10,10,10);
                            row.addView(tv);
                            TextView tv0 = new TextView(getBaseContext());
                            tv0.setText("Batsman");
                            tv0.setTextColor(getResources().getColor(R.color.statusBarWhite));
                            tv0.setGravity(Gravity.CENTER);
                            tv0.setPadding(10,10,10,10);
                            row.addView(tv0);
                            TextView tv1 = new TextView(getBaseContext());
                            tv1.setText("Bowler");
                            tv1.setTextColor(getResources().getColor(R.color.statusBarWhite));
                            tv1.setPadding(10,10,10,10);
                            tv1.setGravity(Gravity.CENTER);
                            row.addView(tv1);
                            TextView tv2 = new TextView(getBaseContext());
                            tv2.setText("Length");
                            tv2.setTextColor(getResources().getColor(R.color.statusBarWhite));
                            tv2.setPadding(10,10,10,10);
                            tv2.setGravity(Gravity.CENTER);
                            row.addView(tv2);
                            TextView tv3 = new TextView(getBaseContext());
                            tv3.setText("Line");
                            tv3.setTextColor(getResources().getColor(R.color.statusBarWhite));
                            tv3.setPadding(10,10,10,10);
                            tv3.setGravity(Gravity.CENTER);
                            row.addView(tv3);
                            TextView tv4 = new TextView(getBaseContext());
                            tv4.setText("Shot");
                            tv4.setTextColor(getResources().getColor(R.color.statusBarWhite));
                            tv4.setPadding(10,10,10,10);
                            tv4.setGravity(Gravity.CENTER);
                            row.addView(tv4);
                            TextView tv5 = new TextView(getBaseContext());
                            tv5.setText("Runs");
                            tv5.setTextColor(getResources().getColor(R.color.statusBarWhite));
                            tv5.setPadding(10,10,10,10);
                            tv5.setGravity(Gravity.CENTER);
                            row.addView(tv5);
                            allStats.addView(row);
                            int j=0;
                           for(int i=0;i<documents.size();i++)
                           {
                               if(documents.get(i).get("batsman").toString().equals(selectedName)){
                                   TableRow _row = new TableRow(getBaseContext());
                                   TextView _tv = new TextView(getBaseContext());
                                   _tv.setText(String.valueOf(j+1));
                                   j++;
                                   _tv.setTextColor(getResources().getColor(R.color.statusBarWhite));
                                   _tv.setPadding(10,10,10,10);
                                   _tv.setGravity(Gravity.CENTER);
                                   _row.addView(_tv);
                                   TextView _tv0 = new TextView(getBaseContext());
                                   _tv0.setText(documents.get(i).get("batsman").toString());
                                   _tv0.setTextColor(getResources().getColor(R.color.statusBarWhite));
                                   _tv0.setPadding(10,10,10,10);
                                   _tv0.setGravity(Gravity.CENTER);
                                   _row.addView(_tv0);
                                   TextView _tv1 = new TextView(getBaseContext());
                                   _tv1.setText(documents.get(i).get("bowler").toString());
                                   _tv1.setTextColor(getResources().getColor(R.color.statusBarWhite));
                                   _tv1.setPadding(10,10,10,10);
                                   _tv1.setGravity(Gravity.CENTER);
                                   _row.addView(_tv1);
                                   TextView _tv2 = new TextView(getBaseContext());
                                   _tv2.setText(documents.get(i).get("length").toString());
                                   _tv2.setTextColor(getResources().getColor(R.color.statusBarWhite));
                                   _tv2.setPadding(10,10,10,10);
                                   _tv2.setGravity(Gravity.CENTER);
                                   _row.addView(_tv2);
                                   TextView _tv3 = new TextView(getBaseContext());
                                   _tv3.setText(documents.get(i).get("line").toString());
                                   _tv3.setTextColor(getResources().getColor(R.color.statusBarWhite));
                                   _tv3.setPadding(10,10,10,10);
                                   _tv3.setGravity(Gravity.CENTER);
                                   _row.addView(_tv3);
                                   TextView _tv4 = new TextView(getBaseContext());
                                   _tv4.setText(documents.get(i).get("shot").toString());
                                   _tv4.setTextColor(getResources().getColor(R.color.statusBarWhite));
                                   _tv4.setPadding(10,10,10,10);
                                   _tv4.setGravity(Gravity.CENTER);
                                   _row.addView(_tv4);
                                   TextView _tv5 = new TextView(getBaseContext());
                                   _tv5.setText(documents.get(i).contains("runs")?documents.get(i).get("runs").toString():"N/A");
                                   _tv5.setTextColor(getResources().getColor(R.color.statusBarWhite));
                                   _tv5.setPadding(10,10,10,10);
                                   _tv5.setGravity(Gravity.CENTER);
                                   _row.addView(_tv5);
                                   allStats.addView(_row);
                               }
                           }
                            FirebaseFirestore.getInstance().collection("challenges").document(getDate).collection(selectedId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                   List<DocumentSnapshot> data = queryDocumentSnapshots.getDocuments();
                                   if(data.size()>0)
                                   {
                                       String mode = data.get(0).get("mode").toString();
                                       if(mode.equals("C"))
                                       {
                                           String achieved = data.get(0).get("achieved").toString();
                                           challenge.setText("Mode: C\nAchieved: "+achieved);
                                       }
                                       else
                                           challenge.setText("Mode: "+mode);
                                   }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(),"There was some error! Please try later",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(),"There was some error! Please try later",Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (ParseException e) {
                    Toast.makeText(getBaseContext(),"There was some error! Please Provide date in dd/MM/YYYY format",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}