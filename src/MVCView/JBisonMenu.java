package MVCView;

import MVCController.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class JBisonMenu extends JPanel {


    ImagePanel bisonMenuImage = new ImagePanel(ResourcesPath.getPath("bisons.jpg"));
    ImagePanel center = new ImagePanel(ResourcesPath.getPath("water.jpg"));
    ImagePanel planche = new ImagePanel(ResourcesPath.getPath("planche.jpg"));

    Bouton NewGame = new Bouton("Nouvelle Partie"){
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] datas = {};
            Controller.update(1, datas);
        }
    };
    Bouton Settings = new Bouton("Parametres"){
        @Override
        public void mouseClicked(MouseEvent e) {
            int[] datas = {};
            Controller.update(2, datas);
        }
    };

    public JBisonMenu(){

        setLayout(new BorderLayout());

        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createRigidArea(new Dimension(0, 50)));
        bisonMenuImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        planche.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        planche.add(NewGame);
        planche.add(Settings);
        center.add(bisonMenuImage);
        center.add(planche);

        add(center, BorderLayout.NORTH);
    }


}
