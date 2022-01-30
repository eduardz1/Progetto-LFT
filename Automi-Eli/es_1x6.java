import java.io.*;

import javax.lang.model.util.ElementScanner14;

public class es_1x6 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch >= '0' && ch <= '9')
                        state = 1;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '1' || ch == '3' || ch == '5' || ch == 7 || ch == '9')
                        state = 2;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 3;
                    else
                        state = -1;
                    break;
                case 2:
                    if (ch == '1' || ch == '3' || ch == '5' || ch == 7 || ch == '9')
                        state = 3;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 2;
                    else if (ch >= 'a' && ch <= 'k' || ch >= 'A' && ch <= 'K')
                        state = 4;
                    else if (ch >= 'l' && ch <= 'z' || ch >= 'L' && ch <= 'Z')
                        state = 5;
                    else
                        state = -1;
                    break;
                case 3:
                    if (ch == '1' || ch == '3' || ch == '5' || ch == 7 || ch == '9')
                        state = 3;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 2;
                    else if (ch >= 'a' && ch <= 'k' || ch >= 'A' && ch <= 'K')
                        state = 6;
                    else if (ch >= 'l' && ch <= 'z' || ch >= 'L' && ch <= 'Z')
                        state = 7;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 4 || state == 6;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}
