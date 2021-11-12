public class ex_1x4 {
    public static void main(String[] args) {
        System.out.println(scan4(" 123456 Bianchi") == true);
        System.out.println(scan4("654321 Rossi")    == true);
        System.out.println(scan4("2Bi anchi")       == true);
        System.out.println(scan4("  122  B ")       == true);
        System.out.println(scan4("654321Bianchi")   == false);
        System.out.println(scan4("123456Rossi")     == false);
        System.out.println(scan4("654321")          == false);
        System.out.println(scan4("Rossi")           == false);
        System.out.println(scan4("12346Bianchi5")   == false);
        System.out.println(scan4("65431Rossi2")     == false);
        System.out.println(scan4("123 456 Bianchi") == false);
        System.out.println(scan4(" 12345 6Bianchi") == false);
    }

    public static boolean scan4(String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);
            Character.toUpperCase(ch);
            final int chAscii = (int) ch;

            switch (state){

                case 0:
                    if(Character.isWhitespace(ch))
                        state = 0;
                    else if(Character.isDigit(ch) && ch%2 == 0)
                        state = 1; // if digit even q1
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 2; // if digit un-even q2
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
                    else if(Character.isWhitespace(ch))
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
                    else if(Character.isWhitespace(ch))
                        state = 3;
                    else 
                        state = -1;
                    break;

                case 3:
                    if(Character.isLetter(ch) || Character.isWhitespace(ch))
                        state = 3;
                    else 
                        state = -1;
                    break;
            }
        }
        return state == 3;
    }
}
