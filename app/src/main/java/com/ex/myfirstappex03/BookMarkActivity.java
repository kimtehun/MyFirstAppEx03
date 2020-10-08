package com.ex.myfirstappex03;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ex.myfirstappex03.SQLite.SQLiteHelper;
import com.ex.myfirstappex03.bookListView.BookMarkAdapter;

import java.util.ArrayList;

public class BookMarkActivity extends AppCompatActivity {

    SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;

    Cursor cursor;

    ListView listView;
    BookMarkAdapter bookMarkAdapter;

    ArrayList<String> arrayList;

    String txtContentsForTransfer;
    String content;
    String realPath;

    String[] selectResult;

    int lineCountForTransfer;
    int mainTextLinearHeight;

    int count = 0;

    int listViewPosition = 0;

    Button addBookMark;
    Button close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        listView = findViewById(R.id.listView);
        addBookMark = findViewById(R.id.addBookMark);
        close = findViewById(R.id.close);

        Intent getIntent = getIntent();

        txtContentsForTransfer = getIntent.getExtras().getString("txtContents");
        lineCountForTransfer = getIntent.getExtras().getInt("lineCount");
        mainTextLinearHeight = getIntent.getExtras().getInt("height");
        content = getIntent().getExtras().getString("content");
        realPath = getIntent().getExtras().getString("realPath");


        sqLiteHelper = new SQLiteHelper(
                BookMarkActivity.this,
                "test3.db",
                null
                , 2
        );

        arrayList = new ArrayList<String>();


        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        sqLiteHelper.onCreate(sqLiteDatabase);


        //디미에있는내용을 select 문으로 받아온뒤에 어뎁터에 저장시킨뒤 리스트뷰에 뿌린다

        cursor = sqLiteDatabase.query(sqLiteHelper.TABLE_NAME, null,
                null, null, null, null, null);

        selectResult = cursor.getColumnNames();

        while (cursor.moveToNext()) {

            for (int i = 0; i < cursor.getCount(); i++) {

                selectResult[0] = content;
                selectResult[1] = realPath;

            }
        }

        //  Toast.makeText(getApplicationContext(), selectResult[0] + " / " + selectResult[1], Toast.LENGTH_LONG).show();


        bookMarkAdapter = new BookMarkAdapter();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                listViewPosition = position;
                return false;
            }
        });

        registerForContextMenu(listView);

        selectListView();

        listView.setAdapter(bookMarkAdapter);

        sqLiteDatabase.close();
        sqLiteHelper.close();
        db_close();

        addBookMark.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.addBookMark) {

                    sqLiteDatabase = sqLiteHelper.getWritableDatabase();

                    content = getIntent().getExtras().getString("content");
                    realPath = getIntent().getExtras().getString("realPath");
                    String sql = "INSERT INTO " + sqLiteHelper.TABLE_NAME + " VALUES('" + content + "','" + realPath + "');";
                    if (CheckIsInDBorNot()) {
                        try {
                            sqLiteDatabase.execSQL(sql);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("txtContents", txtContentsForTransfer);
                intent.putExtra("lineCount", lineCountForTransfer);
                intent.putExtra("height", mainTextLinearHeight);
                intent.putExtra("content", content);
                intent.putExtra("realPath", realPath);
                //식별자 (오직 식별을 위한 코드)
                intent.putExtra("bookMark", "bookMark");
                startActivity(intent);

            }
        });

    }

    public void db_close() {
        sqLiteHelper.close();
    }

    public void selectListView() {

        String sql = "select * from bookmark";
        Cursor cursorSelect = sqLiteDatabase.rawQuery(sql, null);

        while (cursorSelect.moveToNext()) {

            content = cursorSelect.getString(0);
            realPath = cursorSelect.getString(1);

            bookMarkAdapter.addItem(content, realPath);

        }
        cursorSelect.close();
    }

    private boolean CheckIsInDBorNot() {

        content = getIntent().getExtras().getString("content");
        realPath = getIntent().getExtras().getString("realPath");
        count = 0;
        //select 로 검색이 되면 위에 insert 넣는 구문 실행 안됨
        String selectQuery = "SELECT  * FROM " + sqLiteHelper.TABLE_NAME;

        Cursor cursor2 = sqLiteDatabase.rawQuery(selectQuery, null);

        cursor2.moveToFirst();
        while (cursor2.moveToNext()) {

            if (cursor2.getString(0).equals(content) && cursor2.getString(1).equals(realPath)) {
                count++;

            }
        }


        if (count == 1) {
            cursor2.close();
//            Toast.makeText(getApplicationContext(), "중복으로 인한 등록 실패", Toast.LENGTH_LONG).show();
            return false;
        }
        cursor2.close();
//        Toast.makeText(getApplicationContext(), "등록 성공", Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {


        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        if (v == listView) {
            menuInflater.inflate(R.menu.bookmark_menu1, menu);

        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_bookmark) {
            deleteBookMark();
        }


        return false;
    }

    public void deleteBookMark() {
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        String sql = "DELETE FROM " + sqLiteHelper.TABLE_NAME
                + " WHERE " + sqLiteHelper.TEXT_PATH + " IN (SELECT "
                + sqLiteHelper.TEXT_PATH + " FROM " + sqLiteHelper.TABLE_NAME
                + " LIMIT 1 OFFSET " + listViewPosition + ")";
        sqLiteDatabase.execSQL(sql);

    }
}
