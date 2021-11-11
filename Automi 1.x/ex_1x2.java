/*

Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
in un linguaggio in stile Java: un identificatore e una sequenza non vuota di lettere, numeri, ed il `
simbolo di “underscore” _ che non comincia con un numero e che non puo essere composto solo `
dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.

*/

public class ex_1x2 {

    public static void main(String[] args){

        // All test passed
        System.out.println(scan("x")       == true);
        System.out.println(scan("flag1")   == true);
        System.out.println(scan("x2y2")    == true);
        System.out.println(scan("x_1")     == true);
        System.out.println(scan("lft_lab") == true);
        System.out.println(scan("_temp")   == true);
        System.out.println(scan("x_1_y_2") == true);
        System.out.println(scan("x___")    == true);
        System.out.println(scan("__5")     == true);
        System.out.println(scan("5")       == false);
        System.out.println(scan("221B")    == false);
        System.out.println(scan("123")     == false);
        System.out.println(scan("9_to_5")  == false);
        System.out.println(scan("___")     == false);
    }

    public static boolean scan(String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch (state){
                case 0:
                    if(ch == '_')
                        state = 1;
                    else if(Character.isLetter(ch))
                        state = 2;
                    else 
                        state = -1;
                    break;
                    
                case 1:
                    if(Character.isLetterOrDigit(ch))
                        state = 2;
                    else if(ch == '_')
                        state = 1;
                    else 
                        state = -1;
                    break;

                case 2:
                    if(Character.isLetterOrDigit(ch) || ch == '_')
                        state = 2;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 2;
    }
}
