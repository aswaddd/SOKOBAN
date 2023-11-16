import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
/**
 * @author: TARIQ, Aswad Mohammed (22209786)
 */
public class Sokoban {

    public static final char UP = 'W';
    public static final char DOWN = 'S';
    public static final char LEFT = 'A';
    public static final char RIGHT = 'D';
    public static final char PLAYER = 'o';
    public static final char BOX = '@';
    public static final char WALL = '#';
    public static final char GOAL = '.';
    public static final char BOXONGOAL = '%';


    public static void main(String[] args) {
        new Sokoban().runApp();
    }

    public void runApp() {
        
        String mapfile = "map1.txt"; //change this to test other maps
        char[][] map = readmap(mapfile); // load map file into an array
        char[][] oldMap = readmap(mapfile); // load a copy of the map in its unaltered state
        
        if (map == null) { // validity check of whether the map file exists
            System.out.println("Map file not found");
            return;
        }
        int[] start = findPlayer(map); // stores the starting coordinates of the player character
        if (start.length == 0) { // if the array has no elements then the user is prompted
            System.out.println("Player not found");
            return;
        }
        int row = start[0];
        int col = start[1];
        while (!gameOver(map)) { // to keep the game running
            printMap(map); // draw game board
            System.out.println("\nPlease enter a move (WASD): ");
            char input = readValidInput(); // loads the valid input into the variable
            if (input == 'q')  // used to quit the game with an input
                break;
            if (input == 'r') {  // used to restart the game
                map = readmap(mapfile);
                row = start[0];    // accesses the first coordinate of the player
                col = start[1];
                continue;
            }
            if (input == 'h') { // prints instructions to help the player
                printHelp();
            }
            if (!isValid(map, row, col, input)) // validity checks before allowing the player to move
                continue;
            movePlayer(map, row, col, input); // moving the player according to the input

            fixMap(map, oldMap);  // update the map with original map structure after player's movement

            int[] newPos = findPlayer(map); // stores the players new position on the map
            row = newPos[0]; // updates the value of row to check validity later **
            col = newPos[1]; 

        }
        System.out.println("Bye!");
    }

    public void printHelp() { // printing instructions
        System.out.println("Sokoban Help:"); 
        System.out.println("Move up: W");
        System.out.println("Move down: S");
        System.out.println("Move right: D");
        System.out.println("Move left: A");
        System.out.println("Restart: r");
        System.out.println("Quit: q");
    }

    public char readValidInput() {
        Scanner in = new Scanner(System.in); // if input doesn't match the specified characters then prints message.
        char input = in.next().charAt(0);
        while (!(input == 'W' || input == 'A' || input == 'S' || input == 'D' ||
                input == 'h' || input == 'r' || input == 'q')) {
            System.out.println("Invalid input. PLease enter again: ");
            input = in.next().charAt(0);
        } // filters input character and only returns once valid.
        return input;
    }

    public void fixMap(char[][] updated, char[][] base) {
        // reprint the GOAL after BOXONGOAL state is given to the GOAL location then the BOX is pushed out of it.
        for(int i = 0; i < updated.length; i++) { // without this method, the GOAL disappears after BOX is placed on it and then moved.
            for(int j = 0; j < updated[i].length; j++) {
                if (base[i][j] == GOAL && updated[i][j] == ' ') {
                    updated[i][j] = base[i][j];
                }
            }
        }
    }

    public void moveBox(char[][] map, int row, int col, char direction) {
        if (direction == UP) {
            if (map[row - 1][col] == GOAL){ // checking ahead of BOX for GOAL for each direction
                map[row - 1][col] = BOXONGOAL; // same scenario but with BOXONGOAL
            } else {
                map[row - 1][col] = BOX; // if not obstructions then the BOX is moved
            }
            map[row][col] = ' ';
        }
        if (direction == DOWN) {
            if (map[row + 1][col] == GOAL){
                map[row + 1][col] = BOXONGOAL;
            } else {
                map[row + 1][col] = BOX;
            }
            map[row][col] = ' ';
        }
        if (direction == RIGHT) {
            if (map[row][col + 1] == GOAL){
                map[row][col + 1] = BOXONGOAL;
            } else {
                map[row][col + 1] = BOX;
            }
            map[row][col] = ' ';
        }
        if (direction == LEFT) {
            if (map[row][col - 1] == GOAL){
                map[row][col - 1] = BOXONGOAL;
            } else {
                map[row][col - 1] = BOX;
            }
            map[row][col] = ' ';
        }

    }

    public void movePlayer(char[][] map, int row, int col, char direction) {
        if (direction == UP) {
            if (map[row - 1][col] == BOX) { // checking ahead of PLAYER for BOX in each direction
                moveBox(map, row - 1, col, direction);
            }
            if (map[row - 1][col] == BOXONGOAL) { // checking ahead of PLAYER for BOXONGOAL in each direction
                moveBox(map, row - 1, col, direction);
            }
            map[row - 1][col] = PLAYER; // PLAYER is moved once the checks are complete
            map[row][col] = ' ';
        }
        if (direction == DOWN) {
            if (map[row + 1][col] == BOX) {
                moveBox(map, row + 1, col, direction);
            }
            if (map[row + 1][col] == BOXONGOAL) {
                moveBox(map, row + 1, col, direction);
            }
            map[row + 1][col] = PLAYER;
            map[row][col] = ' ';
        }
        if (direction == RIGHT) {
            if (map[row][col + 1] == BOX) {
                moveBox(map, row, col + 1, direction);
            }
            if (map[row][col + 1] == BOXONGOAL) {
                moveBox(map, row, col + 1, direction);
            }
            map[row][col + 1] = PLAYER;
            map[row][col] = ' ';
        }
        if (direction == LEFT) {
            if (map[row][col - 1] == BOX) {
                moveBox(map, row, col - 1, direction);
            }
            if (map[row][col - 1] == BOXONGOAL) {
                moveBox(map, row, col - 1, direction);
            }
            map[row][col - 1] = PLAYER;
            map[row][col] = ' ';
        }
    }

    public boolean gameOver(char[][] map) {
        for (int i = 0; i < map.length; i ++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == GOAL) { // if at least one GOAL is detected then the game continues
                    return false;
                }
            }
        }
        return true;
    }

    public int numberOfRows(String fileName) throws FileNotFoundException {
        File inputFile = new File(fileName);
        if (!(inputFile.exists())) { // existence detection
            return -1;
        }
        Scanner fileReader = new Scanner(inputFile);
        int rows = 0;
        while (fileReader.hasNextLine()) { // counts the rows and returns
            rows++;
            fileReader.nextLine(); // takes the scanner to the next line, otherwise while loop becomes infinite
        }
        fileReader.close();
        return rows;
    }

    public char[][] readmap (String fileName) {

        try {
            int rows = numberOfRows(fileName); // getting the number of rows from another method
            File mapFile = new File(fileName);

            Scanner fileReader = new Scanner(mapFile);
            String[] lineOfMap = new String[rows];

            int maxLength = 0;
            for (int i = 0; i < rows; i++) {
                lineOfMap[i] = fileReader.nextLine(); // getting each line's content and then max column size through dot length later
                if (maxLength < lineOfMap[i].length()) {
                    maxLength = lineOfMap[i].length();
                }
            }

            char[][] map = new char[rows][maxLength]; // using an array of varying column sizes lead to complications later so I opted to keep it this way.

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < lineOfMap[i].length(); j++) {
                    map[i][j] = lineOfMap[i].charAt(j); // loading the chars into the array
                }
            }
            fileReader.close();
            return map;

        } catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " not found."); // exception is dealt with
            return null;
        }
    }

    public int[] findPlayer(char[][] map) {
        int[] positionOfPlayer = new int[2];
        for (int i = 0; i < map.length; i++) { // loop through map to match coordinates with PLAYER
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == PLAYER) {
                    positionOfPlayer[0] = i;
                    positionOfPlayer[1] = j;
                    return positionOfPlayer; // once player is found, the position is returned
                }
            }
        }
        return null; // if player is not found, returns null
    }

    public boolean isValid(char[][] map, int row, int col, char direction) {
        if (direction == UP) {
            if (map[row - 1][col] == WALL) { // checking around the PLAYER. for each direction, the corresponding checks are conducted for all directions.
                return false; // if a WALL is detected then returns false.
            }
            if (map[row - 1][col] == BOX && map[row - 2][col] == WALL) {
                return false; // if a BOX is followed by a WALL is detected then false.
            }
            if (map[row - 1][col] == BOX && map[row - 2][col] == BOX) {
                return false; // if two consecutive BOXes then false.
            }
            if ((map[row - 1][col] == BOX && map[row - 2][col] == BOXONGOAL) || map[row - 1][col] == BOXONGOAL && map[row - 2][col] == BOX) {
                return false; // if BOX is followed by BOXONGOAL or vice versa then false.
            }
            if (map[row - 1][col] == BOXONGOAL && map[row - 2][col] == WALL) {
                return false; // if a BOXONGOAL is followed by a WALL is detected then false.
            }
        } // the same code is repeated for each direction with changes in how the values of row and columns are changed
        if (direction == DOWN) {
            if (map[row + 1][col] == WALL) {
                return false;
            }
            if (map[row + 1][col] == BOX && map[row + 2][col] == WALL) {
                return false;
            }
            if (map[row + 1][col] == BOX && map[row + 2][col] == BOX) {
                return false;
            }
            if ((map[row + 1][col] == BOX && map[row + 2][col] == BOXONGOAL) || map[row + 1][col] == BOXONGOAL && map[row + 2][col] == BOX) {
                return false;
            }
            if (map[row + 1][col] == BOXONGOAL && map[row - 2][col] == WALL) {
                return false;
            }
        }
        if (direction == LEFT) {
            if (map[row][col - 1] == WALL) {
                return false;
            }
            if (map[row][col - 1] == BOX && map[row][col - 2] == WALL) {
                return false;
            }
            if (map[row][col - 1] == BOX && map[row][col - 2] == BOX) {
                return false;
            }
            if ((map[row][col - 1] == BOX && map[row][col - 2] == BOXONGOAL) || map[row][col - 1] == BOXONGOAL && map[row][col - 2] == BOX) {
                return false;
            }
            if (map[row][col - 1] == BOXONGOAL && map[row][col - 2] == WALL) {
                return false;
            }
        }
        if (direction == RIGHT) {
            if (map[row][col + 1] == WALL) {
                return false;
            }
            if (map[row][col + 1] == BOX && map[row][col + 2] == WALL) {
                return false;
            }
            if (map[row][col + 1] == BOX && map[row][col + 2] == BOX) {
                return false;
            }
            if ((map[row][col + 1] == BOX && map[row][col + 2] == BOXONGOAL) || map[row][col + 1] == BOXONGOAL && map[row][col + 2] == BOX) {
                return false;
            }
            if (map[row][col + 1] == BOXONGOAL && map[row][col + 2] == WALL) {
                return false;
            }
        }
        return true; // once all checks are passed, true is returned
    }
  
    public void printMap(char[][] map) {
        System.out.print("  "); // print the column index line above the map
        for (int i = 0, j = 0; i < map[0].length; i++, j++) { // all columns are made equal in readmap method to simply code here
            if ( j > 9) {
                j = 0;
            }
            System.out.print(j);
        }
        System.out.println();
        for (int i = 0; i < map.length; i++) { // print the row index along with the map
            for (int j = 0; j < map[i].length; j++) {
                if (j == 0) {
                    System.out.printf("%-2d", i); // formatting index
                }
                if (map[i][j] == '\0') { // to deal with the null elements in the map array
                    System.out.print("");
                    break;
                } else {
                    System.out.print(map[i][j]);
                }
            }
            System.out.println();
        }
    }
}