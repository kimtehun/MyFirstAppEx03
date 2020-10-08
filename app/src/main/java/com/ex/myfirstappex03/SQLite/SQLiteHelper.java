package com.ex.myfirstappex03.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class SQLiteHelper extends android.database.sqlite.SQLiteOpenHelper{

    public final String TABLE_NAME = "bookmark" ;
    public final String TEXT_TITLE = "textTitle";
    public final String TEXT_PATH = "textPath";

    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create_query = "create table if not exists " + TABLE_NAME + "("
                + TEXT_TITLE + " text not null , "
                + TEXT_PATH + " text primary key);" ;

        db.execSQL(create_query);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String drop_query = "drop table " + TABLE_NAME + ";";

        db.execSQL(drop_query);

        onCreate(db);

    }
}
