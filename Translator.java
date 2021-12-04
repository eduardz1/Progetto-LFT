import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();

    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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
        int lnext_prog = code.newLabel();

        switch (look.tag) {
            case Tag.ASSIGN:
                statlist(lnext_prog); // the label is utilized for JMP and conditinal JMP
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }

            case Tag.PRINT:
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }

            case Tag.READ:
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }

            case Tag.WHILE:
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }

            case Tag.IF:
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }

            case '{':
                statlist(lnext_prog);
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }

            default:
                error("Error in prog");
        }

        error("Could not switch(look.tag)");
    }

    public void statlist(int lnext_statlist) {
        switch (look.tag) {
            case Tag.ASSIGN:
                stat(lnext_statlist);
                code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                code.emitLabel(lnext_statlist);
                break;

            case Tag.PRINT:
                stat(lnext_statlist);
                code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                code.emitLabel(lnext_statlist);
                break;

            case Tag.READ:
                stat(lnext_statlist);
                code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                code.emitLabel(lnext_statlist);
                break;

            case Tag.WHILE:
                stat(lnext_statlist);
                code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                code.emitLabel(lnext_statlist);
                break;

            case Tag.IF:
                stat(lnext_statlist);
                code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                code.emitLabel(lnext_statlist);
                break;

            case '{':
                stat(lnext_statlist);
                code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                code.emitLabel(lnext_statlist);
                break;

            default:
                error("Error in statlist");
        }
    }

    public void stat(int lnext_statlist) {
        switch (look.tag) {

            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                code.emit(OpCode.istore);
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match(Tag.LPT);
                exprlist();
                code.emit(OpCode.invokestatic, 1);
                match(Tag.RPT);
                break;

            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag == Tag.ID) {
                    int id_addr = st.lookupAddress(((Word) look).lexeme);
                    if (id_addr == -1) {
                        id_addr = count;
                        st.insert(((Word) look).lexeme, count++);
                    }
                    match(Tag.ID);
                    match(')');
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, id_addr);
                }
                error("Error in grammar (stat) after read( with " + look);

            default:
                error("Error in stat");
        }
    }

    private void idlist(int lnext_idlist) {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);

            default:
                error("Error in idlist");
        }
    }

    private void expr() {
        switch (look.tag) {
            // ... completare ...
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            // ... completare ...
        }
    }
    // ... completare ...
}
