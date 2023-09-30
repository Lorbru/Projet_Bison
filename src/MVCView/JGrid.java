package MVCView;

import games.bisons.BisonsGameBoard;
import games.bisons.BisonsGameMove;

import javax.swing.*;
import java.awt.*;

public class JGrid extends JPanel {

    public Case[][] grid = new Case[BisonsGameBoard.DEFAULT_HEIGHT][BisonsGameBoard.DEFAULT_WIDTH];

    public JGrid(){

        setLayout(new GridLayout(BisonsGameBoard.DEFAULT_HEIGHT, BisonsGameBoard.DEFAULT_WIDTH, 2, 2));
        setPreferredSize(new Dimension(80*BisonsGameBoard.DEFAULT_WIDTH, 80*BisonsGameBoard.DEFAULT_HEIGHT));
        setBackground(Color.BLUE);
        build();
        draw();

    }

    public Case getCase(int x, int y){
        return grid[x][y];
    }

    public void play(BisonsGameMove bisonsGameMove){

        int ox = bisonsGameMove.getOrigin().x;
        int oy = bisonsGameMove.getOrigin().y;

        int dx = bisonsGameMove.getDest().x;
        int dy = bisonsGameMove.getDest().y;

        CaseType caseType = grid[ox][oy].getType();
        grid[ox][oy].setCase(CaseType.Vide);
        grid[dx][dy].setCase(caseType);

        for (int x = 0; x < BisonsGameBoard.DEFAULT_HEIGHT; x++){
            for (int y = 0; y < BisonsGameBoard.DEFAULT_WIDTH; y++){
                grid[x][y].setStatus(CaseStatus.NotSelected);
            }
        }

        repaint();
        revalidate();
    }

    public void build(){
        for (int x=0; x<BisonsGameBoard.DEFAULT_HEIGHT; x++){
            for (int y=0; y<BisonsGameBoard.DEFAULT_WIDTH; y++) {

                if (x == 0){
                    grid[x][y] = new Case(CaseType.Bison, x, y);
                }
                else if (x == BisonsGameBoard.DEFAULT_HEIGHT - 2 && (y == 3 || y == 4 || y == 6 || y == 7)){
                    grid[x][y] = new Case(CaseType.Chien, x, y);
                }
                else if (x == BisonsGameBoard.DEFAULT_HEIGHT - 2 && y == 5){
                    grid[x][y] = new Case(CaseType.Indien, x, y);
                }
                else  {
                    grid[x][y] = new Case(CaseType.Vide, x, y);
                }


                if(x == 0 || x == BisonsGameBoard.DEFAULT_HEIGHT-1) {
                    grid[x][y].setBackground(Color.GREEN.darker().darker());
                    grid[x][y].setColor(Color.GREEN.darker().darker());
                }
                else {
                    grid[x][y].setBackground(Color.GREEN.darker());
                    grid[x][y].setColor(Color.GREEN.darker());
                }
            }
        }
    }

    public void draw(){
        for (int x=0; x<BisonsGameBoard.DEFAULT_HEIGHT; x++){
            for (int y=0; y<BisonsGameBoard.DEFAULT_WIDTH; y++) {
                add(grid[x][y]);
            }
        }
    }


    public void select(int x, int y) {
        for (int xx=0; xx< BisonsGameBoard.DEFAULT_HEIGHT; xx++){
            for (int yy=0; yy < BisonsGameBoard.DEFAULT_WIDTH; yy++){

                if (xx == x && yy == y){
                    grid[xx][yy].setStatus(CaseStatus.Selected);
                }
                else {
                    grid[xx][yy].setStatus(CaseStatus.NotSelected);
                }

            }
        }
        repaint();
        revalidate();


    }

    public void show(int dx, int dy) {
        grid[dx][dy].setStatus(CaseStatus.Show);
        repaint();
        revalidate();
    }
}
