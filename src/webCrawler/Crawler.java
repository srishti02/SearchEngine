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

/**
 * @author Siddharth, Srishti
 * @description This class recursively performs crawling over web urls to fetch a
 *              network of URLs referenced through each other.
 */
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

  /**
   * @brief Getter for urlLinks hashmap
   *
   * @return urlLinks
   */
  public HashMap<String, List<String>> getReferencedLinksMap()
  {
    return urlLinks;
  }

  /**
   * @brief Getter for set of urls
   *
   * @return urls
   */
  public HashSet<String> getUrls()
  {
    return urls;
  }

  /**
   * @brief Method to add referenced url to urlLinks hashmap.
   *
   * @param key and value to add
   */
  private void addToReferenceLinks(String key, String value)
  {
    urlLinks.get(key).add(value);
  }
}
