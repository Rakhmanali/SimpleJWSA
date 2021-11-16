package org.wsa.sjwsa;

import org.wsa.sjwsa.internal.Constants;
import org.apache.hc.core5.http.HttpHost;

public class SessionContext {
    public static final String route = "/BufferedMode/Service/";

    private static String restServiceAddress;
    private static String login;
    private static String password;
    private static boolean isEncrypted;
    private static int appId;
    private static String appVersion;
    private static String domain;
    private static HttpHost proxy;
    private static String token;

    public static void create(String rsa,
                              String l,
                              String pwd,
                              boolean ie,
                              int ai,
                              String av,
                              String d,
                              HttpHost p,
                              String t) {
        restServiceAddress = rsa;
        login = l;
        password = pwd;
        isEncrypted = ie;
        appId = ai;
        appVersion = av;
        domain = d;
        proxy = p;
        token = t;
    }

    public static String getRestServiceAddress() {
        return restServiceAddress;
    }

    public static String getLogin() {
        return login;
    }

    public static String getPassword() {
        return password;
    }

    public static boolean isIsEncrypted() {
        return isEncrypted;
    }

    public static int getAppId() {
        return appId;
    }

    public static String getAppVersion() {
        return appVersion;
    }

    public static String getDomain() {
        return domain;
    }

    public static HttpHost getProxy() {
        return proxy;
    }

    public static String getToken() {
        return token;
    }

    public static void refresh() throws Exception {
        String requestUri = SessionContext.route + Constants.WS_INITIALIZE_SESSION;
        SessionService sessionService = new SessionService(getRestServiceAddress(),
                requestUri,
                login,
                password,
                isEncrypted,
                appId,
                appVersion,
                domain,
                ErrorCodes.collection,
                proxy);
        token = sessionService.send(HttpMethod.GET);
    }
}
