package MVCController;

import MVCModel.Model;
import MVCModel.PlayerType;
import MVCView.View;
import games.bisons.BisonsGameMove;
import games.bisons.BisonsGameRole;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

    public static Model model;
    public static View view;

    public Controller(){
        model = new Model();
        view = new View();
    }

    public static void update(int code, int[] datas){

        switch (code){

            //Quitter
            case 0 -> view.dispose();

            //Nouvelle partie
            case 1 -> {
                switch (view.getBisonPlayerType()){
                    case "AI" -> model.setPlayerType(BisonsGameRole.BISONS, PlayerType.IA);
                    case "Human" -> model.setPlayerType(BisonsGameRole.BISONS, PlayerType.Human);
                    case "Random" -> model.setPlayerType(BisonsGameRole.BISONS, PlayerType.Random);
                }
                switch (view.getIndienPlayerType()){
                    case "AI" -> model.setPlayerType(BisonsGameRole.INDIEN, PlayerType.IA);
                    case "Human" -> model.setPlayerType(BisonsGameRole.INDIEN, PlayerType.Human);
                    case "Random" -> model.setPlayerType(BisonsGameRole.INDIEN, PlayerType.Random);
                }
                switch (view.getFirstPlayer()){
                    case "Bisons" -> model.setPlayerTurn(BisonsGameRole.BISONS);
                    case "Indien" -> model.setPlayerTurn(BisonsGameRole.INDIEN);
                    case "Random" -> {
                        Random rd = new Random();
                        int alea = rd.nextInt(2);
                        if (alea == 0){
                            model.setPlayerTurn(BisonsGameRole.BISONS);
                        }
                        else {
                            model.setPlayerTurn(BisonsGameRole.INDIEN);
                        }
                    }
                }
                view.buildGameInterface();
                model.buildDefaultGame();
                view.setTimeLeftInfo("Unlimited");
                switch(model.getPlayerTurn()){
                    case BISONS -> view.setPlayerTurnInfo("BISONS");
                    case INDIEN -> view.setPlayerTurnInfo("INDIEN");
                }
                switch(model.getPlayerType(model.getPlayerTurn())){
                    case IA -> view.setPlayerTypeInfo("AI");
                    case Human -> view.setPlayerTypeInfo("Human");
                    case Random -> view.setPlayerTypeInfo("Random");
                }
                view.setGameInterface();
            }

            //Parametres
            case 2 -> {
                view.buildSettingsInterface();
                view.setSettingsInterface();
            }

            //Sauvegarde des parametres
            case 3 -> {

                view.buildMenuInterface();
                view.setMenuInterface();
            }

            //Menu
            case 4 -> {
                view.buildMenuInterface();
                view.setMenuInterface();
            }

            //Human tries to select
            case 10 -> {

                int x = datas[0];
                int y = datas[1];

                if (view.isNotSelected(x, y)){
                    if (model.isValidSelect(x, y)){
                        view.select(x, y);
                        model.setOriginHumanMove(x, y);
                        ArrayList<BisonsGameMove> posMoves = model.possibleMoves();
                        for (BisonsGameMove move: posMoves){
                            if (x == move.getOrigin().x && y == move.getOrigin().y){
                                int dx = move.getDest().x;
                                int dy = move.getDest().y;
                                view.show(dx, dy);
                            }
                        }
                    }
                }
                else if (view.isShow(x, y)){
                    model.setDestHumanMove(x, y);
                    model.play(model.getHumanMove());
                    view.play(model.getHumanMove());
                    model.changePlayerTurn();
                    view.setTimeLeftInfo("Unlimited");
                    switch (model.getPlayerTurn()){
                        case BISONS -> view.setPlayerTurnInfo("BISONS");
                        case INDIEN -> view.setPlayerTurnInfo("INDIEN");
                    }
                    switch (model.getPlayerType(model.getPlayerTurn())){
                        case IA -> view.setPlayerTypeInfo("AI");
                        case Random -> view.setPlayerTypeInfo("Random");
                        case Human -> view.setPlayerTypeInfo("Human");
                    }
                    view.setLastMoveInfo(model.getHumanMove().toString());
                    view.setTimeLeftInfo("Unlimited");
                    if (model.isGameOver()){
                        switch (model.winner()){
                            case BISONS -> view.setWinner("BISONS");
                            case INDIEN -> view.setWinner("INDIEN");
                        }
                    }
                }
            }

            //Next turn
            case 15 -> {

                view.setTimeLeftInfo("Unlimited");

                switch (model.getPlayerType(model.getPlayerTurn())) {

                    case IA -> {
                        BisonsGameMove bestMove = model.getBestMove();
                        model.play(bestMove);
                        view.play(bestMove);
                        view.setLastMoveInfo(bestMove.toString());
                        model.changePlayerTurn();
                        switch (model.getPlayerTurn()){
                            case BISONS -> view.setPlayerTurnInfo("BISONS");
                            case INDIEN -> view.setPlayerTurnInfo("INDIEN");
                        }
                        switch (model.getPlayerType(model.getPlayerTurn())){
                            case IA -> view.setPlayerTypeInfo("AI");
                            case Random -> view.setPlayerTypeInfo("Random");
                            case Human -> view.setPlayerTypeInfo("Human");
                        }
                    }

                    case Random -> {
                        view.setPlayerTypeInfo("Random");
                        BisonsGameMove bestMove = model.getRandomMove();
                        model.play(bestMove);
                        view.play(bestMove);
                        view.setLastMoveInfo(bestMove.toString());
                        model.changePlayerTurn();
                        switch (model.getPlayerTurn()){
                            case BISONS -> view.setPlayerTurnInfo("BISONS");
                            case INDIEN -> view.setPlayerTurnInfo("INDIEN");
                        }
                        switch (model.getPlayerType(model.getPlayerTurn())){
                            case IA -> view.setPlayerTypeInfo("AI");
                            case Random -> view.setPlayerTypeInfo("Random");
                            case Human -> view.setPlayerTypeInfo("Human");
                        }
                    }

                }

                if (model.isGameOver()){
                    switch(model.winner()){
                        case BISONS -> view.setWinner("BISONS");
                        case INDIEN -> view.setWinner("INDIEN");
                    }
                }


            }

        }






    }
}
