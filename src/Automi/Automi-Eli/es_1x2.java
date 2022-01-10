import java.io.*;


public class es_1x2
{
    public static boolean scan(String s){
    int state = 0;
    int i = 0;
    while (state >= 0 && i < s.length()) {
    final char ch = s.charAt(i++);
    switch (state) {
    case 0:
    if (ch >= 'a'&& ch>='z'||ch>='A'&&ch>='Z')
    state = 1;
    else if (ch >= '0'&& ch<='9')
    state = -1;
    break;
    case 1:
    if (ch >= 'a'&& ch>='z')
    state = 1;
    else if (ch >= '0'&& ch<='9')
    state = 1;
   else 
   state=-1;
    break;
    }
    }
    return state==1;   
    }


public static void main(String[] args)
{
System.out.println(scan(args[0]) ? "OK" : "NOPE");
}
}
    


