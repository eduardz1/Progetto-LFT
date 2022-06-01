import java.util.LinkedList;
import java.io.*;

public class CodeGenerator {
    final LinkedList<Instruction> instructions = new LinkedList<>();
    int label = 0;

    public void emit(OpCode opCode) {
        instructions.add(new Instruction(opCode));
    }

    public void emit(OpCode opCode, int operand) {
        instructions.add(new Instruction(opCode, operand));
    }

    public void emitLabel(int operand) {
        emit(OpCode.label, operand);
    }

    public int newLabel() {
        return label++;
    }

    public void toJasmin() throws IOException { // v writes text to char files
        PrintWriter out = new PrintWriter(new FileWriter("Output.j"));
        // ^ prints a formatted representation of an object to a text-output stream
        StringBuilder temp = new StringBuilder();

        temp.append(header);

        while (instructions.size() > 0) {
            Instruction tmp = instructions.remove();
            temp.append(tmp.toJasmin());
        }

        temp.append(footer);

        out.println(temp); // prints temp followed by \n
        out.flush();
        out.close();
    }

    private static final String header = """
            .class public Output\s
            .super java/lang/Object

            .method public <init>()V
             aload_0
             invokenonvirtual java/lang/Object/<init>()V
             return
            .end method

            .method public static print(I)V
             .limit stack 2
             getstatic java/lang/System/out Ljava/io/PrintStream;
             iload_0\s
             invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
             invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
             return
            .end method

            .method public static read()I
             .limit stack 3
             new java/util/Scanner
             dup
             getstatic java/lang/System/in Ljava/io/InputStream;
             invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
             invokevirtual java/util/Scanner/next()Ljava/lang/String;
             invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
             ireturn
            .end method

            .method public static run()V
             .limit stack 1024
             .limit locals 256
            """;
    private static final String footer = """
             return
            .end method

            .method public static main([Ljava/lang/String;)V
             invokestatic Output/run()V
             return
            .end method
            """;
}
