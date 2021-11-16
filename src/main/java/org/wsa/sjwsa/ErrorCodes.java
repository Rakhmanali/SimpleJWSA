package org.wsa.sjwsa;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodes {
    final public static Map<String, String> collection = new HashMap<>() {
        {
            put("MI001", "Service function name is empty");
            put("MI002", "Login or password is empty or wrong");
            put("MI003", "Credentials format error. Use one from - token or login/password for specifying credentials");
            put("MI004", "Login/password or token is empty or wrong");
            put("MI005", "Token is empty or wrong");
            put("MI006", "Service function name is wrong");
            put("MI007", "Token is empty");
            put("MI008", "Session has expired");
            put("MI009", "Using token by other device");
            put("MI010", "Login or password is empty");
            put("MI011", "Unable to login. Please, check the login and the password");
            put("MI012", "Error in loading of verification fields");
            put("MI013", "Can't get the connection, login or password is wrong");
            put("MI014", "Can't login, login or password is wrong");
            put("MI015", "System exception");
            put("MI016", "Restriction, you can not use the value for parameter in the given request");
            put("MI017", "Restricted function, you can not use this finction ");
            put("MI018", "Query string is empty or wrong");
            put("MI019", "Query string is not well format");
            put("MI020", "Parsing Query string as a XML error");
            put("MI021", "SESSION is empty for given token");
            put("MI022", "SESSION is empty for given login");
            put("MI023", "This account has been temporary blocked due to too many tries. The account will be automatically unblocked after few minutes");
            put("MI024", "This account has been temporary suspended due to too many attempts of the function calling with wrong parameters or calling function which doesn't exist");
            put("MI025", "Cannot serialize the Session object to JSON");
            put("MI026", "Unable to login. You cannot login with your current IP address");
            put("MI027", "There is no one session");
            put("MI028", "Cannot specify the file name");
            put("MI029", "The file does not exist");
            put("MI030", "Cannot specify the Large Object Id");
            put("MI031", "Not valid Large Object Id");
            put("MI032", "The local storage has not have the connection string for the given domain");
            put("MI033", "Cannot get the domain from the InitializeSession request");
            put("MI034", "Unable to start the work. Please, check the company name");

            put("EX001", "System exception");
            put("EX002", "PostgreSql function is not return set function");
            put("EX003", "PostgreSql function is return set function");
            put("EX004", "Query string is not well format");
            put("EX005", "RoutineType key is not specified or has a wrong value");
            put("EX006", "Restriction, you can not use the value for parameter in the given request");
            put("IS001", "InitilizeSession POST contains bad XML request");
        }
    };
}
