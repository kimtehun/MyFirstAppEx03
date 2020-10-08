package com.ex.myfirstappex03;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FilelistActivity extends ListActivity {

    List<String> item = null;
    List<String> path = null;

    int lineCount;
    int mainTextLinearHeight;

    String root = "/sdcard/";
    String content;
    String realPath;
    String extension;
    String readStr;

    TextView myPath;

    private static final int MY_PERMISSION_STORAGE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filelist);

        myPath = findViewById(R.id.path);

        //메뉴 액티비티에서 넘어온 height 값 저장
        mainTextLinearHeight = getIntent().getExtras().getInt("height");
        //로직실행

        checkPermission();
        getDir(root);

    }


    public void getDir(String dirPath) {


        myPath.setText("Location: " + dirPath);

        item = new ArrayList<String>();
        path = new ArrayList<String>();

        //파일객체생성
        File f = new File(dirPath);

        //각파일들을 담는 파일 배열 생성

        File[] files = f.listFiles();
        Log.d("TEST", toString().valueOf(f.listFiles()));
        if (!dirPath.equals(root)) {


            item.add(root);
            path.add(root);

            item.add("../");
            path.add(f.getParent());

        }


        for (int i = 0; i < files.length; i++) {

            File file = files[i];
            path.add(file.getPath());

            if (file.isDirectory()) {
                item.add(file.getName() + "/");
            } else {
                item.add(file.getName());
            }


            ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
            setListAdapter(fileList);
        }
    }

    //목록 중 하나를 터치했을 때
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(path.get(position));
        content = file.getName();//파일의 풀네임을 저장한다. (나중에 지지고 복기 위함)
        realPath = file.getPath();


        if (file.isDirectory()) {
            if (file.canRead()) {


                /**

                 if(getExtension(content) == "txt") {
                 Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                 startActivity(intent);
                 finish();
                 }
                 **/

//               Toast.makeText(getApplicationContext(),"content 가 뭐지 = > " + content ,Toast.LENGTH_LONG).show();
                getDir(path.get(position));
            } else {

                Toast.makeText(getApplicationContext(), "이게나오냐?222222", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(this).setIcon(R.drawable.button)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();


            }
        } else {
            try {
                //선택된 파일에 내용을 readStr 이라는 string 파일에 저장하면서 한라인에 글자수가 25이상이면 다음라인으로
                //임시로 한것 보강 필요
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getPath()), "EUC-KR"));
                lineCount = 0;
                readStr = "";
                int forCut;
                String forNextIf = "";
                String str = null;
                while ((str = br.readLine()) != null) {
                    forCut = str.length();
                    int forCount = 0;
                    if (forCut > 28) {
                        while (forCut > 28) {
                            readStr += str.substring(forCount * 28, (forCount + 1) * 28) + "\n";
                            forNextIf = str.substring(((forCount + 1) * 28));
                            lineCount++;
                            forCount++;
                            forCut -= 28;
                            if (forCut > 0 && forCut <= 28) {
                                readStr += forNextIf + "\n";
                                lineCount++;
                            }
                        }
                    } else {
                        readStr += str + "\n";
                        lineCount++;
                    }
                }
                //Toast.makeText(getApplicationContext(), "lineCount  = > > >   " + lineCount, Toast.LENGTH_LONG).show();
                br.close();
                //    Toast.makeText(getApplicationContext(),readStr.substring(0, readStr.length() - 1 ), Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "file not found", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            extension = getExtension(content);

            if (getIntent().getExtras() != null) {
                Intent intent = getIntent();
                if (getIntent().getExtras().getString("fromMenuVoice") != null) {

                    Toast.makeText(getApplicationContext(), "나오나", Toast.LENGTH_LONG).show();

                }
            }

            //파일의 확장자가 txt 라면
            if (extension.equals("txt")) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("txtContents", readStr);
                intent.putExtra("lineCount", lineCount);
                intent.putExtra("height", mainTextLinearHeight);
                intent.putExtra("content", content);
                intent.putExtra("realPath", realPath);
                //메인액티비티에서 받을때의 식별자
                intent.putExtra("fromFileActivity", "fromFileActivity");
//                Toast.makeText(getApplicationContext(),content + realPath,Toast.LENGTH_LONG).show();
                startActivity(intent);
                //파일리스트액티비티 닫아주기
                finish();
                //pdf 파일일때
            } else if (extension.equals("pdf")) {


                Uri uri = Uri.fromFile(file);

                //생성시 ACtION_VIEW 상수를 넣음으로 pdf 를 열수있는 뷰어 목록 하단에 추가 , 암시적 인텐트 생성
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.putExtra("contents", uri);

                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "PDF보기위한 뷰어없음", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "PDF 파일이 뷰어없음", Toast.LENGTH_LONG).show();
            }

        }
    }

    //파일을 선택하였을때 확장자 얻는 메소드
    public static String getExtension(String fileStr) {
        String fileExtension = fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
        return TextUtils.isEmpty(fileExtension) ? null : fileExtension;
    }



    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(FilelistActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }
}