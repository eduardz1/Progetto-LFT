import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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
        // ... completare ...
        expr();
        match(Tag.EOF);
        // ... completare ...
    }

    private void expr() {
        term();
        exprp();
    }

    private void exprp() {
        switch (look.tag) {

            case '+':
                term();
                exprp();

            case '-':
                term();
                exprp();

            case ' ':
                // nothing

        }
    }

    private void term() {
        fact();
        termp();
    }

    private void termp() {
        switch(look.tag){

            case '*':
                fact();
                termp();

            case '/':
                fact();
                termp();

            case ' ':
                //nothing

        }
    }

    private void fact() {
        expr();
        // or
        match(Tag.NUM);
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\occhi\\Github\\university\\LFT_lab\\prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}