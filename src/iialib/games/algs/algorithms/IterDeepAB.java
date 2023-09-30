package iialib.games.algs.algorithms;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

import java.util.ArrayList;

public class IterDeepAB<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

    // Constants
    /** Default value for depth limit */
    private final static int DEPTH_MAX_DEFAUT = 4;
    /** Default value for time research **/
    private final static long DEFAULT_TIME_LIMIT = 5000;

    // Attributes
    /** Role of the max player */
    private final Role playerMaxRole;
    /** Role of the min player */
    private final Role playerMinRole;
    /** Algorithm max depth */
    private int depthMax = DEPTH_MAX_DEFAUT;
    /** Heuristic used by the max player */
    private IHeuristic<Board, Role> h;
    /** Time limit of research **/
    private long timeLimit = DEFAULT_TIME_LIMIT;
    /** Timer **/
    private long startTime;
    /** Timer mode **/
    private boolean timerModeActivated = false;
    /** Number of internal visited (developed) nodes (for stats) */
    private int nbNodes;
    /** Number of leaves nodes nodes (for stats) */
    private int nbLeaves;

    // --------- CONSTRUCTORS ---------

    public IterDeepAB(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
        this.playerMaxRole = playerMaxRole;
        this.playerMinRole = playerMinRole;
        this.h = h;
        this.timerModeActivated = false;
    }

    public IterDeepAB(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
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

    public Move bestMove(Board board){
        System.out.println("[IterDeepAB]");

        if (timerModeActivated){launchTimer();}

        int depth = 1;
        int bestPrevious = 0;

        ArrayList<Move> Moves = board.possibleMoves(this.playerMaxRole);
        Move bgm = Moves.get(bestPrevious);
        while (depth <= depthMax && !timeIsOver()){
            bestPrevious = bestMoveForDepth(board, depth, bestPrevious);
            bgm = Moves.get(bestPrevious);
            depth++;
        }
        return bgm;
    }


    // --------- PUBLIC METHODS ---------

    /**
     * Activer/Desactiver le controle du temps. Si désactivé, attention à la profondeur de recherche...
     * ================================================================================================
     * @param active, bool activer/desactiver
     */
    public void setTimerMod(boolean active){
        this.timerModeActivated = active;
        this.timeLimit = DEFAULT_TIME_LIMIT;
    }

    /**
     * Indiquer la limite de temps de recherche en (ms)
     * ================================================
     * @param milliSeconds, temps de recherche autorisé
     */
    public void setTimeLimit(long milliSeconds){
        this.timeLimit = milliSeconds;
    }

    public String toString() {
        return "AlphaBeta(ProfMax=" + depthMax + ")";
    }

    // --------- PRIVATE METHODS ---------

    /**
     * Etape de l'algorithme Alpha Beta avec Iterative Deepening.
     * ==========================================================
     * @param board, le plateau à évaluer
     * @param depth, profondeur actuelle de recherche
     * @param idBestPrevious, le meilleur indice du coup pour l'étape depth-1
     * @return idBest, le meilleur indice du coup à l'étape depth (ou idBestPrevious, si on dépasse le temps de recherche)
     */
    private int bestMoveForDepth(Board board, int depth, int idBestPrevious) {


        ArrayList<Move> Moves = board.possibleMoves(this.playerMaxRole);
        int alpha = this.h.MIN_VALUE;
        int next = 0;


        /** Priorité à la meilleure branche de l'étape précédente **/
        int ibest = idBestPrevious;
        Board nextBoard = board.play(Moves.get(ibest), this.playerMaxRole);
        next = minMax(nextBoard, depth-1, alpha, this.h.MAX_VALUE);
        if (next > alpha){
            alpha = next;
        }
        //Si temps écoulé on renvoie le meilleur coup à profondeur précédente
        if (timeIsOver()){
            System.out.println(" # Research Time is over. Depth := " + (depth - 1));
            return idBestPrevious;
        }


        /** Recherche sur les autres après... **/
        for (int i=0; i < Moves.size(); i++){
            if (i != idBestPrevious){
                nextBoard = board.play(Moves.get(i), this.playerMaxRole);
                next = minMax(nextBoard, depth-1, alpha, this.h.MAX_VALUE);
                if (next > alpha){
                    alpha = next;
                    ibest = i;
                }
            }
            // Si temps écoulé, on renvoie le meilleur coup à profondeur précédente
            if (timeIsOver()){
                return idBestPrevious;
            }
        }
        return ibest;
    }

    /**
     * Lancement du timer à l'intérieur de la classe
     * =============================================
     */
    private void launchTimer(){
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Evaluation du temps écoulé
     * ==========================
     * @return bool true si temps écoulé et controle de temps activé
     */
    private boolean timeIsOver(){
        return timerModeActivated && ( (System.currentTimeMillis() - this.startTime) > this.timeLimit);
    }

    /**
     * Etape Min de Alpha Beta
     * =======================
     * @param board
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    private int maxMin(Board board, int depth, int alpha, int beta){
        ArrayList<Move> Moves = board.possibleMoves(this.playerMaxRole);
        if (depth==0 || Moves.size() == 0) {
            return this.h.eval(board, playerMaxRole);
        }

        for (int i = 0; i<Moves.size(); i++){
            if (timeIsOver()){return -1;} //Coupure si le temps est écoulé
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

    /**
     * Etape Max de Alpha Beta
     * =======================
     * @param board
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    private int minMax(Board board, int depth, int alpha, int beta){
        ArrayList<Move> Moves = board.possibleMoves(this.playerMinRole);
        if (depth==0 || Moves.size() == 0) {
            return this.h.eval(board, playerMaxRole);
        }

        for (int i = 0; i<Moves.size(); i++){
            if (timeIsOver()){return -1;} //Coupure si le temps est écoulé
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
