package com.ghostwriter.ghostwriter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import net.daum.mf.speech.api.SpeechRecognizeListener;
import net.daum.mf.speech.api.SpeechRecognizerClient;
import net.daum.mf.speech.api.SpeechRecognizerManager;
import net.daum.mf.speech.api.impl.util.PermissionUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SpeechRecognizeListener {
    private SpeechRecognizerClient client;

    public static final String APIKEY = "";

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



                client = builder.build();

                client.setSpeechRecognizeListener(this);
                client.startRecording(true);

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

    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onPartialResult(String s) {
        EditText e = (EditText) findViewById(R.id.Text);
        e.setText(s);

    }

    @Override
    public void onResults(Bundle bundle) {

    }

    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onFinished() {

    }
}