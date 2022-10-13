package automi;

/*
 * Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
 * in un linguaggio in stile Java: un identificatore e una sequenza non vuota di lettere, numeri, ed il `
 * simbolo di underscore _ che non comincia con un numero e che non puo essere composto solo `
 * dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.
 */

public class ex_1x2 {

    public static void main(String[] args){
        System.out.println(scan2("x")       == true);
        System.out.println(scan2("flag1")   == true);
        System.out.println(scan2("x2y2")    == true);
        System.out.println(scan2("x_1")     == true);
        System.out.println(scan2("lft_lab") == true);
        System.out.println(scan2("_temp")   == true);
        System.out.println(scan2("x_1_y_2") == true);
        System.out.println(scan2("x___")    == true);
        System.out.println(scan2("__5")     == true);
        System.out.println(scan2("5")       == false);
        System.out.println(scan2("221B")    == false);
        System.out.println(scan2("123")     == false);
        System.out.println(scan2("9_to_5")  == false);
        System.out.println(scan2("___")     == false);
    }

    public static boolean scan2(String s){
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
