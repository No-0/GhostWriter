package com.ghostwriter.ghostwriter;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class SelectSubActivity extends AppCompatActivity {
    static final String[] LIST_MENU = {"국어", "수학","국사","사회문화","화학","생명과학","물리"} ;// 서버에서 진행중인 수업 리스트 불러오기
    public  static Context mContext;
    String strText;


    String IPadr;
    String PortN;
    SocketClient client_Server;
    ReceiveThread RT;


    Socket socket;
    TextView show;
    Handler msghandler;




    public String GetText(){
        return strText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub);

        mContext = this;

        IPadr = "223.194.153.40"; //아이피주소
        PortN =  "5000"; //포트번호
        

        client_Server =new SocketClient(IPadr, PortN);

        client_Server.start();



        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;



        ListView listview = (ListView) findViewById(R.id.listview) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position) ;//선택된 과목명을 strText에 저장함

                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivity(intent);

                // TODO : use strText
            }
        }) ;

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
                switch(RT.getMsg()){
                    case "선생님 국어" :
                    case "선생님 수학" :
                }
                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();


                output.writeUTF(mac);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
