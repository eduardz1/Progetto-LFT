package automi;

public class ex_1x10 {
    public static void main(String[] args) {
        System.out.println(scan10("aaa/****/aa") == true);
        System.out.println(scan10("aa/*a*a*/") == true);
        System.out.println(scan10("aaaa") == true);
        System.out.println(scan10("/****/") == true);
        System.out.println(scan10("/*aa*/") == true);
        System.out.println(scan10("*/a") == true);
        System.out.println(scan10("a/**/***a") == true);
        System.out.println(scan10("a/**/***/a") == true);
        System.out.println(scan10("a/**/aa/***/a") == true);
        System.out.println(scan10("aaa/*/aa") == false);
        System.out.println(scan10("a/**//***a") == false);
        System.out.println(scan10("aa/*aa") == false);     
    }

    public static boolean scan10(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            final int chAscii = (int) ch;

            switch (state) {
                case 0:
                    if (chAscii == '/')
                        state = 1;
                    else if (chAscii == 'a' || chAscii == '*')
                        state = 4;
                    else
                        state = -1;
                    break;

                case 1:
                    if (chAscii == '*')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if (chAscii == 'a' || chAscii == '/')
                        state = 2;
                    else if (chAscii == '*')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 3:
                    if (chAscii == '/')
                        state = 4;
                    else if (chAscii == 'a')
                        state = 2;
                    else if (chAscii == '*')
                        state = 3;
                    else
                        state = -1;
                    break;

                case 4:
                    if (chAscii == 'a' || chAscii == '*')
                        state = 4;
                    else if (chAscii == '/')
                        state = 5;
                    else
                        state = -1;
                    break;

                case 5:
                    if (chAscii == '/' || chAscii == 'a')
                        state = 4;
                    else if (chAscii == '*')
                        state = 2;
                    else
                        state = -1;
            }
        }

        return state == 4;
    }
}
