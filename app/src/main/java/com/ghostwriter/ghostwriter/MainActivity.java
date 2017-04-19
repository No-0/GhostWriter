package com.ghostwriter.ghostwriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;
import net.daum.mf.speech.api.impl.util.PermissionUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

//import static com.ghostwriter.ghostwriter.R.id.ip_EditText;
//import static com.ghostwriter.ghostwriter.R.id.port_EditText;

public class MainActivity extends Activity implements View.OnClickListener, SpeechRecognizeListener {
    private SpeechRecognizerClient client;
    private SpeechRecognizerClient client2;
    private int i;
    public static final String APIKEY = "3feaa382db9fdfe5ac35fa0094b4f986";

    Object SelectGrade;
    Object SelectClass;

    String kkk;

    String IPadr;
    String PortN;

    Button cnt;
    Button snb;
    Button But;
    Handler msghandler;

    SocketClient client_Server;
    SendThread send;
    SendThread send2;
    SendThread2 send3;
    Socket socket;

    String KKK2;

    SpeechRecognizerClient.Builder builder1 = new SpeechRecognizerClient.Builder().
            setApiKey(APIKEY).
            setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION)
            .setGlobalTimeOut(50);


    LinkedList<SocketClient> threadList;
    HashMap<String, Integer> datafromteacher = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView pictureim = (ImageView)findViewById(R.id.imageView2);
        pictureim.setImageResource(R.drawable.title);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        findViewById(R.id.Button).setOnClickListener(this);
        findViewById(R.id.button2).setEnabled(false);

        //cnt = (Button)findViewById(R.id.connect_Button);
        //snb = (Button)findViewById(R.id.send_button);
       // But = (Button)findViewById(R.id.radioButton);
        threadList = new LinkedList<MainActivity.SocketClient>();

        IPadr = "223.194.154.49"; //아이피주소
        PortN =  "5001"; //포트번호



        client_Server = new SocketClient(IPadr, PortN);
        threadList.add(client_Server);
        client_Server.start();



        //학년, 반 선택
        Spinner gradeSpinner=(Spinner)findViewById(R.id.Grade);
        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SelectGrade = adapterView.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner SlassSpinner=(Spinner)findViewById(R.id.Class);

        SlassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                SelectClass = adapterView.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


//        snb.setOnClickListener(new View.OnClickListener() {                                          //보내기버튼 not need now
//            @Override
//            public void onClick(View arg0) {
//                if (et.getText().toString() != null) {
//                    send = new SendThread(socket);
//                    send.start();
//                    et.setText("");
//                }
//            }
//        });

        //But.setOnClickListener(this);

    }


    class SocketClient extends Thread {
        boolean threadAlive;
        String ip;
        String port;
        String mac;

        //InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader br = null;

        private DataOutputStream output = null;

        public SocketClient(String ip, String port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(ip, Integer.parseInt(port));

                //inputStream = socket.getInputStream();
                output = new DataOutputStream(socket.getOutputStream());


                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();

                output.writeUTF(mac);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class SendThread extends Thread {
        private Socket socket;
        DataOutputStream output;

        public SendThread(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());

            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                Log.d(ACTIVITY_SERVICE, "11111");
                String mac = null;
                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();

                if (output != null) {
                    if (kkk != null) {

                        output.writeUTF(mac + " : " + kkk);
                        //KKK2 = kkk;
                        //onResults();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe)

            {
                npe.printStackTrace();
            }

        }
    }
    class SendThread2 extends Thread {
        private Socket socket;
        DataOutputStream output;

        public SendThread2(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());

            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                Log.d(ACTIVITY_SERVICE, "11111");
                String mac = null;
                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();

                if (output != null) {
                    if (kkk != null) {
                        output.writeUTF(mac + " : " + kkk);
                        //onResults();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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
//               SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
//                        setApiKey(APIKEY).
//                        setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION)
//                        .setGlobalTimeOut(50);
//
//                client = builder.build();
//                client.setSpeechRecognizeListener(this);
                findViewById(R.id.Button).setEnabled(false);// 수업시작 버튼 비활성화
                findViewById(R.id.button2).setEnabled(true);

                client = builder1.build();
                client.setSpeechRecognizeListener(this);
                client.startRecording(true);
                Log.i("startRe",""+i);

            }
        }

        if(id == R.id.button2){
                findViewById(R.id.Button).setEnabled(true);//수업 시작 버튼 활성화
            findViewById(R.id.button2).setEnabled(false);
          //      client = builder1.build();
           //     client.stopRecording();
            //    Log.i("End",""+i);


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(VoiceRecoActivity.EXTRA_KEY_RESULT_ARRAY);

            final StringBuilder builder = new StringBuilder();
            for (String result : results) {
                builder.append(result);
                builder.append("\n");
            }

            new AlertDialog.Builder(this).
                    setMessage(builder.toString()).
                    setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).
                    show();
        }
        else if (requestCode == RESULT_CANCELED) {
            // 음성인식의 오류 등이 아니라 activity의 취소가 발생했을 때.
            if (data == null) {
                return;
            }

            int errorCode = data.getIntExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_CODE, -1);
            String errorMsg = data.getStringExtra(VoiceRecoActivity.EXTRA_KEY_ERROR_MESSAGE);

            if (errorCode != -1 && !TextUtils.isEmpty(errorMsg)) {
                new AlertDialog.Builder(this).
                        setMessage(errorMsg).
                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        show();
            }
        }
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {
        //client.stopRecording();
       // Log.i("STOPREC", ""+i);
        i++;

        //client.startRecording(true);
        Log.i("startRe",""+i);
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onPartialResult(String s) {

//        kkk=s;
//        if(KKK2 != null)
//            kkk = kkk.substring(KKK2.length(), kkk.length());
//        send2 = new SendThread(socket);
//        send2.start();


    }



    @Override
    public void onResults(Bundle bundle) {
        final StringBuilder builder = new StringBuilder();

        ArrayList<String> Text = bundle.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = bundle.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);


        Strcontroler strcontroler = new Strcontroler();
        strcontroler.str = Text.get(0);

        strcontroler.Thrcounter = i;

        kkk = strcontroler.str;

        findViewById(R.id.Button).performClick();
        send3= new SendThread2(socket);
        send3.start();

        Log.i("str", strcontroler.str);             //  -->서버로 보내질 음성인식 스트링
        Log.i("i", ""+strcontroler.Thrcounter);    //  -->서버로 보내질 음성인식 순서



    }

    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onFinished() {

        client.startRecording(true);

    }

}
class Strcontroler{
    String str;
    int Thrcounter;

}
