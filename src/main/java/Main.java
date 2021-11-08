import jwsa.*;
import jwsa.services.HttpMethod;

public class Main {

    public static void main(String[] args) {

        Session session = new Session("sadmin@upstairs.com",
                "Gromit12",
                false,
                34,
                "244",
                "upstairstest",
                null);
        session.createByRestServiceAddress("https://webservice.test.naiton.com");

        Command command = new Command("companymanager_getresellers");
        command.getParameters().Add("_businessid", PgsqlDbType.Integer, 1);
        command.getParameters().Add("_companyid", PgsqlDbType.Integer, 13);
        command.setWriteSchema(WriteSchema.TRUE);
        try {
            String xmlResult = Command.execute(command,
                    RoutineType.DataSet,
                    HttpMethod.GET,
                    ResponseFormat.JSON,
                    CompressionType.NONE,
                    CompressionType.NONE);
            System.out.println(xmlResult);
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }
}
