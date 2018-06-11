package com.example.android.miwok.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.miwok.R;
import com.example.android.miwok.model.Word;

import java.util.List;

/**
 * Created by diegolopez on 7/5/17.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int colorResourceId;

    public WordAdapter(Context context, int resource, List<Word> arrayList, int backgroundColor) {
        super(context, 0, arrayList);
        this.colorResourceId = backgroundColor;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Word currentWord = getItem(position);

        TextView txtMihow = (TextView)convertView.findViewById(R.id.miwokTranslation);
        txtMihow.setText(currentWord.getMihowTraslation());

        TextView txtEnglish = (TextView)convertView.findViewById(R.id.englishTranslation);
        txtEnglish.setText(currentWord.getEnglishTraslation());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageTraslation);

        if(currentWord.hasImage()){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(currentWord.getImageTraslation());
        } else {
            imageView.setVisibility(View.GONE);
        }

        LinearLayout textContainer = (LinearLayout)convertView.findViewById(R.id.textContainer);
        int color = ContextCompat.getColor(getContext(), colorResourceId);
        textContainer.setBackgroundColor(color);

        return convertView;
    }
}
