package jwsa;

import jwsa.internal.Constants;
import jwsa.services.HttpMethod;
import org.apache.hc.core5.http.HttpHost;

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
        this.proxy=proxy;
    }

    public String createByRestServiceAddress(String restServiceAddress)
    {
        String requestUri = SessionContext.route+ Constants.WS_INITIALIZE_SESSION;
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
}
