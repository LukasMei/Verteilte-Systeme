package Laboruebung_05;

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
public class SimpleClient2 implements Runnable {

    private Thread thread = null;
	private String userName;
	private String receiver;
	private String message = "Na Echo-Eve, wie gehts meiner geliebten ?";
	private boolean running;
    private static final int BUF_SIZE = 264;
	private static final String DSLP_EOL = "\r\n";
	private static final String DSLP_HEADER = "dslp-4.0/";
	private static final String DSLP_USER_JOIN = "user join";
	private static final String DSLP_USER_JOIN_ACK = "user join ack";
	private static final String DSLP_USER_TEXT_NOTIFY = "user text notify";
	private static final String DSLP_USER_LEAVE = "user leave";
	private static final String DSLP_USER_LEAVE_ACK = "user leave ack";
	private static final String DSLP_BODY = "dslp-body";
	private boolean gesendet = false;

    // <receiver> is the user name you send your message to, e.g. "Echo-Eve"
	public SimpleClient2(String receiver) {
		this.thread = new Thread(this);
		// ...
		this.receiver = receiver;
		this.userName = "Verteilter-Student";
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
			System.out.println(adresse);
			System.out.println(" Verbindung geschafft");
			running = true;
			write(sc, (DSLP_HEADER + DSLP_USER_JOIN + DSLP_EOL + this.userName + DSLP_EOL + DSLP_BODY + DSLP_EOL));
        }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
		if(sc != null) {
			while(running) {
				try {
					String resp = read(sc);
					if(resp != null){
					String[] splitArray = resp.split("\r\n");           //Trennung der Zeilen mit \r\n
						if(splitArray[0].equals(DSLP_HEADER + "/user join ack")){ //Prüfung ob ack gesendet wurde
							for(String zeile : splitArray){
									System.out.println(zeile);
									
								}
								System.out.println("ACK empfangen !");
								running = false;
								break;
					}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        // write(..)
		        // ...
			}
		}
		
		//zweite Nachricht
		try {
			sc.configureBlocking(false);
			running = true;
			write(sc, (DSLP_HEADER + DSLP_USER_TEXT_NOTIFY + DSLP_EOL + this.userName + DSLP_EOL + this.receiver + DSLP_EOL + "1" + DSLP_EOL + DSLP_BODY + DSLP_EOL + this.message + DSLP_EOL));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(sc != null) {
			while(running) {
		        try {
                    String resp = read(sc);
					if(resp.length() < 2){
						Thread.sleep(5000);
						resp = read(sc);
					}
					String[] splitArray = resp.split("\r\n");           //Trennung der Zeilen mit \r\n
					for(String zeile : splitArray){
						System.out.println(zeile);
					}
					running = false;
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        // write(..)
		        // ...
 				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		//leave Nachricht
		try {
			running = true;
			write(sc, (DSLP_HEADER + DSLP_USER_LEAVE + DSLP_EOL + this.userName + DSLP_EOL + DSLP_BODY + DSLP_EOL));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(sc != null) {
			while(running) {
		        try {
                    String resp = read(sc);
					String[] splitArray = resp.split("\r\n");           //Trennung der Zeilen mit \r\n
					for(String zeile : splitArray){
						System.out.println(zeile);
					}
					running = false;
					sc.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        // write(..)
		        // ...
			}
		}

	}

	private String read(SocketChannel sc) throws IOException {
	    ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);				//neuer Buffer
		sc.read(buf);												//socketchannel liest in den buffer rein
		
		String answer  = new String(buf.array(), StandardCharsets.UTF_8);   //Umwandlung in String mit UTF_8
		return answer;

	}

	private void write(SocketChannel sc, String msg) throws IOException {
		byte[] dataBytes = msg.getBytes(StandardCharsets.UTF_8);               //Nachricht wird als Byte Array übergeben und gleichzeitig in UTF8 umgewandelt 

		ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);							//neuer Buffer mit größe BUF_SIZE
		buf = ByteBuffer.wrap(dataBytes);   									//byte array wird gewrappt und dem Buffer zugewiesen
		sc.write(buf);															// socketchannel schreibt buffer
	}
	

}
