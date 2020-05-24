package com.xwl.prisonbreak.common.util.lfasr;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.speech_to_text.v1.model.SpeechModels;
import com.ibm.watson.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.speech_to_text.v1.websocket.BaseRecognizeCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Author: xwl
 * @Date: 2019/6/5 21:13
 * @Description: IBM语言转文字
 */
public class SpeechToTextExample {

    public static void main(String[] args) throws FileNotFoundException {
        test1();
    }

    public static void test1() throws FileNotFoundException {
        Authenticator authenticator = new IamAuthenticator("oLvlIlMCKpxQB8ke1dv7vDlb4A__3-24F26fQXoN1eHD");
        SpeechToText speechToText = new SpeechToText(authenticator);
        speechToText.setServiceUrl("https://api.us-east.speech-to-text.watson.cloud.ibm.com/instances/4bcf9549-270b-4eb5-b1c3-787fa68a2905");

        File audio = new File("G:\\workspace_idea\\prisonbreak\\src\\main\\resources\\audio\\news.wav");
        RecognizeOptions options =
                new RecognizeOptions.Builder()
                        .audio(audio)
                        .contentType(HttpMediaType.AUDIO_WAV)
                        .build();
        SpeechModels speechModels = speechToText.listModels().execute().getResult();
        SpeechRecognitionResults transcript = speechToText.recognize(options).execute().getResult();

        System.out.println(speechModels);

        System.out.println(transcript);
    }

    public static void test2() {
        IamAuthenticator authenticator = new IamAuthenticator("oLvlIlMCKpxQB8ke1dv7vDlb4A__3-24F26fQXoN1eHD");
        SpeechToText speechToText = new SpeechToText(authenticator);
        speechToText.setServiceUrl("https://api.us-east.speech-to-text.watson.cloud.ibm.com/instances/4bcf9549-270b-4eb5-b1c3-787fa68a2905");

        try {
            RecognizeOptions recognizeOptions = new RecognizeOptions.Builder()
                    .audio(new FileInputStream("G:\\workspace_idea\\prisonbreak\\src\\main\\resources\\audio\\news.mp3"))
//                    .audio(new FileInputStream("D:\\Users\\XUNING\\Desktop\\audio-file.flac"))
                    .contentType("audio/mp3")
                    .model("zh-CN_BroadbandModel")
//                    .keywords(Arrays.asList("colorado", "tornado", "tornadoes"))
//                    .keywordsThreshold((float) 0.5)
//                    .maxAlternatives(3)
                    .build();

            BaseRecognizeCallback baseRecognizeCallback = new BaseRecognizeCallback() {
                @Override
                public void onTranscription(SpeechRecognitionResults speechRecognitionResults) {
                    System.out.println(speechRecognitionResults);
                }

                @Override
                public void onDisconnected() {
                    System.exit(0);
                }
            };

            speechToText.recognizeUsingWebSocket(recognizeOptions, baseRecognizeCallback);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}