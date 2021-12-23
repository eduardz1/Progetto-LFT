public class ex_1x5 {
    public static void main(String[] args) {
        System.out.println(scan5("Bianchi123456") == true);
        System.out.println(scan5("Rossi654321")   == true);
        System.out.println(scan5("Bianchi2")      == true);
        System.out.println(scan5("B122")          == true);
        System.out.println(scan5("Bianchi654321") == false);
        System.out.println(scan5("Rossi123456")   == false);
        System.out.println(scan5("654321")        == false);
        System.out.println(scan5("Rossi")         == false);
        System.out.println(scan5("Bianchi12346B") == false);
        System.out.println(scan5("Ros2si65431")   == false);
    }

    public static boolean scan5(String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);
            Character.toUpperCase(ch);
            final int chAscii = (int) ch;

            switch (state){
                case 0:
                    if(chAscii >= 65 && chAscii <= 75) // pongo ch compreso tra A e K, corso A se matricola pari 
                        state = 1;
                    else if(chAscii > 75 && chAscii <= 90) // pongo ch compreso tra L e Z, corso B se matricola dispari 
                        state = 4;
                    else 
                        state = -1;
                    break;
                    
                case 1:
                    if(Character.isLetter(ch))
                        state = 1;
                    else if(Character.isDigit(ch) && ch%2 == 0)
                        state = 2;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 3;
                    else 
                        state = -1;
                    break;

                case 2:
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 2;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 3;
                    else 
                        state = -1;
                    break;

                case 3:
                    if(Character.isDigit(ch) && ch%2 != 0)
                        state = 3;
                    else if(Character.isDigit(ch) && ch%2 == 0) 
                        state = 2;
                    else 
                        state = -1;
                    break;

                case 4:
                    if(Character.isLetter(ch))
                        state = 4;
                    else if(Character.isDigit(ch) && ch%2 == 0)
                        state = 6;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 5;
                    else 
                        state = -1;
                    break;

                case 5:
                    if(Character.isDigit(ch) && ch%2 == 0)
                        state = 6;
                    else if(Character.isDigit(ch) && ch%2 != 0)
                        state = 5;
                    else 
                        state = -1;
                    break;

                case 6:
                    if(Character.isDigit(ch) && ch%2 != 0)
                        state = 5;
                    else if(Character.isDigit(ch) && ch%2 == 0) 
                        state = 6;
                    else 
                        state = -1;
                    break;
            }
        }
        return state == 2 || state == 5;
    }
}
