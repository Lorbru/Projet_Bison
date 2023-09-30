package iialib.games.algs.algorithms;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

import java.util.ArrayList;

public class AlphaBeta<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

    // Constants
    /** Default value for depth limit */
    private final static int DEPTH_MAX_DEFAUT = 4;

    // Attributes
    /** Role of the max player */
    private final Role playerMaxRole;
    /** Role of the min player */
    private final Role playerMinRole;
    /** Algorithm max depth */
    private int depthMax = DEPTH_MAX_DEFAUT;
    /** Heuristic used by the max player */
    private IHeuristic<Board, Role> h;
    /** Number of internal visited (developed) nodes (for stats) */
    private int nbNodes;
    /** Number of leaves nodes nodes (for stats) */
    private int nbLeaves;

    // --------- CONSTRUCTORS ---------

    public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
        this.playerMaxRole = playerMaxRole;
        this.playerMinRole = playerMinRole;
        this.h = h;
    }

    public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
        this(playerMaxRole, playerMinRole, h);
        this.depthMax = depthMax;
    }

    // --------- IAlgo METHODS ---------

    public void setDepth(int depth){
        this.depthMax = depth;
    }

    @Override
    public Move bestMove(Board board, Role playerRole) {
        // In this version, the playerRole parameter is ignored,
        // since playerMaxRole is fixed in the constructor.
        return bestMove(board);
    }

    public Move bestMove(Board board) {
        System.out.println("[AlphaBeta]");

        ArrayList<Move> Moves = board.possibleMoves(this.playerMaxRole);
        Move bestMove = Moves.get(0);
        int alpha = this.h.MIN_VALUE;
        int next = 0;
        for (int i=0; i < Moves.size(); i++){
            Board nextBoard = board.play(Moves.get(i), this.playerMaxRole);
            next = minMax(nextBoard, this.depthMax-1, alpha, this.h.MAX_VALUE);
            if (next > alpha){
                bestMove = Moves.get(i);
                alpha = next;
            }
        }

        return bestMove;
    }

    // --------- PUBLIC METHODS ---------

    public String toString() {
        return "AlphaBeta(ProfMax=" + depthMax + ")";
    }

    // --------- PRIVATE METHODS ---------

    private int maxMin(Board board, int depth, int alpha, int beta){
        ArrayList<Move> Moves = board.possibleMoves(this.playerMaxRole);
        if (depth==0 || Moves.size() == 0) {
            return this.h.eval(board, playerMaxRole);
        }

        for (int i = 0; i<Moves.size(); i++){
            Board nextBoard = board.play(Moves.get(i), playerMaxRole);
            int next = minMax(nextBoard, depth-1, alpha, beta);
            if (next > alpha){
                alpha = next;
            }
            if (alpha >= beta) {
                return beta;
            }
        }

        return alpha;
    }

    private int minMax(Board board, int depth, int alpha, int beta){
        ArrayList<Move> Moves = board.possibleMoves(this.playerMinRole);
        if (depth==0 || Moves.size() == 0) {
            return this.h.eval(board, playerMaxRole);
        }

        for (int i = 0; i<Moves.size(); i++){
            Board nextBoard = board.play(Moves.get(i), playerMinRole);
            int next = maxMin(nextBoard, depth-1, alpha, beta);
            if (next < beta){
                beta = next;
            }
            if (alpha >= beta) {
                return alpha;
            }
        }

        return beta;
    }

}
