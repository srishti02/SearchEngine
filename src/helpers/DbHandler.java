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
 * @author Siddharth
 * @description Class to handle connection, information insertion and retrieval
 *              to/from database.
 */
public class DbHandler {

	private static String ip;
	private static String port;
	private static String user;
	private static String password;

	final String TableName = "url_table";

  /**
   * @brief Parameterized Constructor for DBHandler
   */
	public DbHandler(String ip,String port,String user,String password){

		DbHandler.ip = ip;
		DbHandler.port = port;
		DbHandler.user = user;
		DbHandler.password = password;

	}

  /**
   * @brief Function for establishing connection with the database
   *
   * @return Connection
   */
	public Connection connection_estabilish() {
		Connection c = null ;
		/**Configuring the connection with the database*/
		try {
			Class.forName("org.postgresql.Driver");
			/**Basic service for managing set of JDBC Drivers */
			c = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/postgres",
					user ,password );
			System.out.println("Opened database successfully");

		}catch ( Exception e ) {
			/**Printing exception if any */
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		return c;		

	}

	/** 
   * @brief Function for insertion into the table with HashMap value as argument
   *        Steps : 
   *        Check if table exists, else create table
   *        truncate table to avoid data duplicacy
   *        insert into db
   *
   * @param links : hashmap containg links vs list of referenced links to be
   *                stored in database.
   */                
	public void insertion(HashMap<String, List<String>> links) {

		long start,end;
		Connection c = connection_estabilish();
		try {

			/**Query to create url table in the database*/
			String create_url_table = "Create Table " + TableName + " (url text primary key,refrenced_url text[])";
			PreparedStatement createTableSt = c.prepareStatement(create_url_table);
			DatabaseMetaData metaData = c.getMetaData();
			String[] types = {"TABLE"};
			ArrayList<String> all_tables = new ArrayList<String>();
			/**To fetch all the table names in the current database schema*/
			ResultSet rs = metaData.getTables(null, null, "%",types );
			/**Iterating through the result set to get the table names */
			while(rs.next())
			{
				all_tables.add(rs.getString(3));
			}
			/**To verify if the table name is already present in the current database*/
			if(!all_tables.contains("url_table"))
			{
				System.out.println("Create table");
				ResultSet rs2 = createTableSt.executeQuery();
			}


			/** Truncate all previous records using Prepared statement */
			start = System.currentTimeMillis();
			String sql = "TRUNCATE " + TableName; 
			PreparedStatement statement = c.prepareStatement(sql); 
			statement.executeUpdate();
			end = System.currentTimeMillis();
			System.out.println("Truncated Table Successfully");
			System.out.println("Truncate Time Taken: " +(end-start)+"ms");


			/** Insert query using Prepared statement for the security purpose */
			start = System.currentTimeMillis();
			String sql1 = "INSERT INTO " + TableName + " VALUES (?, ?)";
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
			end = System.currentTimeMillis();
			System.out.println("Insert Time Taken: " +(end-start)+"ms");

		}catch ( Exception e ) {
			/**Printing exception if any */
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}
		System.out.println("Inserted Data into Table Successfully");
	}

	/**
   * @brief Function for searching from the tble
   *
   * @return HashMap containg links vs links refered by the key link
   * */
	public HashMap<String, List<String>> search() {

		long start,end;
		Connection c = connection_estabilish();
		HashMap<String, List<String>> hashMap
			= new HashMap<String, List<String>>();

		try {
			start = System.currentTimeMillis();
			/** Search query using Prepared statement and the ResultSet */
			String sql2 = "SELECT * FROM " + TableName;
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
				//System.out.println("hashmap: " + hashMap + hashMap.size()); 
			}
			end = System.currentTimeMillis();
			System.out.println("Search Time Taken: " +(end-start) +"ms");
			//System.out.println("Counter: "+count);
		}catch ( Exception e ) {
			/**Printing exception if any */
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
		}	
		System.out.println("Retrived Data from Table Successfully");
		return hashMap;
	}
}

