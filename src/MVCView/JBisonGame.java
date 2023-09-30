package MVCView;

import games.bisons.BisonsGameMove;

import javax.swing.*;
import java.awt.*;

public class JBisonGame extends JPanel {

    public JGrid grid = new JGrid();
    public JToolGameBar bar = new JToolGameBar();
    public JInfoGame infoGame = new JInfoGame();

    public JBisonGame(){
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        add(bar, BorderLayout.NORTH);
        add(grid ,BorderLayout.CENTER);
        add(infoGame, BorderLayout.EAST);
    }

    public void select(int x, int y) {
        grid.select(x, y);
    }

    public void show(int dx, int dy) {
        grid.show(dx, dy);
    }

    public void play(BisonsGameMove move) {
        grid.play(move);
    }
}
