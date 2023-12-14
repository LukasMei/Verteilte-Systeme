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
	private String groupName = "Freitag-Teams";
	private String userName;
	private String message;
	private boolean running;
    private static final int BUF_SIZE = 264;
	private static final String DSLP_EOL = "\r\n";
	private static final String DSLP_HEADER = "dslp-4.0/";
	private static final String DSLP_GROUP_JOIN = "group join";
	private static final String DSLP_GROUP_JOIN_ACK = "group join ack";
	private static final String DSLP_GROUP_NOTIFY = "group notify";
	private static final String DSLP_GROUP_LEAVE = "group leave";
	private static final String DSLP_GROUP_LEAVE_ACK = "group leave ack";
	private static final String DSLP_GROUP_NAME = "Freitag-Teams";
	private static final String DSLP_BODY = "dslp-body";


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
			InetSocketAddress adresse = new InetSocketAddress(host, port);
		    sc.connect(adresse);
			System.out.println(adresse);
			System.out.println(" Verbindung geschafft");
			running = true;
			write(sc, (DSLP_HEADER + DSLP_GROUP_JOIN + DSLP_EOL + DSLP_GROUP_NAME + DSLP_EOL + DSLP_BODY + DSLP_EOL));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(sc != null) {
			while(running) {
		        try {
                    String resp = read(sc);
					String[] splitArray = resp.split("\r\n");           //Trennung der Zeilen mit \r\n
					for(String zeile : splitArray){
						if(zeile.equals("dslp-4.0/group join ack") || zeile.equals("Freitag-Teams") || zeile.equals("dslp-body")){
							System.out.println(zeile);
						}else{
							System.out.println("Falsche Ausgabe vom Server !");
						}
					}
					running = false;
					break;
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
			running = true;
			write(sc, (DSLP_HEADER + DSLP_GROUP_NOTIFY + DSLP_EOL + this.userName + DSLP_EOL + DSLP_GROUP_NAME + DSLP_EOL + "1" + DSLP_EOL + DSLP_BODY + DSLP_EOL + this.message + DSLP_EOL));
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
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        // write(..)
		        // ...
			}
		}

		//dritte Nachricht
		try {
			running = true;
			write(sc, (DSLP_HEADER + DSLP_GROUP_LEAVE + DSLP_EOL + DSLP_GROUP_NAME + DSLP_EOL + DSLP_BODY + DSLP_EOL));
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