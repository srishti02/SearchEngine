package pageRanking;

import java.util.HashMap;
import java.util.List;

public class PageRanker {

  public static void generateRanks(HashMap<String,List<String>> refLinks)
  {
    System.out.println("Generate Ranks Called");
    EdgeWeightedDigraph graph = new EdgeWeightedDigraph(refLinks.size());

  }
}
