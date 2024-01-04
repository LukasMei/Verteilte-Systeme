package Laboruebung_05;

public class Tester_05 {
    // for group notify testing

  private static String userName1;
  

  public static void main(String[] args) {

     userName1 = args[0];

     //String msg1 = args[1];    
     

     SimpleClient2 client1 = new SimpleClient2(userName1);
     

     client1.start();

     // sleep for 2 s     

     // sleep for 1 s

     //client1.stop(); // forces group leave process to begin

  }
}
