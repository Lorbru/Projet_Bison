package MVCView;

import games.bisons.BisonsGameMove;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame implements IView {

    JBisonMenu bisonMenu;
    JBisonGame bisonGame;
    JBisonSettings bisonSettings;

    public View(){

        setSize(new Dimension(1200, 900));
        setPreferredSize(new Dimension(1200, 900));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 900));

        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.GREEN);
        setContentPane(content);

        bisonMenu = new JBisonMenu();
        bisonGame = new JBisonGame();
        bisonSettings = new JBisonSettings();

        setMenuInterface();

        setVisible(true);

    }

    @Override
    public boolean isShow(int x, int y) {
        return bisonGame.grid.getCase(x, y).getCaseStatus() == CaseStatus.Show;
    }

    @Override
    public boolean isNotSelected(int x, int y) {
        return bisonGame.grid.getCase(x, y).getCaseStatus() == CaseStatus.NotSelected;
    }

    @Override
    public boolean isSelected(int x, int y) {
        return bisonGame.grid.getCase(x, y).getCaseStatus() == CaseStatus.Selected;
    }

    @Override
    public String getBisonPlayerType() {
        return bisonSettings.getBisonType();
    }

    @Override
    public String getIndienPlayerType() {
        return bisonSettings.getIndienType();
    }

    @Override
    public String getFirstPlayer() {
        return bisonSettings.getFirstPlayer();
    }

    @Override
    public void setMenuInterface() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.removeAll();
        contentPane.add(bisonMenu);
        contentPane.revalidate();
        setContentPane(contentPane);
    }

    @Override
    public void buildMenuInterface() {
        bisonMenu.removeAll();
        bisonMenu = new JBisonMenu();
        bisonMenu.repaint();
        bisonMenu.revalidate();
    }

    @Override
    public void setSettingsInterface() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.removeAll();
        contentPane.add(bisonSettings);
        contentPane.revalidate();
        setContentPane(contentPane);
    }

    @Override
    public void buildSettingsInterface() {
        bisonSettings.removeAll();
        bisonSettings = new JBisonSettings();
        bisonSettings.repaint();
        bisonSettings.revalidate();
    }

    @Override
    public void setGameInterface() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.removeAll();
        contentPane.add(bisonGame);
        contentPane.revalidate();
        setContentPane(contentPane);
    }

    @Override
    public void buildGameInterface() {
        bisonGame.removeAll();
        bisonGame = new JBisonGame();
        bisonGame.repaint();
        bisonGame.revalidate();
    }

    @Override
    public void setPlayerTurnInfo(String info) {
        bisonGame.infoGame.setPlayerTurnInfo(info);
    }

    @Override
    public void setPlayerTypeInfo(String info) {
        bisonGame.infoGame.setPlayerTypeInfo(info);
    }

    @Override
    public void setTimeLeftInfo(String info) {
        bisonGame.infoGame.setTimeLeftInfo(info);
    }

    @Override
    public void setLastMoveInfo(String info) {
        bisonGame.infoGame.setLastMoveInfo(info);
    }

    @Override
    public void setWinner(String info) {
        bisonGame.infoGame.setWinner(info);
    }

    @Override
    public void show(int x, int y) {
        bisonGame.show(x, y);
    }

    @Override
    public void select(int x, int y){
        bisonGame.select(x, y);
    }

    @Override
    public void play(BisonsGameMove bgm) {
        bisonGame.play(bgm);
    }

}
