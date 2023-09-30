package iialib.games.algs.algorithms;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

import java.util.ArrayList;

/*
 * "The Sooner The Better"
 * Dans certains jeux (où les pièces peuvent revenir à leur position précédente par exemple), 
 * il est possible qu'un joueur ayant une stratégie gagnante n'atteigne jamais la fin de partie 
 * parce que MiniMax repousse dans le temps le noeud de victoire envisagé :
 * 
 *         (A)
 *         / \
 *       [B] [C]+inf
 *       /
 *     (D)
 *     / \
 *   [E] [F]+inf
 *   
 *   AMI joue A -> B (parce que plus loin dans la branche, F est un état de victoire)
 *   ENNEMI joue B -> D
 *   
 *         (D)
 *         / \
 *       [E] [F]+inf
 *       /
 *     (A)
 *     / \
 *   [B] [C]+inf
 *   
 *   AMI joue D -> E (parce que plus loin dans la branche, C est un état de victoire)
 *   ENNEMI joue E -> A
 *   etc.
 * 
 * Une façon de résoudre le problème est de différencier les valeurs associées aux fins de partie
 * pour favoriser les noeuds les moins profonds.
 */

public class MiniMax<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	private final static int DEPTH_MAX_DEFAULT = 4;

	// Attributes
	private final Role playerMaxRole;
	private final Role playerMinRole;
	private int depthMax = DEPTH_MAX_DEFAULT;
	private IHeuristic<Board, Role> h;
	private int nbNodes;
	private int nbLeaves;

	// --------- Constructors ---------

	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	public Move bestMove(Board board, Role playerRole) {
		System.out.println("[MiniMax]");

		this.nbNodes = 1; // root node
		this.nbLeaves = 0;
		int max = IHeuristic.MIN_VALUE;
		int newVal;

		// Compute all possible moves for playerMaxRole
		ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
		System.out.println("    * " + allMoves.size() + " possible moves");
		Move bestMove = (allMoves.size() == 0 ? null : allMoves.get(0));

		
		for (Move move : allMoves) {
			newVal = minMax(board.play(move, playerMaxRole), 1);
			//System.out.println("Le coup " + move + " a pour valeur minimax " + newVal);
			if (newVal > max) {
				max = newVal;
				bestMove = move;
			}
		}
		
		System.out.println("    * " + nbNodes + " nodes explored");
		System.out.println("    * " + nbLeaves + " leaves evaluated");
		System.out.println("Best value is: " + max);
		return bestMove;
	}

	@Override
	public void setDepth(int initDepth) {
		depthMax = initDepth;
	}

	/*
	 * PUBLIC METHODS ==============
	 */

	public String toString() {
		return "MiniMax(ProfMax=" + depthMax + ")";
	}

	/*
	 * PRIVATE METHODS ===============
	 */
	private int maxMin(Board board, int depth) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			// Evaluate board with h while playerMaxRole is about to play
			return h.eval(board, playerMaxRole);
		} else {
			nbNodes++;
			int max = IHeuristic.MIN_VALUE;
			// Compute all possible moves for playerMaxRole
			ArrayList<Move> allMoves = board.possibleMoves(playerMaxRole);
			int newVal;
			
			for (Move move : allMoves) {
				newVal = minMax(board.play(move,playerMaxRole), depth+1);
				if (newVal == IHeuristic.MAX_VALUE) {
					// It is considered that playerMax will favor
					// the least deep case of playerMax-victory
					newVal -= depth;
				}
				
				max = Math.max(max, newVal);
			}
			
			return max;
		}
	}

	private int minMax(Board board, int depth) {
		if (depth == depthMax || board.isGameOver()) {
			nbLeaves++;
			// Evaluate board with h while playerMinRole is about to play
			return h.eval(board, playerMinRole);
		} else {
			nbNodes++;
			int min = IHeuristic.MAX_VALUE;
			// Compute all possible moves for playerMinRole
			ArrayList<Move> allMoves = board.possibleMoves(playerMinRole);
			int newVal;
			
			for (Move move : allMoves) {
				newVal =  maxMin(board.play(move,playerMinRole), depth+1);
				if (newVal == IHeuristic.MIN_VALUE) {
					// It is considered that playerMin will favor
					// the least deep case of playerMax-defeat
					newVal += depth;
				}
				
				min = Math.min(min, newVal);
			}

			return min;
		}
	}

}
