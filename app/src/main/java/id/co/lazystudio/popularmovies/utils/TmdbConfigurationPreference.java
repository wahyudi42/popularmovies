package id.co.lazystudio.popularmovies.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.co.lazystudio.popularmovies.entity.Configuration;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class TmdbConfigurationPreference {
    // Context
    Context _context;

    // Sharedpref file name
    private static final String URL_PREF_NAME = "base_url";
    private static final String BACKDROP_SIZE_PREF_NAME = "backdrop_sizes";
    private static final String LOGO_SIZE_PREF_NAME = "logo_sizes";
    private static final String POSTER_SIZE_PREF_NAME = "poster_sizes";
    private static final String PROFILE_SIZE_PREF_NAME = "profile_sizes";
    private static final String STILL_SIZE_PREF_NAME = "still_sizes";
    private static final String BASE_URL_KEY = "base_url";
    private static final String SECURE_BASE_URL_KEY = "secure_base_url";

    public TmdbConfigurationPreference(Context context){
        _context = context;
    }

    public void setConfiguration(Configuration configuration){
        setBaseUrl(configuration.getImages());
    }

    private void setBaseUrl(Configuration.ImagePath configuration){
        SharedPreferences urlPref = _context.getSharedPreferences(URL_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor urlEditor = urlPref.edit();
        urlEditor.putString(BASE_URL_KEY, configuration.getBaseUrl());
        urlEditor.putString(SECURE_BASE_URL_KEY, configuration.getSecureBaseUrl());
        urlEditor.apply();

        setBackdropSizes(configuration.getBackdropSizes());
        setLogoSizes(configuration.getLogoSizes());
        setPosterSizes(configuration.getPosterSizes());
        setProfileSizes(configuration.getProfileSizes());
        setStillSizes(configuration.getStillSizes());
    }

    private void setBackdropSizes(List<String> backdropSizes){
        SharedPreferences backdropSizePref = _context.getSharedPreferences(BACKDROP_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor backdropSizeEditor = backdropSizePref.edit();
        for(int i = 0; i < backdropSizes.size(); i++){
            backdropSizeEditor.putString(String.valueOf(i), backdropSizes.get(i));
        }
        backdropSizeEditor.apply();
    }

    private void setLogoSizes(List<String> logoSizes){
        SharedPreferences logoSizePref = _context.getSharedPreferences(LOGO_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor logoSizeEditor = logoSizePref.edit();
        for(int i = 0; i < logoSizes.size(); i++){
            logoSizeEditor.putString(String.valueOf(i), logoSizes.get(i));
        }
        logoSizeEditor.apply();
    }

    private void setPosterSizes(List<String> posterSizes){
        SharedPreferences posterSizePref = _context.getSharedPreferences(POSTER_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor posterSizeEditor = posterSizePref.edit();
        for(int i = 0; i < posterSizes.size(); i++){
            posterSizeEditor.putString(String.valueOf(i), posterSizes.get(i));
        }
        posterSizeEditor.apply();
    }

    private void setProfileSizes(List<String> profileSizes){
        SharedPreferences profileSizePref = _context.getSharedPreferences(PROFILE_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor profileSizeEditor = profileSizePref.edit();
        for(int i = 0; i < profileSizes.size(); i++){
            profileSizeEditor.putString(String.valueOf(i), profileSizes.get(i));
        }
        profileSizeEditor.apply();
    }

    private void setStillSizes(List<String> stillSizes){
        SharedPreferences stillSizePref = _context.getSharedPreferences(STILL_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor stillSizeEditor = stillSizePref.edit();
        for(int i = 0; i < stillSizes.size(); i++){
            stillSizeEditor.putString(String.valueOf(i), stillSizes.get(i));
        }
        stillSizeEditor.apply();
    }

    public String getBaseUrl(){
        SharedPreferences pref = _context.getSharedPreferences(URL_PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(BASE_URL_KEY, null);
    }

    public List<String> getBackdropSizes(){
        SharedPreferences pref = _context.getSharedPreferences(BACKDROP_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = pref.getAll();
        List<String> response = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.add(null);
        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.set(Integer.valueOf(entry.getKey()), entry.getValue().toString());
        }
        return response;
    }

    public List<String> getLogoSizes(){
        SharedPreferences pref = _context.getSharedPreferences(BACKDROP_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = pref.getAll();
        List<String> response = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.add(null);
        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.set(Integer.valueOf(entry.getKey()), entry.getValue().toString());
        }
        return response;
    }

    public List<String> getPosterSizes(){
        SharedPreferences pref = _context.getSharedPreferences(BACKDROP_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = pref.getAll();
        List<String> response = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.add(null);
        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.set(Integer.valueOf(entry.getKey()), entry.getValue().toString());
        }
        return response;
    }

    public List<String> getProfileSizes(){
        SharedPreferences pref = _context.getSharedPreferences(BACKDROP_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = pref.getAll();
        List<String> response = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.add(null);
        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.set(Integer.valueOf(entry.getKey()), entry.getValue().toString());
        }
        return response;
    }

    public List<String> getStillSizes(){
        SharedPreferences pref = _context.getSharedPreferences(BACKDROP_SIZE_PREF_NAME, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = pref.getAll();
        List<String> response = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.add(null);
        }
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            response.set(Integer.valueOf(entry.getKey()), entry.getValue().toString());
        }
        return response;
    }
}
