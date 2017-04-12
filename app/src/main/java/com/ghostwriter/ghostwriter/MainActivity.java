package com.ghostwriter.ghostwriter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {
    private SpeechRecognizerClient client;
    private int i;
    public static final String APIKEY = "3feaa382db9fdfe5ac35fa0094b4f986";

    EditText et;
    EditText ip_EditText;
    EditText port_EditText;
    Button cnt;
    Button snb;
    Handler msghandler;

    SocketClient client_Server;
    SendThread send;
    Socket socket;

    LinkedList<SocketClient> threadList;
    HashMap<String, Integer> datafromteacher = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        findViewById(R.id.radioButton).setOnClickListener(this);

        et =(EditText) findViewById(R.id.Text);
        ip_EditText = (EditText)findViewById(R.id.ip_EditText);
        port_EditText = (EditText)findViewById(R.id.port_EditText);
        cnt = (Button)findViewById(R.id.connect_Button);
        snb = (Button)findViewById(R.id.send_button);
        threadList = new LinkedList<MainActivity.SocketClient>();

        ip_EditText.setText(""); //아이피주소
        port_EditText.setText(""); //포트번호




        cnt.setOnClickListener(new View.OnClickListener() {  //서버연결
            @Override
            public void onClick(View arg0) {
                client_Server = new SocketClient(ip_EditText.getText().toString(),
                        port_EditText.getText().toString());
                threadList.add(client_Server);
                client_Server.start();
            }
        });

        snb.setOnClickListener(new View.OnClickListener() {  //보내기버튼
            @Override
            public void onClick(View arg0) {
                if (et.getText().toString() != null) {
                    send = new SendThread(socket);
                    send.start();

                    et.setText("");
                }
            }
        });







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
        String sendmsg = et.getText().toString();
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
                    if (sendmsg != null) {
                        output.writeUTF(mac + " : " + sendmsg);
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

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;

        if(id == R.id.radioButton){
            if(PermissionUtils.checkAudioRecordPermission(this)) {
                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                        setApiKey(APIKEY).
                        setServiceType(SpeechRecognizerClient.SERVICE_TYPE_DICTATION);
                i = 0;
                client = builder.build();
                client.setSpeechRecognizeListener(this);
                client.startRecording(true);
                Log.i("startRe",""+i);

            }
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
        client.stopRecording();
        Log.i("STOPREC", ""+i);
        i++;

        client.startRecording(true);
        Log.i("startRe",""+i);
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onPartialResult(String s) {

    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> Text = bundle.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        ArrayList<Integer> confs = bundle.getIntegerArrayList(SpeechRecognizerClient.KEY_CONFIDENCE_VALUES);

        Strcontroler strcontroler = new Strcontroler();
        strcontroler.str = Text.get(0);
        strcontroler.Thrcounter = i;

        datafromteacher.put(strcontroler.str, strcontroler.Thrcounter);

        Log.i("str", strcontroler.str);             //  -->서버로 보내질 음성인식 스트링
        Log.i("i", ""+strcontroler.Thrcounter);    //  -->서버로 보내질 음성인식 순서

    }

    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onFinished() {

    }
}
class Strcontroler{
    String str;
    int Thrcounter;

}