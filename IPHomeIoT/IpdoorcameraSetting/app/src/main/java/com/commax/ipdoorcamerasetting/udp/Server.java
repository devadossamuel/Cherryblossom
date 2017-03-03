package com.commax.ipdoorcamerasetting.udp;

import android.util.Log;

import com.koushikdutta.async.AsyncDatagramSocket;
import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.callback.DataCallback;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * UDP 서버
 * Created by reweber on 12/20/14.
 */
public class Server {

    private static final String LOG_TAG = Server.class.getSimpleName();
    private final UDPReceiveDataListener listener;
    private InetSocketAddress host;
    private AsyncDatagramSocket asyncDatagramSocket;


    public Server(String host, int port, UDPReceiveDataListener listener) {
        this.host = new InetSocketAddress(host, port);
        this.listener = listener;
        setup();
    }

    /**
     * 초기화
     */
    private void setup() {

        try {
            asyncDatagramSocket = AsyncServer.getDefault().openDatagram(host, true);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        asyncDatagramSocket.setDataCallback(new DataCallback() {
            @Override
            public void onDataAvailable(DataEmitter emitter, ByteBufferList bb) {


                //bb.getAllByteArray() 한번 읽으면 다시 못읽음
                listener.onReceive(new String(bb.getAllByteArray()));
            }
        });

        asyncDatagramSocket.setClosedCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Server] Successfully closed connection");
            }
        });

        asyncDatagramSocket.setEndCallback(new CompletedCallback() {
            @Override
            public void onCompleted(Exception ex) {
                if (ex != null) throw new RuntimeException(ex);
                System.out.println("[Server] Successfully end connection");
            }
        });
    }


    /**
     * 브로드캐스트 전송
     *
     * @param msg
     */
    public void send(String msg) {
        asyncDatagramSocket.send(host, ByteBuffer.wrap(msg.getBytes()));
    }

    /**
     * 닫기
     */
    public void close() {



        //disconnect가 아니고 close를 사용해야 정상적으로 종료되는 듯
        asyncDatagramSocket.close();
    }
}
