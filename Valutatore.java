import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    public void start() {
        int expr_val;

        switch (look.tag) {
            case '(':
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;

            case Tag.NUM:
                expr_val = expr();
                match(Tag.EOF);
                System.out.println(expr_val);
                break;

            default:
                error("Error in start");
        }
    }

    private int expr() {
        // int term_val, exprp_i, exprp_val, expr_val;

        switch (look.tag) {
            case '(':
                /*
                 * term_val = term();
                 * exprp_i = term_val;
                 * exprp_val = exprp(exprp_i);
                 * expr_val = exprp_val;
                 * return expr_val;
                 */
                return exprp(term());

            case Tag.NUM:
                /*
                 * term_val = term();
                 * exprp_i = term_val;
                 * exprp_val = exprp(exprp_i);
                 * expr_val = exprp_val;
                 * return expr_val;
                 */
                return exprp(term());

            default:
                error("Error in expr");

        }
        return 0; // the method must return an int
    }

    // La procedura restituisce 1 attributo perché ne sintetizza 1 e riceve 1
    // argomento perché ne eredita uno
    private int exprp(int exprp_i) {
        // int term_val, exprp_val, exprp1_val;

        switch (look.tag) {
            case '+':
                match('+');
                /*
                 * term_val = term();
                 * exprp1_i = exprp_i + term_val
                 * exprp1_val = exprp(exprp1_i);
                 * exprp_val = exprp1_val;
                 * return exprp1_val; <-- il metodo restituisce gli attributi sintetizzati dalla
                 * produzione
                 */
                return exprp(exprp_i + term());

            case '-':
                match(Tag.SUB);
                /*
                 * term_val = term();
                 * exprp1_i = exprp_i - term_val
                 * exprp1_val = exprp(exprp1_i);
                 * exprp_val = exprp1_val;
                 * return exprp1_val;
                 */
                return exprp(exprp_i - term());

            case ')':
                break;

            case -1:
                break;

            default:
                error("Error in exprp");
        }
        return exprp_i;
    }

    private int term() {
        // int termp_i, fact_val, term_val, termp_val;

        switch (look.tag) {
            case '(':
                /*
                 * fact_val = fact();
                 * termp_i = fact_val;
                 * termp_val = termp(termp_i);
                 * term_val = termp_val;
                 * return term_val;
                 */
                return termp(fact());

            case Tag.NUM:
                /*
                 * fact_val = fact();
                 * termp_i = fact_val;
                 * termp_val = termp(termp_i);
                 * term_val = termp_val;
                 * return term_val;
                 */
                return termp(fact());

            default:
                error("Error in term");

        }
        return 0;
    }

    private int termp(int termp_i) {
        // int termp1_i, fact_val, termp_val, termp1_val;

        switch (look.tag) {
            case '*':
                match(Tag.MUL);
                /*fact_val = fact();
                termp1_i = termp_i * fact_val;
                termp1_val = termp(termp1_i);
                termp_val = termp1_val;
                return termp_val; */
                return termp(termp_i * fact());

            case '/':
                match(Tag.DIV);
                /*fact_val = fact();
                termp1_i = termp_i / fact_val;
                termp1_val = termp(termp1_i);
                termp_val = termp1_val;
                return termp_val; */
                return termp(termp_i / fact());

            case '+':
                break;

            case '-':
                break;

            case ')':
                break;

            case -1:
                break;

            default:
                error("Error in termp");

        }
        return termp_i;
    }

    private int fact() {
        int fact_val;
        // int expr_val, NUM_val;

        switch (look.tag) {
            case '(':
                match(Tag.LPT);
                fact_val = expr();
                match(Tag.RPT);
                return fact_val;

            case Tag.NUM:
                fact_val = ((NumberTok)look).value;
                match(Tag.NUM);    
                return fact_val;

            default:
                error("Error in fact");

        }
        return 0;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\occhi\\Github\\university\\LFT_lab\\File_Prova\\prova2.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
