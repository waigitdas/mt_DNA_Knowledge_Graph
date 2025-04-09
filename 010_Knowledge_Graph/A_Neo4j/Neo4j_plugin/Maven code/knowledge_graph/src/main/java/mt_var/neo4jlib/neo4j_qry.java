/**
 * Copyright 2021-2023 
 * David A Stumpf, MD, PhD
 * Who Am I -- Graphs for Genealogists
 * Woodstock, IL 60098 USA
 */
package mt_var.neo4jlib;
    import java.util.ArrayList;
    import org.neo4j.driver.Result;
    import org.neo4j.driver.Session;    
    import java.util.List;       
    import java.util.*;  
    import java.util.function.Function;
import java.util.regex.Pattern;
    import org.neo4j.driver.Record;
    import org.neo4j.driver.Value;

public class neo4j_qry {
    
    
    public static void CreateIndex(String nodeNm,String propertyNm){
            try{
                String cq ="CREATE INDEX " + nodeNm + "_" + propertyNm + " FOR (n:" + nodeNm + ") ON (n." + propertyNm + ")";
                qry_write(cq);
            }
            catch (Exception e) 
            {
                int www=0;
            }
    }   

    public static void CreateCompositeIndex(String nodeNm,String propertyNmList){
            try{
                String[] n = propertyNmList.split(",");
                String s = "";
                for (int i=0; i < n.length; i++) {
                    s = s + "n." + n[i];
                    if (i<n.length-1) {s = s + ",";}
                }
                String cq ="CREATE INDEX " + nodeNm + "_" + s.replace(",","_").replace("n.","").replace(" ","")  + " FOR (n:" + nodeNm + ") ON (" + s + ")";
                
                qry_write(cq); 
            }
            catch (Exception e) {}
    }   


    public static void CreateRelationshipIndex(String relationship_type,String relationship_property){
            try{
                String cq ="CREATE INDEX rel_" + relationship_type + "_" + relationship_property + " FOR ()-[r:" + relationship_type + "]-() ON (r." + relationship_property + ")";
                qry_write(cq);
            }
            catch (Exception e) {}
    }   

    
    public static void qry_write (String cq) {
        mt_var.conn.connTest.cstatus();
        Session session =  mt_var.conn.connTest.session;
           
            {
            
                session.executeWrite( tx -> {
                   Result result = tx.run( cq );
                   //session.close();
               return 1;
               
                } );
            } 
           }
   
//    public static void qry_write_qry (String cq) {
//        mt_var.neo4jlib.neo4j_info.wrt = mt_var.neo4jlib.neo4j_info.wrt + cq + "\n" ;  //with xxx\n";
//    }
//   

   
   //****************************************************
   
    public static String qry_to_csv(String cq) {
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;

        return java_session.executeRead(tx -> {
        String c = "";          
             //int rw = 0;
            Result result = tx.run(cq.replace("[[","[").replace("]]","]") );
            while ( result.hasNext() )
            {
                Record r = result.next();
           
                List<Value> v =  r.values();
                  for (int i = 0; i < v.size(); i++) {
                  c = c + String.valueOf(r.values().get(i));
                  if (i <v.size()-1) { c = c + ",";
                  }
                  else { c = c + "\n"; }
                 }
                    
                }
            mt_var.neo4jlib.file_lib.writeFile("**\n" + c  + "\n**\n\n" + cq, "c://temp/csv1.csv");
            //java_session.close();
            return c;
        }) ;
    }
     
    public static String qry_to_pipe_delimited_str(String cq) {
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;

        return java_session.executeRead( tx -> {
            String c = "";
             //int rw = 0;
            Result result = tx.run(cq.replace("[[","[").replace("]]","]") );
            while ( result.hasNext() )
            {
                Record r = result.next();
           
                List<Value> v =  r.values();
                  for (int i = 0; i < v.size(); i++) {
                  c = c + String.valueOf(r.values().get(i));
                  if (i <v.size()-1) { c = c + "|";
                  }
                  else { c = c + "\n"; }
                 }
                    //rw = rw + 1;
                }
            //java_session.close();
            return c;
        }) ;
        
        
        
    }
     
   public static List<String> qry_str_list(String cq) {
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;
        
        return java_session.executeRead(tx -> {
            List<String> names = new ArrayList<>();
            Result result = tx.run(cq );
            while ( result.hasNext() )
            {
                names.add( result.next().get( 0 ).asString() );
            }
            //java_session.close();
           return names;
        } );
   }
   
public static <T> List<T> readCyphers(String cq, Function<Record, T> mapper) {
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;
        try (java_session) {
            Result result = java_session.run(cq);
            //java_session.close();            
            return result.list(mapper);
        }
    }
   
public static Result qry_obj_all(String cq) {
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;
        
        return java_session.executeRead( tx -> 
        {
            Result result = tx.run(cq );
            List lr = result.list();
            //java_session.close();             
            return result; 
        } 
        );
   }
      

//****************************************************
  public static List<Long> qry_long_list(String cq) {
    // Test connection status (can be adjusted depending on your need)
    mt_var.conn.connTest.cstatus();
    
    // Ensure session is available and open
    Session java_session = mt_var.conn.connTest.session;
    
    // Create a list to hold the results
    List<Long> names = new ArrayList<>();
    
    // Execute the transaction to run the query
    try {
        java_session.readTransaction(tx -> {
            // Execute the query
            Result result = tx.run(cq);
            
            // Iterate through the result and collect the Long values
            while (result.hasNext()) {
                // Assuming the first column contains the probe IDs
                List<Long> probeIds = result.next().get("pids").asList(Value::asLong); // Get the pids list
                names.addAll(probeIds); // Add all probe IDs to the names list
            }
            return null; // Nothing to return, as results are directly stored in 'names'
        });
    } catch (Exception e) {
        // Log or print error if query execution fails
        System.err.println("Error executing query: " + e.getMessage());
        e.printStackTrace();
    } finally {
        // Optionally, you can close the session if necessary (depends on your connection management)
        // java_session.close(); // Uncomment if session should be closed manually
    }
    
    return names; // Return the list of Long values (probe IDs)
}


    public static List<Object> qry_obj_list(String cq) {
 
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;


        return java_session.executeRead( tx -> {
            List<Object> names = new ArrayList<>();
            Result result = tx.run(cq );
            while ( result.hasNext() )
            {
                names.add(result.next().get( 0 ).asObject() );
            }
            //java_session.close();
            return names;
        } );
   }
   
      public static String qry_str(String cq) {
 
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;


        return java_session.executeRead( tx -> {
            String names = "";
            Result result = tx.run(cq );
            while ( result.hasNext() )
            {
                names = names + result.next().values().toString() + "; ";
            }
            names = names.substring(0, names.length()-2);
            //java_session.close();
            return names;
        } );
   }
      
      public static String qry_to_pipe_delimited(String cq,String csv_File) {
        String Q = "\"";
        String q = "call apoc.export.csv.query(" + Q + cq + Q + ",'" + csv_File + "' , {delim:'|',  quotes: false, format: 'plain'})"; 
        mt_var.conn.connTest.cstatus();
         Session java_session =  mt_var.conn.connTest.session;

        return java_session.executeRead( tx -> {
         
            tx.run(q);
  
                        
            //java_session.close();
             return csv_File;
        } 
            );
   }
   
 public static String qry_to_csv(String cq,String csv_File) {
        mt_var.conn.connTest.cstatus();
        Session java_session =  mt_var.conn.connTest.session;

            return java_session.executeRead(tx -> {
            String Q = "\"";
            String q = "call apoc.export.csv.query(" + Q  + cq + Q + ",'" + csv_File + "' , {delim:',', quotes: true, format: 'plain'})"; 
                        
            tx.run(q);
      
            //java_session.close();
            return csv_File;
        } 
            );
     
   }

public static String APOCPeriodicIterateCSV(String LoadCSV, String ReplaceCypher, int batchsize) {
    //use parallel = false to avoid deadlocks!
    //optimize with specific query design:
    //https://neo4j.com/developer/kb/a-significant-change-in-apoc-periodic-iterate-in-apoc-4-0
    String Q = "\"";
    String csv = "CALL apoc.periodic.iterate(" + Q + LoadCSV + Q + ", " + Q + ReplaceCypher + Q + ",{batchSize: " + batchsize + ", parallel:false, iterateList:true, retries:25})";
    
    qry_write(csv);
            return csv; 
}

 public static void addRowNumbertoNode(String id_name, String c1_for_n_sorted_node)
 {
     String cq = c1_for_n_sorted_node + " WITH n  WITH collect(n) AS nodes UNWIND range(0, size(nodes) - 1)  AS index WITH nodes[index] AS node, index + 100001 AS rowNumber SET node." + id_name + "= rowNumber";
     mt_var.neo4jlib.neo4j_qry.qry_write(cq);
 }

}
        