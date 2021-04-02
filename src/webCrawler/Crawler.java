package webCrawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;

public class Crawler {

  private HashSet<String> urls = new HashSet<String>();
  
  private HashMap<String, List<String>> urlLinks = 
    new HashMap<String, List<String>>();
 
  private int maxLinksToCrawl = 10;
  
  public Crawler(int maxLinks)
  {
	  maxLinksToCrawl = maxLinks;
  }

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
    if(!urls.contains(referenceUrl) && urls.size() < maxLinksToCrawl)
    {
      /** add url to crawled urls set*/
      urls.add(referenceUrl);
      urlLinks.putIfAbsent(referenceUrl, new ArrayList<String>());

      //System.out.println("URL : " + referenceUrl);
      try 
      {
        /** fetch html data from url*/
        Document doc = Jsoup.connect(referenceUrl).get();

        /** parse html and extract reffered links on page*/
        Elements urlsToCrawl = doc.select("a[href]");

        /** for all urls fetched from page*/
        for(Element url : urlsToCrawl)
        {
          if(urls.size() >= maxLinksToCrawl)
        	break;
        	
          /** add to reference hash map*/
          addToReferenceLinks(referenceUrl,url.attr("abs:href"));

          /** recursive call*/
          crawl(url.attr("abs:href"));
        }
      } 
      catch (IOException e) 
      {}
    }
  }

  public HashMap<String, List<String>> getReferencedLinksMap()
  {
    return urlLinks;
  }

  public HashSet<String> getUrls()
  {
    return urls;
  }

  private void addToReferenceLinks(String key, String value)
  {
    urlLinks.get(key).add(value);
  }

  public static void main(String[] args)
  {
    Crawler webCrawler = new Crawler(5);
    webCrawler.crawl("http://uwindsor.ca");
    System.out.println("");
    HashMap<String, List<String>> map = webCrawler.getReferencedLinksMap();
    Iterator iter = map.entrySet().iterator();
    
    while(iter.hasNext())
    {
    	HashMap.Entry entry = (HashMap.Entry) iter.next();
    	System.out.println("[Key] : " + entry.getKey());
    	System.out.println("[Value] :" + entry.getValue());
    }   
  }
}
