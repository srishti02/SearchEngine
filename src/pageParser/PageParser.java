package pageParser;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import java.util.StringTokenizer;
import java.util.TreeSet;

import helpers.HtmlToText;
import helpers.Structures.UrlStruct;
import patternMatching.NetworkTST;

/**
 * @author Margaret, Srishti
 * @description Class to perform parsing operations on a url page retrived from
 *              DB or web crawler.
 *
 *              Responsibilities of class:
 *              - Using HtmlToText to get base data for pattern matching.
 *              - Generate tokens using tokenizer
 *              - Performing Page Ranking
 *              - Inserting data in pattern matcher.
 */
public class PageParser {

  private static NetworkTST tst = new NetworkTST();
  
  public static TreeSet<UrlStruct> search(String key)
  {
	  return tst.get(key);
  }

  public static void parse(HashSet<String> urls)
  {
    System.out.println("Converting to text : " + Instant.now());
    /** get content of all urls as text*/
    HashMap<String,String> urlsContent =
      HtmlToText.convertToText(urls);
    System.out.println("Converted to text : " + Instant.now());

    /** for each url*/
    Iterator contentItr = urlsContent.entrySet().iterator();
    while(contentItr.hasNext())
    {
      HashMap.Entry entry = (HashMap.Entry) contentItr.next();

      /** get tokens from content of each url*/
      StringTokenizer tokenizer = new StringTokenizer((String) entry.getValue()," ,.()");

      HashMap<String/*token*/, Integer /*occurance*/> map = new
        HashMap<String,Integer>();

      //--------------------Page Ranking Logic----------------------------------
      /** for all tokens*/
      while(tokenizer.hasMoreElements())
      {
        /** get next token*/
        String token = tokenizer.nextToken();

        /** check if exists in map*/
        if(map.containsKey(token))
        {
          /**update occurances*/
          map.replace(token,map.get(token) + 1);
        }
        else
        {
          /** add new entry*/
          map.put(token,1);
        }
      }
      //------------------------------------------------------------------------
    

      Iterator it = map.entrySet().iterator();
      while(it.hasNext())
      {
        HashMap.Entry entry1 = (HashMap.Entry) it.next();
        
        /**Insert to TST*/
        tst.put((String)entry1.getKey(), 
        		new UrlStruct((String)entry.getKey(),(int)entry1.getValue()));
      }
    }
    System.out.println("Insertion complete for TST : " + Instant.now());
  }
}
