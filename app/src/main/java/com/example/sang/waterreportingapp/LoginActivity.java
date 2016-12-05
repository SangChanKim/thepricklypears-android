package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private Boolean passwordIsValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameTextField = (EditText) findViewById(R.id.usernameTextField);
        final EditText passwordTextField = (EditText) findViewById(R.id.passwordTextField);

        final Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = usernameTextField.getText().toString();
                String password = passwordTextField.getText().toString();
                authenticate(username,password);
            }
        });

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cancelIntent = new Intent(LoginActivity.this, MainScreenActivity.class);

                LoginActivity.this.startActivity(cancelIntent);
            }
        });
    }

    private void authenticate(String user, String pass){
        final String password = pass;
        passwordIsValid = false;
        if (db.child("users").child(user) == null) {
            System.out.println("User not in system");
            passwordIsValid = false;
            Toast toast = Toast.makeText(getApplicationContext(), "INCORRECT USERNAME PASSSWORD COMBINATION", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            DatabaseReference ref = db.child("users").child(user).child("password");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    // do some stuff once
                    if (snapshot == null || snapshot.getValue() == null) {
                        System.out.println("User is not in the system");
                        passwordIsValid = false;
                        Toast toast = Toast.makeText(getApplicationContext(), "INCORRECT USERNAME PASSSWORD COMBINATION", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (snapshot.getValue().toString().equals(password)) {
                        passwordIsValid = true;
                        System.out.println("User is authenticated");
                        Intent loginIntent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                        LoginActivity.this.startActivity(loginIntent);
                    } else {
                        System.out.println("Incorrect password");
                        Toast toast = Toast.makeText(getApplicationContext(), "INCORRECT USERNAME PASSSWORD COMBINATION", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError firebaseError) {
                }
            });
        }
    }
}
