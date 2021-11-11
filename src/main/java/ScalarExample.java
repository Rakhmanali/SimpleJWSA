import jwsa.*;
import jwsa.exceptions.RestServiceException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScalarExample {
    public Command getCommand1()
    {
        return this.getCommandEx1();
    }

    public CommandEx getCommandEx1()
    {
        CommandEx command = new CommandEx("vehiclemanager_vehicle_upsert");
        command.getParameters().add("_id", PgsqlDbType.Integer,  61);
        command.getParameters().add("_addressid", PgsqlDbType.Integer, 4357);
        command.getParameters().add("_businessid", PgsqlDbType.Integer, 941);
        command.getParameters().add("_vehicletypeid", PgsqlDbType.Integer, 2);
        command.getParameters().add("_vehiclemodelid", PgsqlDbType.Integer, 4);
        command.getParameters().add("_maintenanceplanid", PgsqlDbType.Integer, 1);
        command.getParameters().add("_vehiclename", PgsqlDbType.Varchar, "W-01 (VDT-76-S)");
        command.getParameters().add("_licenceplate", PgsqlDbType.Varchar, "VK-663-S");
        command.getParameters().add("_vinnumber", PgsqlDbType.Varchar, "");
        command.getParameters().add("_built", PgsqlDbType.Date, LocalDate.of(2016, 1, 1));
        command.getParameters().add("_tracktracevehicleid", PgsqlDbType.Varchar);
        command.getParameters().add("_capacity", PgsqlDbType.Numeric, 0.0);
        command.getParameters().add("_fuelcapacity", PgsqlDbType.Numeric, 0.0);
        command.getParameters().add("_dailydistance", PgsqlDbType.Numeric, 0.0);
        command.getParameters().add("_description", PgsqlDbType.Text, "");
        command.getParameters().add("_homelocation", PgsqlDbType.Varchar, "Nederland ,Weert ,Risseweg");
        command.getParameters().add("_homelongitude", PgsqlDbType.Double, 5.7358587);
        command.getParameters().add("_homelatitude", PgsqlDbType.Double, 51.2622869);
        command.getParameters().add("_entitytypeid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_activefrom", PgsqlDbType.Timestamp, LocalDateTime.of(2018, 1, 1, 16, 29, 34));
        command.getParameters().add("_activeto", PgsqlDbType.Timestamp, LocalDateTime.of(2030, 12, 31, 11, 11, 9));
        command.getParameters().add("_companyid", PgsqlDbType.Integer, null);

        command.setRoutineType(RoutineType.Scalar);
        command.setOutgoingEncodingType(EncodingType.NONE);

        return command;
    }
    public String runTest1()
    {
        Command command = this.getCommand1();

        try {
            String xmlResult = Command.execute(command,
                    RoutineType.Scalar,
                    HttpMethod.POST,
                    ResponseFormat.JSON,
                    CompressionType.NONE,
                    CompressionType.NONE);
            System.out.println(xmlResult);

        } catch (RestServiceException ex){
            System.out.println("code: "+ex.getCode()+", message: "+ex.getMessage());
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }

        return null;
    }

    public Command getCommand2()
    {
        return this.getCommandEx2();
    }

    public CommandEx getCommandEx2()
    {
        CommandEx command = new CommandEx("clientmanager_findclientbyemailbusiness");
        command.getParameters().add("_businessid", PgsqlDbType.Integer).setValue(1);
        command.getParameters().add("_email", PgsqlDbType.Text).setValue("femkedijkema@hotmail.com");
        command.getParameters().add("_personid", PgsqlDbType.Integer).setValue(3);

        command.setRoutineType(RoutineType.Scalar);
        command.setOutgoingEncodingType(EncodingType.NONE);

        return command;
    }

    public String runTest2()
    {
        Command command = this.getCommand2();

        try {
            String xmlResult = Command.execute(command,
                    RoutineType.Scalar,
                    HttpMethod.GET,
                    ResponseFormat.JSON,
                    CompressionType.NONE,
                    CompressionType.NONE);
            System.out.println(xmlResult);

            return xmlResult;
        } catch (RestServiceException ex){
            System.out.println("code: "+ex.getCode()+", message: "+ex.getMessage());
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }

        return null;
    }


    public String runTest3() {
        Command command = new Command("migration.help_ws_es_int64_array_test");

        try {
            Object value = Command.execute(command,
                    RoutineType.Scalar,
                    HttpMethod.POST,
                    ResponseFormat.XML);
            String result = value.toString();

            System.out.println(result);

            return result;
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        return null;
    }
}
