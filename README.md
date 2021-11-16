## The work with the data access web service by the SimpleJWSA library

The short description

The data access web service allows executing the PostgreSql functions by the HTTP
requests. To perform it a client should have the appropriate credentials. If credentials
okay, then the data access web service creates a session, and returns a token.
To perform the request the client should select the PostgreSql function, fill parameters
with appropriate values, attach the token (this library does it itself), and make the
HTTP request.
The SimpleJWSA library has been created to simplify this work.

### 1. Session

The data access web service can provide data access to the group of databases. And there
can be different instances of web service for different groups of databases.

If it is known the database and the web service address extracting data from it, then it is possible
to create a session by the web service address:

  ```java
      ...
        
      Session session = new Session("<login>",
                                    "<password>",
                                    false,
                                    <app id>,
                                    "<app version>",
                                    "<domain>",
                                    <web proxy>);
        try {
            session.createByRestServiceAddress("<replace it with the web service address>");
        } catch( Exception ex) {
            ex.printStackTrace();
        }
        
	  ...
  ```

The second case is the case when known the domain name (the company name) and the connection provider
address. Then the following code creates the session:

  ```java
      ...
        
      Session session = new Session("<login>",
                                    "<password>",
                                    false,
                                    <app code>,
                                    "<app version>",
                                    "<domain>",
                                    <web proxy>);
        try {
            session.createByConnectionProviderAddress("<replace it with the connection provider address>");
        } catch( Exception ex) {
            ex.printStackTrace();
        }
        
	  ...
  ```

### 2. How to call a PostgreSql function returning the scalar data

The following code is an example of how to get the scalar data:

   ```java
      ...
        
        Command command = new Command("clientmanager_findclientbyemailbusiness");
        command.getParameters().add("_businessid", PgsqlDbType.Integer).setValue(1);
        command.getParameters().add("_email", PgsqlDbType.Text).setValue("femkedijkema@hotmail.com");
        command.getParameters().add("_personid", PgsqlDbType.Integer).setValue(3);

        // the HTTP GET is default
        command.setHttpMethod(HttpMethod.GET);

        // a result in the JSON format is default
        command.setResponseFormat(ResponseFormat.JSON);

        // specifies the text-based data be encoded before sending,
        // the default is EncodingType.NONE, i.e. sends as is
        command.setOutgoingEncodingType(EncodingType.NONE);

        // allows compressing data in the HTTP POST before sending,
        // the default is CompressionType.NONE, i.e. as is
        command.setOutgoingCompressionType(CompressionType.NONE);

        // requires responding data be compressed,
        // the default is CompressionType.NONE, i.e. as is
        command.setReturnCompressionType(CompressionType.NONE);

        try {
            String xmlResult = Command.execute(command, RoutineType.Scalar);
            System.out.println(xmlResult);
        } catch (RestServiceException ex) {
            System.out.println("code: " + ex.getCode() + ", message: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
	  ...
   ```

the response in XML format:

   ```java
      <clientmanager_findclientbyemailbusiness drn='00:00:00.0033761'><returnValue>576887</returnValue></clientmanager_findclientbyemailbusiness>
   ```

the same only in JSON format:

   ```java
      {"clientmanager_findclientbyemailbusiness":{"returnValue":576887}}
   ```

### 3. How to call the PostgreSql function returning the data in the out parameters

```java
      ...
        
        Command command = new Command("brandmanager_hidebrand");
        command.getParameters().add("_brandid", PgsqlDbType.Integer, 13);
        command.getParameters().add("_ishidden", PgsqlDbType.Boolean).setValue(false);

        // out parameter
        command.getParameters().add("_returnvalue", PgsqlDbType.Integer);
        
        command.setOutgoingEncodingType(EncodingType.BASE64);

        try {
            String xmlResult = Command.execute(command,
                    RoutineType.NonQuery,
                    HttpMethod.POST,
                    ResponseFormat.XML,
                    CompressionType.NONE,
                    CompressionType.NONE);
            System.out.println(xmlResult);
        } catch (RestServiceException ex) {
            System.out.println("code: "+ex.getCode()+", message: "+ex.getMessage());
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
	  ...
```

the response in XML format:

   ```java
      <brandmanager_hidebrand drn='00:00:00.0039548'><returnValue>-1</returnValue><arguments><_returnvalue>0</_returnvalue></arguments></brandmanager_hidebrand>
   ```

the same only in JSON format:

   ```java
      {"brandmanager_hidebrand":{"returnValue":-1,"arguments":{"_returnvalue":0}}}
   ```

RoutineType.NonQuery uses to call PostgreSql functions with OUT or INOUT parameters. In the example above "_returnvalue" is OUT parameter and the response contains
the appropriate value - 0.


### 4. How to call the PostgreSql function returning the data set

```java
      ...
        
        Command command = new Command("companymanager_getresellers");
        command.getParameters().add("_businessid", PgsqlDbType.Integer, 1);
        command.getParameters().add("_companyid", PgsqlDbType.Integer, 13);

        // applies when ResponseFormat.XML
        command.setWriteSchema(WriteSchema.TRUE);
        command.setResponseFormat(ResponseFormat.XML);

        command.setHttpMethod(HttpMethod.GET);
        try {
            String xmlResult = Command.execute(command,
            RoutineType.DataSet);
            
            System.out.println(xmlResult);
        } catch (RestServiceException ex) {
            System.out.println("code: " + ex.getCode() + ", message: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
      ...
```

### 5. How to call more than one the same type of PostgreSql functions

For example, let's describe how to call two PostgreSql functions eveach of them
returns the set of data:

```java
      ...

	  // the same type of routines in one HTTP request

        Command command1 = new Command("companymanager_getresellers");
        command1.getParameters().add("_businessid", PgsqlDbType.Integer).setValue(1);
        command1.getParameters().add("_companyid", PgsqlDbType.Integer, 13);

        // applies when ResponseFormat.XML
        command1.setWriteSchema(WriteSchema.TRUE);

        Command command2 = new Command("currencymanager_getbusinessessuppliers");
        command2.getParameters().add("_currencyid", PgsqlDbType.Integer, 1);

        // applies when ResponseFormat.XML
        command2.setWriteSchema(WriteSchema.TRUE);

        try {
            String xmlResult = Command.ExecuteAll(Arrays.asList(command1, command2),
                    RoutineType.DataSet,
                    ResponseFormat.XML,
                    CompressionType.NONE,
                    CompressionType.GZip,
                    ParallelExecution.TRUE);
            System.out.println(xmlResult);
        } catch (RestServiceException ex) {
            System.out.println("code: " + ex.getCode() + ", message: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
      ...
```

The value "ParallelExecution.TRUE" of the parameter "parallelExecution" instructs the server
to execute the PostgreSql functions "companymanager_getresellers" and
"currencymanager_getbusinessessuppliers" parallely.

### 6. There is possible to execute different type of PostgreSql functions

```java
      ...
        
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
        
      ...
```