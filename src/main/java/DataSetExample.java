import jwsa.*;
import jwsa.exceptions.RestServiceException;

import java.time.LocalDateTime;

public class DataSetExample {
    public Command getCommand1()
    {
        return this.getCommandEx1();
    }

    public CommandEx getCommandEx1()
    {
        CommandEx command = new CommandEx("ordermanager_getfilteredorders_iod");

        command.getParameters().add("_orderid", PgsqlDbType.Integer, true , null);
        command.getParameters().add("_clientid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_clientorcompany", PgsqlDbType.Varchar, "");
        command.getParameters().add("_sourceid", PgsqlDbType.Integer, true, new int[] { 0, 1, 2, 3, 4 });
        command.getParameters().add("_orderstatusid", PgsqlDbType.Integer , true).setValue(new int[] { 0, 1, 3 });
        command.getParameters().add("_businessid", PgsqlDbType.Integer, true).setValue(new int[] { 944, 16309, 1629, 941, 942, 1, 943 });
        command.getParameters().add("_deliveryid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_availabilityid", PgsqlDbType.Integer,0);
        command.getParameters().add("_paymentstatusid", PgsqlDbType.Integer,0);
        command.getParameters().add("_invoicestatusid", PgsqlDbType.Integer,0);
        command.getParameters().add("_productid", PgsqlDbType.Integer,0);
        command.getParameters().add("_productname", PgsqlDbType.Varchar, "");
        command.getParameters().add("_datefrom", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_dateto", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_clientreference", PgsqlDbType.Varchar, "");
        command.getParameters().add("_clientremark", PgsqlDbType.Varchar, "");
        command.getParameters().add("_internalremark", PgsqlDbType.Varchar, "");
        command.getParameters().add("_deliveryremark", PgsqlDbType.Varchar, "");
        command.getParameters().add("_taskstatusid", PgsqlDbType.Integer, -2);
        command.getParameters().add("_billable", PgsqlDbType.Integer,0);
        command.getParameters().add("_deliverydate", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_validationstatusid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_schedulestart", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_scheduleend", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_coursevalue", PgsqlDbType.Numeric, 1.00000);
        command.getParameters().add("_salesmanagerd", PgsqlDbType.Integer, 0);
        command.getParameters().add("_ordertypeid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_deliveryperiodstart", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_deliveryperiodend", PgsqlDbType.Timestamp, null);

        LocalDateTime _shipmentstart = null;
        LocalDateTime _shipmentend = null;
        boolean _searchshipmentdateequalnull = _shipmentstart == LocalDateTime.MIN && _shipmentend == LocalDateTime.MIN;

        command.getParameters().add("_shipmentstart", PgsqlDbType.Timestamp).setValue(_searchshipmentdateequalnull ? null : _shipmentstart);
        command.getParameters().add("_shipmentend", PgsqlDbType.Timestamp).setValue( _searchshipmentdateequalnull ? null : _shipmentend);
        command.getParameters().add("_searchshipmentdateequalnull", PgsqlDbType.Boolean, _searchshipmentdateequalnull);
        command.getParameters().add("_dpistart", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_dpiend", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_invstart", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_invend", PgsqlDbType.Timestamp, null);
        command.getParameters().add("_deliveryaddresses", PgsqlDbType.Text, "");
        command.getParameters().add("_createdbyemployeeid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_subscriptionid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_approved", PgsqlDbType.Integer, 0);
        command.getParameters().add("_calculateavailable", PgsqlDbType.Boolean, false, true);
        command.getParameters().add("_company", PgsqlDbType.Text, "");
        command.getParameters().add("_trip", PgsqlDbType.Text, "");
        command.getParameters().add("_limit", PgsqlDbType.Integer, 200);

        command.setRoutineType(RoutineType.DataSet);
        command.setCommandTimeout(1800);
        command.setWriteSchema(WriteSchema.TRUE);

        return command;
    }

    public String runTest1()
    {
        try {
            Command command = this.getCommand1();
            String xmlResult = Command.execute(command,
                    RoutineType.DataSet,
                    HttpMethod.POST,
                    ResponseFormat.XML);
            System.out.println(xmlResult);

            return xmlResult;
        } catch (RestServiceException ex){
            System.out.println("code: "+ex.getCode()+", message: "+ex.getMessage());
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }

//        DataSet dataSet = new DataSet(command.Name);
//        using (MemoryStream stream = new MemoryStream(Encoding.UTF8.GetBytes(xmlResult)))
//        {
//            dataSet.ReadXml(stream);
//        }
//
//        Console.WriteLine($"there are {dataSet.Tables.Count} tables");
//        for (int i = 0; i < dataSet.Tables.Count; i++)
//        {
//            Console.WriteLine($"table {dataSet.Tables[i].TableName} contains {dataSet.Tables[i].Rows.Count} records");
//        }

        return null;
    }
}
