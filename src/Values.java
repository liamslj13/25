enum ValueType {
    NULL,
    INT,
    FLOAT,
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

class NullVal extends RuntimeVal {
    public NullVal() {
        super(ValueType.NULL);
    }

    @Override
    public String toString() {
        return "null";
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