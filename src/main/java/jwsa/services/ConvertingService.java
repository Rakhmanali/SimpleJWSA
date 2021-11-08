package jwsa.services;

import jwsa.EncodingType;
import jwsa.PgsqlDbType;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConvertingService implements IConvertingService {

    private Object ConvertScalarObjectToDb(PgsqlDbType pgsqlDbType, Object value, EncodingType outgoingEncodingType) {
        if (value == null) {
            return null;
        }

        Object result;

        switch (pgsqlDbType) {
            case Time: {
                java.time.LocalTime localTime = (java.time.LocalTime) value;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                result = localTime.format(dateTimeFormatter);
                break;
            }
            case TimeTZ: {
                OffsetTime offsetTime = (OffsetTime) value;
                result = offsetTime.toString();
                break;
            }
            case Timestamp: {
                LocalDateTime localDateTime = (LocalDateTime) value;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                result = localDateTime.format(dateTimeFormatter);
                break;
            }
            case TimestampTZ: {
                OffsetDateTime offsetDateTime = (OffsetDateTime) value;
                result = offsetDateTime.toString();
                break;
            }
            case Date: {
                LocalDate localDate = (LocalDate) value;
                result = localDate.toString();
                break;
            }
            case Bigint: {
                result = (long) value;
                break;
            }
            case Smallint: {
                result = (short) value;
                break;
            }
            case Integer: {
                result = (int) value;
                break;
            }
            case Real: {
                result = (float) value;
                break;
            }
            case Double: {
                result = (double) value;
                break;
            }
            case Money:
            case Numeric: {
                result = (BigDecimal) value;
                break;
            }
            case Varchar:
            case Text:
            case Xml:
            case Json:
            case Jsonb: {
                //string t = Convert.ToString(value, CultureInfo.InvariantCulture).SparseString();
                String t = (String) value;
                if (t.isEmpty() == false && t.isBlank() == false && outgoingEncodingType == EncodingType.NONE) {
                    t = "<![CDATA[" + t + "]]>";
                }
                result = t;

                break;
            }
            case Bytea: {
                result = Base64.getEncoder().encode((byte[]) value);
                break;
            }
            default: {
                result = value;
                break;
            }
        }

        return result;
    }

    public Object[] ConvertObjectToDb(PgsqlDbType pgsqlDbType, boolean isArray, Object value, EncodingType outgoingEncodingType) {
        if (value == null) {
            return null;
        }

        Object[] result;

        if (isArray == false) {
            result = new Object[1];
            result[0] = this.ConvertScalarObjectToDb(pgsqlDbType, value, outgoingEncodingType);
            return result;
        }

        int arrayLength = Array.getLength(value);
        result = new Object[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            result[i] = Array.get(value, i);
        }

        if (result.length > 0) {
            for (int i = 0; i < result.length; i++) {
                if (result[i] != null) {
                    result[i] = this.ConvertScalarObjectToDb(pgsqlDbType, result[i], outgoingEncodingType);
                }
            }
        }

        return result;
    }

    public String EncodeTo(Object value, EncodingType outgoingEncodingType) {
        if (value == null) {
            value = "null";
        }

        String result = String.valueOf(value);

        switch (outgoingEncodingType) {
            case BASE64: {
                result = Base64.getEncoder().encodeToString(result.getBytes(StandardCharsets.UTF_8));
                break;
            }
            case NONE:
            default: {
                break;
            }
        }

        return result;
    }
}
