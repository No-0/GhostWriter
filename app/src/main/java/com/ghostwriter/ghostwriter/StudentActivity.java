package com.ghostwriter.ghostwriter;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class StudentActivity extends AppCompatActivity {


    String IPadr;
    String PortN;
    SocketClient client_Server;
    ReceiveThread RT;

    String GETText;

    Socket socket;
    TextView show;
    Handler msghandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        IPadr = "223.194.153.40"; //아이피주소
        PortN =  "5000"; //포트번호
        show = (TextView)findViewById(R.id.show);

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 1111) {
                    show.append(hdmsg.obj.toString() + "\n");
                }
            }
        };

        client_Server = new SocketClient(IPadr, PortN);

        client_Server.start();

        GETText = ((SelectSubActivity)SelectSubActivity.mContext).GetText();//학생이 선택한 과목명반환함수
            switch(GETText){//받은 과목명에 따른 포트번호 할당, 현재는 임시 포트번호
                case "국어": PortN =  "5001";break;
                case "수학": PortN =  "5002";break;
                case "국사": PortN =  "5003";break;
                case "사회문화": PortN =  "5004";break;
                case "화학": PortN =  "5005";break;
                case "생명과학": PortN =  "5006";break;
                case "물리": PortN =  "5007";break;
                default: {
                    Toast.makeText(this, "Select Subject Error", Toast.LENGTH_LONG).show();
                    //어플 종료
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }
        //폰트 크기 설정
        Button FontUp = (Button) findViewById(R.id.font_up);
        FontUp.setOnClickListener(new View.OnClickListener() {
            TextView textView = (TextView) findViewById(R.id.show);
            float textSize;

            @Override
            public void onClick(View v) {
                textSize = textView.getTextSize();
                if (textSize < 90) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize + 4);
                }
            }
        });

        Button FontDown = (Button) findViewById(R.id.font_down);
        FontDown.setOnClickListener(new View.OnClickListener() {
            float textSize;
            TextView textView = (TextView) findViewById(R.id.show);

            @Override
            public void onClick(View v) {
                textSize = textView.getTextSize();
                if (textSize > 18) {
                    textSize = textSize - 4;
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
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
                RT = new ReceiveThread(socket);
                RT.start();

                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();


                output.writeUTF(mac);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ReceiveThread extends Thread {
        private Socket socket = null;
        DataInputStream input;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                while (input != null) {
                    String msg = input.readUTF();
                    if (msg != null) {
                        Log.d(ACTIVITY_SERVICE, "test");

                        Message hdmsg = msghandler.obtainMessage();
                        hdmsg.what = 1111;
                        hdmsg.obj = msg;
                        msghandler.sendMessage(hdmsg);
                        Log.d(ACTIVITY_SERVICE, hdmsg.obj.toString());

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
