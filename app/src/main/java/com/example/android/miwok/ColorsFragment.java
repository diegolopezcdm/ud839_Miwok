package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.miwok.adapter.WordAdapter;
import com.example.android.miwok.model.Word;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    AudioManager audioManager;
    MediaPlayer media;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_words, container, false);

        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        List<Word> wordList = new ArrayList<Word>();
        wordList.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        wordList.add(new Word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        wordList.add(new Word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        wordList.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        wordList.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        wordList.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        wordList.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        wordList.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));

        WordAdapter adapter = new WordAdapter(getActivity(), R.id.listItem, wordList, R.color.category_colors);
        ListView lv = (ListView) rootView.findViewById(R.id.numberListView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                releaseMediaPLayer();
                Word word = (Word) parent.getItemAtPosition(position);
                final int mediaId = word.getAudioTranslation();

                onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener(){
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                        //Log.v("onAudioFocusChange", media.toString());
                        if (media!=null && (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)) {
                            media.pause();
                            media.seekTo(0);
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            releaseMediaPLayer();
                        } else if (media!=null && focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                            media.start();
                        }
                    }
                };

                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    releaseMediaPLayer();
                    media = MediaPlayer.create(getActivity().getApplicationContext(), mediaId);
                    media.start();
                }

                media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        releaseMediaPLayer();
                    }
                });
            }
        });
        lv.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPLayer();
    }

    private void releaseMediaPLayer() {
        if(media!=null){
            Log.v("releaseMediaPLayer", media.toString());
            media.release();
            media=null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);

        }
    }
}
