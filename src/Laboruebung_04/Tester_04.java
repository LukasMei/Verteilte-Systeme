package Laboruebung_04;

public class Tester_04 {

  // for group notify testing

  private static String userName1 = "Heinz";
  private static String msg1 = "Hallo";


  public static void main(String[] args) {

     /* userName1 = args[0];

     String msg1 = args[1]; */    
     

     LeonsClient client1 = new LeonsClient(userName1, msg1);

     

     client1.start();

     // sleep for 2 s     

     // sleep for 1 s

     client1.stop(); // forces group leave process to begin

  }

}



