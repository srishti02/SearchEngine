package helpers;

import java.util.Comparator;

public class Structures {

  public static class UrlStructComparator implements Comparator<UrlStruct>
  {
    @Override
    public int compare(UrlStruct arg0, UrlStruct arg1) 
    {
      return Float.compare(arg0.rank, arg1.rank);
    }
  }

  public static class UrlStruct
  {
    public UrlStruct(String urlArg)
    {
      url = urlArg;
      rank = (float) 1.0;
    }

    public String url;
    public float rank;
  }
}
