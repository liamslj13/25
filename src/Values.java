import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

enum ValueType {
    NULL,
    INT,
    FLOAT,
    BOOL,
    OBJ,
    ARRAY,
}

abstract class RuntimeVal {
    private final ValueType type;

    public RuntimeVal(ValueType type) {
        this.type = type;
    }

    public ValueType getType() {
        return type;
    }
}

class ArrayVal extends RuntimeVal {
    ArrayList<RuntimeVal> values;

    public ArrayVal(ArrayList<RuntimeVal> values) {
        super(ValueType.ARRAY);
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder arrToString = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            arrToString.append(values.get(i));
            if (i < values.size() - 1) {
                arrToString.append(", ");
            }
        }
        return "[" + arrToString + "]";
    }
}

class ObjectVal extends RuntimeVal {
    Map<String, RuntimeVal> values;

    public ObjectVal(Map<String, RuntimeVal> values) {
        super(ValueType.OBJ);
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder objToString = new StringBuilder();
        objToString.append("{");
        int i = 0;
        for (Map.Entry<String, RuntimeVal> entry : values.entrySet()) {
            objToString.append(entry.getKey()).append(": ").append(entry.getValue().toString());
            if (i < values.size() - 1) {
                objToString.append(", ");
            }
            i++;
        }
        objToString.append("}");
        return objToString.toString();
    }
}



class NullVal extends RuntimeVal {
    public NullVal() {
        super(ValueType.NULL);
    }

    @Override
    public String toString() {
        return "null";
    }
}

class BoolVal extends RuntimeVal {
    //Value will default to false
    private boolean value = false;

    public BoolVal() {
        super(ValueType.BOOL);
    }
    public BoolVal(boolean value) {
        super(ValueType.BOOL);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Boolean: " + value;
    }
}


class IntVal extends RuntimeVal {
    private final int value;

    public IntVal(int value) {
        super(ValueType.INT);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public ValueType getType() {
        return ValueType.INT;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}

class FloatVal extends RuntimeVal {
    private float value;

    public FloatVal(float value) {
        super(ValueType.FLOAT);
        this.value = value;
    }

    public float getValue() {
        return this.value;
    }

    @Override
    public ValueType getType() {
        return ValueType.FLOAT;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}