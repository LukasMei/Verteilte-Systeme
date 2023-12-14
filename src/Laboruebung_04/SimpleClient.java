package Laboruebung_04;

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
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

// code line number: 280
public class SimpleClient implements Runnable {

	private Thread thread = null;
	private String groupName = "?";
	private String userName;
	private String message;
	private boolean running;

	private static final int BUF_SIZE = 264;

	private static final String DSLP_EOL = "\r\n";
	private static final String DSLP_HEADER = "dslp-4.0";
	private static final String DSLP_GROUP_JOIN = "group join";
	private static final String DSLP_GROUP_JOIN_NOTIFY = "group notify";
	private static final String DSLP_GROUP_LEAVE = "group leave";
	private static final String DSLP_GROUP_NAME = "Freitag-Teams";
	private static final String DSLP_GROUP_JOIN_ACK = "group join ack";
	private static final String DSLP_BODY = "dslp-body";

	String writeMessage = DSLP_HEADER + DSLP_GROUP_JOIN + DSLP_EOL + DSLP_GROUP_NAME + DSLP_EOL + DSLP_BODY;

	public SimpleClient(String name, String message) {
		this.thread = new Thread(this, name);
		this.userName = name;
		this.message = message;
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
			InetSocketAddress address = new InetSocketAddress(host, port);
			sc.connect(address);
			System.out.println("Verbindung erfolgreich");
			running = true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sc != null) {
			while (running) {
				try {
					write(sc, writeMessage);
					String response = read(sc);
					String[] split_line_break = response.split("\r}n");
					for (String zeile : split_line_break) {
						System.out.println(zeile);
					}
					sc.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	/*
	 * Initialisierung eines ByteBuffer Objektes. Der Socketchannel liest den Buffer
	 * ein.
	 * Umwandlung der Nachricht in einen String.
	 */
	private String read(SocketChannel sc) throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);

		sc.read(buffer);
		String answer = new String(buffer.array(), StandardCharsets.UTF_8);

		return answer;
	}


	/* Wir übergebn die Nachricht an ein byte Array.
	 * Initialisieren einen neuen ByteBuffer mit der gräße Buff_Size.
	 * Das byte Array wird vom Byte Buffer gewrappt und dann vom socketchanlle geschrieben.
	 * 
	 */
	private void write(SocketChannel sc, String msg) throws IOException {
		byte[] data = msg.getBytes(StandardCharsets.UTF_8);

		ByteBuffer buffer = ByteBuffer.allocate(BUF_SIZE);
		buffer = ByteBuffer.wrap(data);

		sc.write(buffer);
	}

}
