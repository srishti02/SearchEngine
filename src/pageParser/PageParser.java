package pageParser;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import helpers.HtmlToText;
import patternMatching.TST;
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

	private static TST<String> tst = new TST<String>();
	
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
        tst.put(token,null);
      }
    }
  }
  
  public static void main(String[] args) 
  {
	System.out.println(Instant.now());
    Crawler webCrawler = new Crawler(1000);
    webCrawler.crawl("http://ask.uwindsor.ca");
    System.out.println("Crawling completed");
    System.out.println(Instant.now());
    parse(webCrawler.getUrls());
    System.out.println(Instant.now());
  }
}
