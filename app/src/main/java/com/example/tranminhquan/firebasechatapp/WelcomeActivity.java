package com.example.tranminhquan.firebasechatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import object.User;

public class WelcomeActivity extends AppCompatActivity {

    // component
    private EditText etName;
    private Button btnJoin;

    // firebase
    FirebaseDatabase mDatabase;
    DatabaseReference memRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initFirebase();
        matchComponent();
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        memRef = mDatabase.getReference().child("members");
    }

    private void matchComponent() {
        etName = findViewById(R.id.etName);
        btnJoin = findViewById(R.id.btnJoin);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().length() != 0){
                    // create user instance
                    User user = User.getInstance();
                    user.setName(etName.getText().toString());

                    // add to member ref
                    String key = memRef.push().getKey();
                    user.setUserid(key);
                    memRef.child(key).setValue(user);

                    // go to main activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
