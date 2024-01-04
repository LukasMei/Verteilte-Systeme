package Laboruebung_05;

/*
    IMPORTANT NOTE: The following imports have already been made for you.
                    Do not import them twice!
                    Additional imports are not necessary and NOT allowed.
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// code line number: 393
public class SimpleClient implements Runnable {

    private Thread thread = null;
    private boolean running = false;
    private String userName;

    private static final int BUF_SIZE = 264;
    private static final String DSLP_EOL = "\r\n";
    private static final String DSLP_HEADER = "dslp-4.0/";
    private static final String DSLP_USER_JOIN = "user join";
    private static final String DSLP_USER_JOIN_ACK = "user join ack";
    private static final String DSLP_USER_NOTIFY = "user text notify";
    private static final String DSLP_USER_LEAVE = "user leave";
    private static final String DSLP_USER_LEAVE_ACK = "user leave ack";
    private static final String DSLP_BODY = "dslp-body";

    // <receiver> is the user name you send your message to, e.g. "Echo-Eve"
    public SimpleClient(String receiver) {
        this.thread = new Thread(this);
        this.userName = receiver;

    }

    public void start() {
        running = true;
        thread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        String host = "dslp.ris.bht-berlin.de";
        int port = 31000;
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            InetSocketAddress adresse = new InetSocketAddress(host, port);
            sc.connect(adresse);
            write(sc, (DSLP_HEADER + DSLP_USER_JOIN + DSLP_EOL + userName + DSLP_EOL + DSLP_BODY + DSLP_EOL));
            ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
            sc.read(buffer);
            
            
            sc.configureBlocking(false);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sc != null) {
            while (running) {
                try {
                    String response = read(sc);
                    String[] splitArray = response.split("\r\n"); // Trennung der Zeilen mit \r\n
                    for (String zeile : splitArray) {
                        System.out.println(zeile);
                    }

                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void write(SocketChannel sc, String msg) throws IOException {
        byte[] dataBytes = msg.getBytes(StandardCharsets.UTF_8); // Nachricht wird als Byte Array übergeben und
                                                                 // gleichzeitig in UTF8 umgewandelt

        ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE); // neuer Buffer mit größe BUF_SIZE
        buf = ByteBuffer.wrap(dataBytes); // byte array wird gewrappt und dem Buffer zugewiesen
        sc.write(buf);
    }

    private String read(SocketChannel sc) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE); // neuer Buffer
        sc.read(buf); // socketchannel liest in den buffer rein

        String answer = new String(buf.array(), StandardCharsets.UTF_8); // Umwandlung in String mit UTF_8
        return answer;
    }

}
