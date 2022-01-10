import java.io.*;

public class es_1x7 {
    public static boolean scan(String s)
    {
    int state = 0;
    int i = 0;
    while (state >= 0 && i < s.length()) {
    final char ch = s.charAt(i++);
    switch (state) {
    case 0:
    if (ch=='E')
    state = 1;
    else 
    state=6;
    break;
    case 1:
    if(ch=='l')
    state=2;
    else 
    state=6;
    break;
    case 2:
    if(ch>='i')
    state=3;
    else 
    state=6;
    break;
    case 3:
    if(ch>='s')
    state=3;
    else 
    state=6;
    break;
    case 4:
    if(ch>='a')
    state=5;
    else 
    state=6;
    break;

    case 5:
    if(ch>='0'&& ch<='9')
    state=5;
    break;
    case 6:
    if(ch>='0'&& ch<='9')
    state=5;
    else if(ch=='-')
    state=7;
    break;
    case 7:
    if(ch>='0'&& ch<='9')
    state=5;
    break;
    }
}
return state==        
    
}
