import jwsa.*;

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
            System.out.println(ex.toString());
        }

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

        DataSetExample dataSetExample=new DataSetExample();
        dataSetExample.runTest1();

//        NonQueryExample nonQueryExample=new NonQueryExample();
//        nonQueryExample.runTest1();

//        ScalarExample scalarExample = new ScalarExample();
//        scalarExample.runTest2();

    }
}
