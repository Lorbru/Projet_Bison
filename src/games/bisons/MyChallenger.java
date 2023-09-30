package games.bisons;

import iialib.games.algs.AIPlayer;
import iialib.games.algs.algorithms.IterDeepAB;
import iialib.games.model.IChallenger;
import iialib.games.model.Score;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MyChallenger implements IChallenger {

	// ---------------------  ATTRIBUTES ---------------------

	/** Constantes du challenger **/
	public final static String TEAMNAME = "BRUCATO_DELMAY";         //Le nom de notre équipe
	public final static long DEFAULT_TIME = 280000;                 //Le temps accordé pour le challenger durant une partie (en ms)
	public final static long DEFAULT_NBLEAF_EXPLORED = 1000000000;  //Le nombre maximal de feuilles que notre challenger a le droit d'évaluer
	public final static long DEFAULT_TIME_CRITICAL = 60000;         //Moment à partir duquel on fait particulièrement attention au temps
	public final static int DEFAULT_MINDEPTH = 8;                   //La profondeur minimale souhaitée
	public final static long DEFAULT_TIME_MOVE_LIMIT = 20000;       //Temps maximum utilisé par coup (en ms) (sécurité)

	/** Attributs du challenger **/
	public AIPlayer<BisonsGameMove, BisonsGameRole, BisonsGameBoard> IA; //L'intelligence artificielle
	public BisonsGameRole iGameRole;                                     //Le role donné à notre intelligence artificielle
	public BisonsGameRole otherGameRole;                                 //Le role donné à notre adversaire
	public BisonsGameBoard GameBoard;                                    //Le plateau de jeu

	/** Paramètres d'algorithme de recherche pour l'IA **/
	public IterDeepAB<BisonsGameMove, BisonsGameRole, BisonsGameBoard> alg; //L'algorithme utilisé : Iterative Deepening Alpha Beta avec controle de temps
	public long time = DEFAULT_TIME;                                        //Nombre de secondes restantes pour notre challenger
	ArrayList<BisonsGameMove> forcedMoves = new ArrayList<>();              //Liste des coups à effectuer en priorité (stratégie)
	public int nbCoups = 0;

	// --------------------- METHODS ------------------------

	@Override
	public Set<String> possibleMoves(String role) {

		Set<String> stringMoves = new HashSet<String>();
		ArrayList<BisonsGameMove> myMoves;

		if (role == "BISONS") {
			myMoves = GameBoard.possibleMoves(BisonsGameRole.BISONS);
			for (int i=0; i<myMoves.size(); i++){
				stringMoves.add(myMoves.get(i).toString());
			}
		} else if (role == "INDIEN") {
			myMoves = GameBoard.possibleMoves(BisonsGameRole.INDIEN);
			for (int i=0; i<myMoves.size(); i++){
				stringMoves.add(myMoves.get(i).toString());
			}
		}
		return stringMoves;
	}

	@Override
	public String boardToString() {
		return GameBoard.boardToString();
	}

	@Override
	public String bestMove() {

		// Mesure du temps
        long startTime = System.currentTimeMillis();

		// Best game move
		BisonsGameMove bgm;

		//chargement stratégie de départ
		if (nbCoups == 0 && iGameRole==BisonsGameRole.INDIEN) {
			openGameStratIndien();
		}

		//Recherche totale
		GameBoard.reactivateAllFlags();

		//coups stratégiques
		while (!forcedMoves.isEmpty()) {
			bgm = forcedMoves.remove(0);
			if (GameBoard.isValidMove(bgm, iGameRole)) {
				long timeElapsed = System.currentTimeMillis() - startTime;
				time -= timeElapsed;
				return bgm.toString();
			}
		}

		// Calcul de la profondeur maximale de recherche pour la situation selon les paramètres du challenger
		int maxDepth = getDepth(false);
		IA.setDepth(maxDepth);

		// Controle de temps si en dessous du seuil critique :
		if (time < DEFAULT_TIME_CRITICAL) {
			alg.setTimeLimit(getTempsMoyenParCoup());
			IA.setAi(alg);
		}
		bgm = IA.bestMove(GameBoard);

		//Remise de recherche par défaut
		GameBoard.reactivateAllFlags();

		//Calcul du temps écoulé
		long timeElapsed = System.currentTimeMillis() - startTime;
		time -= timeElapsed;

		//Notre best Move
		return bgm.toString();
	}

	@Override
	public String victory() {
		return "[ " + TEAMNAME + " WON ! ]\n";
	}

	@Override
	public String defeat() {
		return  "[ " + TEAMNAME + " LOST ! ]\n";
	}

	@Override
	public String tie() {
		return "[ TIE GAME ! ]\n";
	}

	@Override
	public String teamName() {
		return TEAMNAME;
	}

	@Override
	public void setBoardFromFile(String fileName) {
		GameBoard = new BisonsGameBoard(fileName);
	}

	@Override
	public void setRole(String role) {
		switch (role){
			case "BISONS":
				iGameRole = BisonsGameRole.BISONS;
				otherGameRole = BisonsGameRole.INDIEN;
				alg = new IterDeepAB<>(iGameRole, otherGameRole, BisonsGameHeuristics.hBisons, DEFAULT_MINDEPTH);
				alg.setTimerMod(true);
				alg.setTimeLimit(DEFAULT_TIME_MOVE_LIMIT);
				break;
			case "INDIEN":
				iGameRole = BisonsGameRole.INDIEN;
				otherGameRole = BisonsGameRole.BISONS;
				alg = new IterDeepAB<>(iGameRole, otherGameRole, BisonsGameHeuristics.hIndien, DEFAULT_MINDEPTH);
				alg.setTimerMod(true);
				alg.setTimeLimit(DEFAULT_TIME_MOVE_LIMIT);
				break;
		}
		IA = new AIPlayer<>(iGameRole, alg);
	}

	@Override
	public void iPlay(String move) {
		BisonsGameMove bmv = new BisonsGameMove(move);
		try {
			GameBoard = GameBoard.checkAndPlay(bmv, iGameRole); //Verifications
			nbCoups++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void otherPlay(String move) {
		BisonsGameMove bmv = new BisonsGameMove(move);
		try {
			GameBoard = GameBoard.checkAndPlay(bmv, otherGameRole); //Verifications
			nbCoups++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	//----------------------------- PRIVATE METHODS -----------------------------

	/**
	 * Retourne la valeur de la profondeur que l'on accorde à l'exploration actuelle du challenger.
	 * Elle dépend des parametres de classe :
	 *  - NB_LEAF_EXPLORED : le nombre de feuilles maximal que l'on s'autorise à évaluer. On cherche une
	 *  	profondeur dont on sait que dans le pire des cas,
	 *  	le nombre de feuilles évaluées ne peut pas être plus élevé que ce paramètre.
	 *  - DEFAULT_DEPTH : la profondeur minimale que l'on souhaite avoir pour chaque coup :
	 *  	Si les facteurs de branchements sont trop élevés et ne permettent pas d'atteindre
	 *  	cette profondeur, l'algorithme va alors tenter d'activer (si ce n'est pas trop risqué)
	 *  	certaines options pour pouvoir améliorer la profondeur
	 *  	(désactivation analyse de chiens, échantillon des déplacements des chiens).
	 * =====================================================================================================
	 * @return depthMax : la profondeur calculée pour la situation actuelle.
	 *  + activation/désactivation de certaines options.
	 */
	private int getDepth(boolean afficheInfo){

		if (afficheInfo){System.out.println("---------- DETAILS DE RECHERCHE ----------");}
		int depth = -1;
		long nbNodes = -1;
		long savePreviousNbNodes = -1;

		int FBB, FBI;
		boolean indianTurn;

		boolean searchAgain = true;
		while(searchAgain){

			depth = 0;
			indianTurn = (iGameRole == BisonsGameRole.INDIEN);

			if (indianTurn){
				nbNodes = GameBoard.possibleMoves(BisonsGameRole.INDIEN).size();
			} else {
				nbNodes = GameBoard.possibleMoves(BisonsGameRole.BISONS).size();
			}
			indianTurn = !indianTurn;

			FBB = getFacteurBBisons(); //Majoration du facteur de branchement bisons pour le reste
			FBI = getFacteurBIndien(); //Majoration du facteur de branchement indien pour le reste

			if (afficheInfo) { System.out.println(" # FBB / FBI := " + FBB + " / " + FBI);}

			while (nbNodes > 0 && nbNodes < DEFAULT_NBLEAF_EXPLORED){
				savePreviousNbNodes = nbNodes;
				if (indianTurn){
					nbNodes *= FBI;
				} else {
					nbNodes *= FBB;
				}
				depth++;
				indianTurn = !indianTurn;
			}
			if (afficheInfo) {
				System.out.println(" # Depth := " + depth);
				System.out.println(" # Max leaves evaluated for depth := " + savePreviousNbNodes);
				System.out.println(" # Max leaves evaluated for depth+1 := " + nbNodes);
			}
			if (depth < DEFAULT_MINDEPTH) {
				//Si la profondeur minimale souhaitée n'est pas atteinte, on regarde si l'on peut supprimer un chien de l'analyse...
				searchAgain = popDogFlags(afficheInfo);
			} else {
				searchAgain = false;
			}
		}

		if (nbCoups > 10) {
			if (afficheInfo) System.out.println(" Final depth := " + (depth+1) );
			return max(1, depth + 1); //Accentuation de la recherche une fois la partie bien entamée
		} else {
			if (afficheInfo) System.out.println(" Final depth := " + depth);
			return max(depth, 1);
		}
	}


	/**
	 * Retourne le nombre de coups maximal que peut encore durer la partie
	 * Information pouvant être utilisée pour une meilleure gestion du temps.
	 * On peut le déterminer facilement en fonction des bisons restants.
	 * ===================================================================
	 * @return le nombre de coups maximal avant fin de partie,
	 */
	private int getNbCoupsMax(){
		int cpt = 1;
		for (int k=0; k<GameBoard.width(); k++){
			if (GameBoard.getBison(k) != -1){
				cpt += (GameBoard.height() - 2 - GameBoard.getBison(k));
			}
		}
		return cpt;
	}

	/**
	 * Retourne le temps moyen maximal à accorder par coup, dépendant de ce que l'on sait sur le nombre de coups restants
	 * possibles...
	 * ================================================================================================================
	 * @return le temps moyen par coup estimé
	 */
	private long getTempsMoyenParCoup(){
		return time/getNbCoupsMax();
	}

	/**
	 * Retourne une majoration du branchement maximal pour les bisons
	 * Permet d'estimer le nombre de feuilles, au pire, à évaluer
	 * pour un tour du bison
	 * ==============================================================
	 * @return int, branchement maximal possible pour les bisons
	 */
	private int getFacteurBBisons() {
		//Il s'agit du nombre de bisons encore en jeu, auquel on enlève le nombre de chiens qui ne sont
		//pas analysés (s'ils ne sont pas analysés, c'est qu'ils bloquent un bison, et peu importe les coups
		//successifs joués, le bison reste alors bloqué)
		return GameBoard.nbBisons() - (GameBoard.nbChiens() - GameBoard.nbDogsActivated());
	}

	/**
	 * Retourne une majoration du branchement maximal pour l'indien
	 * Permet d'estimer le nombre de feuilles, au pire, à évaluer pour
	 * un tour de l'indien
	 * ============================================================
	 * @return int, branchement maximal possible pour l'indien
	 */
	private int getFacteurBIndien(){
		// Voir partie 1 devoir pour la formule...
		// Ce facteur peut diminuer avec les paramètres de recherche :
		// - si des chiens sont désactivés
		// - si l'on ne prend qu'une moitié des déplacements des chiens
		return 8 + (GameBoard.nbDogsActivated()*( (GameBoard.width()-1) + (GameBoard.height()-3) + 2*(min(GameBoard.width()-1, GameBoard.height()-3))));
	}


	/**
	 * Fonction qui tente de désactiver un chien de l'analyse. Le chien désactivé, s'il existe, doit remplir les conditions
	 * suivantes :
	 *  - Il bloque un bison
	 *  - Il n'y a pas d'autres bisons non bloqués plus avancés que celui qu'il bloque
	 *  - Il est le chien le moins avancé de tous les chiens non encore désactivés.
	 * =============================================================================================================
	 * @return boolean, true si un chien est supprimé de l'analyse, false sinon.
	 */
	public boolean popDogFlags(boolean afficheInfo){

		int maxLineBison = 0;
		boolean bloque;
		for (int b=0; b<GameBoard.width(); b++){
			bloque = false;
			if (GameBoard.getBison(b) > maxLineBison){
				for (int c=0; c<GameBoard.nbChiens(); c++){
					if(GameBoard.getChien(c).y == b && GameBoard.getChien(c).x - 1 == GameBoard.getBison(b)){
						bloque = true;
						break;
					}
				}
				if (!bloque){
					maxLineBison = GameBoard.getBison(b);
				}
			}

		}
		for (int c=0; c<GameBoard.nbChiens(); c++){
			if (GameBoard.isActivated(c)) {
				if (GameBoard.getBison(GameBoard.getChien(c).y) == GameBoard.getChien(c).x - 1 && GameBoard.getChien(c).x - 1 > maxLineBison) {
					GameBoard.desactivateFlag(c);
					if(afficheInfo){System.out.println(" # Desactivation chien : " + c + " -> search again...");}
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Insère dans les coups prioritaires un modèle de départ
	 * pour l'indien en mode par défaut, pour varier les parties.
	 * Si l'indien commence à jouer, les coups choisis sont forcément jouables.
	 * - Stratégie 1 : bloquer à gauche et approcher l'indien de la droite
	 * - Stratégie 2 : bloquer à droite et approcher l'indien de la gauche
	 * - Stratégie 3 : Rien... on laisse l'IA choisir
	 * ======================================================================================================
	 */
	public void openGameStratIndien(){
		Random rd = new Random();
		int id = rd.nextInt(3);
		switch (id){
			case 0:
				forcedMoves.add(new BisonsGameMove("F4-B0"));
				forcedMoves.add(new BisonsGameMove("F5-E6"));
				forcedMoves.add(new BisonsGameMove("F3-D1"));
				forcedMoves.add(new BisonsGameMove("E6-D6"));
				break;
			case 1:
				forcedMoves.add(new BisonsGameMove("F6-B10"));
				forcedMoves.add(new BisonsGameMove("F5-E4"));
				forcedMoves.add(new BisonsGameMove("F7-D9"));
				forcedMoves.add(new BisonsGameMove("E4-D4"));
				break;
		}
	}

	//---------------------------- TESTS -------------------------

	/**
	 * Fonction Main pour lancer une partie entre notre challenger "Bison" et notre challenger "Indien"
	 * ================================================================================================
	 * @param args
	 */
	public static void main(String args[]){

		String boardFile = "src/main/java/boardFiles/default";

		BisonsGameBoard gameBoard = new BisonsGameBoard(boardFile);

		MyChallenger Bisons = new MyChallenger();
		MyChallenger Indien = new MyChallenger();

		Bisons.setRole("BISONS");
		Indien.setRole("INDIEN");
		Bisons.setBoardFromFile(boardFile);
		Indien.setBoardFromFile(boardFile);

		String playerTurn = "INDIEN";
		int nbTurns = 0;


		BisonsGameMove bestMove = new BisonsGameMove("A0-A0");
		try {
			while (!gameBoard.isGameOver()) {
				nbTurns++;
				System.out.println("[Next player is " + playerTurn + " ]");
				System.out.println("The gameBoard is : \n" + gameBoard.boardToString());
				if (playerTurn == "BISONS") {
					bestMove.setString(Bisons.bestMove());
					System.out.println("     >> MOVE BISONS : " + bestMove);
					gameBoard = gameBoard.checkAndPlay(bestMove, BisonsGameRole.BISONS);
					Bisons.iPlay(bestMove.toString());
					Indien.otherPlay(bestMove.toString());
					playerTurn = "INDIEN";
				} else {
					bestMove.setString(Indien.bestMove());
					System.out.println("     >> MOVE INDIEN : " + bestMove);
					gameBoard = gameBoard.checkAndPlay(bestMove, BisonsGameRole.INDIEN);
					Bisons.otherPlay(bestMove.toString());
					Indien.iPlay(bestMove.toString());
					playerTurn = "BISONS";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("[ GAME OVER ]");
		System.out.println("The gameBoard is : \n" + gameBoard.boardToString());
		ArrayList<Score<BisonsGameRole>> scores = gameBoard.getScores();
		for (Score<BisonsGameRole> s : scores)
			System.out.println(s);

		System.out.println("============== STATISTIQUES ==============");
		System.out.println("Nombre de demi-coups joués : " + nbTurns);
		System.out.println("Temps de reflexion restant pour Bison : " + Bisons.time);
		System.out.println("Temps de reflexion restant pour Indien : " + Indien.time);

	}

}
