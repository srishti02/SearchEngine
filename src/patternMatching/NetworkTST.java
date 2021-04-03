package patternMatching;

import java.util.TreeSet;

import helpers.Structures.UrlStruct;
import helpers.Structures.UrlStructComparator;

/**
 * @author Srishti
 *
 * @description This class is an improved version of the TST shared in source code in labs.
 *              It is a symbol table with string keys, 
 *              implemented using a ternary search trie (TST).
 *              
 *              Major Change : Instead of value the node now has a
 *              TreeSet<UrlStruct> which shall store all the urls for which a
 *              token exists and also the rank of those urls.
 *
 *              Whena get query is performed, whole TreeSet shall be returned
 *              leaving the limit of required output to application user.
 */
public class NetworkTST
{
  /** num of nodes in a TST*/
  private int n;

  /** root node*/
  private Node root;

  /**
   * @description A class to hold one vertex/node. 
   */
  private class Node {

    /** character*/
    private char c;

    /** left, middle, and right subtries*/
    private Node left, mid, right;

    /** a sorted set containing all the urls for which a string exists and the 
     * rank of url*/
    private TreeSet<UrlStruct> urlSet = new TreeSet<UrlStruct>(
        new UrlStructComparator());
  }

  /**
   * @brief Return no. of nodes in th TST
   *
   * @return n
   */
  public int size() {
    return n;
  }

  /**
   * @brief Check if key exists in TST
   *
   * @param key : string to search in TST
   *
   * @return true if found, else false
   */
  public boolean contains(String key) {
    return get(key).isEmpty();
  }

  /**
   * @brief Return the value stored against a key.
   *
   * @param key : String for which value is to be returned 
   *
   * @return set of urls which have this string
   */
  public TreeSet<UrlStruct> get(String key) {
    if (key == null) throw new NullPointerException();
    if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
    Node x = get(root, key, 0);
    if (x == null) return new TreeSet<UrlStruct>();
    return x.urlSet;
  }

  // return subtrie corresponding to given key
  private Node get(Node x, String key, int d) {
    if (key == null) throw new NullPointerException();
    if (key.length() == 0) throw new IllegalArgumentException("key must have length >= 1");
    if (x == null) return null;
    char c = key.charAt(d);
    if      (c < x.c)              return get(x.left,  key, d);
    else if (c > x.c)              return get(x.right, key, d);
    else if (d < key.length() - 1) return get(x.mid,   key, d+1);
    else                           return x;
  }

  /**
   * @brief Insert key,val to TST
   *
   * @param s : key or string to be inserted as node/vertex
   * @param val : url for which string is to be inserted
   */
  public void put(String s, UrlStruct val) {
    if (!contains(s)) n++;
    root = put(root, s, val, 0);
  }

  private Node put(Node x, String s, UrlStruct val, int d) {
    char c = s.charAt(d);
    if (x == null) {
      x = new Node();
      x.c = c;
    }
    if      (c < x.c)             x.left  = put(x.left,  s, val, d);
    else if (c > x.c)             x.right = put(x.right, s, val, d);
    else if (d < s.length() - 1)  x.mid   = put(x.mid,   s, val, d+1);
    else                          x.urlSet.add(val);
    return x;
  }

      // all keys in symbol table
      public Iterable<String> keys() {
          Queue<String> queue = new Queue<String>();
          collect(root, "", queue);
          return queue;
      }
  
      //    // all keys in subtrie rooted at x with given prefix
      private void collect(Node x, String prefix, Queue<String> queue) {
          if (x == null) return;
          collect(x.left,  prefix,       queue);
          if (!x.urlSet.isEmpty()) queue.enqueue(prefix + x.c);
          collect(x.mid,   prefix + x.c, queue);
          collect(x.right, prefix,       queue);
      }
}
