package jwsa;

public class Parameter {
    private String name;
    private Object value;
    private PgsqlDbType pgsqlDbType;
    private boolean isArray = false;

    public Parameter(String name) {
        this.name = name;
    }

    public Parameter(String name, PgsqlDbType pgsqlDbType) {
        this.name = name;
        this.pgsqlDbType = pgsqlDbType;
    }

    public Parameter(String name, PgsqlDbType pgsqlDbType, Object value) {
        this.name = name;
        this.pgsqlDbType = pgsqlDbType;
        this.value = value;
    }

    public Parameter(String name, PgsqlDbType pgsqlDbType,  Object value, boolean isArray) {
        this.name = name;
        this.pgsqlDbType = pgsqlDbType;
        this.value = value;
        this.isArray = isArray;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public PgsqlDbType getPgsqlDbType() {
        return pgsqlDbType;
    }

    public boolean isArray() {
        return isArray;
    }
}
