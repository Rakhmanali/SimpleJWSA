package org.wsa.sjwsa.services;

import org.wsa.sjwsa.EncodingType;
import org.wsa.sjwsa.PgsqlDbType;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class ConvertingService implements IConvertingService {

    private Object convertScalarObjectToDb(PgsqlDbType pgsqlDbType, Object value, EncodingType outgoingEncodingType) {
        if (value == null) {
            return null;
        }

        Object result;

        switch (pgsqlDbType) {
            case Time: {
                java.time.LocalTime localTime = (LocalTime) value;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
                result = localTime.format(dateTimeFormatter);
                break;
            }
            case TimeTZ: {
                OffsetTime offsetTime = (OffsetTime) value;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_TIME;
                result = offsetTime.format(dateTimeFormatter);
                break;
            }
            case Timestamp: {
                LocalDateTime localDateTime = (LocalDateTime) value;

                // new web service uses the npgsql version 5 and higher
                // and the following DateTimeFormatter is relevant
                //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                // the current version of the web service uses the npgsql version 2.2.7 and
                // the DataTimeFormatter created like the following is right yet
                DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .append(DateTimeFormatter.ISO_LOCAL_DATE)
                        .appendLiteral(' ')
                        .append(DateTimeFormatter.ISO_LOCAL_TIME)
                        .toFormatter();

                result = localDateTime.format(dateTimeFormatter);
                break;
            }
            case TimestampTZ: {
                OffsetDateTime offsetDateTime = (OffsetDateTime) value;

                // new web service uses the npgsql version 5 and higher
                // and the following DateTimeFormatter is relevant
                // DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

                // the current version of the web service uses the npgsql version 2.2.7 and
                // the DataTimeFormatter created like the following is right yet
                DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .append(DateTimeFormatter.ISO_LOCAL_DATE)
                        .appendLiteral(' ')
                        .append(DateTimeFormatter.ISO_LOCAL_TIME)
                        .parseLenient()
                        .appendOffsetId()
                        .parseStrict()
                        .toFormatter();

                result = offsetDateTime.format(dateTimeFormatter);
                break;
            }
            case Date: {
                LocalDate localDate = (LocalDate) value;
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
                result = localDate.format(dateTimeFormatter);
                break;
            }
            case Bigint: {
                result = Long.parseLong(value.toString());
                break;
            }
            case Smallint: {
                result = Short.parseShort(value.toString());
                break;
            }
            case Integer: {
                result = Integer.parseInt(value.toString());
                break;
            }
            case Real: {
                result = Float.parseFloat(value.toString());
                break;
            }
            case Double: {
                result = Double.parseDouble(value.toString());
                break;
            }
            case Money:
            case Numeric: {
                result = new BigDecimal(value.toString());
                break;
            }
            case Varchar:
            case Text:
            case Xml:
            case Json:
            case Jsonb: {

                // the plain text is going to be put to the CDATA section
                // in the writeArguments(...) method of the Request class
                result = value.toString();

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


    public Object[] convertObjectToDb(PgsqlDbType pgsqlDbType, boolean isArray, Object value, EncodingType outgoingEncodingType) {
        if (value == null) {
            return null;
        }

        Object[] result;

        if (isArray == false) {
            result = new Object[1];
            result[0] = this.convertScalarObjectToDb(pgsqlDbType, value, outgoingEncodingType);
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
                    result[i] = this.convertScalarObjectToDb(pgsqlDbType, result[i], outgoingEncodingType);
                }
            }
        }

        return result;
    }

    public String encodeTo(Object value, EncodingType outgoingEncodingType) {
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
