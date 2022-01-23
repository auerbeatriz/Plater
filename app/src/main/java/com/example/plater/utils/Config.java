package com.example.plater.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

    //TODO: alterar nome do servidor para o da app no heroku
    public static String SERVER_URL_BASE = "http://plater.tech/";
    public static String API_KEY = "AIzaSyCQtaGdMDFJ1z6MQe6QVbpsRF4BvMOKAW8";

    public static void setLogin(Context context, String login) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("login", login).commit();
    }

    public static String getLogin(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        return mPrefs.getString("login", "");
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("password", password).commit();
    }

    public static String getPassword(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        return mPrefs.getString("password", "");
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("username", username).commit();
    }

    public static String getUsername(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        return mPrefs.getString("username", "");
    }

    public static void setName(Context context, String name) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putString("name", name).commit();
    }

    public static String getName(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("configs", 0);
        return mPrefs.getString("name", "");
    }
}
