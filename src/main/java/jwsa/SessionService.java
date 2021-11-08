package jwsa;

import jwsa.internal.Constants;
import jwsa.services.HttpMethod;
import jwsa.services.HttpService;
import jwsa.services.IHttpService;
import org.apache.hc.core5.http.HttpHost;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class SessionService {
    private final String baseAddress;
    private final String requestUri;
    private final String login;
    private final String password;
    private final boolean isEncrypted;
    private final int appId;
    private final String appVersion;
    private final String domain;
    private final Map<String, String> errorCodes;
    private final HttpHost proxy;

    public SessionService(String baseAddress, String requestUri, String login, String password, boolean isEncrypted, int appId, String appVersion, String domain, Map<String, String> errorCodes, HttpHost proxy) {
        this.baseAddress = baseAddress;
        this.requestUri = requestUri;
        this.login = login;
        this.password = password;
        this.isEncrypted = isEncrypted;
        this.appId = appId;
        this.appVersion = appVersion;
        this.domain = domain;
        this.errorCodes = errorCodes;
        this.proxy = proxy;
    }



    /*
     <_routines>
       <_routine>
         <_name>InitializeSession</_name>
         <_arguments>
           <login isEncoded="1">c2FkbWluQHVwc3RhaXJzLmNvbQ==</login>
           <password isEncoded="1">R3JvbWl0MTI=</password>
           <isEncrypt>0</isEncrypt>
           <timeout>20</timeout>
           <appId>13</appId>
           <domain>upstairstest</domain>
         </_arguments>
       </_routine>
       <_returnType>XML</_returnType>
     </_routines>
   */

    private String createXmlRequest() {
        try {
            var documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance().newInstance();
            var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            var xmlRequestDocument = documentBuilder.newDocument();

            var routinesElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ROUTINES);
            xmlRequestDocument.appendChild(routinesElement);

            var routineElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ROUTINE);
            routinesElement.appendChild(routineElement);

            var nameElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_NAME);
            nameElement.setTextContent(Constants.WS_INITIALIZE_SESSION);
            routineElement.appendChild(nameElement);

            var argumentsElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ARGUMENTS);
            routineElement.appendChild(argumentsElement);

            var loginElement = xmlRequestDocument.createElement(Constants.WS_LOGIN);
            loginElement.setAttribute("isEncoded", Integer.toString(EncodingType.BASE64.ordinal()));
            loginElement.setTextContent(Base64.getEncoder().encodeToString(this.login.getBytes(StandardCharsets.UTF_8)));
            argumentsElement.appendChild(loginElement);

            var passwordElement = xmlRequestDocument.createElement(Constants.WS_PASSWORD);
            passwordElement.setAttribute("isEncoded", Integer.toString(EncodingType.BASE64.ordinal()));
            passwordElement.setTextContent(Base64.getEncoder().encodeToString(this.password.getBytes(StandardCharsets.UTF_8)));
            argumentsElement.appendChild(passwordElement);

            var isEncryptElement = xmlRequestDocument.createElement(Constants.WS_IS_ENCRYPT);
            isEncryptElement.setTextContent(this.isEncrypted == true ? "1" : "0");
            argumentsElement.appendChild(isEncryptElement);

            var timeOutElement = xmlRequestDocument.createElement(Constants.WS_TIMEOUT);
            timeOutElement.setTextContent("20");
            argumentsElement.appendChild(timeOutElement);

            var appIdElement = xmlRequestDocument.createElement(Constants.WS_APP_ID);
            appIdElement.setTextContent(Integer.toString(this.appId));
            argumentsElement.appendChild(appIdElement);

            if (this.appVersion != null && this.appVersion.isEmpty() != true && this.appVersion.isBlank() != true) {
                var appVersionElement = xmlRequestDocument.createElement(Constants.WS_APP_VERSION);
                appVersionElement.setTextContent(this.appVersion);
                argumentsElement.appendChild(appVersionElement);
            }

            if (this.domain != null && this.domain.isEmpty() != true && this.domain.isBlank() != true) {
                var domainElement = xmlRequestDocument.createElement(Constants.WS_DOMAIN);
                domainElement.setTextContent(this.domain);
                argumentsElement.appendChild(domainElement);
            }

            // writer.WriteElementString(Constants.WS_XML_REQUEST_NODE_RETURN_TYPE, Constants.WS_RETURN_TYPE_XML);
            var returnTypeElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_RETURN_TYPE);
            returnTypeElement.setTextContent(Constants.WS_RETURN_TYPE_XML);
            routinesElement.appendChild(returnTypeElement);

            var domSource = new DOMSource(xmlRequestDocument);
            var transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            var stringWriter = new StringWriter();
            var streamResult = new StreamResult(stringWriter);
            transformer.transform(domSource, streamResult);

            return stringWriter.toString();
        } catch (Exception ex) {

        }
        return null;
    }

    private final IHttpService httpService = new HttpService();

    private String get(String baseAddress, String requestUri, String queryString, HttpHost proxy) {
        String query = baseAddress + requestUri + "?" + queryString;
        try {
            String result = this.httpService.get(query, proxy, CompressionType.NONE);
            return this.extractToken(result);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return null;
    }

    private String post(String baseAddress, String requestUri, String queryString, HttpHost proxy) {
        String query = baseAddress + requestUri;
        try {
            String result = this.httpService.post(query, queryString, proxy, CompressionType.NONE, CompressionType.NONE);
            return this.extractToken(result);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        return null;
    }

    private final String TOKEN_START = "<" + Constants.WS_TOKEN + ">";
    private final String TOKEN_END = "</" + Constants.WS_TOKEN + ">";

    private String extractToken(String source) {
        int startIndex = source.indexOf(TOKEN_START) + TOKEN_START.length();
        int endIndex = source.indexOf(TOKEN_END);
        return source.substring(startIndex, endIndex);
    }

    public String send(HttpMethod httpMethod) {
        String result = null;

        if (httpMethod == HttpMethod.GET) {
            String queryString = this.createQueryString();
            result = this.get(this.baseAddress, this.requestUri, queryString, this.proxy);
        } else {
            String xmlRequest = this.createXmlRequest();
            result = this.post(this.baseAddress, this.requestUri, xmlRequest, this.proxy);
        }

        return result;
    }

    private String createQueryString() {
        StringBuilder sb = new StringBuilder();

        sb.append(Constants.WS_LOGIN + "=" + Base64.getEncoder().encodeToString(this.login.getBytes(StandardCharsets.UTF_8)));
        sb.append("&" + Constants.WS_PASSWORD + "=" + Base64.getEncoder().encodeToString(this.password.getBytes(StandardCharsets.UTF_8)));
        sb.append("&" + Constants.WS_IS_ENCODED + "=1");
        sb.append("&" + Constants.WS_IS_TOKEN + "=1");

        if (this.isEncrypted == true) {
            sb.append("&" + Constants.WS_IS_ENCRYPT + "=1");
        } else {
            sb.append("&" + Constants.WS_IS_ENCRYPT + "=0");
        }

        sb.append("&" + Constants.WS_TIMEOUT + "=20");
        sb.append("&" + Constants.WS_APP_ID + "=" + Integer.toString(this.appId));
        if (this.appVersion != null && this.appVersion.isEmpty() != true && this.appVersion.isBlank() != true) {
            sb.append("&" + Constants.WS_APP_VERSION + "=" + this.appVersion);
        }

        if (this.domain != null && this.domain.isEmpty() != true && this.domain.isBlank() != true) {
            sb.append("&" + Constants.WS_DOMAIN + "=" + this.domain);
        }

        sb.append("&" + Constants.WS_RETURN_TYPE + "=" + Constants.WS_RETURN_TYPE_XML);

        return sb.toString();
    }
}
