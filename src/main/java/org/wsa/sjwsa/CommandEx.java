package org.wsa.sjwsa;

import org.wsa.sjwsa.exceptions.RestServiceException;
import org.wsa.sjwsa.internal.Constants;
import org.wsa.sjwsa.services.ConvertingService;
import org.wsa.sjwsa.services.HttpService;
import org.wsa.sjwsa.services.IConvertingService;
import org.wsa.sjwsa.services.IHttpService;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.List;

public class CommandEx extends Command {
    public CommandEx(String name) {
        super(name);
    }

    public RoutineType routineType = RoutineType.DataSet;

    public RoutineType getRoutineType() {
        return routineType;
    }

    public void setRoutineType(RoutineType routineType) {
        this.routineType = routineType;
    }

    private final static String postFormat = "%s%sexecutemixpost?token=%s&compression=%s";

    public static String ExecuteAll(List<CommandEx> commandExs,
                                    ResponseFormat responseFormat,
                                    CompressionType outgoingCompressionType,
                                    CompressionType returnCompressionType,
                                    ParallelExecution parallelExecution) throws Exception {
        if (commandExs == null || commandExs.size() == 0) {
            throw new IllegalArgumentException("commands are required...");
        }

        IConvertingService convertingService = new ConvertingService();

        var documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance().newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();
        var xmlRequestDocument = documentBuilder.newDocument();

        var routinesElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ROUTINES);
        xmlRequestDocument.appendChild(routinesElement);

        for (CommandEx commandEx : commandExs) {
            Request request = new Request(commandEx, convertingService);
            request.createXmlRoutine(xmlRequestDocument, commandEx.getRoutineType());
        }

        createRoutinesLevelXmlNodes(xmlRequestDocument, returnCompressionType, parallelExecution, responseFormat);

        var domSource = new DOMSource(xmlRequestDocument);
        var transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        var stringWriter = new StringWriter();
        var streamResult = new StreamResult(stringWriter);
        transformer.transform(domSource, streamResult);

        String requestString = stringWriter.toString();

        IHttpService httpService = new HttpService();

        int count = 0;
        do {
            try {
                String requestUri = String.format(postFormat, SessionContext.getRestServiceAddress(), SessionContext.route, SessionContext.getToken(), outgoingCompressionType.getValue());
                return httpService.post(requestUri,
                        requestString,
                        SessionContext.getProxy(),
                        outgoingCompressionType,
                        returnCompressionType);
            } catch (RestServiceException ex) {
                if (ex.getCode().equals("MI008")) {
                    SessionContext.refresh();

                    count++;

                    if (count == 3) {
                        throw ex;
                    }

                } else {
                    throw ex;
                }

            } catch (Exception ex) {
                throw ex;
            }
        } while (true);
    }
}
