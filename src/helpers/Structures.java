package helpers;

import java.util.Comparator;

public class Structures {

  public static class UrlStructComparator implements Comparator<UrlStruct>
  {
    @Override
    public int compare(UrlStruct arg0, UrlStruct arg1) 
    {
      int val1 = Integer.compare(arg0.rank, arg1.rank);
      int val2 = arg0.url.compareTo(arg1.url);
      if(val1 == 0 && val2 != 0)
        val1 = -1;
      return val1;
    }
  }

  public static class UrlStruct 
  {
    public UrlStruct(String urlArg, int occurances)
    {
      url = urlArg;
      rank = occurances;
    }

    public String url;
    public int rank;
  }
}
