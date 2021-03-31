package helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import webCrawler.Crawler;

/**
 * @description Class with static methods to convert html data to text.
 */
public class HtmlToText {
	  private static HashSet<String> urlAddedList = new HashSet<String>();
	  static List<String> referredUrl= new ArrayList<String>();
	  
	  private static List<String> refList = new ArrayList<String>();

	  /**
	   * This method accepts the Hashmap from the web crawler and iterates through the hashmap keys ie the referenced urls
	   * The text content of each url is then referenced
	   * @param referencedUrl
	   */
   	 public static void parser(HashMap<String, List<String>> referencedUrl) 
     {
   	  
   	  urlAddedList.addAll(referencedUrl.keySet());
   	  for (String urlRef:urlAddedList)  
   	try	 
   	  {      	  
   		  if (!urlRef.isEmpty())
   		  {  refList=referencedUrl.get(urlRef);
   			  for (String ref:referencedUrl.get(urlRef))
   			  {
   	  	  Document doc = Jsoup.connect(ref).get();
   		  String content=doc.text();
   		  System.out.println("url is " +ref);
   		  System.out.println("content is "+content);
   		  
   				  }
   		     }
   	  }
   	 catch (IOException e) 
      {}
     
     }
      
     
   	 
     public static void main(String[] args) 
	  {
	    Crawler webCrawler = new Crawler();
	    webCrawler.crawl("http://ask.uwindsor.ca");
	    System.out.println("");
	    HashMap<String, List<String>> links=webCrawler.getReferencedLinksMap();
	    //System.out.println(links);
	    parser(links);
	  }

}
