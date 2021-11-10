package jwsa.services;

import jwsa.EncodingType;
import jwsa.PgsqlDbType;

public interface IConvertingService {
    Object[] convertObjectToDb(PgsqlDbType pgsqlDbType, boolean isArray, Object value, EncodingType outgoingEncodingType);
    String encodeTo(Object value, EncodingType outgoingEncodingType);
}
