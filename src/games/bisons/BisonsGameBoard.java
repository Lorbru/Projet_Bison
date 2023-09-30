package games.bisons;

import iialib.games.model.IBoard;
import iialib.games.model.Score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static games.bisons.BisonsGameRole.BISONS;
import static java.lang.Math.*;

public class BisonsGameBoard implements IBoard<BisonsGameMove, BisonsGameRole, BisonsGameBoard> {

	public static final int DEFAULT_HEIGHT = 7;
	public static final int DEFAULT_WIDTH = 11;

	// ---------------------  ATTRIBUTES ---------------------

	//Les parametres par défaut du jeu
	private int width;     //Largeur du plateau
	private int height;    //Hauteur du plateau
	private int nb_chiens; //Nombre de chiens

	//Les pions du joueurs bison :
	// - taille : (width)
	// - bisons tous identifiés par i=-1
	private int[] bisons;

	//Les pions du joueur indien :
	// - taille (nb_chiens+1)
	// - chien identifiés par 0 <= i < nb_chiens
	// - indien identifié par i = nb_chiens
	private Position[] indien;

	//Parametres qui limitent la selection de coups de possibleMoves
	private boolean[] indienFlags; //Si indienFlags[i] == false : le pion i n'est pas analysé

	// ---------------------  CONSTRUCTORS ---------------------

	/**
	 * Initialise un plateau selon les trois parametres par défaut. Les pions ne sont pas placés !
	 * ===========================================================================================
	 * @param nwidth, largeur
	 * @param nheight, hauteur
	 * @param nnb_chiens, nombre de chiens
	 */
	public BisonsGameBoard(int nwidth, int nheight, int nnb_chiens){
		width = nwidth;
		height = nheight;
		nb_chiens = nnb_chiens;

		bisons = new int[width];
		indien = new Position[nb_chiens + 1];
		indienFlags = new boolean[nb_chiens];
		Arrays.fill(indienFlags, true);
	}

	/**
	 * Constructeur qui construit le plateau par la lecture d'un fichier selon la convention du sujet
	 * ==============================================================================================
	 * @param fileName, le chemin d'accès au fichier texte
	 */
	public BisonsGameBoard(String fileName) {

		BufferedReader br;

		try {
			//Lecture du fichier et récupération des données.
			br = new BufferedReader(new FileReader(fileName));
			String line = br.readLine();

			width = 0;
			height = 0;
			nb_chiens = 0;
			bisons = new int[width];
			boolean thereIsAnIndian = false;
			Position posIndien = null;
			Vector<Position> posChiens = new Vector<>();
			Vector<Position> posBisons = new Vector<>();

			while(line != null){
				if (line.length() > width){
					width = line.length();
				}
				for (int col = 0; col<line.length(); col++){
					switch (line.charAt(col)){
						case 'B' :
							posBisons.add(new Position(height, col));
							break;
						case 'C' :
							posChiens.add(new Position(height, col));
							nb_chiens++;
							break;
						case 'I' :
							if (thereIsAnIndian) throw new Exception("Invalid gameboard in file : more than one indian");
							posIndien  = new Position(height, col);
							thereIsAnIndian = true;
							break;
					}

				}
				line = br.readLine();
				height++;
			}

			//Y a t-il plus de pions de l'indien que de bisons ?
			if (nb_chiens >= width-1){
				throw new Exception("Invalid gameboard file : there is too many dogs");
			}

			//Placement des pions de l'indien, en vérifiant qu'ils sont bien placés
			indien = new Position[nb_chiens + 1];
			for (int k=0; k<nb_chiens; k++){
				if (posChiens.get(k).x == height-1){
					//Pour comptabiliser une ligne (arrivée des bisons) de plus après les pions de l'indien, si ces derniers sont en dernière ligne du fichier
					height++;
				} else if (posChiens.get(k).x == 0){
					throw new Exception("Invalid gameboard file : a dog is on the first line of the board");
				}
				indien[k] = posChiens.get(k);
			}
			if (!thereIsAnIndian){
				throw new Exception("Invalid gameboard file : there is no indian");
			}
			if (posIndien.x == height-1){
				//Pour comptabiliser une ligne (arrivée des bisons) de plus après les pions de l'indien, si ces derniers sont en dernière ligne du fichier
				height++;
			} else if (posIndien.x == 0){
				throw new Exception("Invalid gameboard file : the indian is on the first line of the board");
			}
			indien[nb_chiens] = posIndien;

			//Placement des pions des bisons, en vérifiant qu'ils sont bien placés, et qu'ils ne sont qu'un par colonne
			bisons = new int[width];
			Arrays.fill(bisons, -1);
			for (Position posBison : posBisons) {
				if (bisons[posBison.y] != -1) {
					throw new Exception("Invalid gameboard file : more than one bison on a same column");
				}
				bisons[posBison.y] = posBison.x;
			}
			indienFlags = new boolean[nb_chiens];
			Arrays.fill(indienFlags, true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ---------------------  PREDICATES ---------------------

	/**
	 * Indique si un coup est valide dans le contexte.
	 * Permet de tester les prochains algorithmes de recherches de deplacements
	 * ========================================================================
	 * @param move,      le deplacement souhaité
	 * @param BisonsGameRole, le role du joueur qui effectue le coup
	 * @return booleen, true si le coup est valide, false sinon
	 */
	@Override
	public boolean isValidMove(BisonsGameMove move, BisonsGameRole BisonsGameRole) {

		//Recuperation données
		int x = move.getOrigin().x;
		int y = move.getOrigin().y;
		int xDest = move.getDest().x;
		int yDest = move.getDest().y;

		//On vérifie d'abord que l'on parle de cases présentes sur le plateau et d'un déplacement non nul
		if (x < 0 || y < 0 || xDest < 0 || yDest < 0 || x >= height || y >= width || xDest >= height || yDest >= width || (x == xDest && y == yDest)) {
			return false;
		}

		//On recherche l'élément de la case
		Case myCase = getCase(x, y);
		Case dest = getCase(xDest, yDest);

		if (BisonsGameRole == BISONS) { //Si le joueur BISONS joue, on vérifie les criteres de deplacement d'un bison :
			if (x + 1 != xDest || y != yDest) { //Un bison doit rester sur sa colonne et n'avance que d'une case
				return false;
			}
			if (myCase != Case.Bison) { //La case concernée doit contenir un bison
				return false;
			}
			//La case de destination doit être vide (le bison ne doit pas être bloqué)
			return dest == Case.Vide;
		} else {
			//Si le joueur INDIEN joue, on vérifie les criteres de deplacement :
			if (xDest == 0 || xDest == height - 1) { //La destination ne doit pas être au dela des rivières
				return false;
			}
			if (myCase == Case.Indien) { //Si le pion est l'indien :
				if (move.norme() != 1) { //Il ne doit pas se deplacer de plus d'une case
					return false;
				}
				//La destination doit être accessible (vide ou bison)
				return dest == Case.Vide || dest == Case.Bison;
			}  else if (myCase == Case.Chien){ //Si le pion est un chien
				if (dest != Case.Vide) { //La destination doit être accessible
					return false;
				}
				int dirId = getDirId(move.getOrigin(), move.getDest());
				if (dirId != -1){ //On doit vérifier aussi que toutes les cases entre la destination et le depart sont libres.
					int dist = distance(move.getOrigin(), move.getDest());
					Position vecDir = getVecDir(dirId);
					assert vecDir != null;
					for (int k=0; k<dist; k++){
						x += vecDir.x;
						y += vecDir.y;
						if (getCase(x, y) != Case.Vide){
							return false;
						}
					}
				} else { //Si la direction n'est pas valide...
					return false;
				}
			} else { //Si le pion n'est ni un chien, ni l'indien
				return false;
			}
		}

		return true;
	}

	/**
	 * Regarde si la partie est terminée dans l'état actuel
	 * ====================================================
	 * @return bool, true si la partie est terminée.
	 */
	@Override
	public boolean isGameOver(){
		int nbBisons = 0;
		for (int k=0; k<width; k++){
			if (bisons[k] == height-1){
				return true; //Partie terminée si un bison atteint l'arrivée
			} else if (bisons[k] != -1){
				nbBisons++;
			}
		}
		return nbBisons == nbBisonsBloques(); //Partie terminée si plus de bisons ou bisons tous bloqués
	}

	/** Regarde si le k-ième chien est désactivé
	 * =========================================
	 * @param idChien, identifiant du chien
	 * @return bool, true si le chien est désactivé
	 */
	public boolean isActivated(int idChien){
		return indienFlags[idChien];
	}


	// ---------------------  GETTERS ---------------------

	/**
	 * Hauteur du plateau
	 * ==================
	 * @return height
	 */
	public int height(){return height;}

	/**
	 * Largeur du plateau
	 * ==================
	 * @return width
	 */
	public int width(){return width;}

	/**
	 * Nombre de chiens
	 * ================
	 * @return nbChiens
	 */
	public int nbChiens(){return nb_chiens;}

	/**
	 * Nombre de bisons encore en jeu
	 * ==============================
	 * @return nbBisons
	 */
	public int nbBisons(){
		int cpt = 0;
		for (int b=0; b<width; b++){
			if (bisons[b] != -1){
				cpt++;
			}
		}
		return cpt;
	}

	/**
	 * Retourne le nombre de bisons directement bloqués par un chien ou l'indien
	 * =========================================================================
	 * @return int, nombre de bisons bloques
	 */
	public int nbBisonsBloques(){
		int nb = 0;
		for (int id=0; id<nb_chiens+1; id++){
			if (bisons[indien[id].y] == indien[id].x-1){
				nb++;
			}
		}
		return nb;
	}

	/**
	 * Retourne le type de case en coordonnée x, y
	 * ===========================================
	 * @param x, coord x
	 * @param y, coord y
	 * @return type de case de la coordonnée (Vide, Bison, Chien, Indien)
	 */
	public Case getCase(int x, int y) {
		Position thisCase = new Position(x, y);
		if (bisons[y] == x) {                            //Case d'un bison ?
			return Case.Bison;
		} else {                                         //Case d'un chien ou de l'indien ?
			for (int id = 0; id <  nb_chiens; id++) {
				if (indien[id].equal(thisCase)) {
					return Case.Chien;
				}
			}
			if (indien[nb_chiens].equal(thisCase)){
				return Case.Indien;
			}
		}
		return Case.Vide;
	}

	/**
	 * Retourne la ligne du bison de la colonne spécifiée
	 * ==================================================
	 * @param k, la colonne recherchée.
	 * @return la ligne du bison de la colonne k
	 */
	public int getBison(int k) {
		return bisons[k];
	}

	/**
	 * Retourne la position du k-ième chien
	 * ==========================================
	 * @param k, identifiant du chien
	 * @return la position (x, y) du k-ième chien
	 */
	public Position getChien(int k) {
		return indien[k];
	}

	/**
	 * Retourne la position de l'indien
	 * ================================
	 * @return indien position (x, y)
	 */
	public Position getIndienPos() {
		return indien[nb_chiens];
	}

	/**
	 * Calcule le nombre de cases entre deux Positions, en nombre de placements d'une case.
	 * Ex : C0 _ _ _ C1 : distance(C0, C1) = 3
	 * ================================================================================
	 * @param p1 , première Position
	 * @param p2 , seconde Position
	 * @return int, nombre de cases entre les deux posisions
	 */
	public int distance(Position p1, Position p2) {
		return max(abs(p1.x - p2.x), abs(p1.y - p2.y)) - 1;
	}

	/**
	 * Retourne une copie du plateau actuel
	 * ===================================
	 * @return Copie du BisonsGameBoard
	 */
	public BisonsGameBoard copy() {

		BisonsGameBoard copy = new BisonsGameBoard(this.width, this.height, this.nb_chiens);
		for (int k=0; k<nb_chiens+1; k++){
			copy.indien[k] = new Position(this.indien[k].x, this.indien[k].y);
		}
		System.arraycopy(this.bisons, 0, copy.bisons, 0, width);
		System.arraycopy(this.indienFlags, 0, copy.indienFlags, 0, nb_chiens);
		return copy;

	}

	/**
	 * Affiche les données brutes de l'objet
	 * =====================================
	 */
	public String toString(){
		String str = "---------------------------------------\n";
		str += "DONNEES \n";
		str += "    * Bisons : [";
		for (int k=0; k<width; k++){
			str += bisons[k] + ", ";
		}
		str += "]\n";

		str += "    * Nombre de bisons : " + nbBisons() + "\n";
		str += "    * Indien : \n";
		for (int k =0; k<nb_chiens; k++){
			str += "       C" + k + " -> ( "+ indien[k].x + ", " + indien[k].y + ")\n";
		}
		str += "       I -> ( "+ indien[nb_chiens].x + ", " + indien[nb_chiens].y + ")\n";
		str += "---------------------------------------\n";
		return str;
	}

	/**
	 * Retourne une chaine représentant le plateau
	 * ===========================================
	 * @return String, représentation du plateau
	 */
	@Override
	public String boardToString(){
		String str = "";
		for (int l=0; l<height; l++){
			for (int c=0; c<width; c++){
				Case myCase = getCase(l, c);
				switch (myCase){
					case Bison : str+="B"; break;
					case Indien : str+="I"; break;
					case Chien : str+="C"; break;
					case Vide : str+=" "; break;
				}
			}
			str+="\n";
		}
		return str;
	}

	/**
	 * Retourne une selection des mouvements possibles pour l'indien
	 * ====================================================================
	 * @param bisonsGameRole, le role du joueur qui joue
	 * @return la liste des mouvements possibles pour les pions indiqués
	 */
	public ArrayList<BisonsGameMove> possibleMoves(BisonsGameRole bisonsGameRole){

		//Initialisation tableau pour les move possibles
		ArrayList<BisonsGameMove> possibleMoves = new ArrayList<BisonsGameMove>();

		//Cas du joueur bison
		if (bisonsGameRole == BISONS){

			boolean[] flags = new boolean[width];
			Arrays.fill(flags, true);
			for (int id=0; id < nb_chiens+1; id++){
				if (indien[id].x == bisons[indien[id].y] + 1){
					flags[indien[id].y] = false;
				}
			}
			for (int k=0; k<width; k++){
				if (flags[k] && bisons[k]!=-1){
					possibleMoves.add(new BisonsGameMove(bisons[k], k, bisons[k]+1, k));
				}
			}

		//Cas du joueur indien
		} else {

			int[][] moveMatrix = new int[nb_chiens + 1][8];

			// 1) Initialisation de la matrice de deplacement : on suppose d'abord que les pieces ne se gènent pas entre elles.
			// On considère que l'indien agit comme un chien pour le moment...
			for (int id = 0; id < nb_chiens + 1; id++) {
				moveMatrix[id][0] = indien[id].x - 1;                          //cases NORD (ligne  - 1)
				moveMatrix[id][1] = height - 2 - indien[id].x;                 //cases SUD  (hauteur - 2 - ligne)
				moveMatrix[id][2] = indien[id].y;                              //cases OUEST (colonne)
				moveMatrix[id][3] = width - 1 - indien[id].y;                  //cases EST (largeur - 1 - colonne)
				moveMatrix[id][4] = min(moveMatrix[id][0], moveMatrix[id][2]); //cases NORD-OUEST (min(NORD, OUEST))
				moveMatrix[id][5] = min(moveMatrix[id][1], moveMatrix[id][3]); //cases SUD-EST (min(SUD, EST))
				moveMatrix[id][6] = min(moveMatrix[id][0], moveMatrix[id][3]); //cases NORD-EST (min(NORD, EST))
				moveMatrix[id][7] = min(moveMatrix[id][1], moveMatrix[id][2]); //cases SUD-OUEST (min(SUD, OUEST))
			}

			// 2) On regarde pour chaque chien (et seulement les chiens car l'indien n'est pas bloqué par les bisons)
			// si des bisons le bloque dans certaines directions, à distances plus proches
			int Dir;   //identifiant de la direction
			int Dist;  //ecart entre un bison et l'objet etudié
			Position bison = new Position(-1, -1); //position du bison

			for (int c = 0; c < width; c++) { //Pour chaque bison
				if (bisons[c] > 0 && bisons[c]!=height-1) { //S'il n'est pas éliminé et n'est pas sur une ligne inatteignable (début ou arrivée)
					bison.x = bisons[c];
					bison.y = c;
					for (int id = 0; id < nb_chiens; id++) { //On regarde pour chaque chien :
						Dir = getDirId(indien[id], bison);   //La direction du chien vers le bison
						if (Dir != -1) {
							Dist = distance(indien[id], bison);  //Et l'ecart entre les deux
							if (Dist < moveMatrix[id][Dir]) { //Si la direction est valide (horizontale, verticale ou diagonale) et l'ecart plus petit que les précédents
								moveMatrix[id][Dir] = Dist; //Alors mise-à-jour !
							}
						}
					}
				}
			}


			// 3) On regarde pour finir les pions de l'indien entre eux, pour voir s'ils se bloquent parfois...
			// Pour optimiser un peu, on peut ne comparer les objets entre eux qu'une seule fois, en imbriquant les boucles de la façon suivante :
			int opDir; //Direction opposée à celle étudiée
			int x;
			int y;
			int xDest;
			int yDest;
			Position VecDir;

			for (int i = 0; i < nb_chiens; i++) { //Pour chaque chien i
				x = indien[i].x;
				y = indien[i].y;
				for (int j = i + 1; j < nb_chiens + 1; j++) { //On le compare à un nouveau chien/indien j > i
					Dir = getDirId(indien[i], indien[j]);
					if (Dir != -1) { //Si la direction est valide (horizontale, verticale ou diagonale)
						Dist  = distance(indien[i], indien[j]);
						opDir = getOppDirId(Dir);
						if (moveMatrix[j][opDir] > Dist) {
							moveMatrix[j][opDir] = Dist;
						}
						if (moveMatrix[i][Dir] > Dist) {
							moveMatrix[i][Dir] = Dist;
						}
					}
					//On s'assure ainsi que l'on a toujours une relation cohérente entre chaque paire de chien/indien
				}
				//Le chien i est connu, on ajoute ses déplacements selon les paramètres d'analyse
				if (indienFlags[i]){
					for (int dir=0; dir < 8; dir++){
						VecDir = getVecDir(dir);
						xDest = x;
						yDest = y;
						for (int k=0; k<moveMatrix[i][dir]; k++){
							xDest += VecDir.x;
							yDest += VecDir.y;
							possibleMoves.add(new BisonsGameMove(x, y, xDest, yDest));
						}
					}
				}
			}

			// 4) Pour finir, on s'occupe de l'indien, qui ne se deplace que d'une case :
			for (int dir=0; dir<8; dir++){
				VecDir = getVecDir(dir);
				x = indien[nb_chiens].x;
				y = indien[nb_chiens].y;
				if (moveMatrix[nb_chiens][dir] != 0){
					possibleMoves.add(new BisonsGameMove(x, y, x + VecDir.x, y + VecDir.y));
				}
			}
		}

		return possibleMoves;
	}

	/**
	 * Retourne le score dans l'état actuel de la partie
	 * (Indique si un joueur a gagné, perdu, ou si personne n'est pour l'instant vainqueur)
	 * ======================================================
	 * @return ArrayList (taille 2) : scores des deux joueurs
	 */
	@Override
	public ArrayList<Score<BisonsGameRole>> getScores() {
		ArrayList<Score<BisonsGameRole>> scores = new ArrayList<>(2);
		int nbBisons=0;
		for (int k=0; k< width; k++){
			if (bisons[k] == height-1){
				scores.add(0, new Score(BisonsGameRole.INDIEN, Score.Status.LOOSE, 0));
				scores.add(1, new Score(BISONS, Score.Status.WIN, 1));
				return scores;
			} else if (bisons[k] != -1) {
				nbBisons++;
			}
		}
		if (nbBisons == nbBisonsBloques()){
			scores.add(0, new Score(BisonsGameRole.INDIEN, Score.Status.WIN, 1));
			scores.add(1, new Score(BISONS, Score.Status.LOOSE, 0));
			return scores;
		}
		scores.add(0, new Score(BisonsGameRole.INDIEN, Score.Status.TIE, 0));
		scores.add(0, new Score(BISONS, Score.Status.TIE, 0));
		return scores;
	}

	/**
	 * Retourne le nombre de chiens de l'analyse
	 * ========================================================
	 * @return int, nombre de chiens analysés dans la recherche
	 */
	public int nbDogsActivated(){
		int cpt = 0;
		for (int i=0; i<nb_chiens; i++){
			if (indienFlags[i]) cpt++;
		}
		return cpt;
	}

	/**
	 * Verifie si un coup est valide et, le cas échéant, joue le coup en question.
	 * Si le coup est invalide, une erreur est levée.
	 * ===========================================================================
	 * @param move,      le deplacement souhaité
	 * @param BisonsGameRole, le role du joueur qui effectue le coup
	 */
	public BisonsGameBoard checkAndPlay(BisonsGameMove move, BisonsGameRole BisonsGameRole) throws Exception {

		if (!isValidMove(move, BisonsGameRole)) throw new Exception("The move is not valid in this context");
		return play(move, BisonsGameRole);

	}

	/**
	 * Joue le coup sans aucune verification préalable sur sa validité
	 * (Un bison éliminé pourrait être déplacé et remis en jeu, les coordonnées peuvent être mauvaises,
	 *  un pion peut passer par-dessus un autre, ils peuvent se superposer...)
	 * ===============================================================================================
	 * @param move, le deplacement souhaité
	 */
	@Override
	public BisonsGameBoard play(BisonsGameMove move, BisonsGameRole role) {
		BisonsGameBoard nextBoard = this.copy();
		Position origin = move.getOrigin();
		Position end = move.getDest();
		if (role==BisonsGameRole.INDIEN){
			if (origin.equal(indien[nb_chiens])){
				nextBoard.indien[nb_chiens].set(end.x, end.y);
				if(end.x == bisons[end.y]){
					nextBoard.bisons[end.y] = -1;
				}
			} else {
				for (int k=0; k<nb_chiens; k++) {
					if (origin.equal(indien[k])) {
						nextBoard.indien[k].set(end.x, end.y);
						break;
					}
				}
			}
		} else {
			nextBoard.bisons[origin.y]++;
		}
		return nextBoard;
	}

	// ---------------------  SETTERS ---------------------

	/**
	 * Desactive l'analyse d'un chien pour le retour des déplacements possibles
	 * ========================================================================
	 * @param idChien, idientifiant du chien désactivé
	 */
	public void desactivateFlag(int idChien){
		indienFlags[idChien] = false;
	}

	/**
	 * Réactive l'ensemble des chiens pour l'analyse des coups possibles
	 * =================================================================
	 */
	public void reactivateAllFlags(){
		Arrays.fill(indienFlags, true);
	}


	// ---------------------  PRIVATE METHODS ---------------------

	/**
	 * Retourne l'identifiant de la direction pour aller d'une Position fromMe à une Position toMe :
	 * -1 : Direction non croisée (ni diagonale, ni horizontale, ni verticale) ou Positions égales (fromME = toME).
	 *  0 : NORD
	 *  1 : SUD
	 *  2 : OUEST
	 *  3 : EST
	 *  4 : NORD OUEST
	 *  5 : SUD EST
	 *  6 : NORD EST
	 *  7 : SUD OUEST
	 *  ============================================================================================================
	 * @param fromMe
	 * @param toMe
	 * @return int, identifiant de la direction.
	 */
	private int getDirId(Position fromMe, Position toMe) {
		if (fromMe.x == toMe.x) {
			if (fromMe.y < toMe.y) {
				return 3; //At East
			} else if (fromMe.y != toMe.y){
				return 2; //At West
			}
		} else if (fromMe.x < toMe.x) {
			if (fromMe.y == toMe.y) {
				return 1; //At South
			} else if (abs(fromMe.x - toMe.x) == abs(fromMe.y - toMe.y)) {
				if (fromMe.y < toMe.y) {
					return 5; //At South East
				} else {
					return 7; //At South West
				}
			}
		} else {
			if (fromMe.y == toMe.y) {
				return 0; //At North
			} else if (abs(fromMe.x - toMe.x) == abs(fromMe.y - toMe.y)) {
				if (fromMe.y < toMe.y) {
					return 6; //At North East
				} else {
					return 4; //At North West
				}
			}
		}
		return -1; //Not valid direction
	}

	/**
	 * Pour retrouver l'identifiant de la direction opposée.
	 * =====================================================
	 * @param dirId, identifiant d'une direction (presupPosition : entre 0 et 7 compris)
	 * @return int, indetifiant de la direction opposée à dirId
	 */
	private int getOppDirId(int dirId) {
		if (dirId%2 == 0) {
			return dirId + 1;
		} else {
			return dirId - 1;
		}
	}

	/**
	 * Retourne le vecteur directionnel correspondant à l'identifiant
	 * =============================================================
	 * @param dirId, identifiant de la direction
	 * @return le vecteur directionnel de l'identifiant
	 */
	private Position getVecDir(int dirId) {
		switch (dirId) {
			case 0:
				return new Position(-1, 0); //NORD
			case 1:
				return new Position(1, 0);  //SUD
			case 2:
				return new Position(0, -1); //OUEST
			case 3:
				return new Position(0, 1);  //EST
			case 4:
				return new Position(-1, -1);//NORD OUEST
			case 5:
				return new Position(1, 1);  //SUD EST
			case 6:
				return new Position(-1, 1); //NORD EST
			case 7:
				return new Position(1, -1); //SUD OUEST
		}

		return null;
	}

}
