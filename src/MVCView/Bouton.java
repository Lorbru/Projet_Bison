package MVCView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class Bouton extends JPanel implements MouseListener {

    public Color DEFAULT_COLOR = Color.BLUE.darker();

    public Bouton(String label){
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);
        setPreferredSize(new Dimension(200, 50));
        setMinimumSize(new Dimension(100, 50));
        setMaximumSize(new Dimension(400, 50));
        setBorder(BorderFactory.createRaisedBevelBorder());
        setBackground(DEFAULT_COLOR);
        add(lbl);
        addMouseListener(this);
    }

    @Override
    public abstract void mouseClicked(MouseEvent e);

    @Override
    public void mousePressed(MouseEvent e) {
        setBorder(BorderFactory.createLoweredBevelBorder());
        setBackground(DEFAULT_COLOR.darker());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(DEFAULT_COLOR.darker());
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(DEFAULT_COLOR);
        setBorder(BorderFactory.createRaisedBevelBorder());
    }

    public void setDefaultColor(Color clr){
        this.DEFAULT_COLOR = clr;
        setBackground(clr);
    }
}
