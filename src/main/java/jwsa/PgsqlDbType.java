package jwsa;

public enum PgsqlDbType {
    Array(java.lang.Integer.MIN_VALUE),
    Bigint(1),
    Boolean(2),
    Box(3),
    Bytea(4),
    Circle(5),
    Char(6),
    Date(7),
    Double(8),
    Integer(9),
    Line(10),
    LSeg(11),
    Money(12),
    Numeric(13),
    Path(14),
    Point(15),
    Polygon(16),
    Real(17),
    Smallint(18),
    Text(19),
    Time(20),
    Timestamp(21),
    Varchar(22),
    Refcursor(23),
    Inet(24),
    Bit(25),
    TimestampTZ(26),
    Uuid(27),
    Xml(28),
    Oidvector(29),
    Interval(30),
    TimeTZ(31),
    Name(32),
    Abstime(33),
    MacAddr(34),
    Json(35),
    Jsonb(36),
    Hstore(37);

    final int value;

    PgsqlDbType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static PgsqlDbType getEnum(int value) {
        for (PgsqlDbType pgsqlDbType : PgsqlDbType.values()) {
            if (pgsqlDbType.getValue() == value) {
                return pgsqlDbType;
            }
        }
        return null;
    }
}
