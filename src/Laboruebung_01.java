import java.net.InetAddress;
import java.net.UnknownHostException;


public class Laboruebung_01 {
    
     
    public static void main(String[] args) throws UnknownHostException  {      
        
                 
        try {
            System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {            
            e.printStackTrace();
        }
    
    }
}
