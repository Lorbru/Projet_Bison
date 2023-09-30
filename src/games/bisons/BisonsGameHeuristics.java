package games.bisons;

import iialib.games.algs.IHeuristic;

import static java.lang.Math.max;

public class BisonsGameHeuristics {


    /**
     * HEURISTIQUE POUR LE BISON
     * =========================
     */
    public static IHeuristic<BisonsGameBoard, BisonsGameRole> hBisons = (board, role) -> {

        int h = 0;

        int nbBisonsRestants = 0;
        int nbBisonsBloques = 0;
        boolean critique = false;
        Position bison = new Position(-1, -1);
        for (int y = 0; y < board.width(); y++) {
            bison.set(board.getBison(y), y);
            if (bison.x == board.height() - 1) {
                return Integer.MAX_VALUE;
            } else if (bison.x == -1) {
                h -= board.height() * board.height();
            } else {
                h += bison.x * bison.x * (board.distance(bison, board.getIndienPos()) + 1);
                nbBisonsRestants++;
                if (bison.x == board.height() - 2) {
                    if (!critique) {
                        critique = true;
                    } else {
                        return Integer.MAX_VALUE;
                    }
                }
            }
        }
        for (int idc = 0; idc < board.nbChiens(); idc++) {
            if (board.getBison(board.getChien(idc).y) == board.getChien(idc).x - 1) {
                nbBisonsBloques++;
            }
        }
        if (nbBisonsRestants == nbBisonsBloques) {
            return Integer.MIN_VALUE;
        }
        return h;


    };


    /**
     * HEURISTIQUE POUR L'INDIEN
     * =========================
     */
    public static IHeuristic<BisonsGameBoard, BisonsGameRole> hIndien = (board, role) -> {


        int h=0;

        boolean critique = false;
        int nbBisonsRestants = 0;
        int nbBisonsBloques = 0;

        int dist = 0;
        int height = board.height();
        int width = board.width();

        Position bison = new Position(-1, -1);
        for (int b=0; b<board.width(); b++){
            bison.set(board.getBison(b), b);
            dist = board.distance(bison, board.getIndienPos());
            if (bison.x < 0){
                h += height*max(height, width)*max(height, width);
            } else {
                nbBisonsRestants++;
                if (bison.x == board.height() - 1){
                    return Integer.MIN_VALUE;
                } else if (bison.x == board.height()-2){
                    if (!critique){
                        critique = true;
                    } else {
                        return Integer.MIN_VALUE;
                    }
                }
                if (bison.x - board.getIndienPos().x > 0) {
                    h -= 2*bison.x * bison.x * dist;
                } else {
                    h -= bison.x * bison.x * dist;
                }
            }
        }
        h -= 4*4*board.distance(board.getIndienPos(), new Position(4, 5));
        if (critique){
            return Integer.MIN_VALUE + 200;
        }
        for (int c = 0; c<board.nbChiens(); c++){
            if (board.getBison(board.getChien(c).y) == board.getChien(c).x - 1){
                nbBisonsBloques++;
                if (nbBisonsRestants  <= board.nbChiens() + 1) {
                    bison.set(board.getChien(c).x - 1, board.getChien(c).y);
                    dist = board.distance(board.getIndienPos(), bison);
                    dist = board.distance(board.getIndienPos(), bison);
                    if (bison.x - board.getIndienPos().x > 0) {
                        h += 2*bison.x * bison.x * dist;
                    } else {
                        h += bison.x * bison.x * dist;
                    }
                }
            }
        }
        if (nbBisonsRestants == nbBisonsBloques){
            return Integer.MAX_VALUE;
        }
        return h;
    };

}
