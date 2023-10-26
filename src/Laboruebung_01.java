import java.net.InetAddress;

public class Laboruebung_01 {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
}
