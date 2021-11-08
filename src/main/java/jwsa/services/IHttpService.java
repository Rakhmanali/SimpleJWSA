package jwsa.services;

import jwsa.CompressionType;
import org.apache.hc.core5.http.HttpHost;

import java.io.IOException;

public interface IHttpService {
    String get(String requestUri, HttpHost proxy, CompressionType returnCompressionType) throws Exception;
    String post(String requestUri, String postData, HttpHost proxy, CompressionType outgoingCompressionType, CompressionType returnCompressionType) throws Exception;
}

