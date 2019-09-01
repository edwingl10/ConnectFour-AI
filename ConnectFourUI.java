/**
 * runs the connect four game 
 * prints out the turns, board, and who 
 * wins or if there is a tie 
 * also asks player for column and 
 * computes computer move 
 * drops piece and switches player turns 
 */
public class ConnectFourUI {
	public static void main(String[] args) {
		System.out.println("Welcome to Connect Four!");
		ConnectFour game = new ConnectFour();
		game.first_player();
		game.display_board();
		//used to place piece on board
		int row;
		int col;
		
		
		while(true) {
		// prints out whose turn it is
		if(game.get_turn() == 1) {
			System.out.println("It's your turn!");
			col = game.choose_column();
			row = game.find_empty_row(col);
		}
		else {
			System.out.println("It's the computers turn!");
			col = game.compute_move();
			row = game.find_empty_row(col);
		}
		
		game.drop_piece(row,col);
		
		//checks if there is a winner
		if(game.has_n_in_a_row(game.get_turn(), row, col, 4)) {
			game.winningPlayer = game.turn;
			game.has_winner = true;
		}
		
		//checks if the board is full 
		else if(game.is_board_full()) {
			game.has_tie = true;
		}
		
		//for displaying purposes 
		System.out.print("\n");
		
		game.display_board();
		
		if(game.has_winner == true || game.has_tie == true) {
			break;
		}
		
		game.switch_turns();
		
		}
		
		game.display_winner();
		System.out.println("Thanks for playing!");
	}
}
