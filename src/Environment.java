import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment {

    private final Environment parent;
    private final Map<String, RuntimeVal> variables;
    private final Set<String> constants;

    public Environment() {
        this(null);
    }

    public Environment(Environment parentENV) {
        this.parent = parentENV;
        this.variables = new HashMap<>();
        this.constants = new HashSet<>();
    }

    public RuntimeVal declareVar(String varName, RuntimeVal value, boolean constant) {
        if (this.variables.containsKey(varName)) {
            System.err.println("Cannot declare variable with name: "
                    + varName + ". It is already defined.");
            System.exit(1);
        }

        this.variables.put(varName, value);
        if (constant) {
            this.constants.add(varName);
        }
        return value;
    }

    public RuntimeVal lookupVar(String varName) {
        Environment env = this.resolve(varName);
        return env.variables.get(varName);
    }

    public RuntimeVal assignVar(String varName, RuntimeVal value) {
        Environment env = this.resolve(varName);

        if (env.constants.contains(varName)) {
            System.err.println("Error: Unable to reassign variable " + varName + "; variable a constant value.");
            System.exit(1);
        }

        env.variables.put(varName, value);
        return value;
    }

    public Environment resolve(String varName) {
        if (this.variables.containsKey(varName))
            return this;

        if (this.parent == null) {
            System.err.println("Could not resolve " + varName + "; a variable with this name does not exist.");
            System.exit(1);
        }
        return this.parent.resolve(varName);
    }
}