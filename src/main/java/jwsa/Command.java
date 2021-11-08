package jwsa;

import jwsa.exceptions.RestServiceException;
import jwsa.internal.Constants;
import jwsa.services.*;

import java.util.List;

public class Command {
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private final ParameterCollection parameters = new ParameterCollection();

    public ParameterCollection getParameters() {
        return parameters;
    }

    private HttpMethod httpMethod = HttpMethod.GET;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    private ResponseFormat responseFormat = ResponseFormat.JSON;

    public ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }

    private CompressionType outgoingCompressionType = CompressionType.NONE;

    public CompressionType getOutgoingCompressionType() {
        return outgoingCompressionType;
    }

    private CompressionType returnCompressionType = CompressionType.NONE;

    public CompressionType getReturnCompressionType() {
        return returnCompressionType;
    }

    public void setReturnCompressionType(CompressionType returnCompressionType) {
        this.returnCompressionType = returnCompressionType;
    }

    // specifies whether being encoded every the text-based data during creating XML request,
    // works in the routine level boundary
    private EncodingType outgoingEncodingType = EncodingType.NONE;

    public EncodingType getOutgoingEncodingType() {
        return outgoingEncodingType;
    }

    // specifies whether the server will encode the text-based data during preparing a response
    // works in the routine level boundary
    private EncodingType returnEncodingType = EncodingType.NONE;

    public EncodingType getReturnEncodingType() {
        return returnEncodingType;
    }

    public void setReturnEncodingType(EncodingType returnEncodingType) {
        this.returnEncodingType = returnEncodingType;
    }

    private WriteSchema writeSchema = WriteSchema.FALSE;

    public WriteSchema getWriteSchema() {
        return writeSchema;
    }

    public void setWriteSchema(WriteSchema writeSchema) {
        this.writeSchema = writeSchema;
    }

    private FromCache fromCache = FromCache.FALSE;

    public FromCache getFromCache() {
        return fromCache;
    }

    public void setFromCache(FromCache fromCache) {
        this.fromCache = fromCache;
    }

    private ClearPool clearPool = ClearPool.FALSE;

    public ClearPool getClearPool() {
        return clearPool;
    }

    public void setClearPool(ClearPool clearPool) {
        this.clearPool = clearPool;
    }

    private IsolationLevel isolationLevel = IsolationLevel.ReadCommitted;

    public IsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    public void setIsolationLevel(IsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    private int commandTimeout = 20;

    public int getCommandTimeout() {
        return commandTimeout;
    }

    public void setCommandTimeout(int commandTimeout) {
        if (commandTimeout < 0) {
            throw new IllegalArgumentException("the parameter commandTimeout cannot be less than zero.");
        }
        this.commandTimeout = commandTimeout;
    }

    public Command(String name) {
        this.name = name;
    }

    public static String execute(Command command, RoutineType routineType) throws Exception {
        return execute(command, routineType, HttpMethod.GET, ResponseFormat.JSON, CompressionType.NONE, CompressionType.NONE);
    }

    public static String execute(Command command, RoutineType routineType, HttpMethod httpMethod) throws Exception {
        return execute(command, routineType, httpMethod, ResponseFormat.JSON, CompressionType.NONE, CompressionType.NONE);
    }

    public static String execute(Command command, RoutineType routineType, HttpMethod httpMethod, ResponseFormat responseFormat) throws Exception {
        return execute(command, routineType, httpMethod, responseFormat, CompressionType.NONE, CompressionType.NONE);
    }

    public static String execute(Command command, RoutineType routineType, HttpMethod httpMethod, ResponseFormat responseFormat, CompressionType outgoingCompressionType) throws Exception {
        return execute(command, routineType, httpMethod, responseFormat, outgoingCompressionType, CompressionType.NONE);
    }

    public static String execute(Command command,
                                 RoutineType routineType,
                                 HttpMethod httpMethod,
                                 ResponseFormat responseFormat,
                                 CompressionType outgoingCompressionType,
                                 CompressionType returnCompressionType) throws Exception {
        command.httpMethod = httpMethod;
        command.responseFormat = responseFormat;
        command.outgoingCompressionType = outgoingCompressionType;
        command.returnCompressionType = returnCompressionType;

        IConvertingService convertingService = new ConvertingService();

        if (routineType == RoutineType.Scalar) {
            ScalarRequest scalarRequest = new ScalarRequest(SessionContext.getRestServiceAddress(),
                    SessionContext.route,
                    SessionContext.getToken(),
                    command,
                    convertingService,
                    SessionContext.getProxy());

            int count = 0;
            do {
                try {
                    Object result = scalarRequest.Send();
                    return String.valueOf(result);
                } catch (RestServiceException ex) {
                    if (ex.getCode() == "MI008") {
                        SessionContext.refresh();
                        scalarRequest.setToken(SessionContext.getToken());

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
        } else if (routineType == RoutineType.NonQuery) {
            NonQueryRequest nonqueryRequest = new NonQueryRequest(SessionContext.getRestServiceAddress(),
                    SessionContext.route,
                    SessionContext.getToken(),
                    command,
                    convertingService,
                    SessionContext.getProxy());

            int count = 0;
            do {
                try {
                    Object result = nonqueryRequest.Send();
                    return String.valueOf(result);
                } catch (RestServiceException ex) {
                    if (ex.getCode() == "MI008") {
                        SessionContext.refresh();
                        nonqueryRequest.setToken(SessionContext.getToken());

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
        } else if (routineType == RoutineType.DataSet) {
            DataSetRequest dataSetRequest = new DataSetRequest(SessionContext.getRestServiceAddress(),
                    SessionContext.route,
                    SessionContext.getToken(),
                    command,
                    convertingService,
                    SessionContext.getProxy());

            int count = 0;
            do {
                try {
                    Object result = dataSetRequest.Send();
                    return String.valueOf(result);
                } catch (RestServiceException ex) {
                    if (ex.getCode() == "MI008") {
                        SessionContext.refresh();
                        dataSetRequest.setToken(SessionContext.getToken());

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

        return null;
    }

    public static String ExecuteAll(List<Command> commands,
                                    RoutineType routineType) throws Exception {
        return ExecuteAll(commands, routineType, ResponseFormat.JSON, CompressionType.NONE, CompressionType.NONE, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<Command> commands,
                                    RoutineType routineType,
                                    ResponseFormat responseFormat) throws Exception {
        return ExecuteAll(commands, routineType, responseFormat, CompressionType.NONE, CompressionType.NONE, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<Command> commands,
                                    RoutineType routineType,
                                    ResponseFormat responseFormat,
                                    CompressionType outgoingCompressionType) throws Exception {
        return ExecuteAll(commands, routineType, responseFormat, outgoingCompressionType, CompressionType.NONE, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<Command> commands,
                                    RoutineType routineType,
                                    ResponseFormat responseFormat,
                                    CompressionType outgoingCompressionType,
                                    CompressionType returnCompressionType) throws Exception {
        return ExecuteAll(commands, routineType, responseFormat, outgoingCompressionType, returnCompressionType, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<Command> commands,
                                    RoutineType routineType,
                                    ResponseFormat responseFormat,
                                    CompressionType outgoingCompressionType,
                                    CompressionType returnCompressionType,
                                    ParallelExecution parallelExecution) throws Exception {
        if (commands == null || commands.size() == 0) {
            throw new IllegalArgumentException("commands are required...");
        }

        String postFormat = DataSetRequest.getPostFormat();
        if (routineType == RoutineType.Scalar) {
            postFormat = ScalarRequest.getPostFormat();
        } else if (routineType == RoutineType.NonQuery) {
            postFormat = NonQueryRequest.getPostFormat();
        }

        IConvertingService convertingService = new ConvertingService();

        StringBuilder sb = new StringBuilder();
        sb.append("<" + Constants.WS_XML_REQUEST_NODE_ROUTINES + ">");
        for (Command command : commands) {
            Request request = new Request(command, convertingService);
            sb.append(request.createXmlRoutine());
        }

        createRoutinesLevelXmlNodes(sb, returnCompressionType, parallelExecution, responseFormat);

        sb.append("</" + Constants.WS_XML_REQUEST_NODE_ROUTINES + ">");
        String requestString = sb.toString();

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
                if (ex.getCode() == "MI008") {
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

    protected static void createRoutinesLevelXmlNodes(StringBuilder sb,
                                                      CompressionType returnCompressionType,
                                                      ParallelExecution parallelExecution,
                                                      ResponseFormat responseFormat) {
        if (returnCompressionType != CompressionType.NONE) {
            sb.append(createXmlNode(Constants.WS_XML_REQUEST_NODE_COMPRESSION, String.valueOf(returnCompressionType.getValue())));
        }

        sb.append(createXmlNode(Constants.WS_XML_REQUEST_NODE_RETURN_TYPE, String.valueOf(responseFormat).toLowerCase()));

        if (parallelExecution == ParallelExecution.TRUE) {
            sb.append(createXmlNode(Constants.WS_XML_REQUEST_NODE_PARALLEL_EXECUTION, "1"));
        } else {
            sb.append(createXmlNode(Constants.WS_XML_REQUEST_NODE_PARALLEL_EXECUTION, "0"));
        }

        if (responseFormat == ResponseFormat.JSON) {
            sb.append(createXmlNode(Constants.WS_XML_REQUEST_NODE_JSON_DATE_FORMAT, "2"));
        }
    }

    private static String createXmlNode(String nodeName, String value) {
        return "<" + nodeName + ">" + value + "</" + nodeName + ">";
    }
}
