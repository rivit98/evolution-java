package agh.po.ewolucja;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//simple singleton class

enum HarnessState{
    UNINITIALIZED,
    INITIALIZED
}

public class Harness {
    private static Harness INSTANCE;
    private static NetPacketEncoder encoder = new NetPacketEncoder();
    private static HarnessState state = HarnessState.UNINITIALIZED;

    private Socket clientSocket;
    private PrintWriter out;
    private boolean allowSending;

    private Harness() {
    }

    public void stopSending(){
        allowSending = false;
    }

    public void startSending(){
        allowSending = true;
    }

    public static Harness getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Harness();
            try{
                INSTANCE.startConnection("127.0.0.1", 13371); //yeeeee hardcoding stuffff
                state = HarnessState.INITIALIZED;
            }catch (IOException e){
                //do nothing
                state = HarnessState.UNINITIALIZED;
            }
        }

        return INSTANCE;
    }

    public void retryConnection(){
        if(state == HarnessState.INITIALIZED){
            return;
        }
        try{
            INSTANCE.startConnection("127.0.0.1", 13371);
            state = HarnessState.INITIALIZED;
        }catch (IOException e){
            state = HarnessState.UNINITIALIZED;
        }
    }

    private void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        allowSending = true;
    }

    public void sendMessage(NetOptions option, Object o) {
        if(!allowSending || state == HarnessState.UNINITIALIZED){
            return;
        }

        String cmd = encoder.encode(option, o);
        out.println(cmd);
    }

    public void stopConnection() throws IOException {
        if(state == HarnessState.UNINITIALIZED){
            return;
        }
        out.close();
        clientSocket.close();
    }
}



