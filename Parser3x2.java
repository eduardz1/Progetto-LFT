import java.io.*;

public class Parser3x2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser3x2(Lexer l, BufferedReader br) {
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

    public void prog() {
        switch(look.tag){
            case 
        }
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