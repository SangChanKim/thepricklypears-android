package com.example.sang.waterreportingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    private static DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        String currentUser = getIntent().getExtras().getString("username");

        final TextView usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        usernameTextView.setText(currentUser);

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

        final EditText nameTextField = (EditText) findViewById(R.id.nameTextField);
        final EditText emailAddressTextField = (EditText) findViewById(R.id.emailAddressTextField);
        final EditText phoneNumberTextField = (EditText) findViewById(R.id.phoneNumberTextField);
        final EditText homeAddressTextField = (EditText) findViewById(R.id.homeAddressTextField);

        DatabaseReference ref = db.child("users").child(currentUser);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println(snapshot);
                nameTextField.setText(snapshot.child("name").getValue().toString());
                emailAddressTextField.setText(snapshot.child("emailAddress").getValue().toString());
                homeAddressTextField.setText(snapshot.child("homeAddress").getValue().toString());
                phoneNumberTextField.setText(snapshot.child("phoneNumber").getValue().toString());

                switch (snapshot.child("userTitle").getValue().toString()) {
                    case "Mr.":
                        userTitleSpinner.setSelection(0);
                        break;
                    case "Ms.":
                        userTitleSpinner.setSelection(1);
                        break;
                    case "Mrs.":
                        userTitleSpinner.setSelection(2);
                        break;
                    case "Dr.":
                        userTitleSpinner.setSelection(3);
                        break;
                    default:
                        userTitleSpinner.setSelection(0);
                }

                switch (snapshot.child("userType").getValue().toString()) {
                    case "User":
                        userTypeSpinner.setSelection(0);
                        break;
                    case "Admin.":
                        userTypeSpinner.setSelection(1);
                        break;
                    case "Worker":
                        userTypeSpinner.setSelection(2);
                        break;
                    case "Manager":
                        userTypeSpinner.setSelection(3);
                        break;
                    default:
                        userTypeSpinner.setSelection(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Button updateProfileButton = (Button) findViewById(R.id.updateProfileButton);
        updateProfileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String username = usernameTextView.getText().toString();
                String password = getIntent().getExtras().getString("password");
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

                this.editAuthUser(newUser);

                Intent updateIntent = new Intent(UpdateProfileActivity.this, HomeScreenActivity.class);
                UpdateProfileActivity.this.startActivity(updateIntent);
            }

            private void editAuthUser(User newUser) {
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

        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cancelIntent = new Intent(UpdateProfileActivity.this, HomeScreenActivity.class);
                UpdateProfileActivity.this.startActivity(cancelIntent);
            }
        });
    }


}
