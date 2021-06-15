package com.gustavosoares.app_hunting_ti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TweetAdapter extends ArrayAdapter<Tweet> {

    public TweetAdapter(Context context, ArrayList<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tweet tweet = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView textUsuario = convertView.findViewById(R.id.tweetUsuario);
        TextView textTexto = convertView.findViewById(R.id.tweetTexto);

        textUsuario.setText(tweet.getUsuario());
        textTexto.setText(tweet.getTexto());

        return convertView;
    }
}