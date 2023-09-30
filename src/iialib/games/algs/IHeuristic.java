package iialib.games.algs;

import iialib.games.model.IBoard;
import iialib.games.model.IRole;

@FunctionalInterface
public interface IHeuristic<Board extends IBoard<?,Role, Board>,Role extends IRole> {
	
	public static int MIN_VALUE = Integer.MIN_VALUE;
	public static int MAX_VALUE = Integer.MAX_VALUE;
		
	int eval(Board board,Role role);

}
 