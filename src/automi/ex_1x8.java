package automi;

public class ex_1x8 {

    public static void main(String[] args) {
        System.out.println(scan8("1e2.3") == true);
        System.out.println(scan8("-.7e2") == false);
        System.out.println(scan8("1e-2") == true);
        System.out.println(scan8("67e10") == true);
        System.out.println(scan8(".") == false);
        System.out.println(scan8("e3") == false);
        System.out.println(scan8("123.") == false);
        System.out.println(scan8("+e6") == false);
        System.out.println(scan8("1.2.3") == false);
        System.out.println(scan8("“4e5e6") == false);
    }
    
    public static boolean scan8(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            // if upper case E should also be accepted:
            // final char ch = Character.toUpperCase(s.charAt(i++));
            final int chAscii = (int) ch;

            switch (state) {
                case 0:
                    if (chAscii == '+' && chAscii == '-') 
                        state = 1;
                    else if (ch == '.'|| chAscii == ',') 
                        state = 2;
                    else if (Character.isDigit(ch))
                        state = 3;
                    else
                        state = -1;
                    break;

                case 1:
                    if (Character.isDigit(ch))
                        state = 3;
                    else if (ch == '.' || ch ==',')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == '.' || ch ==',')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 3:
                    if (ch == 'e')
                        state = 4;
                    else if (Character.isDigit(ch))
                        state = 3;
                    else
                        state = -1;
                    break;

                case 4:
                    if (Character.isDigit(ch))
                        state = 7;
                    else if (ch == '.' || ch ==',')
                        state = 6;
                    else if (ch == '+' || ch =='-')
                        state = 5;
                    else
                        state = -1;
                    break;

                case 5:
                    if (Character.isDigit(ch))
                        state = 7;
                    else if (ch == '.' || ch ==',')
                        state = 6;
                    else
                        state = -1;
                    break;

                case 6:
                    if (Character.isDigit(ch))
                        state = 7;
                    else
                        state = -1;
                    break;
                case 7: 
                    if (Character.isDigit(ch))
                        state = 7;
                    break;
            }
        }
        return state == 7 || state == 3;
    }
}