package Laboruebung_05;

public class Tester_05 {
    // for group notify testing

  private static String userName1;
  private static String msg1;
  

  public static void main(String[] args) throws InterruptedException {

     userName1 = args[0];

     //msg1 = args[1];    
     

     SimpleClient client = new SimpleClient(userName1);
     

     client.start();

     // sleep for 2 s     

     // sleep for 1 s

     Thread.sleep(10000);

     client.stop(); // forces group leave process to begin

  }
}
