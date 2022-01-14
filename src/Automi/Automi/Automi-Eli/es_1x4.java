import java.io.*;

public class es_1x4 {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch == '1' || ch == '3' || ch == '5' || ch == 7 || ch == '9')
                        state = 1;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 2;
                    else
                        state = -1;
                    break;
                case 1:
                    if (ch == '1' || ch == '3' || ch == '5' || ch == 7 || ch == '9')
                        state = 1;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 2;
                    else if (ch >= 'l' && ch <= 'L' || ch >= 'A' && ch >= 'Z')
                        state = 4;
                    else if (ch == '\n')
                        state = 3;
                    break;
                case 2:
                    if (ch == '1' || ch == '3' || ch == '5' || ch == 7 || ch == '9')
                        state = 1;
                    else if (ch == '0' || ch == '2' || ch == '4' || ch == '6' || ch == '8')
                        state = 2;
                    else if (ch >= 'a' && ch <= 'k')
                        state = 4;
                    else if (ch == '\n')
                        state = 5;
                    break;
                case 3:
                    if (ch == '\n')
                        state = 1;
                    else if (ch >= 'l' && ch <= 'z')
                        state = 4;
                    else if (ch >= '0' && ch <= '9')
                        state = -1;
                    break;
                case 4:
                    if (ch > 'a' && ch <= 'z')
                        state = 4;
                    else if (ch >= 'a' && ch <= 'k')
                        state = 5;
                    else if (ch == '\n')
                        state = 6;
                    break;
                case 5:
                    if (ch == '\n')
                        state = 5;
                case 6:
                    if (ch == '\n')
                        state = 4;
                    else if (ch == '\n')
                        state = 6;
                    break;

            }
        }
        return state == 6;
    }

    public static void main(String[] args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}