import java.io.*;

public class Parser3x2 {
    private final Lexer2x3 lex;
    private final BufferedReader pbr;
    private Token look;

    public Parser3x2(Lexer2x3 l, BufferedReader br) {
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
        switch (look.tag) {
            /*
             * GUIDA[<prog> := <statlist>EOF] = FIRST[<stat>]
             * FIRST[<stat>] = {assign} U {print} U {read} U {while} U {if} U {{}
             */
            case Tag.ASSIGN, '{', Tag.IF, Tag.WHILE, Tag.READ, Tag.PRINT -> {
                // no match because the production does not ask us to
                statlist();
                match(Tag.EOF);
            }
            default -> error("Error in prog");
        }
    }

    public void statlist() {
        switch (look.tag) {
            /*
             * GUIDA[<statlist> := <stat><statlistp>] = FIRST[<stat>]
             * FIRST[<stat>] = {assign} U {print} U {read} U {while} U {if} U {{}
             */
            case Tag.ASSIGN, '{', Tag.IF, Tag.WHILE, Tag.READ, Tag.PRINT -> {
                // same as before, we must not match here
                stat();
                statlistp();
            }
            default -> error("Error in statlist");
        }
    }

    public void statlistp() {
        switch (look.tag) {
            // GUIDA[<statlistp> := ;<stat><statlistp>] = FIRST[<statlistp>] = {;}
            case ';':
                match(Tag.SEM);
                stat();
                statlistp();
                break;

            // GUIDA[<statlistp>] := ε] = {EOF} U {}}
            case -1:
                break;

            case '}':
                break;

            default:
                error("Error in statlistp");
        }

    }

    public void stat() {
        switch (look.tag) {
            // GUIDA[<stat> := assign<expr>to<idlist>] = {assign}
            case Tag.ASSIGN -> {
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
            }

            // GUIDA[<stat> := print(<expr>)] = {print}
            case Tag.PRINT -> {
                match(Tag.PRINT);
                match(Tag.LPT);
                exprlist();
                match(Tag.RPT);
            }

            // GUIDA[<stat> := read(<expr>)] = {read}
            case Tag.READ -> {
                match(Tag.READ);
                match(Tag.LPT);
                idlist();
                match(Tag.RPT);
            }

            // GUIDA[<stat> := while(<bexpr>)] = {while}
            case Tag.WHILE -> {
                match(Tag.WHILE);
                match(Tag.LPT);
                bexpr();
                match(Tag.RPT);
                stat();
            }

            // GUIDA[<stat> := if(<bexpr>)<stat><statp>] = {if}
            case Tag.IF -> {
                match(Tag.IF);
                match(Tag.LPT);
                bexpr();
                match(Tag.RPT);
                stat();
                statp();
            }

            // GUIDA[<stat> := {<statlist>}] = {{}
            case '{' -> {
                match(Tag.LPG);
                statlist();
                match(Tag.RPG);
            }
            default -> error("Error in stat");
        }
    }

    public void statp() {
        switch (look.tag) {
            // GUIDA[<statp> := end] = {end}
            case Tag.END -> match(Tag.END);


            // GUIDA[<statp> := else<stat>end] = {else}
            case Tag.ELSE -> {
                match(Tag.ELSE);
                stat();
                match(Tag.END);
            }
            default -> error("Error in stat");
        }
    }

    public void idlist() {
        // GUIDA[<idlist> := ID<idlistp>] = {ID}
        if (look.tag == Tag.ID) {
            match(Tag.ID);
            idlistp();
        } else {
            error("Error in idlist");
        }
    }

    public void idlistp() {
        switch (look.tag) {
            // GUIDA[<idlistp> := ,ID<idlistp>] = {,}
            case ',':
                match(Tag.COM);
                match(Tag.ID);
                idlistp();
                break;

            /*
             * GUIDA[<idlistp> := ε] = FOLLOW[<idlistp>]
             * FOLLOW[<idlistp>] = {EOF} U {;} U {}} U {end} U {else} U {)}
             */
            case -1:
                break;

            case ';':

            case ')':

            case Tag.ELSE:

            case Tag.END:

            case '}':
                break;

            default:
                error("Error in idlistp");

        }
    }

    public void bexpr() {
        // GUIDA[<bexpr> := RELOP<expr><expr>] = {RELOP}
        if (look.tag == Tag.RELOP) {
            match(Tag.RELOP);
            expr();
            expr();
        } else {
            error("Error in bexpr");
        }
    }

    public void expr() {
        switch (look.tag) {
            // GUIDA[<expr> := +(<exprlist>)] = {+}
            case '+' -> {
                match(Tag.SUM);
                match(Tag.LPT);
                exprlist();
                match(Tag.RPT);
            }

            // GUIDA[<expr> := -<expr><expr>] = {-}
            case '-' -> {
                match(Tag.SUB);
                expr();
                expr();
            }

            // GUIDA[<expr> := *(<exprlist>)] = {*}
            case '*' -> {
                match(Tag.MUL);
                match(Tag.LPT);
                exprlist();
                match(Tag.RPT);
            }

            // GUIDA[<expr> := /<expr><expr>] = {/}
            case '/' -> {
                match(Tag.DIV);
                expr();
                expr();
            }

            // GUIDA[<expr> := NUM] = {NUM}
            case Tag.NUM -> match(Tag.NUM);


            // GUIDA[<expr> := ID] = {ID}
            case Tag.ID -> match(Tag.ID);
            default -> error("Error in expr");
        }
    }

    public void exprlist() {
        switch (look.tag) {
            /*
             * GUIDA[<exprlist> := <expr><exprlistp>] = FIRST[<expr>]
             * FIRST[<expr>] = {+} U {-} U {*} U {/} U {NUM} U {ID}
             */
            case '+', '-', '*', '/', Tag.NUM, Tag.ID -> {
                // same as before we recognize the tags but avoid matching any of them
                expr();
                exprlistp();
            }
            default -> error("Error in exprlist");
        }
    }

    public void exprlistp() {
        switch (look.tag) {
            // GUIDA[<exprlistp> := ,<expr><exprlistp>] = {,}
            case ',':
                match(Tag.COM);
                expr();
                exprlistp();
                break;

            // GUIDA[<exprlistp> := ε] = FOLLOW[<exprlistp>] = {)}
            case ')':
                break;

            default:
                error("Error in exprlistp");
        }
    }

    public static void main(String[] args) {
        Lexer2x3 lex = new Lexer2x3();
        String path = "src/test_files/max_tre_num.lft"; // il percorso del file da leggere
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
