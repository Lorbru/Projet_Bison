package MVCView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BoutonFleche extends JPanel {

    public Color DEFAULT_COLOR = Color.BLUE.darker();
    public ArrayList<String> choices;
    public int idChoice;

    Bouton goRight;
    Bouton goLeft;
    JLabel title = new JLabel();
    JLabel label = new JLabel();

    public BoutonFleche(String title, ArrayList<String> labels){

        choices = labels;
        setBackground(DEFAULT_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        setPreferredSize(new Dimension(600, 60));
        setMinimumSize(new Dimension(600, 60));
        setMaximumSize(new Dimension(600, 60));
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        goRight = new Bouton(">"){
            @Override
            public void mouseClicked(MouseEvent e){
                idChoice = (idChoice+1)%(choices.size());
                label.setText(choices.get(idChoice));
            }
        };

        goLeft = new Bouton("<"){
            @Override
            public void mouseClicked(MouseEvent e){
                if (idChoice == 0){
                    idChoice = choices.size()-1;
                }
                else {
                    idChoice = (idChoice-1);
                }
                label.setText(choices.get(idChoice));
            }
        };

        goRight.DEFAULT_COLOR = this.DEFAULT_COLOR;
        goRight.setPreferredSize(new Dimension(40, 40));
        goRight.setMinimumSize(new Dimension(40, 40));
        goRight.setMaximumSize(new Dimension(40, 40));
        goLeft.DEFAULT_COLOR = this.DEFAULT_COLOR;
        goLeft.setPreferredSize(new Dimension(40, 40));
        goLeft.setMinimumSize(new Dimension(40, 40));
        goLeft.setMaximumSize(new Dimension(40, 40));

        this.title.setText(title);
        this.title.setFont(new Font("Arial", Font.BOLD, 15));
        this.title.setForeground(Color.WHITE);

        idChoice = 0;
        label.setText(choices.get(idChoice));

        JPanel getTitle = new JPanel();
        JPanel getLabel = new JPanel();
        getTitle.setBackground(DEFAULT_COLOR);
        getLabel.setBackground(DEFAULT_COLOR.darker());
        getLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        getTitle.setPreferredSize(new Dimension(200, 40));
        getLabel.setPreferredSize(new Dimension(200, 40));
        getTitle.add(this.title);
        getLabel.add(label);
        add(getTitle);
        add(goLeft);
        add(getLabel);
        add(goRight);
    }

    public BoutonFleche(String title, ArrayList<String> labels, int choice) {
        choices = labels;
        setBackground(DEFAULT_COLOR);
        setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
        setPreferredSize(new Dimension(600, 60));
        setMinimumSize(new Dimension(600, 60));
        setMaximumSize(new Dimension(600, 60));
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        goRight = new Bouton(">"){
            @Override
            public void mouseClicked(MouseEvent e){
                idChoice = (idChoice+1)%(choices.size());
                label.setText(choices.get(idChoice));
            }
        };

        goLeft = new Bouton("<"){
            @Override
            public void mouseClicked(MouseEvent e){
                if (idChoice == 0){
                    idChoice = choices.size()-1;
                }
                else {
                    idChoice = (idChoice-1);
                }
                label.setText(choices.get(idChoice));
            }
        };

        goRight.DEFAULT_COLOR = this.DEFAULT_COLOR;
        goRight.setPreferredSize(new Dimension(40, 40));
        goRight.setMinimumSize(new Dimension(40, 40));
        goRight.setMaximumSize(new Dimension(40, 40));
        goLeft.DEFAULT_COLOR = this.DEFAULT_COLOR;
        goLeft.setPreferredSize(new Dimension(40, 40));
        goLeft.setMinimumSize(new Dimension(40, 40));
        goLeft.setMaximumSize(new Dimension(40, 40));

        this.title.setText(title);
        this.title.setFont(new Font("Arial", Font.BOLD, 15));
        this.title.setForeground(Color.WHITE);

        idChoice = choice;
        label.setText(choices.get(idChoice));

        JPanel getTitle = new JPanel();
        JPanel getLabel = new JPanel();
        getTitle.setBackground(DEFAULT_COLOR);
        getLabel.setBackground(DEFAULT_COLOR.darker());
        getLabel.setBorder(BorderFactory.createLoweredBevelBorder());
        getTitle.setPreferredSize(new Dimension(200, 40));
        getLabel.setPreferredSize(new Dimension(200, 40));
        getTitle.add(this.title);
        getLabel.add(label);
        add(getTitle);
        add(goLeft);
        add(getLabel);
        add(goRight);
    }

    public String getLabel(){
        return choices.get(idChoice);
    }

    public void setDefaultColor(Color clr) {
        DEFAULT_COLOR = clr;
    }

    public int getChoice() {
        return idChoice;
    }
}
