import java.util.List;

//Base statement class
abstract class Stmt {
    private final NodeType kind;

    public Stmt(NodeType kind) {
        this.kind = kind;
    }

    public NodeType getKind() {
        return kind;
    }
}

class VariableDeclaration extends Stmt {
    private final boolean constant;
    private final Identifier identifier;
    private final Expr value;

    public VariableDeclaration(boolean constant, Identifier identifier, Expr value) {
        super(NodeType.VARIABLEDECLARATION);
        this.constant = constant;
        this.identifier = identifier;
        this.value = value;
    }

    public boolean isConstant() {
        return constant;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public Expr getValue() {
        return value;
    }
}

class FuncDeclaration extends Stmt {
    private final List<String> parameters;
    private final String name;
    private final List<String> body;

    public FuncDeclaration(List<String> parameters, String name, List<String> body) {
        super(NodeType.FUNCDECLARATION);
        this.parameters = parameters;
        this.name = name;
        this.body = body;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public String getName() {
        return name;
    }

    public List<String> getBody() {
        return body;
    }
}

class IfStatement extends Stmt {
    private final Expr test;
    private final List<Stmt> body;
    private final List<Stmt> alternate; //Can take a null value if no further conditionals

    public IfStatement(Expr test, List<Stmt> body, List<Stmt> alternate) {
        super(NodeType.IFSTATEMENT);
        this.test = test;
        this.body = body;
        this.alternate = alternate;
    }
}

class TryCatchStatement extends Stmt {
    private final List<Stmt> body;
    private final List<Stmt> alternate; //This one cannot be null

    public TryCatchStatement(List<Stmt> body,List<Stmt> alternate) {
        super(NodeType.TRYCATCHSTATEMENT);
        this.body = body;
        this.alternate = alternate;
    }

    public List<Stmt> getBody() {
        return body;
    }

    public List<Stmt> getAlternate() {
        return alternate;
    }

    @Override
    public String toString() {
        return "TryCatch Statement: \nMain body: \n" +
                body + "\nAlternate body: \n" + alternate;
    }
}

class ForStatement extends Stmt {
    private final VariableDeclaration init;
    private final Expr test;
    private final AssignmentExpr update;
    private final List<Stmt> body;

    public ForStatement(VariableDeclaration init, Expr test, AssignmentExpr update, List<Stmt> body) {
        super(NodeType.FORSTATEMENT);
        this.init = init;
        this.test = test;
        this.update = update;
        this.body = body;
    }

    public List<Stmt> getBody() {
        return body;
    }
}

class WhileStatement extends Stmt {
    private final Expr test;
    private final List<Stmt> body;

    public WhileStatement(Expr test, List<Stmt> body) {
        super(NodeType.WHILESTATEMENT);
        this.test = test;
        this.body = body;
    }

    public List<Stmt> getBody() {
        return body;
    }
}

// =================================================================== //
/*
 * Program is created to hold many statements.
 */
class Program extends Stmt {
    private final List<Stmt> body;

    public Program(List<Stmt> body) {
        super(NodeType.PROGRAM);
        this.body = body;
    }

    public List<Stmt> getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Program: \n");
        for (Stmt stmt : body) {
            sb.append(stmt.toString()).append("\n");
        }
        return sb.toString();
    }
}
// =================================================================== //

//Base expression class
abstract class Expr extends Stmt {
    public Expr(NodeType kind) {
        super(kind);
    }
}

class AssignmentExpr extends Expr {
    private final Expr assignee;
    private final Expr value;

    public AssignmentExpr(Expr assignee, Expr value) {
        super(NodeType.ASSIGNMENTEXPR);
        this.assignee = assignee;
        this.value = value;
    }

    @Override
    public String toString() {
        return "assignee: " + assignee + ", value: " + value;
    }
}

class BinaryExpr extends Expr {
    private final Expr left;
    private final Expr right;
    private final String operator;

    public BinaryExpr(Expr left, Expr right, String operator) {
        super(NodeType.BINARYEXPR);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight() {
        return right;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "Left: (" + left + "), right: (" + right + "), operator: (" + operator + ")";
    }
}

class CallExpr extends Expr {
    private final List<Expr> args;
    private final Expr caller;

    public CallExpr(List<Expr> args, Expr caller) {
        super(NodeType.CALLEXPR);
        this.args = args;
        this.caller = caller;
    }

    public List<Expr> getArgs() {
        return args;
    }

    public Expr getCaller() {
        return caller;
    }

    @Override
    public String toString() {
        return "CallExpr, args: " + args + ", caller: " + caller;
    }
}

class MemberExpr extends Expr {
    private final Expr object;
    private final Expr property;
    private final boolean computed;

    public MemberExpr(Expr object, Expr property, boolean computed) {
        super(NodeType.MEMBEREXPR);
        this.object = object;
        this.property = property;
        this.computed = computed;
    }

    public Expr getObject() {
        return object;
    }

    public Expr getProperty() {
        return property;
    }

    public boolean isComputed() {
        return computed;
    }

    @Override
    public String toString() {
        return "MemberExpr, object: " + object + ", property: " + property + ", is computed? " + computed;
    }
}

//Base identifier class
class Identifier extends Expr {
    private final String symbol;

    public Identifier(String symbol) {
        super(NodeType.IDENTIFIER);
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Identifier: " + symbol;
    }
}

class IntLiteral extends Expr {
    private final int value;

    public IntLiteral(int value) {
        super(NodeType.INTLITERAL);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IntLiteral: " + value;
    }
}

class FloatLiteral extends Expr {
    private final float value;

    public FloatLiteral(float value) {
        super(NodeType.INTLITERAL);
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "FloatLiteral: " + value;
    }
}

// =================================================================== //
record Property(String key, Expr value) {}
// =================================================================== //

class ObjLiteral extends Expr {
    private final List<Property> properties;

    public ObjLiteral(List<Property> properties) {
        super(NodeType.OBJLITERAL);
        this.properties = properties;
    }

    public List<Property> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "ArrayLiteral: " + properties;
    }
}

class ArrayLiteral extends Expr {
    private final List<Expr> values;

    public ArrayLiteral(List<Expr> values) {
        super(NodeType.ARRAYLITERAL);
        this.values = values;
    }

    public List<Expr> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "ArrayLiteral: " + values;
    }
}

class StringLiteral extends Expr {
    private final String value;

    public StringLiteral(String value) {
        super(NodeType.STRINGLITERAL);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringLiteral: " + value;
    }
}

class NullLiteral extends Expr {
    private final String value;

    public NullLiteral(String value) {
        super(NodeType.NULLLITERAL);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NullLiteral: " + value;
    }
}