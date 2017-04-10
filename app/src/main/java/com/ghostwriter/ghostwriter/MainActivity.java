package com.ghostwriter.ghostwriter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;
import net.daum.mf.speech.api.impl.util.PermissionUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {
    private SpeechRecognizerClient client;
    private int i;

    public static final String APIKEY = "3feaa382db9fdfe5ac35fa0094b4f986";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);

        findViewById(R.id.radioButton).setOnClickListener(this);
        }




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
        Log.i("str", strcontroler.str);
        Log.i("i", ""+strcontroler.Thrcounter);

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