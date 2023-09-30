package MVCView;

import MVCController.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class Case extends JPanel implements MouseListener {


    public void setStatus(CaseStatus newCaseStatus) {

        caseStatus = newCaseStatus;
        switch (caseStatus){
            case NotSelected -> {
                setBackground(DefaultColor);
            }
            case Selected -> {
                setBackground(Color.MAGENTA);
            }
            case Show -> {
                setBackground(Color.CYAN);
            }
        }


    }

    public CaseStatus getCaseStatus(){
        return caseStatus;
    }


    private CaseStatus caseStatus;
    private CaseType caseType;
    private Color DefaultColor;
    private int x;
    private int y;

    public CaseType getType() {
        return caseType;
    }

    public Case(CaseType newCaseType, int nx, int ny){

        super();
        x = nx;
        y = ny;
        caseStatus = CaseStatus.NotSelected;
        caseType = newCaseType;

        switch (caseType){
            case Bison -> {
                add(new ImagePanel(ResourcesPath.getPath("bison.jpg")));
            }
            case Chien -> {
                add(new ImagePanel(ResourcesPath.getPath("Dog.jpg")));
            }
            case Indien -> {
                add(new ImagePanel(ResourcesPath.getPath("indien.jpg")));
            }

        }

        addMouseListener(this);
    }

    public void setColor(Color c){
        DefaultColor = c;
    }

    public void setCase(CaseType newCaseType) {
        removeAll();
        caseType = newCaseType;
        switch (caseType){
            case Bison -> {
                add(new ImagePanel(ResourcesPath.getPath("bison.jpg")));
            }
            case Chien -> {
                add(new ImagePanel(ResourcesPath.getPath("Dog.jpg")));
            }
            case Indien -> {
                add(new ImagePanel(ResourcesPath.getPath("indien.jpg")));
            }

        }
        repaint();
        revalidate();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int[] datas = {x, y};
        Controller.update(10, datas);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBorder(BorderFactory.createEmptyBorder());
    }
}
