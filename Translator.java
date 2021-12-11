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

    public void statlist(int lnext_prog) {
        int lnext_statlist = code.newLabel();

        switch (look.tag) {
            case Tag.ASSIGN: // complete
                stat(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                break;

            case Tag.PRINT:
                stat(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                break;

            case Tag.READ: // complete
                stat(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                break;

            case Tag.WHILE: /*
                             * LAB 1:
                             * bexpr
                             * ifcmp
                             * <stat>
                             * LAB 2:
                             * <stat>
                             */

                stat(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                break;

            case Tag.IF:
                stat(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                break;

            case '{':
                stat(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                statlistp(lnext_statlist);
                //code.emitLabel(lnext_statlist);
                break;

            default:
                error("Error in statlist");
        }
    }

    public void statlistp(int lnext_statlist) {
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

    public void stat(int lnext_statlist) {
        // code.emitLabel(code.newLabel());

        switch (look.tag) {

            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(0); // we pass 0 to identify the "assign" case

                // code.emitLabel(lnext_stat);
                break;

            case Tag.PRINT: // non acora fatto
                match(Tag.PRINT);
                match(Tag.LPT);
                exprlist();
                match(Tag.RPT);

                // code.emitLabel(lnext_stat);
                break;

            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist(1); // we pass 1 to identify the "read" case
                match(')');

                // code.emitLabel(lnext_stat);
                break;

            case Tag.WHILE: {
                match(Tag.WHILE);
                match('(');

                int while_true = code.newLabel();
                int while_false = code.newLabel();
                int while_start = code.newLabel();

                code.emitLabel(while_start);

                bexpr(while_true, while_false);
                match(')');

                code.emitLabel(while_true);

                stat(while_start);

                code.emit(OpCode.GOto, while_start);
                code.emitLabel(while_false);
                break;
            }

            case Tag.IF: {
                match(Tag.IF);
                match(Tag.LPT);

                int if_false = code.newLabel();

                bexpr(-1, if_false);

                match(Tag.RPT);

                stat();
                statp(if_false);

                // we emit the label in statp() because of the two cases: end or else
                break;
            }

            case '{':
                match(Tag.LPG);
                statlist();
                match(Tag.RPG);

                // code.emitLabel(lnext_stat);
                break;

            default:
                error("Error in stat");
        }
    }

    public void statp(int if_false) {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                code.emitLabel(if_false);
                break;

            case Tag.ELSE: {
                match(Tag.ELSE);
                stat();
                match(Tag.END);
                code.emitLabel(if_false);
                break;
            }

            default:
                error("Error in statp()");
        }
    }

    private void idlist(int read_assign) { // read 1, assign 0
        switch (look.tag) {
            case Tag.ID: {
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    if (read_assign == 0)
                        st.insert(((Word) look).lexeme, count++);
                    error("Error in idlist(): Identifier not declared for print: " + ((Word) look).lexeme);
                }

                if (read_assign == 0)
                    code.emit(OpCode.istore);

                code.emit(OpCode.iload, id_addr);
                code.emit(OpCode.invokestatic, 1);

                match(Tag.ID);
                idlistp(read_assign);
                break;
            }

            default:
                error("Error in idlist");
        }
    }

    private void idlistp(int read_assign) {
        switch (look.tag) {
            // GUIDA[<idlistp> := ,ID<idlistp>] = {,}
            case ',': {
                match(Tag.COM);

                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    if (read_assign == 0)
                        st.insert(((Word) look).lexeme, count++);
                    error("Error in expr() : Identifier not defined: " + ((Word) look).lexeme);
                }

                if (read_assign == 0)
                    code.emit(OpCode.istore);

                code.emit(OpCode.iload, id_addr);
                code.emit(OpCode.invokestatic, 1);

                match(Tag.ID);
                idlistp(read_assign);
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

    private void bexpr(int label_true, int label_false) {
        switch (look.tag) {
            case Tag.RELOP: {
                String relop = ((Word) look).lexeme; // save relop value in a local variable because we need to match
                                                     // before the switch case
                match(Tag.RELOP);
                expr(); // we need to write expr1 and expr2 first
                expr();

                switch (relop) {
                    case "||":
                        code.emit(OpCode.ior); /*
                                                * then we verify if it's true and send it to label_true when
                                                * it's not true anymore we jump at label_false or
                                                * skip this instruction directly
                                                */
                        break;

                    case "&&":
                        code.emit(OpCode.iand);
                        break;

                    case "<":
                        code.emit(OpCode.if_icmplt, label_true);
                        break;

                    case ">":
                        code.emit(OpCode.if_icmpgt, label_true);
                        break;

                    case "==":
                        code.emit(OpCode.if_icmpeq, label_true);
                        break;

                    case "<=":
                        code.emit(OpCode.if_icmple, label_true);
                        break;

                    case "<>":
                        code.emit(OpCode.if_icmpne, label_true);
                        break;

                    case ">=":
                        code.emit(OpCode.if_icmpge, label_true);
                        break;

                    default:
                        error("Error in Word.java RELOP definition");
                }
                break;
            }

            default:
                error("Error in bexpr()");
        }
        code.emit(OpCode.GOto, label_false);
    }

    private void expr() {
        int counter_operators = -1;

        switch (look.tag) {
            case '+': {
                match(Tag.SUM);
                match(Tag.LPT);

                // int counter_operators;
                counter_operators = exprlist();
                while (counter_operators > 0)
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

                while (counter_operators > 0)
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

                code.emit(OpCode.ldc, ((NumberTok) look).value);
                match(Tag.NUM);
                break;

            case Tag.ID: {
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1)
                    error("Error in expr() : identifier not defined");
                code.emit(OpCode.iload, id_addr);
                match(Tag.ID);
                break;
            }

            default:
                error("Error in expr()");
        }
    }

    private void exprlist() {
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

    private void exprlistp() {
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
