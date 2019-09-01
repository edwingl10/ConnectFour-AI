import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import java.util.Random;

/** ConenctFour class defines the entire game logic 
 * also implements the AI logic part of the game 
 */
public class ConnectFour {
	
	//the amount of rows and columns for the board
	private final int ROWS = 6;
	private final int COLS = 7;
	//the board that will be used for the game 
	private int [][] board = new int[ROWS][COLS];
	//used to identify a user piece on game board
	private int none = 0;
	private int human = 1;
	private int computer = 2;
	//used to store the winning player
	public int winningPlayer;
	//used to check if there is a winner 
	public boolean has_winner = false;
	//used when board is full
	public boolean has_tie = false;
	//used to identify who's turn it is 
	public int turn;
	//keeps amount of moves to determine if board is full
	private int moves;
	//keeps track of available columns for computer to play
	private ArrayList<Integer> available_cols = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6));
	private ArrayList<Integer> middle_cols = new ArrayList<>(Arrays.asList(2,3,4));
	
	/**
	 * gets the current turn 
	 * @return whose turn it is
	 */
	public int get_turn() {
		return turn;
	}
	
	/**
	 * displays a column number above each column on board
	 * makes it easier for user to identify columns on board
	 */
	private void display_header() {
		for(int i=1; i<=COLS; i++) {
			System.out.print(i+" ");
		}
		System.out.print("\n");
	}
	
	/**
	 * uses display_header() method
	 * prints the game board 
	 * shows where the placed pieces are 
	 */
	public void display_board() {
		display_header();
		for(int i=0; i<ROWS; i++){
			
			for(int j=0; j<COLS; j++){
				
				if(board[i][j] == none) {
					System.out.print(". ");
				}
				
				if(board[i][j] == human) {
					System.out.print("X ");
				}
				
				if(board[i][j] == computer) {
					System.out.print("O ");
				}
			}
			System.out.print("\n");
		}
		System.out.println("_____________");
	}
	
	/**
	 * asks user for input on who goes first
	 * initializes instance variable turn to the first player
	 */
	public void first_player() {
		while(true) {
			try {
				Scanner s = new Scanner(System.in);
				System.out.print("Would you like to go first? (y or n): ");
				char input = s.next().charAt(0);
				if(input == 'y') {
					turn = human;
				}
				else if(input == 'n'){
					turn = computer;
				}	
				else {
					System.out.println("Invalid input. Please try again!");
					continue;
				}
				break;
			
			} catch(Exception e) {
				System.out.println("Invalid input. Please try again.");
				continue;
			}
		}
		
	}
	
	
	/**
	 * switches player turns 
	 * updates instance variable turn 
	 */
	public void switch_turns() {
		if(turn == human) {
			turn = computer;
		}
		else
			turn = human;
	}
	
	/**
	 * checks if a column is full 
	 * @param column number used to check if full 
	 * @return if column is full or not
	 */
	public boolean is_col_full(int column) {
		if(board[0][column] != none) {
			return true;
		}
		return false;
	}

	
	/**
	 * asks for user input on what column they 
	 * want to drop their piece 
	 * checks if input is valid 
	 * @return column number for piece 
	 */
	public int choose_column() {
		while(true) {
			try {
			Scanner input = new Scanner(System.in);
			System.out.print("Choose a column number to drop your piece: ");
			int column = input.nextInt()-1;
			if(is_col_full(column)) {
				System.out.println("Column is full. Please select another column.");
				continue;
			}
			else {
				return column;
			}	
		} catch(Exception e) {
			System.out.println("Invalid input. Please try again.");
			continue;
		}
		}
	}
	
	/**
	 * finds an empty row on the board 
	 * to place player piece
	 * @param column used to find empty row
	 * @return row number to place player piece at 
	 */
	public int find_empty_row(int column) {
		for(int i= ROWS-1; i>=0; i--) {
			if(board[i][column] == none) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * uses moves instance variable to check
	 * if board is full 
	 * @return if board is full or not
	 */
	public boolean is_board_full() {
		return moves == ROWS * COLS;
	}
	
	/**
	 * drops player piece on board
	 * updates moves instance variable 
	 * updates the available columns 
	 * @param row number to drop piece 
	 * @param column number to drop piece
	 */
	public void drop_piece(int row, int column) {
			board[row][column] = turn;
			moves++;
			update_available_cols();		
	}
	
	/**
	 * checks horizontally, vertically and diagonally
	 * on board if player has n in a row
	 * @param turn the players turn 
	 * @param row number of piece on the board
	 * @param col number of piece on the board
	 * @param total number used to check for streak
	 * @return if player has streak that equals the total 
	 */
	public boolean has_n_in_a_row(int turn, int row, int col, int total) {
		//gets the player id
		int player = turn;
		
		//checks horizontal 
		int count = 0;
		for(int col_num = 0; col_num < COLS; col_num++) {
			if(board[row][col_num] == player) {
				count++;
			}
			else {
				count = 0;
			}
			if(count == total) {
				return true;
			}
		}
		
		//checks vertical
		count = 0;
		int row_num = row;
		while(row_num < ROWS && board[row_num][col] == player) {
			count++;
			row_num++;
		}
		if(count == total) {
			return true;
		}
		
		//checks diagonal top left to bottom right
		ArrayList<ArrayList<Integer>> diagonals = topL_bottomR_diagonals(row,col);
		count = 0;
		for(int i=0; i< diagonals.size(); i++) {
			if( board[diagonals.get(i).get(0)][diagonals.get(i).get(1)] == player) {
				count++;
			}
			else {
				count = 0;
			}
			if(count == total) {
				return true;
			}
		}
		
		//checks diagonal bottom left to top right
		ArrayList<ArrayList<Integer>> diagonals2 = bottomL_topR_diagonals(row,col);
		count = 0;
		for(int i=0; i<diagonals2.size(); i++) {
			if(board[diagonals2.get(i).get(0)][diagonals2.get(i).get(1)] == player) {
				count++;
			}
			else {
				count = 0;
			}
			if(count == total) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * gets diagonal coordinates on board from 
	 * top left to bottom right 
	 * @param row number of player piece 
	 * @param col number of player piece 
	 * @return ArrayList of ArrayLists that contain all diagonal coordinates
	 */
	public ArrayList<ArrayList<Integer>> topL_bottomR_diagonals(int row, int col){
		ArrayList<ArrayList<Integer>> diagonals = new ArrayList<ArrayList<Integer>>();
		
		//gets upper diagonal coordinates from piece location
		for(int row_num = row-1, col_num = col-1; is_valid_row_number(row_num) && is_valid_column_number(col_num);
				row_num--, col_num--) {
			diagonals.add(new ArrayList<Integer>(Arrays.asList(row_num,col_num)));
		}
		
		//gets lower diagonal coordinates from piece location
		for(int row_num = row+1, col_num = col+1; is_valid_row_number(row_num) && is_valid_column_number(col_num);
				row_num++, col_num++) {
			diagonals.add(new ArrayList<Integer>(Arrays.asList(row_num,col_num)));
		}
		//adds current piece location to diagonals list 
		diagonals.add(new ArrayList<Integer>(Arrays.asList(row,col)));
		
		//sorts the diagonals list by row 
		Collections.sort(diagonals, new Comparator<ArrayList<Integer>>() {
			public int compare(ArrayList<Integer> int1, ArrayList<Integer> int2) {
				return int1.get(0).compareTo(int2.get(0));
			}
		});
		
		return diagonals;
		
	}
	
	/**
	 * gets diagonals coordinates on board from 
	 * bottom left to top right
	 * @param row number of player piece
	 * @param col number of player piece 
	 * @return ArrayList of ArrayLists that contain all diagonal coordinates
	 */
	public ArrayList<ArrayList<Integer>> bottomL_topR_diagonals(int row, int col){
		ArrayList<ArrayList<Integer>> diagonals = new ArrayList<ArrayList<Integer>>();
		
		//gets lower diagonal coordinates from piece location 
		for(int row_num = row+1, col_num = col-1; is_valid_row_number(row_num) && is_valid_column_number(col_num);
				row_num++, col_num--) {
			diagonals.add(new ArrayList<Integer>(Arrays.asList(row_num,col_num)));
		}
		
		//gets upper diagonal coordinates from piece location 
		for(int row_num = row-1, col_num = col+1; is_valid_row_number(row_num) && is_valid_column_number(col_num);
				row_num--, col_num++) {
			diagonals.add(new ArrayList<Integer>(Arrays.asList(row_num,col_num)));
		}
		
		//adds current player piece to diagonals list 
		diagonals.add(new ArrayList<Integer>(Arrays.asList(row,col)));
		
		//sorts the diagonals list by column 
		Collections.sort(diagonals, new Comparator<ArrayList<Integer>>() {
			public int compare(ArrayList<Integer> int1, ArrayList<Integer> int2) {
				return int1.get(1).compareTo(int2.get(1));
			}
		});
		
		return diagonals;
	}
	
	/**
	 * checks if given column number is valid
	 * @param column number to check
	 * @return if column given is valid or not
	 */
	public boolean is_valid_column_number(int column) {
		return column >=0 && column < COLS;
	}
	
	/**
	 * checks if given row number is valid
	 * @param row number to check
	 * @return if row given is valid or not
	 */
	public boolean is_valid_row_number(int row) {
		return row >=0 && row < ROWS;
	}
	
	/**
	 * displays the winner to the console 
	 * if no winner, displays that there is a tie
	 */
	public void display_winner() {
		if(has_tie) {
			System.out.println("It's A Tie!");
		}
		else {
			if(winningPlayer == human) {
				System.out.println("You Win!");
			}
			else {
				System.out.println("Computer Wins!");
			}
		}
	}
	
	/**
	 * computes a move for the computer to take
	 * @return column number for computer piece 
	 */
	public int compute_move() {
		//places piece at very middle if not taken
		if (board[5][3] == none) {
			return 3;
		}
		
		else {
			
			//takes winning move if there is one 
			for(int i=0; i< available_cols.size(); i++) {
				int col = available_cols.get(i);
				int row = find_empty_row(col);
				board[row][col] = computer;
				if(has_n_in_a_row(computer,row,col,4)) {
					board[row][col] = none;
					return col;
				}
				board[row][col] = none;
			}
			
			//blocks winning move of opponent
			for(int i=0; i<available_cols.size(); i++) {
				int col = available_cols.get(i);
				int row = find_empty_row(col);
				board[row][col] = human;
				if(has_n_in_a_row(human,row,col,4)) {
					board[row][col] = none;
					return col;
				}
				board[row][col] = none;
			}
			
			//makes a 3 streak if possible 		
			for(int i=0; i<available_cols.size(); i++) {
				int col = available_cols.get(i);
				int row = find_empty_row(col);
				board[row][col] = computer;
				if(has_n_in_a_row(computer,row,col,3)) {
					board[row][col] = none;
					return col;
				}
				board[row][col] = none;
			}
			
			//blocks a 3 streak from opponent if possible 
			for(int i=0; i<available_cols.size(); i++) {
				int col = available_cols.get(i);
				int row = find_empty_row(col);
				board[row][col] = human;
				if(has_n_in_a_row(human,row,col,3)) {
					board[row][col] = none;
					return col;
				}
				board[row][col] = none;
			}
			
			
			Random rand = new Random();	
			// randomly chooses a middle column
			if(middle_cols.size() != 0) {
				int index = rand.nextInt(middle_cols.size());
				int col = middle_cols.get(index);
				return col;
			}
			// randomly chooses an outer column
			else {
				int index = rand.nextInt(available_cols.size());
				int col = available_cols.get(index);
				return col;
			}
				
		}			
	}

	/**
	 * updates available_cols and middle_cols
	 * instance variable
	 * as columns fill up, they get taken out of 
	 * both ArrayLists
	 */
	public void update_available_cols() {
		for(int i= 0; i<available_cols.size(); i++) {
			if(is_col_full(available_cols.get(i))) {
				middle_cols.remove(Integer.valueOf(available_cols.get(i)));
				available_cols.remove(Integer.valueOf(available_cols.get(i)));
			}
		}
	}
	
	
}

