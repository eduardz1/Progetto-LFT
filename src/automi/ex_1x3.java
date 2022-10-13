package automi;

/* Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
 * contengono un numero di matricola seguito (subito) da un cognome, dove la combinazione di
 * matricola e cognome corrisponde a studenti del corso A che hanno un numero di matricola pari
 * oppure a studenti del corso B che hanno un numero di matricola dispari. 
 * Si ricorda che gli studenti del corso A sono quelli con cognomi la cui iniziale 
 * `e compresa tra A e K, e gli studenti del corso B sono quelli con cognomi 
 * la cui iniziale `e compresa tra L e Z.
 * Nel contesto di questo esercizio, un numero di matricola non ha un numero 
 * prestabilito di cifre (ma deve essere composto di almeno una cifra). 
 * Un cognome corrisponde a una sequenza di lettere, e deve essere composto di almeno una lettera.
 */

public class ex_1x3 {
    public static void main(String[] args) {
        System.out.println(scan3("123456Bianchi") == true);
        System.out.println(scan3("654321Rossi")   == true);
        System.out.println(scan3("2Bianchi")      == true);
        System.out.println(scan3("122B")          == true);
        System.out.println(scan3("654321Bianchi") == false);
        System.out.println(scan3("123456Rossi")   == false);
        System.out.println(scan3("654321")        == false);
        System.out.println(scan3("Rossi")         == false);
        System.out.println(scan3("12346Bianchi5") == false);
        System.out.println(scan3("65431Rossi2")   == false);
    }

    public static boolean scan3(String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);
            Character.toUpperCase(ch);
            final int chAscii = (int) ch;

            switch (state){
                case 0:
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 1;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 2;
                    else 
                        state = -1;
                    break;
                    
                case 1:
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 1;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 2;
                    else if(chAscii >= 65 && chAscii <= 75) // pongo ch compreso tra A e K, corso A se matricola pari 
                        state = 3;
                    else 
                        state = -1;
                    break;

                case 2:
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 1;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 2;
                    else if(chAscii > 75 && chAscii <= 90) // pongo ch compreso tra L e Z, corso B se matricola dispari 
                        state = 3;
                    else 
                        state = -1;
                    break;

                case 3:
                    if(Character.isLetter(ch))
                        state = 3;
                    else 
                        state = -1;
                    break;
            }
        }
        return state == 3;
    }
}
