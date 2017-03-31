package com.ghostwriter.ghostwriter;

import android.content.Intent;
import android.os.Bundle;

import net.daum.mf.speech.api.SpeechRecognizerActivity;

import java.util.ArrayList;
import java.util.List;

public class VoiceRecoActivity extends SpeechRecognizerActivity {
    public static String EXTRA_KEY_RESULT_ARRAY = "result_array"; // 결과값 목록. ArrayList<String>
    public static String EXTRA_KEY_MARKED = "marked"; // 첫번째 값의 신뢰도가 현저하게 높은 경우 true. 아니면 false. Boolean
    public static String EXTRA_KEY_ERROR_CODE = "error_code"; // 에러가 발생했을 때 코드값. 코드값은 SpeechRecognizerClient를 참조. Integer
    public static String EXTRA_KEY_ERROR_MESSAGE = "error_msg"; // 에러 메시지. String

    protected void putStringFromId(RES_STRINGS key, int id) {
        putString(key, getResources().getString(id));
    }

    // 이 함수를 꼭 상속받아서 구현해야 한다.
    // 내부에서 사용하는 리소스 id를 모두 등록해준다.
    // 일부 표시하지 않을 view의 경우에는 layout에서 제거하지 말고 visibility를 hidden으로 설정하는 것을 권장한다.
    @Override
    protected void onResourcesWillInitialize() {
        // strings


        // view id
        // main


        // top


        // suggest list

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // activity가 표시될 때의 transition 효과 설정

        // 물방울 애니메이션에 사용되는 drawable의 id 목록


    }

    @Override
    public void finish() {
        super.finish();

        // activity가 사라질 때 transition 효과 지정

    }

    @Override
    protected void onRecognitionSuccess(List<String> result, boolean marked) {
        // result는 선택된 결과 목록이 담겨있다.
        // 첫번째 값의 신뢰도가 낮아 후보 단어를 선택하는 과정을 거쳤을 경우에는 그 때 선택된 값이 가장 처음으로 오게 된다.
        // 첫번째 값의 신뢰도가 현저하게 높았거나, 이용자가 선택을 했을 경우에는 marked 값은 true가 된다. 이 이외에는 false가 된다.
        Intent intent = new Intent().
                putStringArrayListExtra(EXTRA_KEY_RESULT_ARRAY, new ArrayList<String>(result)).
                putExtra(EXTRA_KEY_MARKED, marked);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onRecognitionFailed(int errorCode, String errorMsg) {
        Intent intent = new Intent().
                putExtra(EXTRA_KEY_ERROR_CODE, errorCode).
                putExtra(EXTRA_KEY_ERROR_MESSAGE, errorMsg);

        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
