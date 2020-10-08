package com.ex.myfirstappex03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {


    Button openFileButton;
    Button movePageButton;
    Button readAtVoiceButton;
    Button autoScrollButton;
    Button bookMarkButton;
    Button MoresButton;

    String txtContentsForTransfer;
    String content;
    String realPath;


    int lineCountForTransfer;

    int mainTextLinearHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상태바없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);

        openFileButton = findViewById(R.id.openFileButton);
        movePageButton = findViewById(R.id.movePageButton);
        readAtVoiceButton = findViewById(R.id.readAtVoiceButton);
        autoScrollButton = findViewById(R.id.autoScrollButton);
        bookMarkButton = findViewById(R.id.bookMarkButton);
        MoresButton = findViewById(R.id.MoresButton);

        //처음 액티비티 호출 후 실행되게 되는 코드
        if (getIntent().getExtras() != null) {

            //인텐트 받기
            Intent intent = getIntent();

            content = getIntent().getExtras().getString("content");
            realPath = getIntent().getExtras().getString("realPath");

            //처음 MainActivity 에 menuStart() 메소드가 실행되면 하나의 값 인 height 값이 넘어오기 때문에
            //위로 빼준 코드
            mainTextLinearHeight = intent.getExtras().getInt("height");

            //메뉴액티비티가 불려질때 txtContents 란 값이 null 이 아닐경우 호출
            //이 코드의 핵심은 txtContents 라는 key 값은 넘어올때는 모두 같기때문에 메인액티비티처럼
            // 여러개의 if else 문을 구사하지않고 간편하게 줄여서 코딩 된 것
            if (getIntent().getExtras().getString("txtContents") != null) {
                txtContentsForTransfer = intent.getExtras().getString("txtContents");
                lineCountForTransfer = intent.getExtras().getInt("lineCount");

            }
        }

        //파일열기 버튼 눌렀을때
        openFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.openFileButton) {

                    Intent intent = new Intent(getApplicationContext(), FilelistActivity.class);
                    //파일 액티비티로 전송할때 height 값을 보내는 이유는 MainActivity 에서 꼭 받아야 하기 때문
                    intent.putExtra("height", mainTextLinearHeight);
                    startActivity(intent);

                }
            }
        });

        //페이지 이동 버튼 눌렀을때
        movePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //읽어주기 버튼 눌렀을때
        readAtVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (v.getId() == R.id.readAtVoiceButton) {
                    //넘어가는 인텐트들의 키값은 txtContents , lineCount , height 로 통일
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("txtContents", txtContentsForTransfer);
                    intent.putExtra("lineCount", lineCountForTransfer);
                    intent.putExtra("height", mainTextLinearHeight);
                    //식별자 (오직 식별을 위한 코드)
                    intent.putExtra("readAtVoice", "readAtVoice");
                    startActivity(intent);
                }
            }
        });

        //자동스크롤 버튼 눌렀을때
        autoScrollButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                if (v.getId() == R.id.autoScrollButton) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("txtContents", txtContentsForTransfer);
                    intent.putExtra("lineCount", lineCountForTransfer);
                    intent.putExtra("height", mainTextLinearHeight);
                    //식별자 (오직 식별을 위한 코드)
                    intent.putExtra("autoScroll", "autoScroll");
                    startActivity(intent);
                }

            }
        });

        //북마크 버튼 눌렀을때
        bookMarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()== R.id.bookMarkButton){
                    Intent intent = new Intent(getApplicationContext(), BookMarkActivity.class);
                    intent.putExtra("txtContents", txtContentsForTransfer);
                    intent.putExtra("lineCount", lineCountForTransfer);
                    intent.putExtra("height", mainTextLinearHeight);
                    intent.putExtra("content", content);
                    intent.putExtra("realPath", realPath);
                    startActivity(intent);
                }

            }
        });

        //더보기 버튼 눌렀을때
        MoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}