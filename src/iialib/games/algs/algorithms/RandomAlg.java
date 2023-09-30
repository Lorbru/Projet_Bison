package iialib.games.algs.algorithms;

import iialib.games.algs.GameAlgorithm;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

import java.util.ArrayList;
import java.util.Random;

public class RandomAlg<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

    private Random rd = new Random();

    @Override
    public Move bestMove(Board board, Role playerRole) {
        System.out.println("[ RANDOM ]");
        ArrayList<Move> possibleMoves = board.possibleMoves(playerRole);
        return possibleMoves.get(rd.nextInt(possibleMoves.size()));
    }

    @Override
    public void setDepth(int initDepth) {

    }
}
