package App.Algorithms;

public class UpperBoundAlgorithm {
    public static int UpperBound(String[] vertex, String[][] edges){
        int n = vertex.length;
        int[][] graph = new int[n + 1][n + 1];
        for(int i=0; i<edges.length; i++){
            int u = Integer.parseInt(edges[i][0]);
            int v = Integer.parseInt(edges[i][1]);
            graph[u][v] = 1;
            graph[v][u] = 1;
        }
        int[] clrofver = new int[n + 1];
        boolean[] isavailable = new boolean[n + 1];
        for(int k=1; k<=n; k++){
            for(int s=1; s<=n; s++){
                isavailable[s] = true;
            }
            for(int r=1; r<=n; r++){
                if (graph[k][r]==1 && clrofver[r]!=0){
                    isavailable[clrofver[r]] = false;
                }
            }
            int q;
            for(q=1; q<=n; q++){
                if(isavailable[q]){
                    break;
                }
            }
            clrofver[k] = q;
        }
        int currans=0;
        for (int j=1; j<=n; j++){
            currans = Math.max(currans, clrofver[j]);
        }
        return currans;
    }
}
