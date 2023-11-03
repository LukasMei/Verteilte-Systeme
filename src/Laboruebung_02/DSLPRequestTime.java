package Laboruebung_02;
import java.io.IOException;
import java.nio.channels.SocketChannel;


 // Die nachfolgenden Klassen wurden bereits importiert:
 import java.io.IOException;
 import java.net.InetSocketAddress;
 import java.nio.ByteBuffer;
 import java.nio.channels.SocketChannel;
 import java.nio.charset.StandardCharsets;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.Locale;
 import java.util.TimeZone;
  
 // Weitere Importe sind nicht notwendig bzw. nicht zulässig!
  


public class DSLPRequestTime {

	private SocketChannel sc = null;

	//private static final int BUF_SIZE = ?

	//private static final String DSLP_EOL = ?
	//private static final String DSLP_HEADER = ?
	//private static final String DSLP_REQUEST_HEADER = ?
	//private static final String DSLP_RESPONSE_HEADER = ?
	//private static final String DSLP_BODY = ?

    // Diese Zeile entspricht Nummer 47 der Compiler-Ausgabe

	/*
     * This method connects to the given host and port.
     * A successful connection is reported on the console (see format!)
     */

	public void connect(String host, int port) throws IOException {

		sc = SocketChannel.open();
		InetSocketAddress adresse = new InetSocketAddress(host,port);
		sc.connect(adresse);

		        
		System.out.println("Verbunden mit " + adresse);
	}

	/**
	 * This method uses the connection from above to send a DSLP request time.
	 * There's no output to this method. See DSLP specs for details.
	 */
	public void requestTime() throws IOException {
		// ...
		// ...
		// ...
		// ...
	}

	/**
     * This method receives the DSLP response time. If the DSLP request from
     * above was correct the DSLP server will answer accordingly.
     * There are three lines of output to this method.
     */
	public void responseTime() throws IOException {
		// ...
        // ...
		// ...
        // ...
		System.out.println("Datum auf dem Server: " + ": XX.XX.XXXX");
		System.out.println("Zeit auf dem Server (UTC): " + "XX:XX");
		System.out.println("Zeit auf dem Server (CEST): " + "");
	    sc.close(); // close the connection
	}
}
