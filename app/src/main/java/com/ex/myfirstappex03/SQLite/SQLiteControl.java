package com.ex.myfirstappex03.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLiteControl {


    SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String[]> arrayList;

    String[] selectArray;


    public SQLiteControl(SQLiteHelper _helper){
        this.sqLiteHelper = _helper;
    }

    public void insert(String textTitle, String textPath){

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        arrayList = new ArrayList<>();

        values.put(sqLiteHelper.TEXT_TITLE, textTitle);
        values.put(sqLiteHelper.TEXT_PATH, textPath);

        sqLiteDatabase.insert(sqLiteHelper.TABLE_NAME, null , values);

    }

    public String[] select(String title, String path){

        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(sqLiteHelper.TABLE_NAME, null,
                null,null,null,null,null);



        try {


            String sql = "SELECT * FROM " + sqLiteHelper.TABLE_NAME;

            Cursor selectCursor = sqLiteDatabase.rawQuery(sql, null);

            int count = selectCursor.getCount();

            selectArray = new String[count];

            for( int i = 0 ; i < count ; i ++){

               selectCursor.moveToNext();

               arrayList.add(new String[]{title, path});

            }
        } catch (Exception e){
            e.printStackTrace();
        }

        String[] columnName = {sqLiteHelper.TEXT_TITLE, sqLiteHelper.TEXT_PATH};

        String[] returnValue = new String[columnName.length];

   /*     while(cursor.moveToNext()){
            for(int i = 0 ; i < returnValue.length ; i ++){
                returnValue[i] = cursor.getString(cursor.getColumnIndex(columnName[i]));
                Log.e("DB select : " , i + " - " + returnValue[i]);

            }
        }*/

        cursor.close();
        return  returnValue;

    }

    public void update(String textTitle, String textPath){
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(textTitle , textPath );

        sqLiteDatabase.update(sqLiteHelper.TABLE_NAME, values, "textPath=?",
                new String[]{textPath});
    }

    public void delete(String textPath){
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        sqLiteDatabase.delete(sqLiteHelper.TABLE_NAME, "textPath=?"
                , new String[]{textPath});
    }

}
