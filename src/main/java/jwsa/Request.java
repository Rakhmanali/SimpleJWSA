package jwsa;

import jwsa.internal.Constants;
import jwsa.services.HttpMethod;
import jwsa.services.HttpService;
import jwsa.services.IConvertingService;
import jwsa.services.IHttpService;


import org.apache.hc.core5.http.HttpHost;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Request {
    protected String serviceAddress;
    protected String route;
    protected String token;
    protected final Command command;
    protected final IConvertingService convertingService;
    protected HttpHost proxy;
    private final IHttpService httpService = new HttpService();
    private String format;

    public Request(Command command, IConvertingService convertingService) {
        this.command = command;
        this.convertingService = convertingService;
    }

    public Request(String serviceAddress, String route, String token, Command command, IConvertingService convertingService, HttpHost proxy, String format) {
        this.serviceAddress = serviceAddress;
        this.route = route;
        this.token = token;
        this.command = command;
        this.convertingService = convertingService;
        this.proxy = proxy;
        this.format = format;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private void writeArguments(Document xmlRequestDocument,
                                Command command,
                                IConvertingService convertingService) {
        if (command == null || command.getParameters().getCount() == 0) {
            return;
        }

        var routinesNode = xmlRequestDocument.getFirstChild();
        var routineNode = routinesNode.getFirstChild();

        var argumentsElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ARGUMENTS);
        routineNode.appendChild(argumentsElement);

        Object[] value;
        String parameterName;
        for (Parameter parameter : command.getParameters()) {
            parameterName = parameter.getName().toLowerCase();
            value = convertingService.ConvertObjectToDb(parameter.getPgsqlDbType(), parameter.isArray(), parameter.getValue(),
                    command.getOutgoingEncodingType());
            if (value == null || value.length == 0) {
                var parameterNameElement = xmlRequestDocument.createElement(parameterName);
                parameterNameElement.setAttribute("isNull", "true");
                argumentsElement.appendChild((parameterNameElement));
            } else {
                if (command.getOutgoingEncodingType() == EncodingType.NONE) {
            /*
               if outgoingEncodingType == EncodingType.NONE and value is string family object then
               the function ConvertObjectToDb() "converts" it to something like

               <![CDATA[ ... ]]>

               but the XmlWriter converts special symbols to the "safe" for XML, for example, it
               converts "<" to "&lt;"

               To prevent it here we need some additional code and using
               WriteRaw method of XmlWriter
            */
                    if (this.isEncodingAllowedType(parameter.getPgsqlDbType()) == true) {
                        for (Object v : value) {
                            var parameterNameElement = xmlRequestDocument.createElement(parameterName);
                            if (v == null) {
                                parameterNameElement.setTextContent(Constants.STRING_NULL);
                            } else {
                                Text text = xmlRequestDocument.createTextNode(String.valueOf(v));
                                parameterNameElement.appendChild(text);
                            }
                            argumentsElement.appendChild(parameterNameElement);
                        }
                    } else {
                        for (Object v : value) {
                            var parameterNameElement = xmlRequestDocument.createElement(parameterName);
                            if (v == null) {
                                parameterNameElement.setTextContent(Constants.STRING_NULL);
                            } else {
                                parameterNameElement.setTextContent(String.valueOf(v));
                            }
                            argumentsElement.appendChild(parameterNameElement);
                        }
                    }
                } else {
                    if (this.isEncodingAllowedType(parameter.getPgsqlDbType()) == true) {
                        int encodingType = command.getOutgoingEncodingType().ordinal();
                        String encodedValue;
                        for (Object v : value) {
                            encodedValue = convertingService.EncodeTo(v, command.getOutgoingEncodingType());
                            var parameterNameElement = xmlRequestDocument.createElement(parameterName);
                            if (encodedValue.trim().length() > 0) {
                                parameterNameElement.setAttribute(Constants.WS_XML_REQUEST_ATTRIBUTE_ENCODING, Integer.toString(encodingType));
                            }
                            parameterNameElement.setTextContent(encodedValue);
                            argumentsElement.appendChild(parameterNameElement);
                        }
                    } else {
                        for (Object v : value) {
                            var parameterNameElement = xmlRequestDocument.createElement(parameterName);
                            if (v == null) {
                                parameterNameElement.setTextContent(Constants.STRING_NULL);
                            } else {
                                parameterNameElement.setTextContent(String.valueOf(v));
                            }
                            argumentsElement.appendChild(parameterNameElement);
                        }
                    }
                }

            }

        }
    }

    private boolean isEncodingAllowedType(PgsqlDbType pgsqlDbType) {
        switch (pgsqlDbType) {
            case Varchar:
            case Text:
            case Xml:
            case Json:
            case Jsonb:
                return true;
        }
        return false;
    }

    private Element writeOptions(Document xmlRequestDocument, Command command) {
        var routinesNode = xmlRequestDocument.getFirstChild();
        var routineNode = routinesNode.getFirstChild();

        var optionsElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_OPTIONS);
        routineNode.appendChild(optionsElement);

        if (command.getClearPool() == ClearPool.TRUE) {
            var clearPoolElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_CLEAR_POOL);
            clearPoolElement.setTextContent(String.valueOf(command.getClearPool().ordinal()));
            optionsElement.appendChild(clearPoolElement);
        }

        if (command.getFromCache() == FromCache.TRUE) // Default value is FALSE
        {
            var fromCacheElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_FROM_CACHE);
            fromCacheElement.setTextContent(String.valueOf(command.getFromCache().ordinal()));
            optionsElement.appendChild(fromCacheElement);
        }

        if (command.getWriteSchema() == WriteSchema.TRUE) // Default value is FALSE
        {
            var writeSchemaElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_WRITE_SCHEMA);
            writeSchemaElement.setTextContent(String.valueOf(command.getWriteSchema().ordinal()));
            optionsElement.appendChild(writeSchemaElement);
        }

        if (command.getCommandTimeout() != 20) // Default value is 20 seconds
        {
            var commandTimeoutElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_COMMAND_TIMEOUT);
            commandTimeoutElement.setTextContent(String.valueOf(command.getCommandTimeout()));
            optionsElement.appendChild(commandTimeoutElement);
        }

        if (command.getReturnEncodingType() != EncodingType.NONE) {
            var returnEncodingTypeElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ENCODING);
            returnEncodingTypeElement.setAttribute(Constants.WS_XML_REQUEST_NODE_ENCODING_ATTRIBUTE_IS_ENTRY, "0");
            returnEncodingTypeElement.setTextContent(String.valueOf(command.getReturnEncodingType().ordinal()));
            optionsElement.appendChild(returnEncodingTypeElement);
        }

        if (command.getIsolationLevel() != IsolationLevel.ReadCommitted) //Default value is ReadCommitted
        {
            var isolationLevelElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ISOLATION_LEVEL);
            isolationLevelElement.setTextContent(String.valueOf(command.getIsolationLevel().getValue()));
            optionsElement.appendChild(isolationLevelElement);
        }

        return optionsElement;
    }

    private void writeOptions(Document xmlRequestDocument, Command command, RoutineType routineType) {
        Element optionsElement = this.writeOptions(xmlRequestDocument, command);

        var routineTypeElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ROUTINE_TYPE);
        routineTypeElement.setTextContent(String.valueOf(routineType.getValue()));
        optionsElement.appendChild(routineTypeElement);
    }

    private Element writeRoutine(Document xmlRequestDocument,
                                 Command command,
                                 IConvertingService convertingService) {
        var routinesElement = xmlRequestDocument.getDocumentElement();

        var routineElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ROUTINE);
        routinesElement.appendChild(routineElement);

        var nameElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_NAME);
        nameElement.setTextContent(command.getName().toLowerCase());
        routineElement.appendChild(nameElement);

        this.writeArguments(xmlRequestDocument, command, convertingService);
        this.writeOptions(xmlRequestDocument, command);

        return routineElement;
    }

    private Element writeRoutine(Document xmlRequestDocument,
                                 Command command,
                                 IConvertingService convertingService,
                                 RoutineType routineType) {
        var routinesElement = xmlRequestDocument.getDocumentElement();

        var routineElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ROUTINE);
        routinesElement.appendChild(routineElement);

        var nameElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_NAME);
        nameElement.setTextContent(command.getName().toLowerCase());
        routineElement.appendChild(nameElement);

        this.writeArguments(xmlRequestDocument, command, convertingService);
        this.writeOptions(xmlRequestDocument, command, routineType);

        return routineElement;
    }

    public String createXmlRequest() {
        try {
            var documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance().newInstance();
            var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            var xmlRequestDocument = documentBuilder.newDocument();

            var routinesElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_ROUTINES);
            xmlRequestDocument.appendChild(routinesElement);

            this.writeRoutine(xmlRequestDocument, this.command, this.convertingService);

            if (this.command.getReturnCompressionType() != CompressionType.NONE) {
//                xmlWriter.WriteElementString(Constants.WS_XML_REQUEST_NODE_COMPRESSION, ((int)this.command.ReturnCompressionType).ToString());
                var returnCompressionTypeElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_COMPRESSION);
                CompressionType compressionType = this.command.getReturnCompressionType();
                returnCompressionTypeElement.setTextContent(String.valueOf(compressionType.getValue()));
                routinesElement.appendChild(returnCompressionTypeElement);
            }

            //xmlWriter.WriteElementString(Constants.WS_XML_REQUEST_NODE_RETURN_TYPE, this.command.ResponseFormat.ToString());
            var returnTypeElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_RETURN_TYPE);
            ResponseFormat responseFormat = this.command.getResponseFormat();
            returnTypeElement.setTextContent(String.valueOf(responseFormat).toLowerCase());
            routinesElement.appendChild((returnTypeElement));

            if (this.command.getResponseFormat() == ResponseFormat.JSON) {
                //xmlWriter.WriteElementString(Constants.WS_XML_REQUEST_NODE_JSON_DATE_FORMAT, "2");
                var jsonDateFormatElement = xmlRequestDocument.createElement(Constants.WS_XML_REQUEST_NODE_JSON_DATE_FORMAT);
                jsonDateFormatElement.setTextContent("2");
                routinesElement.appendChild(jsonDateFormatElement);
            }

            var domSource = new DOMSource(xmlRequestDocument);
            var transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            var stringWriter = new StringWriter();
            var streamResult = new StreamResult(stringWriter);
            transformer.transform(domSource, streamResult);

            return stringWriter.toString();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return null;
    }

    public String createXmlRoutine() throws ParserConfigurationException,
            TransformerException {
        var documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance().newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();
        var xmlRequestDocument = documentBuilder.newDocument();

        this.writeRoutine(xmlRequestDocument, this.command, this.convertingService);

        var domSource = new DOMSource(xmlRequestDocument);
        var transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        var stringWriter = new StringWriter();
        var streamResult = new StreamResult(stringWriter);
        transformer.transform(domSource, streamResult);

        return stringWriter.toString();
    }

    public String createXmlRoutine(RoutineType routineType) throws ParserConfigurationException,
            TransformerException {
        var documentBuilderFactory = DocumentBuilderFactory.newDefaultInstance().newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();
        var xmlRequestDocument = documentBuilder.newDocument();

        this.writeRoutine(xmlRequestDocument, this.command, this.convertingService, routineType);

        var domSource = new DOMSource(xmlRequestDocument);
        var transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        var stringWriter = new StringWriter();
        var streamResult = new StreamResult(stringWriter);
        transformer.transform(domSource, streamResult);

        return stringWriter.toString();
    }

    protected Object get(String requestString) throws Exception {
        String requestUri = String.format(this.format, this.serviceAddress, this.route, this.token, URLEncoder.encode(requestString, StandardCharsets.UTF_8));
        return this.httpService.get(requestUri, this.proxy, this.command.getReturnCompressionType());
    }

    protected Object post(String requestString) throws Exception {
        CompressionType outgoingCompressionType=this.command.getOutgoingCompressionType();
        String requestUri = String.format(this.format, this.serviceAddress, this.route, this.token, outgoingCompressionType.getValue());
        return this.httpService.post(requestUri, requestString, this.proxy, this.command.getOutgoingCompressionType(), this.command.getReturnCompressionType());
    }

    public Object Send() throws Exception {
        String xmlRequest = this.createXmlRequest();
        if (this.command.getHttpMethod() == HttpMethod.POST) {
            return this.post(xmlRequest);
        }
        return this.get(xmlRequest);
    }
}
