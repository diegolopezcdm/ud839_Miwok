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

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    AudioManager audioManager;
    MediaPlayer media;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_words, container, false);
        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        ArrayList<Word> wordList = new ArrayList<Word>();
        wordList.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        wordList.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        wordList.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        wordList.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        wordList.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        wordList.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        wordList.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        wordList.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        wordList.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        wordList.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        WordAdapter adapter = new WordAdapter(getActivity(), R.id.listItem, wordList, R.color.category_numbers);
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
