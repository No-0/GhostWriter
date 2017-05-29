package com.ghostwriter.ghostwriter;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;
import net.daum.mf.speech.api.impl.util.PermissionUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {
    private SpeechRecognizerClient client;
    private int i;
    public static final String APIKEY = "3feaa382db9fdfe5ac35fa0094b4f986";

    Object SelectSubject;
    String kkk;
    String IPadr;
    String PortN;

    boolean CR = false;
    boolean Check = false;


    SocketClient client_Server;
    SocketClient2 lesson_Server;
    SendThread2 send3;
    Socket socket;
    Socket socket2;
    String Subject;

    TextView text;
    Toolbar mToolbar;

    SpeechRecognizerClient.Builder builder1 = new SpeechRecognizerClient.Builder().
            setApiKey(APIKEY).
            setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION)
            .setGlobalTimeOut(50);

    LinkedList<SocketClient> threadList;
    LinkedList<SocketClient2> threadList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        text = (TextView)findViewById(R.id.status);
        ImageView pictureim = (ImageView)findViewById(R.id.imageView2);
        pictureim.setImageResource(R.drawable.title);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        findViewById(R.id.Button).setOnClickListener(this);

        findViewById(R.id.button2).setOnClickListener(this);

        findViewById(R.id.button2).setEnabled(false);

        threadList = new LinkedList<MainActivity.SocketClient>();

        IPadr = "223.194.156.241"; //아이피주소
        PortN =  "5000"; //포트번호


        client_Server = new SocketClient(IPadr, PortN);
        threadList.add(client_Server);
        client_Server.start();

        //과목 선택

        Spinner SlassSpinner=(Spinner)findViewById(R.id.Subject);

        SlassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SelectSubject = adapterView.getItemAtPosition(position);//해당위치의 과목명 저장
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Set a toolbar to  replace to action bar
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//툴바 뒤로가기
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    class SocketClient extends Thread {
        boolean threadAlive;
        String ip;
        String port;
        String mac;


        private PrintWriter output = null;

        public SocketClient(String ip, String port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(ip, Integer.parseInt(port));

                output = new PrintWriter(socket.getOutputStream(), true);

                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();

                output.println(getURLEncode(mac));
                //output.print(mac);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    class SocketClient2 extends Thread {
        boolean threadAlive;
        String ip;
        String port;
        String mac;
        Socket socket;

        private PrintWriter output = null;

        public SocketClient2(String ip, String port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;

            try {
                socket2 = new Socket(this.ip,Integer.parseInt(this.port));
                this.socket = new Socket(this.ip,Integer.parseInt(this.port));
                output =  new PrintWriter(socket2.getOutputStream(), true);

            } catch (Exception e) {
            }
        }

        @Override
        public void run() {
            try {


                //output =  new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()));

                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();

                output.println(getURLEncode("Teacher"));
//                if(CR){
//                    socket2.close();
//                    CR=false;
//                }
            } catch (NullPointerException npe)
            {
                npe.printStackTrace();
            }
        }
    }


    public static String getURLEncode(String content){
        try {
            return URLEncoder.encode(content,"utf-8");
//            return URLDecoder.decode(content, "utf-8");  // EUC-KR
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    class SendThread2 extends Thread {
        private Socket socket;
        PrintWriter output;

        public SendThread2(Socket socket) {
            this.socket = socket;
            try {
                output =  new PrintWriter(socket.getOutputStream(), true);

            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                Log.d(ACTIVITY_SERVICE, "111111");
                String mac = null;
                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();

                if (output != null) {
                    if (kkk != null) {
                        Log.i("kkk", kkk);
                        output.println(getURLEncode(kkk));
                        //output.println(kkk.decode("utf-8"));
                        //onResults();
                    }
                }
            } catch (NullPointerException npe)
            {
                npe.printStackTrace();
            }

        }
    }
    ////////////////////////////////////////////////////////음성인식관련/////////////////////////////////////////////
    @Override
    public void onDestroy() {
        super.onDestroy();

        SpeechRecognizerManager.getInstance().finalizeLibrary();
    }




    @Override
    public void onClick(View v) {

        int id = v.getId();

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_DICTATION;

        if(id == R.id.Button){
            if(PermissionUtils.checkAudioRecordPermission(this)) {
                text.setText("수업중입니다");
                findViewById(R.id.Button).setEnabled(false);// 수업시작 버튼 비활성화
                findViewById(R.id.button2).setEnabled(true);
                if(!Check) {
                    switch (SelectSubject.toString()) {
                        case "국어":
                            PortN = "5001";
                            break;
                        case "수학":
                            PortN = "5002";
                            break;
                        case "국사":
                            PortN = "5003";
                            break;
                        case "사회문화":
                            PortN = "5004";
                            break;
                        case "화학":
                            PortN = "5005";
                            break;
                        case "생명과학":
                            PortN = "5006";
                            break;
                        case "물리":
                            PortN = "5007";
                            break;
                        default: {
                            //어플 종료
                            moveTaskToBack(true);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }



                    }



                    lesson_Server = new SocketClient2(IPadr, PortN);
                    lesson_Server.start();
                    Log.i(PortN,"port");
                    //threadList2.add(lesson_Server);


                    Check = true;
                }


                CR = false;
                client = builder1.build();
                client.setSpeechRecognizeListener(this);
                client.startRecording(true);

                Log.i("startRe", "" + i);

            }
        }


        if(id == R.id.button2){
            text.setText("수업중이 아닙니다");
            findViewById(R.id.button2).setEnabled(false);
            CR=true;
            client.cancelRecording();

            findViewById(R.id.Button).setEnabled(true);//수업 시작 버튼 활성화

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onPartialResult(String s) {

    }



    @Override
    public void onResults(Bundle bundle) {
        final StringBuilder builder = new StringBuilder();

        ArrayList<String> Text = bundle.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = bundle.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);


        Strcontroler strcontroler = new Strcontroler();
        strcontroler.str = Text.get(0);

        strcontroler.Thrcounter = i;
        kkk = "teacher ";
        kkk += strcontroler.str;

        //findViewById(R.id.Button).performClick();

        send3= new SendThread2(socket2);

        send3.start();



        Log.i("str", strcontroler.str);             //  -->서버로 보내질 음성인식 스트링
        Log.i("i", ""+strcontroler.Thrcounter);    //  -->서버로 보내질 음성인식 순서

    }


    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onFinished() {
        if(CR ==false)
            client.startRecording(true);

    }

}
class Strcontroler{
    String str;
    int Thrcounter;

}



