package com.example.cricketclubapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    TextView loginSwitch;
    Spinner hostelSpinner, progSpinner, specialitySpinner;
    String userHostel, userProg, userSpeciality;
    private FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    SignInButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginSwitch = (TextView) findViewById(R.id.loginSwitch);
        hostelSpinner = (Spinner) findViewById(R.id.hostelSpinner);
        progSpinner = (Spinner) findViewById(R.id.progSpinner);
        specialitySpinner = (Spinner) findViewById(R.id.specialitySpinner);
        button = (SignInButton) findViewById(R.id.button);
        hostelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userHostel = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        progSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userProg = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        specialitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userSpeciality = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        loginSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            login();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(v);
            }
        });
    }
    public void signUp(View view){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*Log.d("debug","karan" + String.valueOf(resultCode));
        Log.d("debug","karan" + String.valueOf(requestCode));
        Log.d("debug","karan" + String.valueOf(data));*/
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            final GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseFirestore.collection("emails").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     List<DocumentSnapshot> data = queryDocumentSnapshots.getDocuments();
                     int flag = 0;
                     for(int i=0;i<data.size();i++)
                     {
                         if(account.getEmail().equals(data.get(i).getId()))
                         {
                             flag = 1;
                             break;
                         }
                     }
                     if(flag==1)
                     {
                         mAuth.createUserWithEmailAndPassword(account.getEmail(),passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull Task<AuthResult> task) {
                                 if (task.isSuccessful()) {
                                     // Sign in success, update UI with the signed-in user's information
                            /*myRef.child("users").child(task.getResult().getUser().getUid()).child("email").setValue(emailEditText.getText().toString());
                            myRef.child("users").child(task.getResult().getUser().getUid()).child("username").setValue(usernameEditText.getText().toString());
                            myRef.child("users").child(task.getResult().getUser().getUid()).child("hostel").setValue(userHostel);
                            myRef.child("users").child(task.getResult().getUser().getUid()).child("programme").setValue(userProg);
                            myRef.child("users").child(task.getResult().getUser().getUid()).child("speciality").setValue(userSpeciality);
                            myRef.child("users").child(task.getResult().getUser().getUid()).child("points").setValue(0);*/
                                     Map<String,Object> user = new HashMap<>();
                                     user.put("email",account.getEmail());
                                     user.put("username",usernameEditText.getText().toString());
                                     user.put("hostel",userHostel);
                                     user.put("programme",userProg);
                                     user.put("speciality",userSpeciality);
                                     user.put("points",0);
                                     firebaseFirestore.collection("users").document(task.getResult().getUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void aVoid) {
                                             login();
                                         }
                                     }).addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {

                                             Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                         }
                                     });


                                 } else {
                                     task.addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {
                                             Log.d("dynamic",e.toString()+"hello");
                                         }
                                     });
                                     // If sign in fails, display a message to the user.
                                     Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         });
                     }
                     else{
                         Toast.makeText(getApplicationContext(), "User not in list", Toast.LENGTH_SHORT).show();
                     }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Error:", " signInResult:failed code=" + e.getMessage());
        }
    }
    public void login(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}



