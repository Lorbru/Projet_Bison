package MVCView;

import games.bisons.BisonsGameMove;

public interface IView {


    boolean isShow(int x, int y);

    boolean isSelected(int x, int y);

    boolean isNotSelected(int x, int y);

    String getBisonPlayerType();

    String getIndienPlayerType();

    String getFirstPlayer();

    void setMenuInterface();

    void buildMenuInterface();

    void setSettingsInterface();

    void buildSettingsInterface();

    void setGameInterface();

    void buildGameInterface();

    void setPlayerTurnInfo(String info);

    void setPlayerTypeInfo(String info);

    void setTimeLeftInfo(String info);

    void setLastMoveInfo(String info);

    void setWinner(String info);

    void show(int x, int y);

    void select(int x, int y);

    void play(BisonsGameMove bisonsGameMove);

}
