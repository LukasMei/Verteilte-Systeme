package Laboruebung_02;


public class Tester_02 {

    public static void main(String[] args) {
        
        DSLPRequestTime dslp = new DSLPRequestTime();
	try {
	    dslp.connect("dslp.ris.bht-berlin.de", 31000);
	    dslp.requestTime();
	    dslp.responseTime();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
}
