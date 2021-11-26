import java.io.*;

public class Parser3x1 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser3x1(Lexer l, BufferedReader br) {
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

    
    /// FIRST(start) = FIRST(expr) = FIRST(term) = FIRST(fact) = {NUM} U {(} <== GUIDA(start)
    public void start() {
        switch(look.tag){

            case '(':
                // match(Tag.LPT);
                expr();
                match(Tag.EOF);
                break;

            case Tag.NUM:
                // match(Tag.NUM);
                expr();
                match(Tag.EOF);
                break;

            default:
                error("error in start");
        }
    }

    /// FIRST(expr) = FIRST(term) = FIRST(fact) = {NUM} U {(} <== GUIDA(expr)
    private void expr() {
        switch(look.tag){

            case '(':
                // match(Tag.LPT);
                term();
                exprp();
                break;

            case Tag.NUM:
                // match(Tag.NUM);
                term();
                exprp();
                break;

            default:
                error("error in expr");

        }    
    }
 
    private void exprp() {
        switch (look.tag) {

            /// GUIDA(exprp --> +<term><exprp>) ==> {+}
            case '+':
                match(Tag.SUM); 
                term();
                exprp();
                break;

            /// GUIDA(exprp --> -<term><exprp>) ==> {-}
            case '-':
                match(Tag.SUB);
                term();
                exprp();
                break;

            /// GUIDA(exprp --> epsilon) ==> {)} U EOF
            case ')':
                match(Tag.RPT);
                break;
            
            case -1:
                // match(Tag.EOF);
                break;

            /// ERROR
            default: // if epsilon there's nothign to match and we just break;
                // error("error in exprp");
                break;

        }
    }

    /// FIRST(term) = FIRST(fact) = {NUM} U {(} <== GUIDA(term)
    private void term() {
        switch(look.tag){

            case '(':
                // match(Tag.LPT);
                fact();
                termp();
                break;

            case Tag.NUM:
                // match(Tag.NUM);
                fact();
                termp();
                break;

            default:
                error("error in term");

        }     
        
        
    }

    private void termp() {
        switch(look.tag){

            /// GUIDA(termp --> *<fact><exprp>) ==> {*}
            case '*':
                match(Tag.MUL);
                fact();
                termp();
                break;

            /// GUIDA(termp --> /<fact><exprp>) ==> {/}
            case '/':
                match(Tag.DIV);
                fact();
                termp();
                break;

            /// GUIDA(termp --> epsilon) ==> {)} U EOF
            case ')':
                match(Tag.RPT);
                break;
            
            case -1:
                // match(Tag.EOF);
                break;

            /// ERROR
            default:
                // error("error in termp");
                break;

        }
    }

    private void fact() {
        switch(look.tag){

            case '(':
                match(Tag.LPT);
                expr();
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;
                
            default:
                error("error in fact");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\occhi\\Github\\university\\LFT_lab\\File_Prova\\prova.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser3x1 parser = new Parser3x1(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}