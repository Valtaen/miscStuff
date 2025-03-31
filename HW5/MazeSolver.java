import java.util.Stack;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileInputStream;

/*
Matt Stewart
HW #5 CSCI 221-03, Spring 2022
The purpose of this code is to be able to solve a provided 2D maze while using
a Stack algorithm to progress through the maze and determine if a path from the
beginning (0,0) to the exit (n,n) exists. A direct path is not requiired, and
indicating all spots moved along is required.

THIS CODE IS MY OWN WORK, IT WAS WRITTEN WITHOUT CONSULTING
CODE WRITTEN BY OTHER STUDENTS OR COPIED FROM ONLINE RESOURCES.
Matt Stewart
*/

public class MazeSolver {

  static char[][] maze;
  static int startX, startY;  // indices for starting the maze search
  static int endX, endY; // indices for ending the maze search
  static char[][] solvedMaze;

  // Constructor that creates the maze
  public MazeSolver(String fileName) throws IOException {
    startX = 0;
    startY = 0;
    readMaze(fileName); // initialize maze
  }

  // Helper method for reading the maze content from a file
  public static void readMaze(String filename) throws IOException {
    Scanner scanner;
    try{
      scanner = new Scanner(new FileInputStream(filename));
    }
    catch(IOException ex){
      System.err.println("[ERROR] Invalid filename: " + filename);
      return;
    }

    int N = scanner.nextInt();
    scanner.nextLine();
    maze = new char[N][N];
    endX = N-1; endY = N-1;
    int i = 0;
    while(i < N && scanner.hasNext()) {
      String line =  scanner.nextLine();
      String [] tokens = line.split("\\s+");
      int j = 0;
      for (; j< tokens.length; j++){
        maze[i][j] = tokens[j].charAt(0);
      }
      if(j != N){
        System.err.println("[ERROR] Invalid line: " + i + " has wrong # columns: " + j);
        return;
      }
      i++;
    }
    if(i != N){
      System.err.println("[ERROR] Invalid file: has wrong number of rows: " + i);
      return;
    }
  }

  // Helper method for printing the maze in a matrix format
  public void printMaze() {
     for (int i=0; i < maze.length; i++) {
         for (int j=0; j < maze.length; j++) {
           System.out.print(maze[i][j]);
           System.out.print(' ');
          }
          System.out.println();
     }
  }

  // TODO: Solve the maze stored in the 2D-array "maze" object using a Stack.
  // If your algorithm finds a valid path out of the maze, print a success
  // message: "Maze is solvable." Otherwise, print: "Maze is NOT solvable."
  // Mark the valid positions you visited during your maze walk with an 'X' character.

  public void solveMaze() {
    Stack<MazePosition> search = new Stack<MazePosition>();
    MazePosition start = new MazePosition(0,0);
    if (maze[0][0] == '0') { //For edge case where the start position isn't valid
      search.push(start);
    }
    boolean solved = false;
    while (!search.isEmpty() && (!solved)) {
      //Using a boolean here to stop going through the stack once the end is found
      //or else it will fill in all possible path spots with Xs
      MazePosition mp = search.pop();
      int x = mp.getX();
      int y = mp.getY();
      if ((x == this.endX) && (y == this.endY)) {
        maze[x][y] = 'X';
        System.out.println("Maze is solvable.");
        solved = true;
      }
      else {
        //Having this check in the opposite order of desired priority movement
        //in order to make it populate through the stack more efficiently. Not
        //entirely fool-proof(see maze3), but does help cut down on some of the
        //extra moves. So priority is move right/move down/move up/move left.
        maze[x][y] = 'X';
        if (y > 0) {
          if (maze[x][y-1] == '0') {
            MazePosition temp = new MazePosition(x, y-1);
            search.push(temp);
          }
        }
        if (x > 0) {
          if (maze[x-1][y] == '0') {
            MazePosition temp = new MazePosition(x-1, y);
            search.push(temp);
          }
        }
        if (x < this.endX) {
          if (maze[x+1][y] == '0') {
            MazePosition temp = new MazePosition(x+1, y);
            search.push(temp);
          }
        }
        if (y < this.endY) {
          if (maze[x][y+1] == '0') {
            MazePosition temp = new MazePosition(x, y+1);
            search.push(temp);
          }
        }
      }
    }
    if ((search.isEmpty()) && (!solved)) {
      System.out.println("Maze is NOT solvable.");
    }
  }


  public static void main(String[] args) throws IOException {
    // If no argument is provided, show error message
    if(args.length < 1) {
      System.err.println("[ERROR] Usage: java PathFinder maze_file");
      System.exit(-1);
    }
    // File name is provided properly as the first argument
    String fileName =  args[0];

    MazeSolver ms = new MazeSolver(fileName);
    System.out.println("[Before Traversal] Maze:");
    ms.printMaze();
    System.out.println();

    // Test solver
    ms.solveMaze();
    System.out.println();
    System.out.println("[After Traversal] Maze:");
    ms.printMaze();
  }

}
