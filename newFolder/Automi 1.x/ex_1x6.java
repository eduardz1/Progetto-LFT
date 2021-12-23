/*
DFA che riconosca il linguaggio di stringhe che contengono 
un numero di matricola seguito senza spazi da un cognome 
dove la combinazione corrisponde agli studenti del turno T2 e T3

T2: cognomi tra A e K con penultima cifra pari
T3: cognomi tra L e Z epenultima cifra dispari

num matricola composto da almeno due cifre
*/

/*public class ex_1x6 {
    
}

public static boolean scan(String s){
    int state = 0;
    int i = 0;

    while(state >= 0 && i < s.length()){
        final char ch = s.charAt(i++);
        final int chAscii = (int) ch;

        switch (state){
            
            case 0:
                if(Character.isDigit(ch) && ch%2==0)
                    state = 1;
                else if(Character.isDigit(ch) && ch%2!=0)
                    state = 3;
                else 
                    state = -1;
                break;

            case 1:
                if(Character.isDigit(ch) && ch%2==0)
                    state = 1;
                else if(Character.isDigit(ch) && ch%2!=0)
                    state = 2;
                else if(chAscii >= 65 && chAscii <= 75)
                    state = 5;
                else 
                    state = -1;
                break;
            
            case 2: 
                if(Character.isDigit(ch))
            case 5:
                if(chAscii >= 97 && chAscii <= 122)
                    state = 5;
                else 
                    state = -1;
                break;
        }
    }
    return state == 0;
}*/

