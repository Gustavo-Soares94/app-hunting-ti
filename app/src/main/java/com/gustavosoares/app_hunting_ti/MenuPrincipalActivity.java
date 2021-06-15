package com.gustavosoares.app_hunting_ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuPrincipalActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private String uid;
    private String usuario;

    private ArrayList<String> seguindo;

    private ListView listaTweets;
    private TweetAdapter adapter;

    private ArrayList<Tweet> tweets;

    private ChildEventListener tweetEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        seguindo = new ArrayList<>();

        tweets = new ArrayList<>();

        listaTweets = findViewById(R.id.listTwees);
        View header = getLayoutInflater().inflate(R.layout.list_header, null);
        listaTweets.addHeaderView(header);

        adapter = new TweetAdapter(this, tweets);
        listaTweets.setAdapter(adapter);

        Button btnTweet = findViewById(R.id.btnTweets);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novoTweet();
            }
        });

        Button btnSeguir = findViewById(R.id.btnSeguir);
        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uid == null) return;
                Intent i = new Intent(MenuPrincipalActivity.this, SeguirActivity.class);
                i.putExtra("uid", uid);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) finish();

        getUserInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(tweetEventListener != null){
            DatabaseReference tweetRef = database.getReference("tweets");
            tweetRef.removeEventListener(tweetEventListener);
        }
    }

    private void getUserInfo(){
        uid = mAuth.getCurrentUser().getUid();

        DatabaseReference userRef = database.getReference("users/" + uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.child("usuario").getValue(String.class);

                seguindo.clear();
                for(DataSnapshot s:dataSnapshot.child("seguindo").getChildren()){
                    seguindo.add(s.getValue(String.class));
                }

                TextView headerUsuario = findViewById(R.id.headerUsuario);
                headerUsuario.setText(usuario);

                TextView headerSeguindo = findViewById(R.id.headerSeguindo);
                headerSeguindo.setText("Seguindo: " + seguindo.size());

                setTweetListener();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setTweetListener(){
        tweets.clear();
        tweetEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Tweet tweet = dataSnapshot.getValue(Tweet.class);
                if(seguindo.contains(tweet.getUsuario()) || tweet.getUid().equals(uid)){
                    tweets.add(0, tweet);
                    adapter.notifyDataSetChanged();

                    Log.d("tweet", tweet.getTexto());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DatabaseReference tweetRef = database.getReference("tweets");
        Query tweetQuery = tweetRef.limitToLast(100);

        tweetQuery.addChildEventListener(tweetEventListener);
    }

    private void novoTweet(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Novo Tweet");

        final EditText texto = new EditText(this);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(140);
        texto.setFilters(filterArray);

        alert.setView(texto);

        alert.setPositiveButton("Publicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(texto.getText().toString().equals("")) return;
                Tweet novoTweet = new Tweet(uid, usuario, texto.getText().toString());
                DatabaseReference refTweets = database.getReference("tweets");
                refTweets.push().setValue(novoTweet);
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.create().show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.sair){
            mAuth.signOut();
            finish();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }

    public void irAgendaDeContatos(View view){
        Intent i = new Intent(getApplicationContext(), AgendaDeContatosActivity.class);
        startActivity(i);
    }

    public void irListaDeTarefas(View view){
        Intent i = new Intent(getApplicationContext(), ListaDeTarefasActivity.class);
        startActivity(i);
    }


}