import java.io.*;
import java.util.*;
// this is a random comment
public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n')
                line++;
            readch(br);
        }

        switch (peek) {
        case '!':
            peek = ' ';
            return Token.not;

        case '(':
            peek = ' ';
            return Token.lpt;

        case ')':
            peek = ' ';
            return Token.rpt;

        case '{':
            peek = ' ';
            return Token.lpg;

        case '}':
            peek = ' ';
            return Token.rpg;

        case '+':
            peek = ' ';
            return Token.plus;

        case '-':
            peek = ' ';
            return Token.minus;

        case '*':
            peek = ' ';
            return Token.mult;

        case '/':
            readch(br);
            if (peek == '/') {
                peek = '\n';
                return null;
            } else if (peek == '*') {
                readch(br);
                while (peek != (char) -1) {
                    if (peek == '*') {
                        readch(br);
                        if (peek == '/') {
                            return null;
                        }
                    }
                }
                System.err.println("ERROR");
                return null;
            }
            peek = ' ';
            return Token.div;

        case ';':
            peek = ' ';
            return Token.semicolon;

        case ',':
            peek = ' ';
            return Token.comma;

        case '&':
            readch(br);
            if (peek == '&') {
                peek = ' ';
                return Word.and;
            } else {
                System.err.println("Erroneous character" + " after & : " + peek);
                return null;
            }

        case '|':
            readch(br);
            if (peek == '|') {
                peek = ' ';
                return Word.or;
            } else {
                System.err.println("Erroneous character" + " after | : " + peek);
                return null;
            }

        case '<':
            readch(br);
            if (peek == '>') {
                peek = ' ';
                return Word.ne;
            } else if (peek == '=') {
                peek = ' ';
                return Word.le;
            } else if (peek == ' ') {
                peek = ' ';
                return Word.lt;
            } else {
                System.err.println("Erroneous character" + " after < : " + peek);
                return null;
            }

        case '>':
            readch(br);
            if (peek == '=') {
                peek = ' ';
                return Word.ge;
            } else if (peek == ' ') {
                peek = ' ';
                return Word.gt;
            } else {
                System.err.println("Erroneous character" + " after > : " + peek);
                return null;
            }

        case '=':
            readch(br);
            if (peek == '=') {
                peek = ' ';
                return Word.eq;
            } else {
                System.err.println("Erroneous character" + " after = : " + peek);
                return null;
            }

        case (char) -1:
            return new Token(Tag.EOF);

        default:
            if (Character.isLetter(peek) || peek == '_') { // gestisco identificatori e parole chiave
                int state = 0;

                do {
                    switch (state) {
                    case 0:
                        switch (peek) { // controllo se e' un parola chiave, se non lo e' passo allo state 1 che e' un
                                        // automa che riconsoce gli identificatori

                        case 'a': // assign
                            readch(br);
                            if (peek == 's') {
                                readch(br);
                                if (peek == 's') {
                                    readch(br);
                                    if (peek == 'i') {
                                        readch(br);
                                        if (peek == 'g') {
                                            readch(br);
                                            if (peek == 'n') {
                                                peek = ' ';
                                                return Word.assign;
                                            }
                                        }
                                    }
                                }
                            }
                            state = 2;
                            break;

                        case 't': // to
                            readch(br);
                            if (peek == 'o') {
                                peek = ' ';
                                return Word.to;
                            }
                            state = 2;
                            break;

                        case 'i': // if
                            readch(br);
                            if (peek == 'f') {
                                return Word.iftok;
                            }
                            state = 2;
                            break;

                        case 'e':
                            readch(br);
                            if (peek == 'l') { // else
                                readch(br);
                                if (peek == 's') {
                                    readch(br);
                                    if (peek == 'e') {
                                        peek = ' ';
                                        return Word.elsetok;
                                    }
                                }
                            } else if (peek == 'n') { // end
                                readch(br);
                                if (peek == 'd') {
                                    peek = ' ';
                                    return Word.end;
                                }
                            }
                            state = 2;
                            break;

                        case 'w': // while
                            readch(br);
                            if (peek == 'h') {
                                readch(br);
                                if (peek == 'i') {
                                    readch(br);
                                    if (peek == 'l') {
                                        readch(br);
                                        if (peek == 'e') {
                                            peek = ' ';
                                            return Word.whiletok;
                                        }
                                    }
                                }
                            }
                            state = 2;
                            break;

                        case 'b': // begin
                            readch(br);
                            if (peek == 'e') {
                                readch(br);
                                if (peek == 'g') {
                                    readch(br);
                                    if (peek == 'i') {
                                        readch(br);
                                        if (peek == 'n') {
                                            peek = ' ';
                                            return Word.begin;
                                        }
                                    }
                                }
                            }
                            state = 2;
                            break;

                        case 'p': // print
                            readch(br);
                            if (peek == 'r') {
                                readch(br);
                                if (peek == 'i') {
                                    readch(br);
                                    if (peek == 'n') {
                                        readch(br);
                                        if (peek == 't') {
                                            peek = ' ';
                                            return Word.print;
                                        }
                                    }
                                }
                            }
                            state = 2;
                            break;

                        case 'r':
                            readch(br);
                            if (peek == 'e') {
                                readch(br);
                                if (peek == 'a') {
                                    readch(br);
                                    if (peek == 'd') {
                                        peek = ' ';
                                        return Word.read;
                                    }
                                }
                            }
                            state = 2;
                            break;

                        }

                    default:
                        state = 1;
                        break;

                    /*
                     * case 1: if (peek == ' ') { return Word.identifier; } else if
                     * (Character.isLetterOrDigit(peek)) state = 1; else {
                     * System.err.println("Syntax error" + " on Token : " + peek); return null; }
                     * break;
                     */

                    // 2.2 soluzione per identificatori del tipo
                    // (a+...+Z+(_(_)*(a+...+Z+0+...+9)))(a+...+Z+0+...+9+_)*
                    case 1:
                        if (Character.isLetterOrDigit(peek) || peek == '_')
                            state = 2;
                        else {
                            System.err.println("Syntax error" + " on Token : " + peek);
                            return null;
                        }
                        break;

                    case 2:
                        if (peek == ' ') {
                            return Word.identifier;
                        } else if (Character.isLetterOrDigit(peek)) {
                            state = 2;
                        } else if (peek == '_') {
                            state = 1;
                        } else {
                            System.err.println("Syntax error" + " on Token : " + peek);
                            return null;
                        }
                        break;

                    }
                } while (peek != ' ');

                System.err.println("Syntax error" + " on Token : " + peek); // default behaviour
                return null;

            } else if (Character.isDigit(peek)) {

                while (peek != ' ') {
                    readch(br);
                    if (!Character.isDigit(peek)) {
                        System.err.println("Syntax error" + " on Token : " + peek);
                        return null;
                    }
                }
                return Word.number;

            } else {
                System.err.println("Erroneous character: " + peek);
                return null;
            }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\occhi\\University\\LFT_lab\\prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
