import java.util.ArrayList;

public class Interpreter {
    public RuntimeVal evaluate(Stmt astNode, Environment env) {
        if (astNode instanceof IntLiteral) {
            IntLiteral intLiteral = (IntLiteral) astNode;
            return new IntVal(intLiteral.getValue());
        } else if (astNode instanceof FloatLiteral) {
            FloatLiteral floatLiteral = (FloatLiteral) astNode;
            return new FloatVal(floatLiteral.getValue());
        } else if (astNode instanceof NullLiteral) {
            return new NullVal();
        } else if (astNode instanceof BoolLiteral) {
            BoolLiteral boolLiteral = (BoolLiteral) astNode;
            return new BoolVal(boolLiteral.getValue());
        } else if (astNode instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr) astNode;
            return evaluateBinaryExpr(binaryExpr, env);
        } else if (astNode instanceof Program) {
            return evaluateProgram((Program) astNode, env);
        } else if (astNode instanceof Identifier) {
            return evalIdentifier((Identifier) astNode, env);
        } else if (astNode instanceof VariableDeclaration) {
            return evalVarDeclaration((VariableDeclaration) astNode, env);
        } else if (astNode instanceof AssignmentExpr) {
            return evalAssignment((AssignmentExpr) astNode, env);
        } else if (astNode instanceof ArrayLiteral) {
            return evalArrayExpr((ArrayLiteral) astNode, env);
        } else {
            System.err.println("Unknown AST Node: " + astNode.getKind());
            System.exit(1);
        }
        return null;
    }

    int variable;

    public RuntimeVal evaluateProgram(Program program, Environment env) {
        RuntimeVal lastEvaluated = new NullVal();
        for (Stmt statement : program.getBody()) {
            lastEvaluated = evaluate(statement, env);
        }
        return lastEvaluated;
    }

    public RuntimeVal evalVarDeclaration(VariableDeclaration declaration, Environment env) {
        RuntimeVal value = (declaration.getValue() != null) ? evaluate(declaration.getValue(), env) : new NullVal();
        return env.declareVar(declaration.identifierToString(), value, declaration.isConstant());
    }

    public RuntimeVal evalArrayExpr(ArrayLiteral obj, Environment env) {
        ArrayVal arr = new ArrayVal(new ArrayList<>());

        for (Expr value : obj.getValues()) {
            RuntimeVal runtimeVal = evaluate(value, env);
            arr.values.add(runtimeVal);
        }

        return arr;
    }

    public RuntimeVal evalAssignment(AssignmentExpr node, Environment env) {
        if (node.getAssignee().getKind() != NodeType.IDENTIFIER) {
            System.err.println("Error: Invalid LHS inside assignment expression " + node.getAssignee());
            System.exit(1);
        }

        String varName = ((Identifier)(node.getAssignee())).getSymbol();
        return env.assignVar(varName, evaluate(node.getValue(), env));
    }
    public RuntimeVal evaluateBinaryExpr(BinaryExpr binaryExpr, Environment env) {
        RuntimeVal LHS = evaluate(binaryExpr.getLeft(), env);
        RuntimeVal RHS = evaluate(binaryExpr.getRight(), env);
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
            System.err.println("Type Error: Unsupported operand types for \"" + operator + "\": " +
                    LHS.getClass().getSimpleName() + " and " + RHS.getClass().getSimpleName());
            System.exit(1);
        }
        return null;
    }

    public RuntimeVal evalIdentifier(Identifier ident, Environment env) {
        return env.lookupVar(ident.getSymbol());
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