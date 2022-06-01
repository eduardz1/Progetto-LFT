public class Instruction {
    final OpCode opCode;
    int operand;

    public Instruction(OpCode opCode) {
        this.opCode = opCode;
    }

    public Instruction(OpCode opCode, int operand) {
        this.opCode = opCode;
        this.operand = operand;
    }

    public String toJasmin() {
        String temp = "";
        switch (opCode) {
            case invokestatic -> temp = (operand == 1) ? 
                                      " invokestatic " + "Output/print(I)V" + "\n" : 
                                      " invokestatic " + "Output/read()I" + "\n";
                                                       
            case ldc       -> temp = " ldc " + operand + "\n";
            case iadd      -> temp = " iadd " + "\n";
            case imul      -> temp = " imul " + "\n";
            case idiv      -> temp = " idiv " + "\n";
            case isub      -> temp = " isub " + "\n";
            case ineg      -> temp = " ineg " + "\n";
            case istore    -> temp = " istore " + operand + "\n";
            case ior       -> temp = " ior " + "\n";
            case iand      -> temp = " iand " + "\n";
            case iload     -> temp = " iload " + operand + "\n";
            case if_icmpeq -> temp = " if_icmpeq L" + operand + "\n";
            case if_icmple -> temp = " if_icmple L" + operand + "\n";
            case if_icmplt -> temp = " if_icmplt L" + operand + "\n";
            case if_icmpne -> temp = " if_icmpne L" + operand + "\n";
            case if_icmpge -> temp = " if_icmpge L" + operand + "\n";
            case if_icmpgt -> temp = " if_icmpgt L" + operand + "\n";
            case ifne      -> temp = " ifne L" + operand + "\n";
            case GOto      -> temp = " goto L" + operand + "\n";
            case label     -> temp = "L" + operand + ":\n";
        }
        return temp;
    }
}
