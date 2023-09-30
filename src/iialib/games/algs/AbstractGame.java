package iialib.games.algs;

import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;
import iialib.games.model.Score;

import java.util.ArrayList;

public  abstract class AbstractGame<Move extends IMove, Role extends IRole, Board extends IBoard<Move,Role,Board>> {
	
		// Attributes
		Board currentBoard;		
		
		ArrayList<AIPlayer<Move,Role,Board>> players;		
		

		// Constructor
		public AbstractGame(ArrayList<AIPlayer<Move,Role,Board>> players, Board initialBoard){
			this.currentBoard = initialBoard;
			this.players = players;
		}
				
		// Methods
		public void runGame() {
			int index = 0;
			int turn = 0;
			AIPlayer<Move,Role,Board> currentPlayer = players.get(index);
			System.out.println("Game beginning - First player is: " + currentPlayer);
			System.out.println("The board is:");
			System.out.println(currentBoard.boardToString());
			
			while(!currentBoard.isGameOver()) {
				turn += 1;
				System.out.println("Turn " + turn + " - player " + currentPlayer);
				Move nextMove = currentPlayer.bestMove(currentBoard);
				System.out.println("Best Move is: " + nextMove);
				currentBoard = currentPlayer.playMove(currentBoard, nextMove);
				System.out.println("The board is: ");
				System.out.println(currentBoard.boardToString());

				//System.out.println(currentBoard.toString()) //affichage données brutes

				index = 1 - index;
				currentPlayer = players.get(index);
			}
			
			System.out.println("Game over!");
			ArrayList<Score<Role>> scores = currentBoard.getScores();
			for(AIPlayer<Move,Role,Board> p: players)
				for(Score<Role> s : scores)
					if(p.getRole() == s.getRole())
						System.out.println("" + p + " score is: " + s.getStatus() + " " + s.getScore());
				;
		
		}
		
	


}
