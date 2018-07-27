package org.tangaya.quranasrclient.data;

import android.os.AsyncTask;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class RecognitionTask extends WebSocketAdapter {

    public static String ENDPOINT;

    private WebSocket webSocket;
    private Attempt attempt;
    private Evaluation eval;

    private RecognitionAsyncTask recognitionAsyncTask;

    public RecognitionTask(Attempt attempt) {
        this.attempt = attempt;

        try {
            webSocket = new WebSocketFactory().createSocket(ENDPOINT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        webSocket.addListener(this);
        recognitionAsyncTask = new RecognitionAsyncTask();
    }

    public void execute() {
        webSocket.connectAsynchronously();
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);

        eval = recognitionAsyncTask.doInBackground();

        Timber.d("onConnected()");
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

        Timber.d("onDisconnected()");
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        super.onConnectError(websocket, exception);

        Timber.d("onConnectError()");
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);

        RecognitionResponse response = new RecognitionResponse(text);

        Timber.d("onTextMessage(); message: " + text);
    }

    private class RecognitionAsyncTask extends AsyncTask<Void, Void, Evaluation> {

        @Override
        protected Evaluation doInBackground(Void... voids) {

            Evaluation evaluation = new Evaluation(attempt.getChapterNum(),
                    attempt.getVerseNum(), 123);

            evaluation.setFilepath(attempt.getAudioFilePath());

            File file = new File(attempt.getAudioFilePath());
            int size = (int) file.length();
            byte[] bytes = new byte[size];

            Timber.d("Recognizing (asynctask)" + attempt.getAudioFilePath());
            Timber.d("endpoint:" + ENDPOINT);
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Timber.d( "sending binary. size: " + bytes.length);
            webSocket.sendBinary(bytes);

            return evaluation;
        }

        @Override
        protected void onPostExecute(Evaluation eval) {
            Timber.d("task execution finish");
        }
    }
}
