package helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import webCrawler.Crawler;

/**
 * @author Margaret
 * @description Class with static methods to convert html data to text.
 */
public class HtmlToText {

  
  /**
   * @brief This method accepts the Hashmap from the web crawler and iterates
   *        through the hashmap keys ie the referenced urls
   *        The text content of each url is then referenced
   * @param referencedUrl
   */
  public static HashMap<String,String> convertToText(HashSet<String> urls) 
  {
    /** words to be ignored in content from url*/
    HashSet<String> ignoreWords = new HashSet<String>();

    /** open file stopwords.txt*/
    Path path = Paths.get("src/stopwords.txt");

    /** read all lines*/
    try(Stream<String> stream = Files.lines(path))
    {
      /** each line contains a stop word so add each line to ignore words list*/
      stream.forEach(new Consumer<String>() {
        @Override
        public void accept(String line) {
          //line = "\\b" + line + "\\b";
          ignoreWords.add(line);
        }
      });
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
      // handle exception
    }

    /** Map to store page url and their content as string*/
    HashMap<String/*url*/,String/*content as text*/> pages = 
      new HashMap<String,String>();

    /** For each URL*/
    for (String urlRef:urls)
    {
      try	 
      { 
        /** if url is not empty*/
        if (!urlRef.isEmpty())
        {  
          /** download url content from web*/
          Document doc = Jsoup.connect(urlRef).get();

          /** get content as text*/
          String content=doc.text();

          /** remove ignore words*/
          for(String ignore : ignoreWords)
          {
            content.replaceAll(ignore,"");
            //System.out.println("removed " + ignore);
          }

          /** insert into hash url vs content as text*/
          pages.put(urlRef,content);
        }
      }
      catch (IOException e) 
      {}
    }

    return pages;
  }
}
