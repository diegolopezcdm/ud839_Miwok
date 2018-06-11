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
public class PhasesFragment extends Fragment {

    AudioManager audioManager;
    MediaPlayer media;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;


    public PhasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_words, container, false);
        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> wordList = new ArrayList<Word>();
        wordList.add(new Word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        wordList.add(new Word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        wordList.add(new Word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        wordList.add(new Word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        wordList.add(new Word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        wordList.add(new Word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        wordList.add(new Word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        wordList.add(new Word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        wordList.add(new Word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        wordList.add(new Word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        WordAdapter adapter = new WordAdapter(getActivity(), R.id.listItem, wordList, R.color.category_phrases);
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
