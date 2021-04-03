package searchEngine;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

import helpers.DbHandler;
import helpers.Structures.UrlStruct;
import pageParser.PageParser;
import webCrawler.Crawler;

/**
 * @author Margaret, Siddharth, Srishti
 * @brief Class to act as the main search engine.
 *        Responsibilities -
 *        1. User input / output
 *        2. Delegating work to other classes.
 */
public class MainApplication {

  public static void main(String[] args) 
  {
    /** take configuration inputs from user*/
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

    /** if user wants the application to crawl*/
    if(crawl == true)
    {
      /** create crawler object*/
      Crawler webCrawler = new Crawler(10);
      System.out.println(Instant.now());

      /** start crawling*/
      webCrawler.crawl("http://ask.uwindsor.ca");

      /** start a thread to asynchronously perform insertion to database*/
      new Thread( () -> {
        db.insertion(webCrawler.getReferencedLinksMap());
      }).start();

      System.out.println("Crawling completed : " + Instant.now());
      keyset = webCrawler.getUrls();
    }
    /** if crawling is disabled*/
    else
    {
      /** get urls from db*/
      urlMap = db.search();
      keyset = new HashSet<String>(urlMap.keySet());
    }

    System.out.println("Parsing started : " + Instant.now());
    PageParser.parse(keyset);
    System.out.println("Parsing Completed : " + Instant.now());

    /** Get keywords from user to search*/
    while(true)
    {
      Scanner input1 = new Scanner(System.in);
      String keyword = "";
      while(keyword.isEmpty())
      {
        System.out.println("Enter keyword to search: ");
        keyword = input1.nextLine();
      }
      
      TreeSet<UrlStruct> data = PageParser.search(keyword);
      int count = 0;
      for(UrlStruct str : data.descendingSet())
      {
        if(++count > 10)
          break;
        System.out.println(str.url + " : " + str.rank);
      }
    }
  }
}
