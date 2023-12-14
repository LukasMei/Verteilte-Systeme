package Laboruebung_04;

public class Tester_04 {

  // for group notify testing

  private static String userName1 = "";

  private static String userName2 = "";

  public static void main(String[] args) {

     userName1 = args[0];

     String msg1 = args[1];    
     

     SimpleClient client1 = new SimpleClient(userName1, msg1);

     System.out.println("Name: " + userName1 + "\nNachricht: " + msg1 );

     client1.start();

     // sleep for 2 s     

     // sleep for 1 s

     client1.stop(); // forces group leave process to begin

  }

}



