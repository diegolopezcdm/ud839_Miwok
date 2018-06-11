package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

    AudioManager audioManager;
    MediaPlayer media;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_words, container, false);
        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> wordList = new ArrayList<Word>();
        wordList.add(new Word("father", "әpә", R.drawable.family_father, R.raw.family_father));
        wordList.add(new Word("mother", "әṭa", R.drawable.family_mother, R.raw.family_mother));
        wordList.add(new Word("son", "angsi", R.drawable.family_son, R.raw.family_son));
        wordList.add(new Word("daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter));
        wordList.add(new Word("older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother));
        wordList.add(new Word("younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother));
        wordList.add(new Word("older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister));
        wordList.add(new Word("younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister));
        wordList.add(new Word("grandmother ", "ama", R.drawable.family_grandmother, R.raw.family_grandmother));
        wordList.add(new Word("grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather));

        WordAdapter adapter = new WordAdapter(getActivity(), R.id.listItem, wordList, R.color.category_family);
        ListView lv = (ListView)rootView.findViewById(R.id.numberListView);
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
