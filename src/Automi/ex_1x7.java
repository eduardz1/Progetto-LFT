package automi;

/*
 * DFA che riconosca il linguaggio di stringhe che contengono il mio nome e tutte le stringhe
 * ottenute dopo la sostituzione di un carattere del nome con uno qualsiasi  
 */

public class ex_1x7 {
    public static void main(String[] args) {
        System.out.println(scan7("Eduard")    == true);
        System.out.println(scan7("eduard")    == true);
        System.out.println(scan7("EDUARD")    == true);
        System.out.println(scan7("Edward")    == true);
        System.out.println(scan7("Eduardo")   == false);
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
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = Character.toUpperCase(s.charAt(i++));
            final int chAscii = (int) ch;

            switch (state) {
                case 0:
                    if (chAscii == 'E')
                        state = 1;
                    else
                        state = 7;
                    break;

                case 1:
                    if (chAscii == 'D')
                        state = 2;
                    else
                        state = 8;
                    break;

                case 2:
                    if (chAscii == 'U')
                        state = 3;
                    else
                        state = 9;
                    break;

                case 3:
                    if (chAscii == 'A')
                        state = 4;
                    else
                        state = 10;
                    break;

                case 4:
                    if (chAscii == 'R')
                        state = 5;
                    else
                        state = 11;
                    break;

                case 5:
                    state = 6;
                    break;

                case 6:
                    state = -1;
                    break;

                case 7:
                    if (chAscii == 'D')
                        state = 8;
                    else
                        state = -1;
                    break;

                case 8:
                    if (chAscii == 'U')
                        state = 9;
                    else
                        state = -1;
                    break;

                case 9:
                    if (chAscii == 'A')
                        state = 10;
                    else
                        state = -1;
                    break;

                case 10:
                    if (chAscii == 'R')
                        state = 11;
                    else
                        state = -1;
                    break;

                case 11:
                    if (chAscii == 'D')
                        state = 6;
                    else
                        state = -1;
            }
        }
        
        return state == 6;
    }
}
