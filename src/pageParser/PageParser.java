package pageParser;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Scanner;
import java.util.StringTokenizer;

import helpers.DbHandler;
import helpers.HtmlToText;
import helpers.Structures.UrlStruct;
import patternMatching.NetworkTST;
import webCrawler.Crawler;

/**
 * @description Class to perform parsing operations on a url page retrived from
 *              DB.
 *
 *              Responsibilities of class:
 *              - Using HtmlToText to get base data for pattern matching.
 *              - Providing data to page ranker.
 */
public class PageParser {

  private static NetworkTST tst = new NetworkTST();

  public static void parse(HashSet<String> urls)
  {
    /** get content of all urls as text*/
    HashMap<String,String> urlsContent =
      HtmlToText.convertToText(urls);

    /** for each url*/
    Iterator contentItr = urlsContent.entrySet().iterator();
    while(contentItr.hasNext())
    {
      HashMap.Entry entry = (HashMap.Entry) contentItr.next();

      /** get tokens from content of each url*/
      StringTokenizer tokenizer = new StringTokenizer((String) entry.getValue()," ,.()");

      /** for all tokens*/
      while(tokenizer.hasMoreElements())
      {
        /** get next token*/
        String token = tokenizer.nextToken();

        /**Insert to TST*/
        tst.put(token, new UrlStruct((String)entry.getKey()));
      }
    }
  }

  public static void main(String[] args) 
  {
	  System.out.println("Please enter your database details  :");
	  Scanner input = new Scanner(System.in);
	  System.out.println("Enter ip :");
	  String ip = input.nextLine();
	  System.out.println("Enter port :");
	  String port = input.nextLine();
	  System.out.println("Enter username :");
	  String user = input.nextLine();
	  System.out.println("Enter password :");
	  String password = input.nextLine();
	  System.out.println("\n");
	  System.out.println("Want to Crawl? : ");
	  Boolean crawl = input.nextBoolean();

	  HashSet<String> keyset;
	  Map<String, List<String>> urlMap = new HashMap<String, List<String>>();
	  DbHandler db = new DbHandler(ip,port,user,password);
	  if(crawl == true)
	  {
		  Crawler webCrawler = new Crawler(10);
		  System.out.println(Instant.now());
		  webCrawler.crawl("http://ask.uwindsor.ca");
		  db.insertion(webCrawler.getReferencedLinksMap());
		  System.out.println("Crawling completed & inserted to database");
		  keyset = webCrawler.getUrls();
	  }
	  else
	  {
		  urlMap = db.search();
		  keyset = new HashSet<String>(urlMap.keySet());
	  }
	  parse(keyset);
	  System.out.println(Instant.now());
  }
}
