package App.Algorithms;

/**
* note from ilgaz
* this class detects special case graphs for which the chromatic number
* can be determined quickly (like tree => 2 colors, bipartite => 2 colors,
* complete => number of vertices etc)
*
* HOW TO USE:
*   int result = SpecialCaseChecker.detectSpecialCase(vertices, edges);
*   if (result != -1) {
*       // 'result' is the chromatic number for this special case
*   } else {
*       // It's not a special case. Use your expensive algorithm.
*   }
*/
public class SpecialCases {

    /**
     * main entry point to detect if the graph is a known special case
     * returns:
     *   -1 if no special case is detected and expensive algorithm
     *      is called
     *   otherwise, the exact chromatic number for that special case
     */

    //todo: will complete this method later
     private static boolean isComplete(String[] vertices, String[][] edges) {
        int n = vertices.length;
        // quick check numerically
        // If # edges < n*(n-1)/2, we can skip deeper checks.
        int requiredEdges = n * (n - 1) / 2;
        if (edges.length != requiredEdges) {
            return false;
     }

     //todo: will complete this method later
     public static int detectSpecialCase(String[] vertices, String[][] edges) {
        if() {
            
        }
     }
}
