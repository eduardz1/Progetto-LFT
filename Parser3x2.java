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

    // GUIDA(prog) <== FIRST(stat)
    public void prog() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                statlist();
                match(Tag.EOF);
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                statlist();
                match(Tag.EOF);
                break;

            case Tag.READ:
                match(Tag.READ);
                statlist();
                match(Tag.EOF);
                break;

            case Tag.WHILE:
                match(Tag.WHILE);
                statlist();
                match(Tag.EOF);
                break;

            case Tag.IF:
                match(Tag.IF);
                statlist();
                match(Tag.EOF);
                break;

            case '{':
                match(Tag.LPG);
                statlist();
                match(Tag.EOF);
                break;

            default:
                error("Error in prog");

        }
    }

    // GUIDA(statlist) <== FIRST(stat)
    public void statlist() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                stat();
                statlistp();
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                stat();
                statlistp();
                break;

            case Tag.READ:
                match(Tag.READ);
                stat();
                statlistp();
                break;

            case Tag.WHILE:
                match(Tag.WHILE);
                stat();
                statlistp();
                break;

            case Tag.IF:
                match(Tag.IF);
                stat();
                statlistp();
                break;

            case '{':
                match(Tag.LPG);
                stat();
                statlistp();
                break;

            default:
                error("Error in statlist");
        }
    }

    public void statlistp() {
        switch (look.tag) {
            case ';':
                match(Tag.SEM);
                stat();
                statlistp();
                break;

            case '}':
                match(Tag.RPG);
                break;

            case Tag.END:
                match(Tag.END);
                break;

            default:
                error("Error in statlistp");
        }
    }

    public void stat() {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;

            case Tag.PRINT:
                match(Tag.READ);
                match(Tag.LPT);
                exprlist();
                match(Tag.RPT);
                break;

            case Tag.READ:
                match(Tag.READ);
                match(Tag.LPT);
                idlist();
                match(Tag.RPT);
                break;

            case Tag.WHILE:
                match(Tag.WHILE);
                match(Tag.LPT);
                bexpr();
                match(Tag.RPT);
                stat();
                break;

            case Tag.IF:
                match(Tag.IF);
                match(Tag.LPT);
                bexpr();
                match(Tag.RPT);
                stat();
                statp();
                break;

            case '{':
                match(Tag.LPG);
                statlist();
                match(Tag.RPG);
                break;

            default:
                error("Error in stat");
        }
    }

    public void statp() {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                break;

            case Tag.ELSE:
                stat();
                match(Tag.END);
                break;

            default:
                error("Error in stat");

        }
    }

    public void idlist() {
        switch (look.tag) {
            case Tag.ID:
                match(Tag.ID);
                idlistp();
                break;

            default:
                error("Error in idlist");

        }
    }

    public void idlistp() {
        switch (look.tag) {
            case ',':
                match(Tag.COM);
                match(Tag.ID);
                idlistp();
                break;

            case '}':
                match(Tag.RPG);
                break;

            case (';'):
                match(Tag.SEM);
                break;

            case Tag.END:
                match(Tag.END);
                break;

            case ')':
                match(Tag.RPT);
                break;

            default:
                error("Error in idlistp");

        }
    }

    public void bexpr() {
        switch (look.tag) {
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;

            default:
                error("Error in bexpr");

        }
    }

    public void expr() {
        switch (look.tag) {
            case '+':
                match(Tag.SUM);
                match(Tag.LPT);
                exprlist();
                match(Tag.RPT);
                break;

            case '-':
                match(Tag.SUB);
                expr();
                expr();
                break;

            case '*':
                match(Tag.MUL);
                match(Tag.LPT);
                exprlist();
                match(Tag.RPT);
                break;

            case '/':
                match(Tag.DIV);
                expr();
                expr();
                break;

            case Tag.ID:
                match(Tag.ID);
                break;

            case Tag.NUM:
                match(Tag.NUM);
                break;

            default:
                error("Error in expr");

        }
    }

    public void exprlist() {
        switch (look.tag) {
            case '+':
                match(Tag.SUM);
                expr();
                exprlistp();
                break;

            case '-':
                match(Tag.SUB);
                expr();
                exprlistp();
                break;

            case '*':
                match(Tag.MUL);
                expr();
                exprlistp();
                break;

            case '/':
                match(Tag.DIV);
                expr();
                exprlistp();
                break;

            case Tag.ID:
                match(Tag.ID);
                expr();
                exprlistp();
                break;

            case Tag.NUM:
                match(Tag.NUM);
                expr();
                exprlistp();
                break;

            case ',':
                match(Tag.COM);
                expr();
                exprlistp();
                break;

            default:
                error("Error in exprlist");

        }
    }

    public void exprlistp() {
        switch (look.tag) {
            case ',':
                match(Tag.COM);
                expr();
                exprlistp();
                break;

            default:
                error("Error in exprlistp");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\occhi\\Github\\university\\LFT_lab\\File_Prova\\euclid.lft"; // il percorso del file da
                                                                                              // leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser3x2 parser = new Parser3x2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}