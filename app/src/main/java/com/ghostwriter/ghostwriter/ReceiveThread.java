package com.ghostwriter.ghostwriter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import static android.content.Context.ACTIVITY_SERVICE;


class ReceiveThread extends Thread {
    Handler msghandler;
    private Socket msocket = null;
    DataInputStream input;

    public ReceiveThread(Socket socket) {
        msocket = socket;
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
