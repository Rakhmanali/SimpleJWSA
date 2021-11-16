package org.wsa.sjwsa.services;

import org.wsa.sjwsa.EncodingType;
import org.wsa.sjwsa.PgsqlDbType;

public interface IConvertingService {
    Object[] convertObjectToDb(PgsqlDbType pgsqlDbType, boolean isArray, Object value, EncodingType outgoingEncodingType);
    String encodeTo(Object value, EncodingType outgoingEncodingType);
}
