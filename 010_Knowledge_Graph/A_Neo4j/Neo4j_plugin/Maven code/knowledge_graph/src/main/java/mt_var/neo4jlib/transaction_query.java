package mt_var.neo4jlib;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.Result;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class transaction_query {

    private static Driver driver;

    // Initialize the Neo4j Java Driver
    public static void initializeDriver(String uri, String user, String password) {
        if (driver == null) {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
        }
    }

    @UserFunction
    @Description("Executes a Cypher query and ensures it completes before returning.")
    public String execute_cypher_query(
        @Name("query") String query
    ) {
        // Ensure the driver is initialized before running queries
        initializeDriver("bolt://localhost:7687", mt_var.neo4jlib.neo4j_info.neo4j_username, mt_var.neo4jlib.neo4j_info.neo4j_password);

        // Open a session and execute the Cypher query
       try{
        mt_var.conn.connTest.cstatus();
        Session session =  mt_var.conn.connTest.session;
   
        //try (Session session = driver.session()) {
            return session.writeTransaction(new TransactionWork<String>() {
                @Override
                public String execute(Transaction tx) {
                    Result result = tx.run(query);
                       
                    tx.commit(); // Ensure the transaction is committed
                    return "Query completed successfully";
                }
            });
        } catch (Exception e) {
            return "Error executing query: " + e.getMessage();
        }
    }

    // Optionally, provide a method to shut down the driver properly
    public static void shutdownDriver() {
        if (driver != null) {
            driver.close();
        }
    }
}