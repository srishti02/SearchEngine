package pageParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import helpers.HtmlToText;
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

  public static void parse(HashSet<String> urls)
  {
    /** get content of all urls as text*/
    HashMap<String,String> urlsContent =
      HtmlToText.convertToText(urls);

    Iterator contentItr = urlsContent.entrySet().iterator();
    while(contentItr.hasNext())
    {
    	HashMap.Entry entry = (HashMap.Entry) contentItr.next();

      StringTokenizer tokenizer = new StringTokenizer((String) entry.getValue()," ,.()");
      
      int i = 0;
      /** for all tokens*/
      while(tokenizer.hasMoreElements())
      {
        tokenizer.nextToken();
        ++i;
      }
      System.out.println("No. of strings : " + i);
    }
  }
  
  public static void main(String[] args) 
  {
    Crawler webCrawler = new Crawler(5);
    webCrawler.crawl("http://ask.uwindsor.ca");
    System.out.println("");
    parse(webCrawler.getUrls());
  }
}
