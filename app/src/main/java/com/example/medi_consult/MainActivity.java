package com.example.medi_consult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail,editTextPassword;
    private TextView textViewErrorLogin,textViewForgotPassword,textViewErrorSelectUser,textViewInvalidUser;
    private ProgressBar progressBar;
    private Button buttonLogin,buttonPatient,buttonDoctor;
    private RadioButton patientRadio,doctorRadio,adminRadio;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail=(EditText) findViewById(R.id.loginPageEmailEditText);
        editTextPassword=(EditText) findViewById(R.id.loginPagePasswordEditText);
        textViewErrorLogin=(TextView) findViewById(R.id.textViewErrorLogin);
        textViewForgotPassword=(TextView) findViewById(R.id.textViewForgotPassword);
        textViewErrorSelectUser=(TextView) findViewById(R.id.textViewSelectRadio);
        textViewInvalidUser=(TextView) findViewById(R.id.textViewInvalidUser);
        progressBar=(ProgressBar) findViewById(R.id.mainActivityIndeterminateProgressbar);

        patientRadio=(RadioButton) findViewById(R.id.radioPatient);
        doctorRadio=(RadioButton) findViewById(R.id.radioDoctor);
        adminRadio=(RadioButton) findViewById(R.id.radioAdmin);

        buttonLogin=(Button) findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(this);

        buttonPatient=(Button) findViewById(R.id.buttonPatient);
        buttonPatient.setOnClickListener(this);

        buttonDoctor=(Button) findViewById(R.id.buttonDoctor);
        buttonDoctor.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonPatient:
                startActivity(new Intent(this,RegisterPatient.class));
                break;

            case R.id.buttonDoctor:
                startActivity(new Intent(this,RegisterDoctor.class));
                break;

            case R.id.login_button:
                loginUser();
                textViewInvalidUser.setVisibility(View.GONE);
                textViewErrorLogin.setVisibility(View.INVISIBLE);
                textViewErrorSelectUser.setVisibility(View.GONE);
                break;
        }
    }

    void loginUser(){
        String email=editTextEmail.getText().toString();
        String password=editTextPassword.getText().toString();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide valid email address");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<8){
            editTextPassword.setError("Password should be 8 characters long");
            editTextPassword.requestFocus();
            return;
        }


        //If everything goes right
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            boolean flag=false;

                            if(patientRadio.isChecked()){
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://medi-consult-1bad8-default-rtdb.firebaseio.com/Patients/");
                                FirebaseUser user=mAuth.getCurrentUser();
                                String uid=user.getUid();


                                 reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         Patient patient=snapshot.getValue(Patient.class);
                                         progressBar.setVisibility(View.GONE);

                                         if(patient==null){
                                             textViewInvalidUser.setVisibility(View.VISIBLE);
                                         }
                                         else{
                                             Intent intent=new Intent(MainActivity.this, loginPatient.class);
                                             startActivity(intent);
                                         }
                                     }
                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {
                                         progressBar.setVisibility(View.GONE);
                                         Toast.makeText(MainActivity.this, "Something wrong happened, Try Again!", Toast.LENGTH_SHORT).show();
                                     }
                                 });
                                 flag=true;
                            }

                            if(doctorRadio.isChecked()){
                                DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl("https://medi-consult-1bad8-default-rtdb.firebaseio.com/Doctors/");
                                FirebaseUser user=mAuth.getCurrentUser();
                                String uid=user.getUid();


                                reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Doctor doctor=snapshot.getValue(Doctor.class);
                                        progressBar.setVisibility(View.GONE);

                                        if(doctor==null){
                                            textViewInvalidUser.setVisibility(View.VISIBLE);
                                        }
                                        else{
                                            Intent intent=new Intent(MainActivity.this, loginDoctor.class);
                                            startActivity(intent);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(MainActivity.this, "Something wrong happened, Try Again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                flag=true;
                            }
//                            if(adminRadio.isChecked()){
//                                flag=true;
//                                intent=new Intent(MainActivity.this, loginAdmin.class);
//                            }

                            //if nothing is selected
                            if(flag==false){
                                progressBar.setVisibility(View.GONE);
                                textViewErrorSelectUser.setVisibility(View.VISIBLE);
                                return;
                            }
                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            textViewErrorLogin.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}