package webCrawler;

import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class Crawler {

  private HashSet<String> urls = new HashSet<String>();

  /**
   * @brief Method to crawl the url passed as argument and get all the
   *        referenced urls to crawl recursively.
   *
   * @param referenceUrl : Url to crawl 
   */
  public void crawl(String referenceUrl)
  {
    if(referenceUrl.isEmpty())
      return;

    /** check if the url has already been crawled*/
    if(!urls.contains(referenceUrl))
    {
      /** add url to crawled urls set*/
      urls.add(referenceUrl);

      System.out.println("URL : " + referenceUrl);
      try 
      {
        /** fetch html data from url*/
        Document doc = Jsoup.connect(referenceUrl).get();

        /** parse html and extract reffered links on page*/
        Elements urlsToCrawl = doc.select("a[href]");

        /** for all urls fetched from page*/
        for(Element url : urlsToCrawl)
        {
          /** recursive call*/
          crawl(url.attr("abs:href"));
        }
      } 
      catch (IOException e) 
      {}
    }
  }

  public static void main(String[] args)
  {
    Crawler webCrawler = new Crawler();
    webCrawler.crawl("http://ask.uwindsor.ca");
    System.out.println("");
  }
}
