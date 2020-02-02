import java.util.*;
import java.io.*;
import java.lang.Math;

public class correlation_calc{
  public static void main(String[] args) throws FileNotFoundException{
    HashMap<String,Integer> my_count = new HashMap<>();
    HashMap<String,Integer> google_count = new HashMap<>();
    ArrayList<Integer> my_values = new ArrayList<>();
    ArrayList<Integer> google_values = new ArrayList<>();
    String my_file = args[0];
    String google_file = args[1];
    Scanner my_in = new Scanner(new File(my_file));
    Scanner google_in = new Scanner(new File(google_file));
    while (my_in.hasNextLine()){
      String line = my_in.nextLine();
      String[] fields = line.split(",");
      my_count.put(fields[0],Integer.parseInt(fields[1]));
    }
    while (google_in.hasNextLine()){
      String line = google_in.nextLine();
      String[] fields = line.split(",");
      google_count.put(fields[0],Integer.parseInt(fields[1]));
    }
    for (String root: my_count.keySet()){
      if (google_count.containsKey(root)){
        my_values.add(my_count.get(root));
        google_values.add(google_count.get(root));
      }
    }
    //statistical analysis:
    double my_mean=0;
    double google_mean=0;
    double my_std =0;
    double google_std=0;
    double corr=0;
    for(int i=0; i< my_values.size();i++){
      my_mean += my_values.get(i);
      google_mean+= google_values.get(i);
    }
    my_mean /= my_values.size();
    google_mean /= google_values.size();
    System.out.println(" my mean and google's mean are "+ my_mean+" "+google_mean);
    for(int i=0; i< my_values.size();i++){
      my_std += Math.pow(my_values.get(i)- my_mean,2);
      google_std+= Math.pow(google_values.get(i)- google_mean,2);
      corr += ((my_values.get(i)- my_mean)*(google_values.get(i)- google_mean));
    }
    my_std /= (my_values.size()-1);
    my_std = Math.sqrt(my_std);
    google_std /= (google_values.size()-1);
    google_std = Math.sqrt(google_std);
    System.out.println(" my std and google's std are "+ my_std+" "+google_std);

  corr = corr / (my_std*google_std*(my_values.size()-1));
  System.out.println("correlation is "+corr);
}
}
