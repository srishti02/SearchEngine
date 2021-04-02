package helpers;

import webCrawler.Crawler;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 
 * @description Singleton class to handle connection, information insertion and retrieval
 *              to/from database.
 */
public class DbHandler {
	
	
	 /** Function for establishing connection with the database*/
	private static Connection connection_estabilish() {
		Connection c = null ;
		 /**Configuring the connection with the database*/
		try {
	         Class.forName("org.postgresql.Driver");
	         /**Basic service for managing set of JDBC Drivers */
	         c = DriverManager
	                 .getConnection("jdbc:postgresql://127.0.0.1:5432/postgres",
	                 "postgres", "test");
	         System.out.println("Opened database successfully");
	         
		}catch ( Exception e ) {
			/**Printing exception if any */
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
         }
		return c;		
	}
	
	 /** Function for insertion into the table with HashMap value as argument */
	public static void insertion(HashMap<String, List<String>> links) {
		
		Connection c = connection_estabilish();
		try {
			 /** Insert query using Prepared statement for the security purpose */
			String sql1 = "INSERT INTO url_table VALUES (?, ?)";
	        PreparedStatement pstmt = c.prepareStatement(sql1);
	        
	        /** Iterating the HashMap entries */
	        Iterator iter = links.entrySet().iterator();
	         while (iter.hasNext()) {
	        	 /** Splitting Keys and Values pair */
	             Map.Entry entry = (Map.Entry) iter.next();
	            // System.out.println("[Key] : " + entry.getKey() + " [Value] : " + entry.getValue());

		     List<String> values1 = (List<String>) entry.getValue();
		      //Converting ArrayList to Array
		      String[] arr = new String[values1.size()];
	            
	             for (int i =0; i < values1.size(); i++)
		     {
		     	     arr[i] = values1.get(i);
		     }
	             //Creating an array object 
	             Array value = c.createArrayOf("text", arr);
	             //Inserting the obtained keys and values to the table named url_table  
	             pstmt.setString(1,(String) entry.getKey());
		         pstmt.setArray(2, value);
		         pstmt.executeUpdate(); 
	         }     
		}catch ( Exception e ) {
			/**Printing exception if any */
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
         }	
		System.out.println("Inserted Data into Table Successfully");
	}
	
	/** Function for searching from the tble */
	public static Map<String, List<String>> search() {
		
		Connection c = connection_estabilish();
		Map<String, List<String>> hashMap
        = new HashMap<String, List<String>>();
		
		try {
			/** Search query using Prepared statement and the ResultSet */
			String sql2 = "SELECT * FROM url_table";
	         PreparedStatement pstmt2 = c.prepareStatement(sql2);
	         ResultSet rs = pstmt2.executeQuery();
	         //int count = 0;
	        /** Iterating through the result of search */
	         while(rs.next()) {
	        	 
	        	 /** Obtaining value pairs in the form of array  */
	        	 Array ref_url = rs.getArray(2);
	        	 String[] referneced = (String[])ref_url.getArray(); 
	        	 
	        	 /** Converting array to ArrayList  */
	        	 List<String> myList = new ArrayList();
	        	 Collections.addAll(myList, referneced);
	        	 
	        	 // count=count+1;	
	        	 /** Converting ArrayList to HashMap entries including key-value pairs  */
	        	 hashMap.put(rs.getString(1),myList);
	        	 System.out.println("hashmap: " + hashMap + hashMap.size());  
	         } 
	         //System.out.println("Counter: "+count);
			}catch ( Exception e ) {
				/**Printing exception if any */
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
         }	
		    System.out.println("Retrived Data from Table Successfully");
			return hashMap;
		}
	
	
	public static void main( String args[] ) throws IOException {
	 	              
		/**Creation of object of Crawler.java   */
		  Crawler webCrawler = new Crawler(10);
	      webCrawler.crawl("http://ask.uwindsor.ca");
	  	/**Fetching HashMap results including key-value airs using the crawl function   */  
	      HashMap<String, List<String>> links = webCrawler.getReferencedLinksMap(); 
	    /**Calling above created methods   */ 
	      insertion(links);
	      search();
	}
}

