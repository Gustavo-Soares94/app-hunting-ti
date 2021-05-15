package com.gustavosoares.app_hunting_ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipalActivity extends AppCompatActivity {

    //Sistema de identificacao do firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String uid;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null) finish();

        getUserInfo();
    }

    private void getUserInfo(){

        uid = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("user/" + uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                usuario = datasnapshot.child("usuario").getValue(String.class);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}