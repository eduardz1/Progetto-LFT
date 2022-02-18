package automi;

public class ex_1x1 {
    public static void main(String[] args) {
        System.out.println(scan1("010101") == false);
        System.out.println(scan1("1100011001") == true);
        System.out.println(scan1("10214") == false);
        System.out.println(scan1("000101") == true);
        System.out.println(scan1("0002") == false);
        System.out.println(scan1("0101000") == true);

        System.out.println(scan1m("010101") == true);
        System.out.println(scan1m("1100011001") == false);
        System.out.println(scan1m("10214") == false);
        System.out.println(scan1m("000101") == false);
        System.out.println(scan1m("0002") == false);
        System.out.println(scan1m("0101000") == false);
    }

    // DFA complementare al 1.1, alfabeto {0,1} ma riconosce solo le stringhe
    // che NON contendono 3 zeri consecutivi
    public static boolean scan1m(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '1')
                        state = 0;
                    else if (ch == '0')
                        state = 1;
                    else
                        state = -1;
                    break;

                case 1:
                    if (ch == '1')
                        state = 0;
                    else if (ch == '0')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
            }
        }
        return state != -1;
    }

    // DFA definito sull'alfabeto {0,1} che riconsoce le stringhe in cui compaiono
    // almeno 3 zeri consecutivi
    public static boolean scan1(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '0')
                        state = 1;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;

                case 1:
                    if (ch == '0')
                        state = 2;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == '0')
                        state = 3;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;
                    
                case 3:
                    if (ch == '0' || ch == '1')
                        state = 3;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 3;
    }

}
