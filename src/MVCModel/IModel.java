package MVCModel;

import games.bisons.BisonsGameMove;
import games.bisons.BisonsGameRole;

import java.util.ArrayList;

public interface IModel {

    boolean isValidSelect(int x, int y);

    /* Le deplacement est correct dans le contexte de jeu ? */
    boolean isValidMove(BisonsGameMove bisonsGameMove);

    /* Le jeu est game over ? */
    boolean isGameOver();

    /* Role vainqueur ? */
    BisonsGameRole winner();

    /* Type de joueur pour un role ? */
    PlayerType getPlayerType(BisonsGameRole bisonsGameRole);

    /* Tour du joueur actuel ? */
    BisonsGameRole getPlayerTurn();

    /* Coup humain ? */
    BisonsGameMove getHumanMove();

    /* Coup IA ? */
    BisonsGameMove getBestMove();

    /* Coup random ? */
    BisonsGameMove getRandomMove();

    ArrayList<BisonsGameMove> possibleMoves();

    /* Jouer un coup */
    void play(BisonsGameMove bisonsGameMove);

    /* Saisir tour du joueur */
    void setPlayerTurn(BisonsGameRole bisonsGameRole);

    /* Changer tour du joueur */
    void changePlayerTurn();

    /* Saisir type de joueur */
    void setPlayerType(BisonsGameRole bisonGameRole, PlayerType playerType);

    /* Saisir origine du mouvement humain */
    void setOriginHumanMove(int x, int y);

    /* Saisir destination du mouvement humain */
    void setDestHumanMove(int x, int y);


    void removeHumanMove();

    void buildDefaultGame();

}
