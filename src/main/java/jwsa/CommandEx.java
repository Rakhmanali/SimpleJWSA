package jwsa;

import jwsa.exceptions.RestServiceException;
import jwsa.internal.Constants;
import jwsa.services.ConvertingService;
import jwsa.services.HttpService;
import jwsa.services.IConvertingService;
import jwsa.services.IHttpService;

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

    private final static String postFormat = "%%executemixpost?token=%&compression=%";

    public static String ExecuteAll(List<CommandEx> commandExs) throws Exception {
        return ExecuteAll(commandExs, ResponseFormat.JSON, CompressionType.NONE, CompressionType.NONE, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<CommandEx> commandExs,
                                    ResponseFormat responseFormat) throws Exception {
        return ExecuteAll(commandExs, responseFormat, CompressionType.NONE, CompressionType.NONE, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<CommandEx> commandExs,
                                    ResponseFormat responseFormat,
                                    CompressionType outgoingCompressionType) throws Exception {
        return ExecuteAll(commandExs, responseFormat, outgoingCompressionType, CompressionType.NONE, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<CommandEx> commandExs,
                                    ResponseFormat responseFormat,
                                    CompressionType outgoingCompressionType,
                                    CompressionType returnCompressionType) throws Exception {
        return ExecuteAll(commandExs, responseFormat, outgoingCompressionType, returnCompressionType, ParallelExecution.TRUE);
    }

    public static String ExecuteAll(List<CommandEx> commandExs,
                                    ResponseFormat responseFormat,
                                    CompressionType outgoingCompressionType,
                                    CompressionType returnCompressionType,
                                    ParallelExecution parallelExecution) throws Exception {
        if (commandExs == null || commandExs.size() == 0) {
            throw new IllegalArgumentException("commands are required...");
        }

        IConvertingService convertingService = new ConvertingService();

        StringBuilder sb = new StringBuilder();
        sb.append("<" + Constants.WS_XML_REQUEST_NODE_ROUTINES + ">");
        for (CommandEx commandEx : commandExs) {
            Request request = new Request(commandEx, convertingService);
            sb.append(request.createXmlRoutine(commandEx.getRoutineType()));
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
