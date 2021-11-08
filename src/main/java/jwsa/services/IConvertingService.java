package jwsa.services;

import jwsa.EncodingType;
import jwsa.PgsqlDbType;

public interface IConvertingService {
    Object[] ConvertObjectToDb(PgsqlDbType pgsqlDbType, boolean isArray, Object value, EncodingType outgoingEncodingType);
    String EncodeTo(Object value, EncodingType outgoingEncodingType);
}
