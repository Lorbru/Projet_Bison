package MVCModel;

import games.bisons.*;
import iialib.games.model.Score;

import java.util.ArrayList;
import java.util.Random;

import static games.bisons.BisonsGameRole.BISONS;
import static games.bisons.BisonsGameRole.INDIEN;

public class Model implements IModel {

    private PlayerType bisonPlayerType;
    private PlayerType indienPlayerType;

    private MyChallenger bison;
    private MyChallenger indien;
    private BisonsGameBoard board;
    private BisonsGameRole actualPlayerRole;

    private int ox, oy;
    private int dx, dy;

    public Model(){

        bisonPlayerType = PlayerType.IA;
        indienPlayerType = PlayerType.IA;
        actualPlayerRole = INDIEN;

        buildDefaultGame();
    }


    @Override
    public boolean isValidSelect(int x, int y) {
        boolean valid = false;
        for (BisonsGameMove move : board.possibleMoves(actualPlayerRole)){
            if (move.getOrigin().x == x && move.getOrigin().y == y){
                valid = true;
                break;
            }
        }
        switch (actualPlayerRole){
            case BISONS -> {
                return valid && (bisonPlayerType == PlayerType.Human);
            }
            case INDIEN -> {
                return valid && (indienPlayerType == PlayerType.Human);
            }
        }
        return false;
    }

    @Override
    public boolean isValidMove(BisonsGameMove bisonsGameMove) {
        return board.isValidMove(bisonsGameMove, actualPlayerRole);
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }

    @Override
    public BisonsGameRole winner() {
        ArrayList<Score<BisonsGameRole>> scores = board.getScores();
        if (scores.get(0).getScore() == 1){
            return scores.get(0).getRole();
        } else if (scores.get(1).getScore() == 1) {
            return scores.get(1).getRole();
        }
        return null;
    }

    @Override
    public PlayerType getPlayerType(BisonsGameRole bisonsGameRole) {
        switch (bisonsGameRole){
            case BISONS -> {
                return bisonPlayerType;
            }
            case INDIEN -> {
                return indienPlayerType;
            }
        }
        return null;
    }

    @Override
    public BisonsGameRole getPlayerTurn() {
        return actualPlayerRole;
    }

    @Override
    public BisonsGameMove getHumanMove() {
        return new BisonsGameMove(ox, oy, dx, dy);
    }

    @Override
    public BisonsGameMove getBestMove() {
        switch (actualPlayerRole){
            case BISONS -> {
                return new BisonsGameMove(bison.bestMove());
            }
            case INDIEN -> {
                return new BisonsGameMove(indien.bestMove());
            }
        }
        return null;
    }

    @Override
    public BisonsGameMove getRandomMove() {

        Random rd = new Random();
        int alea = rd.nextInt(board.possibleMoves(actualPlayerRole).size());

        return board.possibleMoves(actualPlayerRole).get(alea);
    }

    @Override
    public ArrayList<BisonsGameMove> possibleMoves() {
        return board.possibleMoves(actualPlayerRole);
    }

    @Override
    public void play(BisonsGameMove bisonsGameMove) {
        if (isValidMove(bisonsGameMove)){

            switch (actualPlayerRole) {

                case BISONS -> {
                    bison.iPlay(bisonsGameMove.toString());
                    indien.otherPlay(bisonsGameMove.toString());
                    board = board.play(bisonsGameMove, actualPlayerRole);
                }

                case INDIEN -> {
                    bison.otherPlay(bisonsGameMove.toString());
                    indien.iPlay(bisonsGameMove.toString());
                    board = board.play(bisonsGameMove, actualPlayerRole);
                }

            }

        }
    }

    @Override
    public void setPlayerTurn(BisonsGameRole bisonsGameRole) {
        actualPlayerRole = bisonsGameRole;
    }

    @Override
    public void changePlayerTurn() {
        switch (actualPlayerRole){
            case BISONS -> actualPlayerRole = INDIEN;
            case INDIEN -> actualPlayerRole = BISONS;
        }
    }

    @Override
    public void setPlayerType(BisonsGameRole bisonGameRole, PlayerType playerType) {
        switch (bisonGameRole){
            case BISONS -> {
                bisonPlayerType = playerType;
            }
            case INDIEN -> {
                indienPlayerType = playerType;
            }
        }
    }

    @Override
    public void setOriginHumanMove(int x, int y) {
        ox = x;
        oy = y;
    }

    @Override
    public void setDestHumanMove(int x, int y) {
        dx = x;
        dy = y;
    }

    @Override
    public void removeHumanMove() {
        ox = -1;
        oy = -1;
        dx = -1;
        dy = -1;
    }

    @Override
    public void buildDefaultGame() {

        bison = new MyChallenger();
        bison.setBoardFromFile("src/boardFiles/default");
        bison.setRole("BISONS");

        indien = new MyChallenger();
        indien.setBoardFromFile("src/boardFiles/default");
        indien.setRole("INDIEN");

        board = new BisonsGameBoard("src/boardFiles/default");

    }
}
