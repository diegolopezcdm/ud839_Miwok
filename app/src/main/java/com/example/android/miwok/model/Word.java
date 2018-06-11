package com.example.android.miwok.model;

/**
 * Created by diegolopez on 7/5/17.
 */

public class Word {

    private String mihowTraslation;
    private String englishTraslation;
    private int imageTraslation;
    private static int NO_IMAGE=0;
    private int audioTranslation;

    public Word(String mihowTraslation, String englishTraslation) {
        this.mihowTraslation = mihowTraslation;
        this.englishTraslation = englishTraslation;
    }

    public Word(String mihowTraslation, String englishTraslation, int audioTranslation) {
        this(mihowTraslation, englishTraslation);
        this.audioTranslation = audioTranslation;
    }

    public Word(String mihowTraslation, String englishTraslation, int imageTraslation, int audioTranslation) {
        this(mihowTraslation, englishTraslation);
        this.imageTraslation = imageTraslation;
        this.audioTranslation = audioTranslation;
    }

    public String getMihowTraslation() {
        return mihowTraslation;
    }

    public void setMihowTraslation(String mihowTraslation) {
        this.mihowTraslation = mihowTraslation;
    }

    public String getEnglishTraslation() {
        return englishTraslation;
    }

    public void setEnglishTraslation(String englishTraslation) {
        this.englishTraslation = englishTraslation;
    }

    public int getImageTraslation() {
        return imageTraslation;
    }

    public void setImageTraslation(int imageTraslation) {
        this.imageTraslation = imageTraslation;
    }

    public boolean hasImage(){
        return imageTraslation != NO_IMAGE;
    }

    public int getAudioTranslation() {
        return audioTranslation;
    }

    public void setAudioTranslation(int audioTranslation) {
        this.audioTranslation = audioTranslation;
    }
}
