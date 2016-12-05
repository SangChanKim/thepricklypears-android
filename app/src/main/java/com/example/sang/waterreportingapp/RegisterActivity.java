package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {



    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText usernameTextField = (EditText) findViewById(R.id.usernameTextField);
        final EditText passwordTextField = (EditText) findViewById(R.id.passwordTextField);
        final EditText confirmPasswordTextField = (EditText) findViewById(R.id.confirmPasswordTextField);
        final EditText nameTextField = (EditText) findViewById(R.id.nameTextField);

        final Spinner userTypeSpinner = (Spinner) findViewById(R.id.userTypeSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.userTypeArray, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter1);

        final Spinner userTitleSpinner = (Spinner) findViewById(R.id.userTitleSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.userTitleArray, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTitleSpinner.setAdapter(adapter2);

        final EditText emailAddressTextField = (EditText) findViewById(R.id.emailAddressTextField);
        final EditText phoneNumberTextField = (EditText) findViewById(R.id.phoneNumberTextField);
        final EditText homeAddressTextField = (EditText) findViewById(R.id.homeAddressTextField);

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cancelIntent = new Intent(RegisterActivity.this, MainScreenActivity.class);

                RegisterActivity.this.startActivity(cancelIntent);
            }
        });

        final Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordTextField.getText().toString();
                String confirmPassword = confirmPasswordTextField.getText().toString();

                // Confirm their password
                if (confirmPassword.equals(password)) {

                    // Get all their attributes
                    String username = usernameTextField.getText().toString();
                    String name = nameTextField.getText().toString();
                    String emailAddress = emailAddressTextField.getText().toString();
                    String homeAddress = homeAddressTextField.getText().toString();
                    String phoneNumber = phoneNumberTextField.getText().toString();

                    String type = userTypeSpinner.getSelectedItem().toString();
                    String title = userTitleSpinner.getSelectedItem().toString();

                    // Set the attributes
                    User newUser = new User(username, password);
                    newUser.setUsername(username);
                    newUser.setName(name);
                    newUser.setEmailAddress(emailAddress);
                    newUser.setHomeAddress(homeAddress);
                    newUser.setPhoneNumber(phoneNumber);
                    newUser.setUserType(type);
                    newUser.setUserTitle(title);


                    this.addAuthUser(newUser);

                    Intent registerIntent = new Intent(RegisterActivity.this, MainScreenActivity.class);

                    RegisterActivity.this.startActivity(registerIntent);

                } else {
                    // Show on the UI that passwords are not equal
                    Toast toast = Toast.makeText(getApplicationContext(), "PASSWORDS DO NOT MATCH", Toast.LENGTH_SHORT);
                    toast.show();
                    passwordTextField.setText("");
                    confirmPasswordTextField.setText("");
                }
            }

            private void addAuthUser(User newUser) {
                DatabaseReference usersRef = db.child("users").child(newUser.getUsername());

                Map<String, String> userDataMap = new HashMap<>();
                userDataMap.put("name", newUser.getName());
                userDataMap.put("username", newUser.getUsername());
                userDataMap.put("password", newUser.getPassword());
                userDataMap.put("emailAddress", newUser.getEmailAddress());
                userDataMap.put("homeAddress", newUser.getHomeAddress());
                userDataMap.put("phoneNumber", newUser.getPhoneNumber());
                userDataMap.put("userType", newUser.getUserType().toString());
                userDataMap.put("userTitle", newUser.getUserTitle().toString());
                usersRef.setValue(userDataMap);
            }
        });



    }
}
