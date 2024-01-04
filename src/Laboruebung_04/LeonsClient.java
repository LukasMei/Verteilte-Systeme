package Laboruebung_04;


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

public class LeonsClient implements Runnable {

	private Thread thread = null;
	private static final String DSLP_HEADER = "dslp-4.0/";
	private static final String DSLP_BODY = "dslp-body";
	private static final String DSLP_EOL = "\r\n";
	private String groupName = "Freitag-Teams";
	private String userName;
	private String message;
	private boolean running;

	public LeonsClient(String name, String message) {
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
		boolean isMessageWritten = false;
		String host = "dslp.ris.bht-berlin.de";
		int port = 31000;
		SocketChannel sc = null;
		try {
			sc = SocketChannel.open(new InetSocketAddress(host, port));

			// group join
			String group_join_string = DSLP_HEADER + "group join" + DSLP_EOL + groupName + DSLP_EOL + DSLP_BODY
					+ DSLP_EOL;
			byte[] group_join_raw = group_join_string.getBytes(StandardCharsets.UTF_8);
			sc.write(ByteBuffer.wrap(group_join_raw));

			// group join ack
			ByteBuffer byteBuffer = ByteBuffer.allocate(50);
			sc.read(byteBuffer);
			String group_join_ack = new String(byteBuffer.array(), StandardCharsets.UTF_8);
			String group_join_ack_header = group_join_ack.split(DSLP_EOL)[0];

			if (!group_join_ack_header.equals(DSLP_HEADER + "group join ack"))
				System.out.println("Die Verbindung konnte nicht hergestellt werden");

			sc.configureBlocking(false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (sc != null) {
			try {
				while (running) {
					// group notifys von Server zu Client
					String resp = read(sc);
					if (resp != null && resp.split(DSLP_EOL)[0].equals(DSLP_HEADER + "group notify")) {
						printMessage(resp);
					}
					// group notify von Client zu server
					if (!isMessageWritten) {
						write(sc, message);
						isMessageWritten = true;
					}
				}
				// group leave
				String group_leave_string = DSLP_HEADER + "group leave" + DSLP_EOL + groupName + DSLP_EOL + DSLP_BODY + DSLP_EOL;
				byte[] group_leave_raw = group_leave_string.getBytes(StandardCharsets.UTF_8);
				sc.write(ByteBuffer.wrap(group_leave_raw));

				// group leave ack
				var leave_ack_received = false;
				while (!leave_ack_received)
				{
					 String resp = read(sc);
					 if (resp != null && resp.split(DSLP_EOL)[0].equals(DSLP_HEADER + "group leave ack")) 
					 {
						leave_ack_received = true;
					 }
				}
		
				sc.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private String read(SocketChannel sc) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		int byteCount = sc.read(byteBuffer);
		if (byteCount == 0)
			return null;
		return new String(byteBuffer.array(), StandardCharsets.UTF_8);
	}

	private void write(SocketChannel sc, String message) throws IOException {
		String group_notify_string = DSLP_HEADER + "group notify" + DSLP_EOL + userName + DSLP_EOL + groupName
				+ DSLP_EOL + message.split(DSLP_EOL).length + DSLP_EOL + DSLP_BODY + DSLP_EOL + message + DSLP_EOL;
		byte[] group_notify_raw = group_notify_string.getBytes(StandardCharsets.UTF_8);
		sc.write(ByteBuffer.wrap(group_notify_raw));
	}

	private void printMessage(String message) {
		String[] group_notify_array = message.split(DSLP_EOL);
		String group_notify_team = group_notify_array[2];
		int len = Integer.parseInt(group_notify_array[3]);
		// Nachricht mit einer Zeile
		if (len == 1)
			System.out.println("(" + group_notify_team + ")" + " " + group_notify_array[5]);
		else {
			// Nachricht mit mehreren Zeilen
			String response = "(" + group_notify_team + ")" + " ";
			for (int i = 5; i < 5 + len; i++) {
				response += group_notify_array[i] + "\n";
			}
			System.out.println(response);
		}
	}

}
