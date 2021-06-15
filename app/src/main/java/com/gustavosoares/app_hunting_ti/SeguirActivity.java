package com.gustavosoares.app_hunting_ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeguirActivity extends AppCompatActivity {

    private FirebaseDatabase database;

    private ListView listSeguindo;
    private ArrayAdapter<String> adapter;

    private ArrayList<String> usuarios;
    private ArrayList<String> seguindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguir);

        final String uid = getIntent().getStringExtra("uid");

        database = FirebaseDatabase.getInstance();

        usuarios = new ArrayList<>();
        seguindo = new ArrayList<>();

        listSeguindo = findViewById(R.id.listaSeguindo);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, usuarios);
        listSeguindo.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listSeguindo.setAdapter(adapter);

        listSeguindo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()){
                    seguindo.add(usuarios.get(i));
                } else {
                    seguindo.remove(seguindo.indexOf(usuarios.get(i)));
                }
                DatabaseReference seguindoRef = database.getReference("users/" + uid + "/seguindo");
                seguindoRef.setValue(seguindo);
            }
        });
        DatabaseReference usersRef = database.getReference("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    if(d.getKey().equals(uid)) {
                        seguindo.clear();
                        for(DataSnapshot s:d.child("seguindo").getChildren()){
                            seguindo.add(s.getValue(String.class));
                        }
                    } else {
                        String usuario = d.child("usuario").getValue(String.class);
                        usuarios.add(usuario);
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(){
        adapter.notifyDataSetChanged();

        for(String u:usuarios){
            listSeguindo.setItemChecked(usuarios.indexOf(u), seguindo.contains(u));
        }
    }
}