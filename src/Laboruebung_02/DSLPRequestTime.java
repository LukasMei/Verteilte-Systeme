// Die nachfolgenden Klassen wurden bereits importiert:
package Laboruebung_02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
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

	private static final int BUF_SIZE = 264;

	private static final String DSLP_EOL = "\r\n";
	private static final String DSLP_HEADER = "dslp-4.0";
	private static final String DSLP_REQUEST_HEADER = "dslp-4.0/request time";
	private static final String DSLP_RESPONSE_HEADER = "dslp-4.0/response time";
	private static final String DSLP_BODY = "dslp-body";

	// Diese Zeile entspricht Nummer 47 der Compiler-Ausgabe
	/**
	 * This method connects to the given host and port.
	 * A successful connection is reported on the console (see format!)
	 */
	public void connect(String host, int port) throws IOException {
		sc = SocketChannel.open();
		InetSocketAddress adresse = new InetSocketAddress(host, port);
		sc.connect(adresse);
		// ...
		System.out.println("Verbunden mit " + adresse);
	}

	/**
	 * This method uses the connection from above to send a DSLP request time.
	 * There's no output to this method. See DSLP specs for details.
	 */
	public void requestTime() throws IOException {
		String newData = DSLP_REQUEST_HEADER + DSLP_EOL + DSLP_BODY + DSLP_EOL;    //Nachricht die übergeben wird
		byte[] dataBytes = newData.getBytes(StandardCharsets.UTF_8);               //Nachricht wird als Byte Array übergeben und gleichzeitig in UTF8 umgewandelt 

		ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);							//neuer Buffer mit größe BUF_SIZE
		buf = ByteBuffer.wrap(dataBytes);   									//byte array wird gewrappt und dem Buffer zugewiesen
		sc.write(buf);															// socketchannel schreibt buffer
	}

	/**
	 * This method receives the DSLP response time. If the DSLP request from
	 * above was correct the DSLP server will answer accordingly.
	 * There are three lines of output to this method.
	 */
	public void responseTime() throws IOException {
		
		ByteBuffer buf = ByteBuffer.allocate(BUF_SIZE);				//neuer Buffer
		sc.read(buf);												//socketchannel liest in den buffer rein
		
		String string  = new String(buf.array(), StandardCharsets.UTF_8);   //

		String[] splitArray = string.split("\r\n");           //Trennung der Zeilen mit \r\n
		String[] datum_zeit = splitArray[2].split(" ");       // Trennung der wichtigen Zeile in Datum und Zeit mit leerzeichen

		System.out.println("Datum auf dem Server: " + datum_zeit[0]);
		System.out.println("Zeit auf dem Server (UTC): " + datum_zeit[1]);

		String[] cest = datum_zeit[1].split(":");						//Zeit in Stunde, Minuten und Sekunden splitten
		int stunde = (Integer.parseInt(cest[0]) + 2);						// Stunde auslesen für +2 um CEST zu erhalten
		cest[0] = Integer.toString(stunde);									// int Stunde wieder zu String damit ins Array zurück
		datum_zeit[1] = (cest[0] + ":" + cest[1] + ":" + cest[2]);			//

		System.out.println("Zeit auf dem Server (CEST): " + datum_zeit[1]);
		sc.close(); // close the connection
	}
}
