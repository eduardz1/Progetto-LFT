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

    public void statlistp(){

    }

    public void stat(int lnext_statlist) {
        code.emitLabel(code.newLabel());

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
                idlist();
                match(')');
                
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

    public void statp(){
        switch(look.tag){
            case Tag.END:
                match(Tag.END);

            case Tag.ELSE:
                {
                    int label = code.newLabel();
                    match(Tag.ELSE);
                    code.emitLabel(OpCode.label, label);
                    stat(label);
                    match(Tag.END);
                    break;
                }

            default:
                error("Error in statp()");
        }
    }

    private void idlist(int lnext_idlist) {
        switch (look.tag) {
            case Tag.ID:
                {int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                break;}

            default:
                error("Error in idlist");
        }
    }

    private void idlistp(){
        switch (look.tag) {
            // GUIDA[<idlistp> := ,ID<idlistp>] = {,}
            case ',':
                {
                    match(Tag.COM);

                    int id_addr = st.lookupAddress((Word)look);
                    if(id_addr == -1)
                        error("Error in expr() : identifier not defined");
                    code.emit(OpCode.iload, id_addr);
                    match(Tag.ID);
        
                    idlistp();
                    break;
                }

            /*
             * GUIDA[<idlistp> := ε] = FOLLOW[<idlistp>]
             * FOLLOW[<idlistp>] = {EOF} U {;} U {}} U {end} U {else} U {)}
             */
            case -1:
                break;

            case ';':
                break;

            case '}':
                break;

            case Tag.END:
                break;

            case Tag.ELSE:
                break;

            case ')':
                break;

            default:
                error("Error in idlistp");

        }
    }

    private void bexpr(int expr_label){
        switch(look.tag){
            case Tag.RELOP:
                {
                    String relop = ((Word)look).lexeme;

                    match(Tag.RELOP);

                    switch(relop){
                        case "or":
                            code.emit(OpCode.ior, expr_label);
                            break;

                        case "and":
                            code.emit(OpCode.iand, expr_label);
                            break;

                        case "lt":
                            code.emit(OpCode.if_icmplt, expr_label);
                            break;

                        case "gt":
                            code.emit(OpCode.if_icmpgt, expr_label);
                            break;

                        case "eq":
                            code.emit(OpCode.if_icmpeq, expr_label);
                            break;

                        case "le":
                            code.emit(OpCode.if_icmple, expr_label);
                            break;

                        case "ne":
                            code.emit(OpCode.if_icmpne, expr_label);
                            break;
                        
                        case "ge":
                            code.emit(OpCode.if_icmpge, expr_label);
                            break;

                        default:
                            error("Error in Word.java RELOP definition");
                    }
                    break;
                }

            default:
                error("Error in bexpr()");
        }
    }

    private void expr() {
        int counter_operators = -1;

        switch (look.tag) {
            case '+':
                {
                    match(Tag.SUM);
                    match(Tag.LPT);
                
                    int counter_operators;
                    counter_operators = exprlist();
                    while(counter_operators > 0)
                        code.emit(OpCode.imul);

                    match(Tag.RPT);
                    break;
                }

            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            
            case '*':
                    match(Tag.MUL);
                    match(Tag.LPT);
                
                    exprlist();

                    while(counter_operators > 0)
                        code.emit(OpCode.imul);

                    match(Tag.RPT);
                    break;

            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;

            case Tag.NUM:
                counter_operators++; // count the number of operators, 1 means 0 and no operations defined
                
                code.emit(OpCode.ldc,((NumberTok)look).value);
                match(Tag.NUM);
                break;

            case Tag.ID:
                {
                    int id_addr = st.lookupAddress((Word)look);
                    if(id_addr == -1)
                        error("Error in expr() : identifier not defined");
                    code.emit(OpCode.iload, id_addr);
                    match(Tag.ID);
                    break;
                }

            default:
                error("Error in expr()");
        }
    }

    private void exprlist(){
        // return 0 when only one operand is present after + - * /
        switch (look.tag) {
            /*
             * GUIDA[<exprlist> := <expr><exprlistp>] = FIRST[<expr>]
             * FIRST[<expr>] = {+} U {-} U {*} U {/} U {NUM} U {ID}
             */
            case '+':
                // same as before we recognize the tags but avoid matching any of them
                expr();
                exprlistp();
                break;

            case '-':
                expr();
                exprlistp();
                break;

            case '*':
                expr();
                exprlistp();
                break;

            case '/':
                expr();
                exprlistp();
                break;

            case Tag.NUM:
                expr();
                exprlistp();
                break;

            case Tag.ID:
                expr();
                exprlistp();
                break;

            default:
                error("Error in exprlist");

        }
    }

    private void exprlistp(){
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
    // ... completare ...
}
