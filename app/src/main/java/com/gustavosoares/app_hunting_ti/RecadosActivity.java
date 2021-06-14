package com.gustavosoares.app_hunting_ti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecadosActivity extends MenuPrincipalActivity {

    public ArrayList<String>seguindo;
    private ListView listaTweets;

    private ArrayList<Tweet> tweets;

    private ChildEventListener tweetEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recados);

        seguindo = new ArrayList<>();

        tweets = new ArrayList<>();

        listaTweets = findViewById(R.id.listTwees);
        View header = getLayoutInflater().inflate(R.layout.list_header, null);
        listaTweets.addHeaderView(header);

        Tweet tweet = new Tweet();

        Button btnTweet = findViewById(R.id.btnTweets);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novoTweet();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null) finish();

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

        DatabaseReference userRef = database.getReference("user/" + uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                usuario = datasnapshot.child("usuario").getValue(String.class);
                seguindo.clear();
                for(DataSnapshot s:datasnapshot.child("seguindo").getChildren()){
                    seguindo.add(s.getValue(String.class));

                }
                TextView headerUsuario = findViewById(R.id.headerUsuario);
                headerUsuario.setText(usuario);

                TextView headerSeguindo = findViewById(R.id.headerUsuario);
                headerSeguindo.setText("Seguindo: " + seguindo.size());

                setTweetListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTweetListener(){
        tweetEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,  String s) {
                Tweet tweet = dataSnapshot.getValue(Tweet.class);
                //if(seguindo.contains(tweet.getUsuario()) || tweet.getUid().equals(uid)){
                    tweets.add(0,tweet);
                    Log.d("tweet", tweet.getTexto());
                //}

            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved( DataSnapshot snapshot,  String s) {

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        };
        DatabaseReference tweetRef = database.getReference("tweets");
        Query tweetQuery = tweetRef.limitToLast(100);

        tweetQuery.addChildEventListener(tweetEventListener);
    }

    private void novoTweet(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Novo Tweet");

        EditText texto = new EditText(this);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(140);
        texto.setFilters((filterArray));

        alert.setView(texto);

        alert.setPositiveButton("publicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if(texto.getText().toString().equals("")) return;
                Tweet novoTweet = new Tweet(uid, usuario,texto.getText().toString());
                DatabaseReference refTweets = database.getReference("tweets");
                refTweets.push().setValue(novoTweet);
            }
        });

        alert.setNegativeButton(("Cancelar"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alert.create().show();
    }

}