import java.io.*;

// dobbiamo scrivere le azioni semantiche per ogni produzione
public class Translator5x3 {
    private Lexer2x3 lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();

    int count = 0;

    public Translator5x3(Lexer2x3 l, BufferedReader br) {
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

    /*
     * GUIDA[<prog> := <statlist>EOF] = FIRST[<stat>]
     * FIRST[<stat>] = {assign} U {print} U {read} U {while} U {if} U {{}
     *
     * P -> {S.next=newlabel()} SL {emitlabel(S.next)} eof
     */
    public void prog() {
        int lnext_prog = code.newLabel();

        switch (look.tag) {
            case Tag.ASSIGN, Tag.PRINT, Tag.READ, Tag.WHILE, Tag.IF, Tag.RPG:
                statlist(lnext_prog); // the label is utilized for JMP and conditional JMP
                code.emitLabel(lnext_prog);
                match(Tag.EOF);
                try {
                    code.toJasmin();
                } catch (java.io.IOException e) {
                    System.out.println("IO error\n");
                }
                break;

            default:
                error("Error in prog");
        }
    }

    /*
     * GUIDA[<statlist> := <stat><statlistp>] = FIRST[<stat>]
     * FIRST[<stat>] = {assign} U {print} U {read} U {while} U {if} U {{}
     *
     * SL -> S SLP {SL.code = S.code || SLP.code}
     */
    public void statlist(int lnext_prog) {
        switch (look.tag) {
            case Tag.ASSIGN, Tag.PRINT, Tag.READ, Tag.WHILE, Tag.IF, Tag.RPG:
                stat();
                statlistp();
                break;

            default:
                error("Error in statlist");
        }
    }

    public void statlistp() {
        switch (look.tag) {
            /*
             * GUIDA[<statlistp> := ;<stat><statlistp>] = FIRST[<statlistp>] = {;}
             *
             * SLP -> ; S SLP {SLP.code = S.code || I(0).code}
             */
            case Tag.SEM:
                match(Tag.SEM);
                stat();
                statlistp();
                break;

            /*
             * GUIDA[<statlistp>] := ε] = {EOF==-1} U {}}
             *
             * SLP -> ε {SLP.code = []}
             */
            case Tag.EOF, Tag.RPG:
                break;

            default:
                error("Error in statlistp");
        }

    }

    public void stat() {
        switch (look.tag) {
            /*
             * GUIDA[<stat> := assign<expr>to<idlist>] = {assign}
             *
             * S -> assign E to I {S.code = E.code || I(0).code}
             */
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(0); // pass 0 to identify the "assign" case
                break;

            /*
             * GUIDA[<stat> := print(<expr>)] = {print}
             *
             * S -> print ( EL ) {S.code = EL(1).code}
             */
            case Tag.PRINT:
                match(Tag.PRINT);
                match(Tag.LPT);
                exprlist(1); /*
                              * pass 1 to identify print, better to emit label in exprlist to manage cases
                              * like "print(a,b,c)"
                              */
                match(Tag.RPT);
                break;

            /*
             * GUIDA[<stat> := read(<expr>)] = {read}
             *
             * S -> read ( I ) {S.code = I(1).code}
             */
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist(1); // pass 1 to identify the "read" case
                match(')');
                break;

            /*
             * GUIDA[<stat> := while(<bexpr>)] = {while}
             *
             * S -> while ( B ) S {
             * S.true = newlabel()
             * S.false = newlabel()
             * S.start = newlabel()
             * S.code = emitlabel(S.start) ||
             * BP(S.true, S.false) ||
             * emitlabel(S.true) ||
             * S.code ||
             * emit(GOTO, S.start) ||
             * emitlabel(S.false)
             * }
             */
            case Tag.WHILE: {
                int while_true = code.newLabel();
                int while_false = code.newLabel();
                int while_start = code.newLabel();
                /*
                 * create while_start as first label so we can JMP to it after the code in the
                 * while is executed, label is emitted before while_true and while_false, while
                 * false is emitted after the while code (<stat>)
                 */
                code.emitLabel(while_start);

                match(Tag.WHILE);
                match('(');
                bexprp(while_true, while_false);
                code.emit(OpCode.GOto, while_false);
                // bexpr(while_true, while_false);
                match(')');

                code.emitLabel(while_true);

                stat();

                code.emit(OpCode.GOto, while_start);
                code.emitLabel(while_false);
                break;
            }

            /*
             * GUIDA[<stat> := if(<bexpr>)<stat><statp>] = {if}
             *
             * S -> if ( B ) S SP {
             * S.true = newlabel()
             * S.false = newlabel()
             * S.end = newlabel()
             * S.code = BP(S.true, S.false) ||
             * emitlabel(S.true) ||
             * S.code ||
             * SP(S.false, S.end).code
             * }
             */
            case Tag.IF: {
                int if_true = code.newLabel();
                int if_false = code.newLabel();
                int if_end = code.newLabel();

                /*
                 * if still to be worked on, label in wrong order, need to add goto to jmp
                 * if(false) condition instead of executing it after if(true)
                 */

                match(Tag.IF);
                match(Tag.LPT);

                bexprp(if_true, if_false);
                code.emit(OpCode.GOto, if_false);
                // bexpr(if_true, if_false);
                match(Tag.RPT);

                code.emitLabel(if_true);

                stat();
                statp(if_false, if_end);

                // we emit the label in statp() because of the two cases: end or else
                break;
            }

            /*
             * GUIDA[<stat> := {<statlist>}] = {{}
             *
             * {S.code = SL(0).code}
             */
            case '{':
                match(Tag.LPG);
                statlist(0); // not sure about that one
                match(Tag.RPG);
                break;

            default:
                error("Error in stat");
        }
    }

    public void statp(int if_false, int if_end) {
        switch (look.tag) {
            /*
             * GUIDA[<statp> := end] = {end}
             *
             * SP -> end {
             * SP.code = emit(GOTO, SP.end) ||
             * emitlabel(SP.false) ||
             * emitlabel(SP.end)
             * }
             */
            case Tag.END:
                code.emit(OpCode.GOto, if_end);
                match(Tag.END);
                code.emitLabel(if_false);
                code.emitLabel(if_end);
                break;

            /*
             * GUIDA[<statp> := else<stat>end] = {else}
             *
             * SP -> else S end {
             * SP.code = emit(GOTO, SP.end) ||
             * emitlabel(SP.false) ||
             * S.code ||
             * emitlabel(SP.end)
             * }
             */
            case Tag.ELSE: {
                code.emit(OpCode.GOto, if_end);
                code.emitLabel(if_false);

                match(Tag.ELSE);
                stat(); // S2
                match(Tag.END);

                code.emitLabel(if_end);

                break;
            }

            default:
                error("Error in statp()");
        }
    }

    /* GUIDA[<idlist> := ID<idlistp>] = {ID} */
    private void idlist(int read_assign) { // read 1, assign 0
        switch (look.tag) {
            case Tag.ID: {
                /*
                 * we check with lookupAddress if the ID is already present in our memory, if
                 * not (id_addr == -1), insert it as new ID
                 */
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }

                match(Tag.ID);

                if (read_assign == 1)
                    code.emit(OpCode.invokestatic, 0); // inkevestatic Output/read()I
                code.emit(OpCode.istore, id_addr);

                idlistp(read_assign);
                break;
            }

            default:
                error("Error in idlist");
        }
    }

    private void idlistp(int read_assign) {
        switch (look.tag) {
            /* GUIDA[<idlistp> := ,ID<idlistp>] = {,} */
            case ',': {
                match(Tag.COM);

                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    // if (read_assign == 0)
                    st.insert(((Word) look).lexeme, count++);
                    // error("Error in expr() : Identifier not defined: " + ((Word) look).lexeme);
                }
                match(Tag.ID);
                if (read_assign == 0) {
                    code.emit(OpCode.istore, id_addr);
                } else {
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, id_addr);
                }

                /*
                 * code.emit(OpCode.iload, id_addr);
                 * code.emit(OpCode.invokestatic, 1);
                 */

                idlistp(read_assign);
                break;
            }

            /*
             * GUIDA[<idlistp> := ε] = FOLLOW[<idlistp>]
             * FOLLOW[<idlistp>] = {EOF} U {;} U {}} U {end} U {else} U {)}
             */
            case Tag.EOF, Tag.SEM, Tag.RPG, Tag.END, Tag.ELSE, Tag.RPT:
                break;

            default:
                error("Error in idlistp");

        }
    }

    private void bexpr(int label_true, int label_false) {
        switch (look.tag) {
            /* GUIDA[<bexpr> := RELOP<expr><expr>] = {RELOP} */
            case Tag.RELOP: {
                String relop = ((Word) look).lexeme; // save relop value in a local variable because we need to match
                                                     // before the switch case
                match(Tag.RELOP);
                expr(); // we need to write expr1 and expr2 first
                expr();

                switch (relop) {
                    case "<" -> code.emit(OpCode.if_icmplt, label_true);
                    case ">" -> code.emit(OpCode.if_icmpgt, label_true);
                    case "==" -> code.emit(OpCode.if_icmpeq, label_true);
                    case "<=" -> code.emit(OpCode.if_icmple, label_true);
                    case "<>" -> code.emit(OpCode.if_icmpne, label_true);
                    case ">=" -> code.emit(OpCode.if_icmpge, label_true);
                    default -> error("Error in Word.java RELOP definition");
                }
                break;
            }

            case Tag.AND, Tag.OR, Tag.NOT:
                bexprp(label_true, label_false);
                break;

            default:
                error("Error in bexpr()");
        }
        // code.emit(OpCode.GOto, label_false);
    }

    private void bexprp(int label_true, int label_false) {
        switch (look.tag) {
            case Tag.AND: {
                int and_true = code.newLabel();

                match(Tag.AND);

                bexpr(and_true, label_false);
                code.emit(OpCode.GOto, label_false);
                code.emitLabel(and_true);
                bexpr(label_true, label_false);
                code.emit(OpCode.GOto, label_false);
                break;
            }

            case Tag.OR: {
                int or_false = code.newLabel();

                match(Tag.OR);

                bexprs(label_true, or_false);

                code.emitLabel(or_false);
                bexprs(label_true, label_false);

                break;
            }

            /* GUIDA[<bexprp> := NOT<bexpr><bexpr>] = {NOT} */
            case Tag.NOT:
                match(Tag.NOT);

                bexprs(label_false, label_true); // invert the labels so that the bexpr statement is inverted

                break;

            /* case epsilon */
            case Tag.RELOP:
                bexprs(label_true, label_false);

                break;

            case Tag.RPT:

                break;

            default:
                error("Error in bexprp");
        }
    }

    private void bexprs(int label_true, int label_false) {
        switch (look.tag) {
            /* GUIDA[<bexpr> := RELOP<expr><expr>] = {RELOP} */
            case Tag.RELOP: {
                String relop = ((Word) look).lexeme; // save relop value in a local variable because we need to match
                                                     // before the switch case
                match(Tag.RELOP);
                expr(); // we need to write expr1 and expr2 first
                expr();

                switch (relop) {
                    case "<" -> code.emit(OpCode.if_icmplt, label_true);
                    case ">" -> code.emit(OpCode.if_icmpgt, label_true);
                    case "==" -> code.emit(OpCode.if_icmpeq, label_true);
                    case "<=" -> code.emit(OpCode.if_icmple, label_true);
                    case "<>" -> code.emit(OpCode.if_icmpne, label_true);
                    case ">=" -> code.emit(OpCode.if_icmpge, label_true);
                    default -> error("Error in Word.java RELOP definition");
                }
                break;
            }

            case Tag.AND, Tag.OR, Tag.NOT:
                bexprp(label_true, label_false);
                break;

            default:
                error("Error in bexprs()");
        }

        code.emit(OpCode.GOto, label_false);
    }

    private void expr() {
        switch (look.tag) {
            /* GUIDA[<expr> := +(<exprlist>)] = {+} */
            case Tag.SUM: {
                match(Tag.SUM);
                match(Tag.LPT);
                exprlist(0); // pass 0 to identify sum
                match(Tag.RPT);

                code.emit(OpCode.iadd);
                break;
            }

            /* GUIDA[<expr> := -<expr><expr>] = {-} */
            case Tag.SUB:
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;

            /* GUIDA[<expr> := *(<exprlist>)] = {*} */
            case Tag.MUL:
                match(Tag.MUL);
                match(Tag.LPT);
                exprlist(2); // pass 2 to identify mul
                match(Tag.RPT);
                code.emit(OpCode.imul);
                break;

            /* GUIDA[<expr> := /<expr><expr>] = {/} */
            case Tag.DIV:
                match(Tag.DIV);
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;

            /* GUIDA[<expr> := NUM] = {NUM} */
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).value);

                match(Tag.NUM);
                break;

            /* GUIDA[<expr> := ID] = {ID} */
            case Tag.ID: {
                /* check if ID is was previously decalred, if not output an error */
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

    private void exprlist(int sum_print_mul) { /* 0==sum, 1==print, 2==mul */
        switch (look.tag) {
            /*
             * GUIDA[<exprlist> := <expr><exprlistp>] = FIRST[<expr>]
             * FIRST[<expr>] = {+} U {-} U {*} U {/} U {NUM} U {ID}
             */
            case '+', '-', '*', '/', Tag.NUM, Tag.ID:
                expr();

                if (sum_print_mul == 1)
                    code.emit(OpCode.invokestatic, 1); // invokestatic Output/print(I)V

                /* we still need to manage the case *(NUM) and +(NUM) */
                exprlistp(sum_print_mul);
                break;

            default:
                error("Error in exprlist");

        }
    }

    private void exprlistp(int invokestatic) {
        switch (look.tag) {
            /* GUIDA[<exprlistp> := ,<expr><exprlistp>] = {,} */
            case ',':
                match(Tag.COM);

                if (invokestatic == 1)
                    code.emit(OpCode.invokestatic, 1); // invokestatic Output/print(I)V

                expr();
                exprlistp(0); // need to check that
                break;

            /* GUIDA[<exprlistp> := ε] = FOLLOW[<exprlistp>] = {)} */
            case ')':
                break;

            default:
                error("Error in exprlistp");
        }
    }

    public static void main(String[] args) {
        Lexer2x3 lex = new Lexer2x3();
        String path = "src/File_Prova/and_or_moodle.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator5x3 translator = new Translator5x3(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
