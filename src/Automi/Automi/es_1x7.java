package Automi.Automi;

/*
DFA che riconosca il linguaggio di stringhe che contengono il mio nome e tutte le stringhe
ottenute dopo la sostituzione di un carattere del nome con uno qualsiasi  
*/

public class es_1x7 {
    public static void main(String[] args) {
        System.out.println(scan7("Eduard")    == true);
        System.out.println(scan7("eduard")    == true);
        System.out.println(scan7("EDUARD")    == true);
        System.out.println(scan7("Edward")    == true);
        System.out.println(scan7("Eduardo")   == true);
        System.out.println(scan7("Ed*ard")    == true);
        System.out.println(scan7("Aduard")    == true);
        System.out.println(scan7("Eduarr")    == true);
        System.out.println(scan7("&duard")    == true);
        System.out.println(scan7("%%uard")    == false);
        System.out.println(scan7("nonEduard") == false);
        System.out.println(scan7("marco")     == false);
        System.out.println(scan7("Edurrr")    == false);
        System.out.println(scan7("(___)")     == false);
    }

    public static boolean scan7(String s) {

    }
}
