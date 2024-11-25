package App.Algorithms;



import  current_color_place;// current_color_place is the put color vertex file
import  Positions;// the file which makes radom position files
import java.util.HashSet;
import java.util.Set;

public class ColoringAlgorithm{

//Used to store the color in current_color_place into currentplace
private current_color_place currentplace; 
//Used to store the position in Positions into positions
private Positions positions;

public ColoringAlgorithm(int vertexCount) {
    //TODO Auto-generated constructor stub
}
public ColorChecker(int size){
    currentplace = new current_color_place(size);//stores into currentplace the size it will be used later
    Positions = new positions();

}
    // to get the color in the position
    public String getColor(int point) {
        if (point >= 0 && point < colors.length) { //colors is the array of the set of colors
            return colors[point];
        }
        return null;
    }


    //for the color which can not be used in this position
    public Set<String> getUnavailableColors(int point) {
        Set<String> unavailableColors = new HashSet<>();

        // Get the color of the neighbor point through the current_color_place class method
        for (int neighbor : currentplace.getNeighbors(point)) {
            String neighborColor = currentplace.getColor(neighbor);
            if (neighborColor != null) {
                unavailableColors.add(neighborColor);
            }
        }

        return unavailableColors;
    }

    //Remind colors that cannot be placed and when placing the wrong color
     public void attemptcurrentcolor(String color) {
        int point = positions.generatePosition(currentplace.colors.length);

        //Get unavailable colors in advance and remind the player
        Set<String> unavailableColors = getUnavailableColors(point);
        if (!unavailableColors.isEmpty()) {
            System.out.println("warn: in this point " + point + " there could not put this color :" + unavailableColors);
        }

        //Checks whether the specified color is placeable
        if (unavailableColors.contains(color)) {
            System.out.println("warn: cannot put in this point " + point + " this color " + color + "; because there is the same color in the neighbor");
        }
    }
 

}

//generate random colors (an easy way would be using rgb numbers)
//check whether or not these colors are to similar to already existing colors (looking at rgb values and checking to see if the minimum diference is met)
//store the colors in a Color[]
//return the color when your method is called with a number