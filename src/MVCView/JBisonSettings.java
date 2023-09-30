package MVCView;

import MVCController.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class JBisonSettings extends JPanel {


    ImagePanel center = new ImagePanel(ResourcesPath.getPath("water.jpg"));
    ImagePanel planche = new ImagePanel(ResourcesPath.getPath("planche.jpg"));

    BoutonFleche bisonType;
    BoutonFleche indienType;
    BoutonFleche firstPlayer;

    public JBisonSettings(){

        setLayout(new BorderLayout());

        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(Box.createRigidArea(new Dimension(0, 50)));
        planche.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));

        ArrayList<String> playerTypeChoices = new ArrayList<>();
        playerTypeChoices.add("AI");
        playerTypeChoices.add("Human");
        playerTypeChoices.add("Random");

        ArrayList<String> firstPlayerTypeChoices = new ArrayList<>();
        firstPlayerTypeChoices.add("Bisons");
        firstPlayerTypeChoices.add("Indien");
        firstPlayerTypeChoices.add("Random");

        if (bisonType == null){
            bisonType = new BoutonFleche( "Joueur bison ", playerTypeChoices, 0);
        } else {
            bisonType = new BoutonFleche("Joueur bison ", playerTypeChoices, bisonType.getChoice());
        }

        if (indienType == null){
            indienType = new BoutonFleche( "Joueur indien ", playerTypeChoices, 0);
        } else {
            indienType = new BoutonFleche("Joueur indien ", playerTypeChoices, indienType.getChoice());
        }

        if (firstPlayer == null){
            firstPlayer = new BoutonFleche( "Premier joueur ", firstPlayerTypeChoices, 0);
        } else {
            firstPlayer = new BoutonFleche("Premier joueur ", firstPlayerTypeChoices, firstPlayer.getChoice());
        }

        planche.setLayout(new BoxLayout(planche, BoxLayout.Y_AXIS));

        planche.add(Box.createRigidArea(new Dimension(0, 3)));
        planche.add(bisonType);
        planche.add(Box.createRigidArea(new Dimension(0, 3)));
        planche.add(indienType);
        planche.add(Box.createRigidArea(new Dimension(0, 3)));
        planche.add(firstPlayer);


        center.add(planche);
        Bouton goBack = new Bouton("Retour au menu"){
            @Override
            public void mouseClicked(MouseEvent e){
                int datas[] = {bisonType.getChoice(), indienType.getChoice(), firstPlayer.getChoice()};
                Controller.update(3, datas);
            }
        };
        goBack.setDefaultColor(Color.GREEN);
        center.add(goBack);

        add(center, BorderLayout.NORTH);

    }

    public String getIndienType() {
        return indienType.choices.get(indienType.idChoice);
    }

    public String getBisonType() {
        return bisonType.choices.get(bisonType.idChoice);
    }

    public String getFirstPlayer(){
        return firstPlayer.choices.get(firstPlayer.idChoice);
    }



}
