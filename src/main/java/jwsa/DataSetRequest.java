package jwsa;

import jwsa.services.HttpMethod;
import jwsa.services.IConvertingService;
import org.apache.hc.core5.http.HttpHost;

public final class DataSetRequest extends Request {
    private final static String getFormat = "%s%sexecutereturnset?token=%s&value=%s";
    private final static String postFormat = "%s%sexecutereturnsetpost?token=%s&compression=%s";

    public DataSetRequest(String serviceAddress,
                          String route,
                          String token,
                          Command command,
                          IConvertingService convertingService,
                          HttpHost proxy) {
        super(serviceAddress,
                route,
                token,
                command,
                convertingService,
                proxy,
                command.getHttpMethod() == HttpMethod.GET ? getFormat : postFormat);
    }

    public static String getPostFormat() {
        return postFormat;
    }
}
