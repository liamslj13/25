import java.util.Scanner;

public class repl {
    public static void main(String[] args) {
        Scanner kbd = new Scanner(System.in);
        Parser parser = new Parser();
        Interpreter interpreter = new Interpreter();
        Environment env = new Environment();
        System.out.println("25 Repl v0.1");
        System.out.println("Input \"EXIT\" to stop the REPL.");

        while (true) {
            System.out.print("> ");
            String input = kbd.nextLine();
            if (input.isEmpty() || input.equals("EXIT")) {
                System.exit(1);
            }
            Program program = parser.produceAST(input);
            RuntimeVal result = interpreter.evaluate(program, env);
            System.out.println(result);
        }
    }
}
