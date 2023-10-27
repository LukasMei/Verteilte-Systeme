import java.net.InetAddress;


public class Laboruebung_01 {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String local = InetAddress.getLocalHost().toString();
        String[] arrLocal = local.split("/");
        System.out.println(arrLocal[0]);
        System.out.println(arrLocal[1]); 
          
            
      
        
            
        
    }
}
