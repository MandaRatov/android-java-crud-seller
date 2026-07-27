package com.medium.seller.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.medium.seller.dao.VendeurDAO;
import com.medium.seller.model.Vendeur;

@Database(entities = {Vendeur.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract VendeurDAO vendeurDAO();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "vendeur_db"
                ).build();
            }
        }
        return INSTANCE;
    }
}
