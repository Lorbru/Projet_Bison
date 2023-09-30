package MVCView;

import MVCController.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class JToolGameBar extends JPanel {

    private Bouton goBack = new Bouton("Quit"){
        @Override
        public void mouseClicked(MouseEvent e) {
            Controller.update(4, null);
        }
    };

    public JToolGameBar(){

        setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
        setPreferredSize(new Dimension(2000, 100));
        setBackground(Color.BLUE.darker().darker().darker());
        add(goBack);


    }


}
