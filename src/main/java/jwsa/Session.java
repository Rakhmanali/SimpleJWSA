package jwsa;

import jwsa.internal.Constants;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.net.URL;

public class Session {
    private final String login;
    private final String password;
    private final boolean isEncrypted;
    private final int appId;
    private final String appVersion;
    private final String domain;
    private final HttpHost proxy;

    public Session(String login, String password, boolean isEncrypted, int appId, String appVersion, String domain, HttpHost proxy) {
        this.login = login;
        this.password = password;
        this.isEncrypted = isEncrypted;
        this.appId = appId;
        this.appVersion = appVersion;
        this.domain = domain;
        this.proxy = proxy;
    }

    public String createByRestServiceAddress(String restServiceAddress) throws Exception {
        String requestUri = SessionContext.route + Constants.WS_INITIALIZE_SESSION;
        SessionService sessionService = new SessionService(restServiceAddress,
                requestUri,
                this.login,
                this.password,
                this.isEncrypted,
                this.appId,
                this.appVersion,
                this.domain,
                ErrorCodes.collection,
                this.proxy);
        String token = sessionService.send(HttpMethod.GET);
        SessionContext.create(restServiceAddress, this.login, this.password, this.isEncrypted, this.appId, this.appVersion, this.domain, this.proxy, token);
        return token;
    }

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

    private String getRestServiceAddress(String domain, String connectionProviderAddress, HttpHost proxy) throws Exception {
        if (domain == null || domain.trim().length() == 0) {
            throw new IllegalArgumentException("domain is invalid");
        }

        if (connectionProviderAddress == null || connectionProviderAddress.trim().length() == 0) {
            throw new IllegalArgumentException("connectionProviderAddress is invalid");
        }

        var connectionManager = this.GetPoolingHttpClientConnectionManager();
        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build()) {

            String requestUri = connectionProviderAddress + "/dataaccess/" + domain + "/restservice/address";
            var httpGet = new HttpGet(requestUri);

            if (proxy != null) {
                RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                httpGet.setConfig(config);
            }

            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                int httpCode = httpResponse.getCode();
                if (httpCode == HttpStatus.SC_OK) {
                    final HttpEntity entity = httpResponse.getEntity();
                    try {
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } catch (final ParseException ex) {
                        throw new ClientProtocolException(ex);
                    }
                } else {
                    var responsePhrase = httpResponse.getReasonPhrase();
                    throw new Exception(String.format("HTTP Code: %d, message: %s", httpCode, responsePhrase));
                }
            }
        }
    }

    public String createByConnectionProviderAddress(String connectionProviderAddress) throws Exception {
        if (connectionProviderAddress == null || connectionProviderAddress.trim().length() == 0) {
            throw new IllegalArgumentException("connectionProviderAddress is invalid");
        }

        String restServiceAddress = this.getRestServiceAddress(this.domain, connectionProviderAddress, this.proxy);
        URL url = new URL(restServiceAddress);
        String baseAddress = url.getProtocol() + "://" + url.getAuthority();
        return this.createByRestServiceAddress(baseAddress);
    }
}
