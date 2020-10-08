package com.ex.myfirstappex03;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    LinearLayout mainTextLinear;

    TextToSpeech textToSpeech;

    ScrollView scrollView;

    String contents;
    String content;
    String realPath;

    ImageView imageView;

    TextView mainTextView;
    TextView pageNumber;
    TextView pagePercent;
    TextView textViewForHeight;

    int lineCountForTransfer;
    int totalPageNumber;

    int deviceHeight;

    int mainTextLinearHeight = 5;
    int temp = 3;

    double scrollTemp1;
    double scrollTemp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //상태바 없애기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                menuButtonStart();
            }

        });

        imageView = findViewById(R.id.imageView);
        scrollView = findViewById(R.id.scrollView);
        //pagePercent 를 계산하기 위한 분모값을 구하기 위한 코드 , 이렇게 하지 않으면 변수처리가 잘 안됐다
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver vto = imageView.getViewTreeObserver();
                vto.removeOnGlobalLayoutListener(this);
                Intent intent = new Intent(MainActivity.this, UselessAvtivity.class);
                intent.putExtra("height", scrollView.getHeight());

                startActivityForResult(intent, 3000);

            }
        });


        //디스플레이의 height 값을 얻기 위해 디스플레이메트릭스 생성
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();

        //이 첫 문장에 의미는 제일 처음 애플리케이션이 켜졌을때 실행되지 않아야하기에 넣은 코드이며

        if (getIntent().getExtras() != null) {

            //처음 이후에 MainActivity 가 실행되었을때 인텐트객체 생성
            Intent intent = getIntent();

            content = getIntent().getExtras().getString("content");
            realPath = getIntent().getExtras().getString("realPath");


            //FileListActivity 에서 넘어온 lineCount 는 모두 쓰이기 때문에 위로 뺐다
            lineCountForTransfer = getIntent().getExtras().getInt("lineCount");

            //아래에 코드들은 아래에 scrollChanged 에서의 오버라이드된 메소드가 잘 동작하기위한 코드로
            //공통으로 들어가는 코드들을 밖으로 빼낸 것
            //xml 파일에 있는 각 애트리뷰트들을 매핑시키는 코드

            mainTextView = findViewById(R.id.mainTextView);
            pageNumber = findViewById(R.id.pageNumber);
            pagePercent = findViewById(R.id.pagePercent);
            textViewForHeight = findViewById(R.id.textViewForHeight);


            //디바이스의 크기를 구하기 위한 코드
            scrollView.measure(0, 0);

            //9375라는 값은 실험으로 알아낸 값, 디바이스의 크기 저장 코드
            deviceHeight = displayMetrics.heightPixels * 9375 * 1 / 10000;

            //pageNumber 에 총 페이지수를 구하기 위해 필요한 코드
            totalPageNumber = (getIntent().getExtras().getInt("lineCount") / 35 + 1);

            //처음 MainActivity 가 실행되어 uselessAvtivity 에서 돌아온 값 이미지 뷰의 크기 값 저장
            mainTextLinearHeight = getIntent().getExtras().getInt("height");


            //아래에 인텐트 객체를 만들어주는 코드와 아래에 if 문과 if else문으로 걸려있는 코드들은
            //메인 액티비티와 다른 액티비티간에 인텐트를 주고 받을때 "식별자" 역할을 해주는 역할
            if (getIntent().getExtras().getString("fromFileActivity") != null) {


                //Toast.makeText(getApplicationContext(),String.valueOf(getIntent().getExtras().getInt("height")) ,Toast.LENGTH_LONG).show();

                //이 코드를 밖으로 빼지 않은 이유는 기능 중 오토스크롤에서는 선행되어야 할 코드가 있어서 후에 필요에 의해 뺐다
                scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                    //중복된 코드로 개발중 필요에 의해 추가 , 위에 이미 정으 됨
                    DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();

                    @Override
                    public void onScrollChanged() {

                        //현재라인수구하는공식=> ((double)( scrollView.getScrollY() + 2304 ) /66 )
                        //맥스라인=>("lineCount")
                        //이밑에서 해상도와 각정수를 변수 처리하는 것 질문하기 (각각 디바이스마다 해상도가 다르므로)

                        //현재페이지와 총 페이지를 구하는 공식
                        pageNumber.setText(((scrollView.getScrollY() / deviceHeight) + 1) + "/" + (totalPageNumber - 1));

                        //테스트 코드
                        textViewForHeight.setText(String.valueOf(((scrollView.getScrollY() + 2304) / 66)));

                        pagePercent.setText(String.format("%.0f", (double) (scrollView.getScrollY() * 4000) / (((getIntent().getExtras().getInt("lineCount")) - 35) * (mainTextLinearHeight))) + "%" + "(미완성)");

                        /*
                        scrollTemp1 = scrollView.getScrollY()/66;
                        scrollTemp2 = (scrollView.getScrollY()+2304)/66;

                        pagePercent.setText(toString().valueOf(scrollTemp2));

                        */

/*


                        pagePercent.setText(toString().valueOf(
                                ((((double)scrollView.getScrollY()/66))*100)
                                        /
                                        ((
                                                        (getIntent().getExtras().getInt("lineCount"))
                                                /
                                                (((scrollView.getScrollY()+2304)/66))        *         ((scrollView.getScrollY()/66))
                                                )+1)
                        ));

*/

/*                        pagePercent.setText(String.format("%.0f",
                                ((((double)scrollView.getScrollY()*100)))
                                        /
                                        (((getIntent().getExtras().getInt("lineCount")*66)/(((scrollView.getScrollY()+34)))*((scrollView.getScrollY())))+1)
                                ));
*/


                        //테스트 코드
//                        pagePercent.setText(String.format("%.0f",(double)(scrollView.getScrollY()*4000)/(((getIntent().getExtras().getInt("lineCount"))-35)*(mainTextLinearHeight)))+"%");
                        //현재스크롤이 얼마냐 아래로 갔는지에 따라 현재 보고있는 글이 얼마나 아래로 갔는것을 퍼센트로 알려주는 코드
                        /*

                        pagePercent.setText(String.format("%.1f", (
//                         (double) (    ( scrollView.getScrollY() + 2034 ) /  ( (double)( scrollView.getScrollY() + 2304 ) /66 )
                                        //미완성 코드
                                        //scrollView.getScrollY()는 현재 스크롤이 y 축으로 얼마나 내려갔는지에 대한 척도
                                        //옆의 코드는 한화면에 크기를 구하는 척도, 50을 뺀것은 메뉴바 상태바의 크기를 합치면 50
                                        //66으로 나눈 이유는 1라인에 66픽셀이고
                                        //35를 뺀것은 분자 분모 모두 35를 빼어서 처음화면이 0%가 되게끔 하기 위함
                                        (((double) (scrollView.getScrollY() + (getIntent().getExtras().getInt("height") - 50)) / 66) - 35)
                                                /
                                                //한 화면의 크기를 구하는 코드에 100을 곱해서 퍼센트화 시켰음
                                                (getIntent().getExtras().getInt("lineCount") - 35) * 100)
                        ) + "%");

                        */
                    }

                });

                //액티비티간 인텐트를 보낼때마다 이 정보를 포함시켜 넘김으로 액티비티전환 마다 화면에 글씨가 출력되도록 하는 코드
                //contents 안에는 txt 파일의 내용이 전부 담겨있다
                contents = intent.getExtras().getString("txtContents");


                mainTextView.setText(contents);


            } else if (getIntent().getExtras().getString("readAtVoice") != null) {


                scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                    @Override
                    public void onScrollChanged() {

                        pageNumber.setText(((scrollView.getScrollY() / deviceHeight) + 1) + "/" + (totalPageNumber - 1));

                        textViewForHeight.setText(String.valueOf(((scrollView.getScrollY() + 2304) / 66)));

                        pagePercent.setText(String.format("%.0f", (double) (scrollView.getScrollY() * 4000) / (((getIntent().getExtras().getInt("lineCount")) - 35) * (mainTextLinearHeight))) + "%" + "(미완성)");

/*

                        pagePercent.setText(String.format("%.1f", (((scrollView.getScrollY() * 3500 / (double) deviceHeight * 100000 / 1001 * 10000 / 9995))
                                / ((getIntent().getExtras().getInt("lineCount") * 962 * 1 / 10) - 3360))) + "%");

*/

                    }

                });

                contents = intent.getExtras().getString("txtContents");

                mainTextView.setText(contents);

                //tet 파일을 기존의 소리를 멈추게하고 읽게해주는 코드
                textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        textToSpeech.speak(contents, TextToSpeech.QUEUE_FLUSH, null);
                        Button mainMenuButton = findViewById(R.id.mainMenuButton);
                        mainMenuButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                textToSpeech.stop();
                                textToSpeech.shutdown();
                                menuButtonStart();
                            }

                        });
                    }
                });
            } else if (getIntent().getExtras().getString("autoScroll") != null) {


                contents = intent.getExtras().getString("txtContents");

                mainTextView.setText(contents);

                scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                    @Override
                    public void onScrollChanged() {

                        pageNumber.setText(((scrollView.getScrollY() / deviceHeight) + 1) + "/" + (totalPageNumber - 1));

                        textViewForHeight.setText(String.valueOf(((scrollView.getScrollY() + 2304) / 66)));

/*
                        pagePercent.setText(String.format("%.1f", ( scrollView.getScrollY() - mainTextLinear.getHeight()
                                / (mainTextLinear.getHeight()) ) ) + "%");
*/

                        pagePercent.setText(String.format("%.0f", (double) (scrollView.getScrollY() * 4000) / (((getIntent().getExtras().getInt("lineCount")) - 35) * (mainTextLinearHeight))) + "%" + "(미완성)");

/*

                        pagePercent.setText(String.format("%.1f", (((scrollView.getScrollY() * 3500 / (double) mainTextView.getBottom()))
                                / ((getIntent().getExtras().getInt("lineCount") * 962 * 1 / 10) - 3360))) + "%");
*/

                    }


                });

                //자동 스크롤 코드
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator.ofInt(scrollView, "scrollY", mainTextView.getBottom()).setDuration(100000).start();
                    }
                });
            } else if (getIntent().getExtras().getString("bookMark") != null) {
                //이 코드를 밖으로 빼지 않은 이유는 기능 중 오토스크롤에서는 선행되어야 할 코드가 있어서 후에 필요에 의해 뺐다
                scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

                    //중복된 코드로 개발중 필요에 의해 추가 , 위에 이미 정으 됨
                    DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();

                    @Override
                    public void onScrollChanged() {

                        //현재라인수구하는공식=> ((double)( scrollView.getScrollY() + 2304 ) /66 )
                        //맥스라인=>("lineCount")
                        //이밑에서 해상도와 각정수를 변수 처리하는 것 질문하기 (각각 디바이스마다 해상도가 다르므로)

                        //현재페이지와 총 페이지를 구하는 공식
                        pageNumber.setText(((scrollView.getScrollY() / deviceHeight) + 1) + "/" + (totalPageNumber - 1));


                        //테스트 코드
                        textViewForHeight.setText(String.valueOf(((scrollView.getScrollY() + 2304) / 66)));

                        pagePercent.setText(String.format("%.0f", (double) (scrollView.getScrollY() * 4000) / (((getIntent().getExtras().getInt("lineCount")) - 35) * (mainTextLinearHeight))) + "%" + "(미완성)");

/*
                        //테스트 코드
                        pagePercent.setText(String.format("%.1f", (((scrollView.getScrollY() * 3500 / (double) deviceHeight * 100000 / 1001 * 10000 / 9995))
                                / ((getIntent().getExtras().getInt("lineCount") * 962 * 1 / 10) - 3360))) + "%");
*/
                        //현재스크롤이 얼마냐 아래로 갔는지에 따라 현재 보고있는 글이 얼마나 아래로 갔는것을 퍼센트로 알려주는 코드
                     /*   pagePercent.setText(String.format("%.1f", (
//                         (double) (    ( scrollView.getScrollY() + 2034 ) /  ( (double)( scrollView.getScrollY() + 2304 ) /66 )
                                        //미완성 코드
                                        //scrollView.getScrollY()는 현재 스크롤이 y 축으로 얼마나 내려갔는지에 대한 척도
                                        //옆의 코드는 한화면에 크기를 구하는 척도, 50을 뺀것은 메뉴바 상태바의 크기를 합치면 50
                                        //66으로 나눈 이유는 1라인에 66픽셀이고
                                        //35를 뺀것은 분자 분모 모두 35를 빼어서 처음화면이 0%가 되게끔 하기 위함
                                        (((double) (scrollView.getScrollY() + (getIntent().getExtras().getInt("height") - 50)) / 66) - 35)
                                                /
                                                //한 화면의 크기를 구하는 코드에 100을 곱해서 퍼센트화 시켰음
                                                (getIntent().getExtras().getInt("lineCount") - 35) * 100)
                        ) + "%");
*/
                    }

                });

                //액티비티간 인텐트를 보낼때마다 이 정보를 포함시켜 넘김으로 액티비티전환 마다 화면에 글씨가 출력되도록 하는 코드
                //contents 안에는 txt 파일의 내용이 전부 담겨있다
                contents = intent.getExtras().getString("txtContents");


                mainTextView.setText(contents);


            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //앱 종료시 textToSpeech 제거
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }


    public void menuButtonStart() {

        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
        //메뉴 버튼을 누를때마다 height 값을 전달, 액티비티간 데이터 교신
        intent.putExtra("height", mainTextLinearHeight);

        //처음 메뉴버튼을 눌렀을때는 인텐트 객체가 없기때문에 위의 문장은 밖에 있는것이고 아래문장은
        //파일을 열어서 실행했을 때 인텐트간에 필수적인 데이터를 교신하기 위한 코드
        if (getIntent().getExtras() != null) {

            //contents 는 파일 내용 , lineCount 는 파일의 라인수
            intent.putExtra("txtContents", contents);
            intent.putExtra("lineCount", lineCountForTransfer);
            intent.putExtra("content", content);
            intent.putExtra("realPath", realPath);
        }
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 3000) {
                //UselessAvtivity 에서 MainActivity 로 보낸 코드를 변수처리하기 위해 변수에 넣는 코드
                mainTextLinearHeight = Integer.parseInt(data.getExtras().getString("height"));

            }
        }
    }
}