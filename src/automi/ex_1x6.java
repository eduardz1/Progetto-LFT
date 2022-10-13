package automi;

/*
 * DFA che riconosca il linguaggio di stringhe che contengono 
 * un numero di matricola seguito senza spazi da un cognome 
 * dove la combinazione corrisponde agli studenti del turno T2 e T3
 * 
 * T2: cognomi tra A e K con penultima cifra pari
 * T3: cognomi tra L e Z epenultima cifra dispari
 * 
 * num matricola composto da almeno due cifre
 */

public class ex_1x6 {
    
    public static void main(String[] args) {
        System.out.println(scan6("654321Bianchi") == true);
        System.out.println(scan6("123456Rossi") == true);
        System.out.println(scan6("221B") == true);
        System.out.println(scan6("123456Bianchi") == false);
        System.out.println(scan6("654321Rossi") == false);
        System.out.println(scan6("5") == false);
        System.out.println(scan6("654322") == false);
        System.out.println(scan6("Rossi") == false);
        System.out.println(scan6("2Bianchi") == false);
    }

    public static boolean scan6(String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);
            final int chAscii = (int) ch;

            switch (state){
                
                case 0:
                    if(Character.isDigit(ch) && ch%2==0)
                        state = 6;
                    else if(Character.isDigit(ch) && ch%2!=0)
                        state = 7;
                    else 
                        state = -1;
                    break;

                case 6:
                    if(Character.isDigit(ch) && ch%2==0)
                        state = 1;
                    else if(Character.isDigit(ch) && ch%2!=0)
                        state = 2;
                    else 
                        state = -1;
                    break;
                
                case 1: 
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 1;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 2;
                    else if(Character.isLetter(ch) && chAscii >= 'A' && chAscii <= 'K')
                        state = 5;
                    else 
                        state = -1;
                    break;

                case 2:
                    if(Character.isLetter(ch) && chAscii >= 'A' && chAscii <= 'K')
                        state = 5;
                    else if(Character.isDigit(ch))
                        state = 3;
                    else 
                        state = -1;
                    break;

                case 5:
                    if(chAscii >= 'a' && chAscii <= 'z')
                        state = 5;
                    else 
                        state = -1;
                    break;

                case 4:
                    if(Character.isLetter(ch) && chAscii >= 'L' && chAscii <= 'Z')
                        state = 5;
                    else if(Character.isDigit(ch))
                        state = 1;
                    else 
                        state = -1;
                    break;

                case 3:
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 4;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 3;
                    else if(Character.isLetter(ch) && chAscii >= 'L' && chAscii <= 'Z')
                        state = 5;
                    else 
                        state = -1;
                    break;
                    
                case 7:
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 4;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 3;
                    else 
                        state = -1;
                    break;
            }
        }
        return state == 5;
    }
}

