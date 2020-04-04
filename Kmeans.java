public class Kmeans{
  public static void main(String[] args){
    double[][] doc_matrix ={{6,0,0,1,0,0,3,0,3,0},{5,1,4,5,0,0,0,0,5,3},{0,1,0,0,5,0,2,5,0,2},{0,0,0,0,0,4,0,0,0,6},{2,6,2,1,6,1,4,6,0,3},{0,0,5,0,6,3,0,1,0,0},{3,0,0,2,1,0,0,6,0,0},{4,0,5,6,0,2,2,6,4,6},{0,2,4,0,0,3,2,0,0,6},{0,3,0,0,0,6,0,0,0,0},{5,0,0,0,4,0,2,0,0,1},{6,6,0,0,1,4,0,1,5,4},{0,0,4,4,0,6,0,0,1,0},{0,4,0,0,0,6,0,0,1,4},{0,5,0,0,0,3,4,2,0,3}};
    for (int i=0;i < doc_matrix.length;i++){
      for (int j=0;j < doc_matrix[i].length;j++)
      System.out.print(doc_matrix[i][j]+" ");
      System.out.println();
    }
      double[][] centroids = {{0,0,0,0,0,4,0,0,0,6},{2,6,2,1,6,1,4,6,0,3},{0,0,5,0,6,3,0,1,0,0}};
      double[] assignments = new double[15];
      int trials =0;
      while (trials <3){
      for (int d=0; d<assignments.length;d++) assignments[d]= Double.MAX_VALUE;
      for (int i=0; i< 15; i++){
        //the centroids are 3,4 and 5:
        int k=1;
        for(int j=0; j<3;j++){
          double dist = compute_LInfDist(doc_matrix[i],centroids[j]);
          System.out.print(dist + " ");
          if (dist < assignments[i]) {
            assignments[i]= dist;
            k=j+1;
          }
          System.out.println();
        }
        assignments[i]=k;
        System.out.println(" Document "+i+" has been assigned to cluster "+ assignments[i]);
      }
      //update centroids:
      System.out.println("Centroids updating ...");
      for(int k=0; k<centroids.length;k++){
        double count =1;
        for (int j=0; j<assignments.length;j++){
          if (assignments[j] == k+1){
            count++;
            for(int l=0; l< doc_matrix[j].length;l++){
              centroids[k][l]+= doc_matrix[j][l];
            }
          }
        }
        for(int m=0; m< centroids[k].length;m++) centroids[k][m]/=count;
      }
      System.out.println("The new centroids are: ");
      for(int i=0; i<3; i++){
        for(int j=0; j<centroids[i].length;j++){
          System.out.print(centroids[i][j]+" ");
        }
        System.out.println();
      }
      trials ++;
      System.out.println("================================");
}
  }
  public static double compute_L1Dist(double[] a, double[] b){
    double dist =0;
    for (int i=0; i<a.length;i++){
      dist += Math.abs(a[i]-b[i]);
     }
    return dist;
    }

    public static double compute_LInfDist(double[] a,double[] b){
      double dist =0;
      for (int i=0; i<a.length;i++){
        if (Math.abs(a[i]-b[i]) > dist )dist = Math.abs(a[i]-b[i]);
       }
      return dist;
    }
}
