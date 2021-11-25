package com.example.plater.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.plater.Ingrediente;
import com.example.plater.PassoPreparo;
import com.example.plater.Recipe;

@Database(entities = {Recipe.class, Ingrediente.class, PassoPreparo.class}, version = 1)
public abstract class MyDB extends RoomDatabase {

    private static MyDB INSTANCE;

    public static MyDB getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), MyDB.class, "borrow_db")
                            .build();
        }
        return INSTANCE;
    }

    public abstract MyDao myDao();

}