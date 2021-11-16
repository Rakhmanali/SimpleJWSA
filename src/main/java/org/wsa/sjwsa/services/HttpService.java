package org.wsa.sjwsa.services;

import com.google.gson.Gson;
import org.wsa.sjwsa.CompressionType;
import org.wsa.sjwsa.ErrorCodes;
import org.wsa.sjwsa.exceptions.RestServiceException;
import org.wsa.sjwsa.internal.ErrorReply;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;


// Examples and Recipes
// https://openjdk.java.net/groups/net/httpclient/recipes.html

// HttpClient Examples (Classic)
// https://hc.apache.org/httpcomponents-client-5.1.x/examples.html#

// Apache HttpClient Tutorial
// https://www.tutorialspoint.com/apache_httpclient/index.htm

public class HttpService implements IHttpService {

    final ICompressionService compressionService = new CompressionService();

    // get() and post() methods works properly without this function, but when proxy is defined, for
    // example, like HttpHost("127.0.0.1", 8888) they rise an error.

    // How to handle invalid SSL certificates with Apache HttpClient?
    // https://stackoverflow.com/questions/1828775/how-to-handle-invalid-ssl-certificates-with-apache-httpclient
    private PoolingHttpClientConnectionManager GetPoolingHttpClientConnectionManager() throws Exception {
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial((chain, authType) -> true).build();

        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslContext, new String[]
                        {/*"SSLv2Hello",*/ "SSLv3", "TLSv1",/*"TLSv1.1",*/ "TLSv1.2"}, null,
                        NoopHostnameVerifier.INSTANCE);

        return new PoolingHttpClientConnectionManager(RegistryBuilder.
                <ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory).build());
    }

    public String get(String requestUri, HttpHost proxy, CompressionType returnCompressionType) throws Exception {
        var connectionManager = this.GetPoolingHttpClientConnectionManager();
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build()) {

            var httpGet = new HttpGet(requestUri);

            if (proxy != null) {
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                httpGet.setConfig(config);
            }

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                int httpCode = httpResponse.getCode();
                if (httpCode == HttpStatus.SC_OK) {
                    final HttpEntity entity = httpResponse.getEntity();
                    if (entity == null) {
                        return null;
                    }
                    try {
                        if (returnCompressionType == CompressionType.NONE) {
                            return EntityUtils.toString(entity);
                        } else {
                            byte[] bytes = this.compressionService.decompress(EntityUtils.toByteArray(entity), returnCompressionType);
                            return new String(bytes, StandardCharsets.UTF_8);
                        }
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    var responsePhrase = httpResponse.getReasonPhrase();
                    this.createAndThrowIfRestServiceException(responsePhrase);
                    throw new Exception(String.format("HTTP Code: %d, message: %s", httpCode, responsePhrase));
                }
            }
        }
    }

    public String post(String requestUri, String postData, HttpHost proxy, CompressionType outgoingCompressionType, CompressionType returnCompressionType) throws Exception {
        var connectionManager = this.GetPoolingHttpClientConnectionManager();
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build()) {
            final var httpPost = new HttpPost(requestUri);

            if (proxy != null) {
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                httpPost.setConfig(config);
            }

            if (outgoingCompressionType == CompressionType.NONE) {
                StringEntity stringEntity = new StringEntity(postData, ContentType.TEXT_XML);
                httpPost.setEntity(stringEntity);
            } else {
                byte[] bytes = this.compressionService.compress(postData, outgoingCompressionType);
                ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes, ContentType.DEFAULT_BINARY);
                httpPost.setEntity(byteArrayEntity);
            }


            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                int httpCode = httpResponse.getCode();
                if (httpCode == HttpStatus.SC_OK) {
                    final HttpEntity entity = httpResponse.getEntity();
                    if (entity == null) {
                        return null;
                    }
                    try {
                        if (returnCompressionType == CompressionType.NONE) {
                            return EntityUtils.toString(entity);
                        } else {
                            byte[] bytes = this.compressionService.decompress(EntityUtils.toByteArray(entity), returnCompressionType);
                            return new String(bytes, StandardCharsets.UTF_8);
                        }
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    var responsePhrase = httpResponse.getReasonPhrase();
                    this.createAndThrowIfRestServiceException(responsePhrase);
                    throw new Exception(String.format("HTTP Code: %d, message: %s", httpCode, responsePhrase));
                }
            }
        }
    }

    protected void createAndThrowIfRestServiceException(String source) throws RestServiceException {
        if (source != null && source.isEmpty() != true && source.isBlank() != true) {
            Gson gson = new Gson();
            ErrorReply errorReply = gson.fromJson(source, ErrorReply.class);
            if (errorReply != null) {
                String wsaMessage = null;
                String code = errorReply.getError().getCode();
                if (ErrorCodes.collection.containsKey(code)) {
                    wsaMessage = ErrorCodes.collection.get(code);
                }
                throw new RestServiceException(wsaMessage, code, errorReply.getError().getMessage());
            }
        }
    }
}



