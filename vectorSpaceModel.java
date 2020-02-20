import java.util.*;
import java.io.*;
public class vectorSpaceModel{
  //preprocessing utils:
  final static ArrayList<String> stop_words = new ArrayList<String>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));
  final static int TF = 0;
  final static int DF = 1;



  public static void main(String[] args) throws IOException{
    //vectorize all documents:
    ArrayList<HashMap<String,Double>> doc_vecs = new ArrayList<>();
    for(int i=0; i<args.length;i++){
      doc_vecs.add(vectorize(args,args[i]));
    }
    //DEBUGGER1
    /*for(int i=0; i<doc_vecs.size();i++){
      for(String key: doc_vecs.get(i).keySet()){
        System.out.println(key+" "+doc_vecs.get(i).get(key));
      }
    }*/


    //generate inverted index
    HashMap<String,HashMap<Integer,Double>> inverted_index = new HashMap<>();
    for(int i=0; i<doc_vecs.size();i++){
      for(String key: doc_vecs.get(i).keySet()){
        if (!inverted_index.containsKey(key)){
          HashMap<Integer,Double> doc_val = new HashMap<>();
          doc_val.put(i,doc_vecs.get(i).get(key));
          inverted_index.put(key,doc_val);
        }
        else{
          inverted_index.get(key).put(i,doc_vecs.get(i).get(key));
        }
      }
    }

   //DEBUGGER2
   /*for(String key: inverted_index.keySet()){
     System.out.println(key);
     for (Integer keyp: inverted_index.get(key).keySet()){
       System.out.print(Integer.toString(keyp) +" "+ inverted_index.get(key).get(keyp)+" new");
     }
     System.out.println();
   }*/
   //----------------------- SEARCH ---------------------------------------
   System.out.println("Database is now searchable.");
   System.out.println("Enter your query: ");
   Scanner in = new Scanner(System.in);
   String query = in.nextLine();
   while (in.hasNextLine()){
     query +=in.nextLine();
   }
   //preprocessing:
   String[] query_tokens = query.split(" ");
   //query_preprocessing:
   for(int j=0; j<query_tokens.length;j++){
     query_tokens[j] = query_tokens[j].toLowerCase();
     query_tokens[j] = query_tokens[j].replaceAll("\\p{Punct}","");
   }
   //vectorize query
   //just compyte Tf
   HashMap<String, Double> query_vec = new HashMap<>();
   double max_qTF =1;
   for(int j=0; j<query_tokens.length;j++){
     if (query_tokens[j].length()>0 && !stop_words.contains(query_tokens[j])){
       if (!query_vec.containsKey(query_tokens[j])){
         query_vec.put(query_tokens[j],1.0);
       }
       else{
         double past_tf = query_vec.get(query_tokens[j]);
         query_vec.put(query_tokens[j],past_tf+1);
         if (query_vec.get(query_tokens[j]) > max_qTF) max_qTF = query_vec.get(query_tokens[j]);
       }
     }
   }
   //DEBUGGER3
   /*for (String key: query_vec.keySet()){
     System.out.println(key+" "+query_vec.get(key));
   }*/
   //----------------------- SEARCH AND MATCHING--------------------------------
   // compute similarity score using lnc.ltc :
   PriorityQueue<Double> scores = new PriorityQueue<>(Collections.reverseOrder());
   double[] scores_arr = new double[args.length];
   HashMap<Double,Integer> doc_matcher = new HashMap<>();
   Arrays.fill(scores_arr, 0);
   //compute normalized wq
   double wq_sum=0;
   for(String q_token: query_vec.keySet()){
     double wq = 1+Math.log(query_vec.get(q_token));
     //System.out.println("WQ "+wq);
     double q_idf = 0;
     //System.out.println(inverted_index.get(q_token).size());
    // System.out.println(inverted_index.get(q_token).size()/args.length);
     //System.out.println(Math.log(inverted_index.get(q_token).size()/args.length));
     if (inverted_index.containsKey(q_token)) q_idf=Math.log((double)args.length/inverted_index.get(q_token).size());
     wq*= q_idf;
     //System.out.println("WQ "+wq);
     query_vec.put(q_token,wq);
     wq_sum+=Math.pow(wq,2);
    // System.out.println("SUM "+wq_sum);
   }
   wq_sum = Math.sqrt(wq_sum);
   //System.out.println("SUM "+wq_sum);

   //compute normalized wd
   double[] wd_norms = new double[args.length];
   for(int i=0; i< doc_vecs.size();i++){
     for(String key: doc_vecs.get(i).keySet()){
       wd_norms[i]+= Math.pow(1+Math.log(doc_vecs.get(i).get(key)),2);
     }
   }

   //MAIN ALGO
   for(String q_token: query_vec.keySet()){
     //Compute wq:
     double wq= query_vec.get(q_token)/wq_sum;
     //System.out.print("WQ "+wq);
     if (inverted_index.containsKey(q_token)){
       for (Integer doc_id: inverted_index.get(q_token).keySet()){
       double wd = (1+Math.log(inverted_index.get(q_token).get(doc_id)))/Math.sqrt(wd_norms[doc_id]);
       //System.out.print("WD  "+wd);
       scores_arr[doc_id] += wq*wd;
     }
   }
   }
   for(int d=0; d<scores_arr.length;d++){
     if (scores_arr[d] >0 ) {
       System.out.println("SCORE "+scores_arr[d]);
       scores.add(scores_arr[d]);
       doc_matcher.put(scores_arr[d],d);
    }
   }
   System.out.println(doc_matcher.size()+" relevant documents retrieved.\nEnter number of documents you would like to see: ");
   //Integer threshold = in.nextInt();
   Integer threshold = 4;
   System.out.println("Document\t\t\t\t\tScore");
   for(int k=0; k< threshold;k++){
     Double score = scores.poll();
     int document = doc_matcher.get(score);
     System.out.println(args[document]+"\t\t"+score);
   }

  }







  //--------------------- VECTORIZE ----------------------------------------
  public static HashMap<String,Double> vectorize(String[] files,String filename) throws FileNotFoundException{
    HashMap<String,Double> doc_vec = new HashMap<>();
    Scanner in = new Scanner(new File(filename));
    int max_tfreq =1;
    int max_docs =1;
    HashMap<String,ArrayList<Integer>> corpus_md = new HashMap<>();

    //preprocessing:
    ArrayList<String> stop_words = new ArrayList<String>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));
    while(in.hasNext()){
      //light-weight preprocessing:
      String token = in.next().toLowerCase();
      token = token.replaceAll("\\p{Punct}","");
      if (!stop_words.contains(token)){
        if (!corpus_md.containsKey(token)){
          ArrayList<Integer> token_md = new ArrayList<>();
          token_md.add(1);
          token_md.add(1);
          corpus_md.put(token,token_md);
        }
        else{
          ArrayList<Integer> token_md = corpus_md.get(token);
          ArrayList<Integer> new_md = new ArrayList<>();
          new_md.add(token_md.get(TF)+1);
          new_md.add(token_md.get(DF));
          //update maximum term frequency:
          if (new_md.get(TF) > max_tfreq) max_tfreq=new_md.get(TF);
          corpus_md.put(token,new_md);
        }
      }
    }
    in.close();
  //read rest of collection:
  for (int i=0; i<files.length && !files[i].equals(filename);i++){
    Scanner inn = new Scanner(new File(files[i]));
    HashSet<String> read_terms = new HashSet<>();
    while (inn.hasNext()){
      String token = inn.next().toLowerCase();
      token = token.replaceAll("\\p{Punct}","");
      if (!stop_words.contains(token) && !read_terms.contains(token) && corpus_md.containsKey(token)){
        read_terms.add(token);
        ArrayList<Integer> token_md = corpus_md.get(token);
        ArrayList<Integer> new_md = new ArrayList<>();
        new_md.add(token_md.get(TF));
        new_md.add(token_md.get(DF)+1);
        corpus_md.put(token,new_md);
      }
    }
    inn.close();
  }//other-files
//computational part:
for(String key: corpus_md.keySet()){
  int term_freq = corpus_md.get(key).get(TF);
  int doc_freq = corpus_md.get(key).get(DF);
  double idf = Math.log((double)files.length/doc_freq);
  //double tf = (double)term_freq/max_tfreq;
  double tf = term_freq;
  //doc_vec.put(key,tf*idf);
  doc_vec.put(key,tf);
  }
return doc_vec;
    }
}
