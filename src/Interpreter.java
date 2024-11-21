/*
 * TODO
 *  MAKE THIS BETTER LOL
 */

public class Interpreter {

    public RuntimeVal evaluate(Stmt astNode) {
        if (astNode instanceof IntLiteral) {
            IntLiteral intLiteral = (IntLiteral)astNode;
            return new IntVal(intLiteral.getValue());
        } else if (astNode instanceof FloatLiteral) {
            FloatLiteral floatLiteral = (FloatLiteral)astNode;
            return new FloatVal(floatLiteral.getValue());
        } else if (astNode instanceof NullLiteral) {
            return new NullVal();
        } else if (astNode instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)astNode;
            return evaluateBinaryExpr(binaryExpr);
        } else if (astNode instanceof Program) {
            return evaluateProgram((Program)astNode);
        } else {
            System.err.println("Unknown AST Node: " + astNode.getKind());
            System.exit(1);
        }
        return null;
    }

    private RuntimeVal evaluateBinaryExpr(BinaryExpr binaryExpr) {
        RuntimeVal LHS = evaluate(binaryExpr.getLeft());
        RuntimeVal RHS = evaluate(binaryExpr.getRight());
        String operator = binaryExpr.getOperator();

        if (LHS instanceof IntVal && RHS instanceof IntVal) {
            return evaluateIntBinaryExpr((IntVal)LHS, (IntVal)RHS, operator);
        } else if (LHS instanceof FloatVal && RHS instanceof FloatVal) {
            return evaluateFloatBinaryExpr((FloatVal)LHS, (FloatVal)RHS, operator);
        } else if (LHS instanceof IntVal && RHS instanceof FloatVal) {
            return evaluateFloatBinaryExpr(new FloatVal(((IntVal)LHS).getValue()), (FloatVal)RHS, operator);
        } else if (LHS instanceof FloatVal && RHS instanceof IntVal) {
            return evaluateFloatBinaryExpr((FloatVal)LHS, new FloatVal(((IntVal)RHS).getValue()), operator);
        } else {
            System.err.println("Type Error: Unsupported operand types for " + operator + ": " +
                    LHS.getClass().getSimpleName() + " and " + RHS.getClass().getSimpleName());
            System.exit(1);
        }
        return null;
    }

    public RuntimeVal evaluateProgram(Program program) {
        RuntimeVal lastEvaluated = new NullVal();
        for (Stmt statement : program.getBody()) {
            lastEvaluated = evaluate(statement);
        }
        return lastEvaluated;
    }

    /* =================================================================== //
                          REPETITIVE EVALUATION BLOCKS
    // =================================================================== */
    public RuntimeVal evaluateIntBinaryExpr(IntVal LHS, IntVal RHS, String operator) {
        int result;
        switch (operator) {
            case "+":
                result = LHS.getValue() + RHS.getValue();
                break;
            case "-":
                result = LHS.getValue() - RHS.getValue();
                break;
            case "*":
                result = LHS.getValue() * RHS.getValue();
                break;
            case "/":
                if (RHS.getValue() == 0) {
                    System.err.println("Arithmetic Error: Division by zero ==> "
                            + LHS.getValue() + " / 0");
                    System.exit(1);
                }
                result = LHS.getValue() / RHS.getValue();
                break;
            case "%":
                result = LHS.getValue() % RHS.getValue();
                break;
            default:
                result = 0;
                break;
        }
        return new IntVal(result);
    }

    public FloatVal evaluateFloatBinaryExpr(FloatVal LHS, FloatVal RHS, String operator) {
        float result;
        switch (operator) {
            case "+":
                result = LHS.getValue() + RHS.getValue();
                break;
            case "-":
                result = LHS.getValue() - RHS.getValue();
                break;
            case "*":
                result = LHS.getValue() * RHS.getValue();
                break;
            case "/":
                if (RHS.getValue() == 0) {
                    System.err.println("Arithmetic Error: Division by zero. " + LHS.getValue() + " / 0");
                    System.exit(1);
                }
                result = LHS.getValue() / RHS.getValue();
                break;
            case "%":
                result = LHS.getValue() % RHS.getValue();
            default:
                result = 0;
                break;
        }
        return new FloatVal(result);
    }
    // =================================================================== */
}