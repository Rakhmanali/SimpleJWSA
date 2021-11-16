import org.wsa.sjwsa.*;
import org.wsa.sjwsa.exceptions.RestServiceException;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Session session = new Session("sadmin@upstairs.com",
                "Gromit12",
                false,
                34,
                "244",
                "upstairstest",
                null);

        try {
            session.createByConnectionProviderAddress("https://connectionprovider.naiton.com");
        } catch (Exception ex) {
             ex.printStackTrace();
        }

//        try {
//            session.createByRestServiceAddress("https://webservice.test.naiton.com");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

//        Command command = new Command("companymanager_getresellers");
//        command.getParameters().add("_businessid", PgsqlDbType.Integer, false, 1);
//        command.getParameters().add("_companyid", PgsqlDbType.Integer, false, 13);
//        command.setWriteSchema(WriteSchema.TRUE);
//        try {
//            String xmlResult = Command.execute(command,
//                    RoutineType.DataSet,
//                    HttpMethod.GET,
//                    ResponseFormat.JSON,
//                    CompressionType.NONE,
//                    CompressionType.NONE);
//            System.out.println(xmlResult);
//        } catch (Exception ex) {
//            System.out.println(ex.toString());
//        }

//        DataSetExample dataSetExample=new DataSetExample();
//        dataSetExample.exampleForReadme5();

//        NonQueryExample nonQueryExample=new NonQueryExample();
//        nonQueryExample.exampleForReadme();

//        ScalarExample scalarExample = new ScalarExample();
//        scalarExample.ExampleForReadme();

        exampleForReadme6();

    }

    private static void exampleForReadme6() {
        // the different type of routines in one HTTP request

        CommandEx command1 = new CommandEx("companymanager_getresellers");
        command1.getParameters().add("_businessid", PgsqlDbType.Integer, 1);
        command1.getParameters().add("_companyid", PgsqlDbType.Integer, 13);
        command1.setRoutineType(RoutineType.DataSet);

        CommandEx command2 = new CommandEx("brandmanager_hidebrand");
        command2.getParameters().add("_brandid", PgsqlDbType.Integer, 13);
        command2.getParameters().add("_ishidden", PgsqlDbType.Boolean, false, false);
        command2.getParameters().add("_returnvalue", PgsqlDbType.Integer);
        command2.setRoutineType(RoutineType.NonQuery);

        CommandEx command3 = new CommandEx("clientmanager_findclientbyemailbusiness");
        command3.getParameters().add("_businessid", PgsqlDbType.Integer, 1);
        command3.getParameters().add("_email", PgsqlDbType.Text, "femkedijkema@hotmail.com");
        command3.getParameters().add("_personid", PgsqlDbType.Integer, 3);
        command3.setRoutineType(RoutineType.Scalar);

        try {
            String xmlResult = CommandEx.ExecuteAll(Arrays.asList(command1, command2, command3),
                    ResponseFormat.JSON,
                    CompressionType.NONE,
                    CompressionType.NONE,
                    ParallelExecution.FALSE);
            System.out.println(xmlResult);
        } catch (RestServiceException ex) {
            System.out.println("code: " + ex.getCode() + ", message: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
