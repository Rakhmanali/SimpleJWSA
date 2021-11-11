import jwsa.*;
import jwsa.exceptions.RestServiceException;

public class NonQueryExample {

    public Command getCommand1()
    {
        return this.getCommandEx1();
    }

    public CommandEx getCommandEx1()
    {
        CommandEx command = new CommandEx("companymanager_updatecompany");
        command.getParameters().add("_companyid", PgsqlDbType.Integer, 1622);
        command.getParameters().add("_companyname", PgsqlDbType.Varchar).setValue("reseller4");
        command.getParameters().add("_companytypeid", PgsqlDbType.Integer).setValue(3);
        command.getParameters().add("_businessid", PgsqlDbType.Integer).setValue(1);
        command.getParameters().add("_email", PgsqlDbType.Varchar).setValue("default@mail.com");
        command.getParameters().add("_url", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_phone", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_addressid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_address", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_housenumber", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_housenumberadd", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_zipcode", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_city", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_countryid", PgsqlDbType.Integer).setValue(0);
        command.getParameters().add("_addressidv", PgsqlDbType.Integer, 0);
        command.getParameters().add("_addressv", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_housenumberv", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_housenumberaddv", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_zipcodev", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_cityv", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_countryidv", PgsqlDbType.Integer).setValue(0);
        command.getParameters().add("_accountmanagerid", PgsqlDbType.Integer).setValue(0);
        command.getParameters().add("_replaceaccountmanagerid", PgsqlDbType.Boolean).setValue(false);
        command.getParameters().add("_clientstatus", PgsqlDbType.Integer, 5);
        command.getParameters().add("_replaceclientstatus", PgsqlDbType.Boolean).setValue(false);
        command.getParameters().add("_holdingid", PgsqlDbType.Integer, 1624);
        command.getParameters().add("_taxnumber", PgsqlDbType.Varchar, "");
        command.getParameters().add("_chambercommercenumber", PgsqlDbType.Varchar, "");
        command.getParameters().add("_paymentdays", PgsqlDbType.Integer, 0);
        command.getParameters().add("_creditline", PgsqlDbType.Numeric, 0.0);
        command.getParameters().add("_authorizationrequired", PgsqlDbType.Boolean, false, false);
        command.getParameters().add("_vatexempt", PgsqlDbType.Boolean).setValue(false);
        command.getParameters().add("_financialsid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_directorid", PgsqlDbType.Integer,0);
        command.getParameters().add("_businessgroupid", PgsqlDbType.Integer, true).setValue(null);
        command.getParameters().add("_addedclientids", PgsqlDbType.Integer , true).setValue(null);
        command.getParameters().add("_deletedclientids", PgsqlDbType.Integer, true ).setValue(new int[]{});
        command.getParameters().add("_hasstock", PgsqlDbType.Boolean, false, false);
        command.getParameters().add("_iban", PgsqlDbType.Varchar, "");
        command.getParameters().add("_bic", PgsqlDbType.Varchar, "");
        command.getParameters().add("_latitude", PgsqlDbType.Numeric, 0);
        command.getParameters().add("_longitude", PgsqlDbType.Numeric, 0);
        command.getParameters().add("_latitudev", PgsqlDbType.Numeric, 0);
        command.getParameters().add("_longitudev", PgsqlDbType.Numeric, 0);
        command.getParameters().add("_discountgroupid", PgsqlDbType.Integer, null);
        command.getParameters().add("_fax", PgsqlDbType.Varchar).setValue("");
        command.getParameters().add("_factoring", PgsqlDbType.Boolean, false, false);
        command.getParameters().add("_deliverytimeinstock", PgsqlDbType.Integer, 0);
        command.getParameters().add("_deliverytimenotinstock", PgsqlDbType.Integer, 0);
        command.getParameters().add("_stockid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_inactive", PgsqlDbType.Boolean, false, false);
        command.getParameters().add("_invoicetoholding", PgsqlDbType.Boolean, false, false);
        command.getParameters().add("_measurementid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_languageid", PgsqlDbType.Integer, 1);
        command.getParameters().add("_billingmethodid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_freedeliveryamount", PgsqlDbType.Numeric, 0.0);
        command.getParameters().add("_bundle", PgsqlDbType.Boolean).setValue(false);
        command.getParameters().add("_currencyid", PgsqlDbType.Integer, 1);
        command.getParameters().add("_ordertypeid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_invoicegroupingid", PgsqlDbType.Integer, 0);
        command.getParameters().add("_resellerid", PgsqlDbType.Integer, null);

        command.getParameters().add("_shipment", PgsqlDbType.Text, null);
        command.getParameters().add("_warehouse", PgsqlDbType.Text, null);

        command.getParameters().add("_errmes", PgsqlDbType.Text);
        command.getParameters().add("_returnvalue", PgsqlDbType.Integer);

        command.setRoutineType(RoutineType.NonQuery);
        command.setCommandTimeout(1800);
        command.setOutgoingEncodingType(EncodingType.BASE64);

        return command;
    }

    public String runTest1()
    {
        Command command = this.getCommand1();

        try {
            String xmlResult = Command.execute(command,
                    RoutineType.NonQuery,
                    HttpMethod.POST,
                    ResponseFormat.JSON);
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

    public Command getCommand2()
    {
        return this.getCommandEx2();
    }

    public CommandEx getCommandEx2()
    {
        CommandEx command = new CommandEx("brandmanager_hidebrand");
        command.getParameters().add("_brandid", PgsqlDbType.Integer, 13);
        command.getParameters().add("_ishidden", PgsqlDbType.Boolean).setValue(false);
        command.getParameters().add("_returnvalue", PgsqlDbType.Integer);

        command.setRoutineType(RoutineType.NonQuery);
        command.setOutgoingEncodingType(EncodingType.BASE64);

        return command;
    }

    public String runTest2()
    {
        Command command = this.getCommand2();

        try {
            String xmlResult = Command.execute(command,
                    RoutineType.NonQuery,
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

        return null;
    }


    public String RunTest3() {
        Command command = new Command("migration.help_ws_en_inout_array");

        command.getParameters().add("p_parameter1", PgsqlDbType.Integer, true, new Integer[]{1, 2, 3, 4});
        command.getParameters().add("p_parameter2", PgsqlDbType.Varchar, true, new String[]{"hello", "naiton", "clients"});

        command.getParameters().add("p_parameter3", PgsqlDbType.Integer, true);
        command.getParameters().add("p_parameter4", PgsqlDbType.Varchar, true);
        command.getParameters().add("p_parameter5", PgsqlDbType.Varchar);
        command.getParameters().add("p_parameter6", PgsqlDbType.Integer);
        command.getParameters().add("p_parameter7", PgsqlDbType.Timestamp);
        command.getParameters().add("p_parameter8", PgsqlDbType.TimestampTZ, true);
        command.getParameters().add("_returnvalue", PgsqlDbType.Integer);

        try {
            String xmlResult = Command.execute(command,
                    RoutineType.NonQuery,
                    HttpMethod.GET,
                    ResponseFormat.XML);

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
}
