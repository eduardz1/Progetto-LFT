package automi;

public class ex_1x9 {
    public static void main(String[] args) {
        System.out.println(scan9("/****/") == true);
        System.out.println(scan9("/*a*a*/") == true);
        System.out.println(scan9("/*a/**/") == true);
        System.out.println(scan9("/**a///a/a**/") == true);
        System.out.println(scan9("/**/") == true);
        System.out.println(scan9("/*/*/") == true);
        System.out.println(scan9("/*/") == false);
        System.out.println(scan9("/**/***/") == false);
        System.out.println(scan9("Bianchi12346B") == false);
    }

    public static boolean scan9(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            final int chAscii = (int) ch;

            switch (state) {
                case 0:
                    if (chAscii == '/')
                        state = 1;
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
                    state = -1;
                    break;
            }
        }

        return state == 4;
    }
}
