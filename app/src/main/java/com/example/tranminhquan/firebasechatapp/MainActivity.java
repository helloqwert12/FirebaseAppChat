package com.example.tranminhquan.firebasechatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import object.Message;
import object.User;

public class MainActivity extends AppCompatActivity {

    // Components
    ListView lvMessages;
    EditText etMessage;
    Button btnSend;

    // Firebase
    FirebaseDatabase mDatabase;
    DatabaseReference msgRef, memRef;

    List<String> msgList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFirebase();
        matchComponents();

        msgList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgList);
        lvMessages.setAdapter(adapter);

    }

    private void matchComponents() {
        lvMessages = findViewById(R.id.lvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().length() == 0)
                    return;

                // Create an instance of message
                Message message = new Message();
                message.setSender(User.getInstance().getName());
                message.setContent(etMessage.getText().toString());

                // add to msgRef
                msgRef.push().setValue(message);
            }
        });
    }

    private void initFirebase() {
        mDatabase = FirebaseDatabase.getInstance();
        msgRef = mDatabase.getReference().child("messages");
        memRef = mDatabase.getReference().child("members");

        msgRef.orderByKey().limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for (DataSnapshot msg: dataSnapshot.getChildren()){
                    Message message = msg.getValue(Message.class);
                    Log.d("FIREBASE", "Name: " + message.getSender() + " | Content: " + message.getContent());
                    msgList.add(message.getSender() + ": " + message.getContent());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        memRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User newUser = dataSnapshot.getValue(User.class);
                msgList.add(newUser.getName() + " has joined the room");
                Log.d("FIREBASE", newUser.getName() + " has joined the room");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User newUser = dataSnapshot.getValue(User.class);
                msgList.add(newUser.getName() + " has left the room");
                Log.d("FIREBASE", newUser.getName() + " has left the room");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
