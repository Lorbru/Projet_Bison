package games.bisons;

import iialib.games.model.IMove;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class BisonsGameMove implements IMove {

    // ---------------------  ATTRIBUTES ---------------------

    private Position origin; //Position d'origine
    private Position dest; //Destination

    // ---------------------  CONSTRUCTORS ---------------------

    /**
     * Construit le déplacement selon les consignes du sujet
     * ======================================================
     * @param move représentation en chaine du deplacement
     */
    public BisonsGameMove(String move){
        origin = new Position(-1, -1);
        dest = new Position(-1, -1);
        setString(move);
    }

    /**
     * Construit le déplacement selon les coordonnées de déplacement
     * ==============================================================
     * @param nx, coord x depart
     * @param ny, coord y depart
     * @param nxDest, coord x destination
     * @param nyDest, coord y destination
     */
    public BisonsGameMove(int nx, int ny, int nxDest, int nyDest){
        origin = new Position(nx, ny);
        dest = new Position(nxDest, nyDest);
    }

    // ---------------------  GETTERS ---------------------

    /**
     * Retourne l'origine du déplacement,
     * ==================================
     * @return l'origine du deplacement
     */
    public Position getOrigin(){return origin;}

    /**
     * Retourne la destination de deplacement
     * ======================================
     * @return la destination de deplacement
     */
    public Position getDest(){return dest;}

    /**
     * Retourne la représentation en chaine du deplacement
     * ===================================================
     * @return string representation
     */
    public String toString(){
        String str = "";
        str += (char)(origin.x+65); //Ligne de départ (lettre)
        str += origin.y;            //Colonne de départ (nombre)
        str += "-";           // -
        str += (char)(dest.x+65); //Ligne d'arrivée (lettre)
        str += dest.y;            //Colonne d'arrivée (nombre)
        return str;
    }

    /**
     * Retourne la norme du deplacement (en nombre de cases parcourues/norme infinie)
     * ===============================================================================
     * @return la norme du deplacement
     */
    public int norme(){return max(abs(origin.x - dest.x), abs(origin.y - dest.y));}

    // ---------------------  SETTERS ---------------------

    /**
     * Charge le mouvement selon à partir d'une chaine de caracteres
     * (selon les consignes du sujet)
     * ==============================================================
     * @param move, la chaine représentant le mouvement
     */
    public void setString(String move){
        int length = move.length();
        origin.x = move.charAt(0) - 65; //Recuperation ASCII du premier caractère (lettre de la ligne de depart) - 65
        int index = 1;
        origin.y = 0;
        while (move.charAt(index) != '-'){
            origin.y = 10*origin.y + (move.charAt(index) - 48); //Construction ASCII de l'indice de la colonne de depart (avant le caractère '-')
            index++;
        }
        dest.x = move.charAt(index + 1) - 65; //Recuperation ASCII de la deuxième lettre (lettre de la ligne d'arrivée) - 65
        dest.y = 0;
        for (int i = index+2; i<length; i++){
            dest.y = 10*dest.y + (move.charAt(i) - 48); //Construction ASCII de l'indice de la colonne d'arrivée (jusqu'à la fin)
        }
    }

}
