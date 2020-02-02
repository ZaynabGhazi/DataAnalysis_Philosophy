import java.util.*;
import java.io.*;
public class my_tokenizer{
  public static void main(String[] args) throws FileNotFoundException, IOException{
    //for counting:
    HashMap<String,Integer> zipf_table = new HashMap<>();
    HashSet<String> spaced_tokensT = new HashSet <>();
    HashSet<String> down_punched_tokensT = new HashSet<>();
    HashSet<String> not_spwordsT = new HashSet<>();
    FileWriter csvWriter = new FileWriter("data_table.csv");
    FileWriter csvWriter_ = new FileWriter("zipf_table.csv");
    ArrayList<String> stop_words = new ArrayList<String>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));
    //write table header:
    csvWriter.append("DB Filename");
    csvWriter.append(",");
    csvWriter.append("Space Delim");
    csvWriter.append(",");
    csvWriter.append("Cumulative(Space Delim)");
    csvWriter.append(",");
    csvWriter.append("Downcased");
    csvWriter.append(",");
    csvWriter.append("Downcased + rm punct");
    csvWriter.append(",");
    csvWriter.append("Cumulative(Dc+rm Punct)");
    csvWriter.append(",");
    csvWriter.append("!stop-words");
    csvWriter.append(",");
    csvWriter.append("Cumulative(!stop-words)");
    csvWriter.append("\n");

    for(int i=0; i< args.length; i++){
      //per file:
      String filename = args[i];
      Scanner in = new Scanner(new File(filename));
      //ArrayList<String> spaced_tk = new ArrayList<>();
      //ArrayList<Integer> count_spaced_tk = new ArrayList<>();
      HashSet<String> spaced_tokens = new HashSet <>();
      HashSet<String> down_tokens = new HashSet <>();
      HashSet<String> down_punched_tokens = new HashSet<>();
      HashSet<String> not_spwords = new HashSet<>();
      //per line:
      while (in.hasNext()){
        String token = in.next();
        if (!spaced_tokens.contains(token)) spaced_tokens.add(token);
        if (!spaced_tokensT.contains(token)) spaced_tokensT.add(token);
        String down_token = token.toLowerCase();
        if (!down_tokens.contains(down_token)) down_tokens.add(down_token);
        if (down_token.matches("[A-Za-z0-9]+")){
          //alphanumeric ==> remove spaced punct
          if (zipf_table.containsKey(down_token)){
            zipf_table.put(down_token,zipf_table.get(down_token)+1);
          }
          else{
            zipf_table.put(down_token,1);
          }
          if(!down_punched_tokens.contains(down_token)) down_punched_tokens.add(down_token);
          if (!down_punched_tokensT.contains(down_token)) down_punched_tokensT.add(down_token);
        }
        else{
          //remove linked punct:
          down_token = down_token.replaceAll("\\p{Punct}","");
          if(!down_punched_tokens.contains(down_token)) down_punched_tokens.add(down_token);
          if (!down_punched_tokensT.contains(down_token)) down_punched_tokensT.add(down_token);
        }
        if (!stop_words.contains(down_token)){
          if (!not_spwords.contains(down_token)) not_spwords.add(down_token);
          if (!not_spwordsT.contains(down_token)) not_spwordsT.add(down_token);
        }
        }
      csvWriter.append(filename);
      csvWriter.append(",");
      csvWriter.append(Integer.toString(spaced_tokens.size()));
      csvWriter.append(",");
      csvWriter.append(Integer.toString(spaced_tokensT.size()));
      csvWriter.append(",");
      csvWriter.append(Integer.toString(down_tokens.size()));
      csvWriter.append(",");
      csvWriter.append(Integer.toString(down_punched_tokens.size()));
      csvWriter.append(",");
      csvWriter.append(Integer.toString(down_punched_tokensT.size()));
      csvWriter.append(",");
      csvWriter.append(Integer.toString(not_spwords.size()));
      csvWriter.append(",");
      csvWriter.append(Integer.toString(not_spwordsT.size()));
      csvWriter.append("\n");
      in.close();
    }
    csvWriter.close();
    //print zipf_table:
    for(String key: zipf_table.keySet()){
      String key_=key.toString();
      String value = Integer.toString(zipf_table.get(key));
      csvWriter_.append(key);
      csvWriter_.append(",");
      csvWriter_.append(value);
      csvWriter_.append("\n");
    }
    csvWriter_.close();
  }
}
