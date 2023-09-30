package MVCView;

import MVCController.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class JInfoGame extends JPanel {

    private final static int TEXT_SIZE = 15;

    private Bouton nextTurn = new Bouton("Tour Suivant"){
        @Override
        public void mouseClicked(MouseEvent e) {
            Controller.update(15, null);
        }
    };

    private JPanel info;

    private JPanel playerTurn;
    private JLabel lblPlayerTurn;

    private JPanel playerType;
    private JLabel lblPlayerType;

    private JPanel timeLeft;
    private JLabel lblTimeLeft;

    private JPanel lastOpponentMove;
    private JLabel lblLastOpponentMove;


    public JInfoGame(){

        setMinimumSize(new Dimension(400, 800));
        setPreferredSize(new Dimension(400, 800));
        setBackground(Color.BLUE.darker().darker().darker());

        add(nextTurn);

        info = new JPanel();
        info.setSize(new Dimension(380, 650));
        info.setBackground(Color.BLACK);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS ));

        playerTurn = new JPanel();
        playerTurn.setPreferredSize(new Dimension(350, 100));
        playerTurn.setBackground(Color.BLACK);

        JLabel titlePlayerTurn = new JLabel();
        titlePlayerTurn.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE));
        titlePlayerTurn.setForeground(Color.WHITE);
        titlePlayerTurn.setText("Player turn : ");
        lblPlayerTurn = new JLabel();
        lblPlayerTurn.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        lblPlayerTurn.setForeground(Color.GREEN);
        lblPlayerTurn.setText("");
        playerTurn.add(titlePlayerTurn);
        playerTurn.add(lblPlayerTurn);

        JLabel titlePlayerType = new JLabel();
        titlePlayerType.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE));
        titlePlayerType.setForeground(Color.WHITE);
        titlePlayerType.setText("Player type : ");
        playerType = new JPanel();
        playerType.setPreferredSize(new Dimension(350, 100));
        playerType.setBackground(Color.BLACK);
        lblPlayerType = new JLabel();
        lblPlayerType.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        lblPlayerType.setForeground(Color.GREEN);
        lblPlayerType.setText("");
        playerType.add(titlePlayerType);
        playerType.add(lblPlayerType);

        JLabel titleTimeLeft = new JLabel();
        titleTimeLeft.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE));
        titleTimeLeft.setForeground(Color.WHITE);
        titleTimeLeft.setText("Time left : ");
        timeLeft = new JPanel();
        timeLeft.setPreferredSize(new Dimension(350, 100));
        timeLeft.setBackground(Color.BLACK);
        lblTimeLeft = new JLabel();
        lblTimeLeft.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        lblTimeLeft.setForeground(Color.GREEN);
        lblTimeLeft.setText("");
        timeLeft.add(titleTimeLeft);
        timeLeft.add(lblTimeLeft);

        JLabel titleMove = new JLabel();
        titleMove.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE));
        titleMove.setForeground(Color.WHITE);
        titleMove.setText("Last opponent move : ");
        lastOpponentMove = new JPanel();
        lastOpponentMove.setPreferredSize(new Dimension(350, 100));
        lastOpponentMove.setBackground(Color.BLACK);
        lblLastOpponentMove = new JLabel();
        lblLastOpponentMove.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        lblLastOpponentMove.setForeground(Color.GREEN);
        lblLastOpponentMove.setText("");
        lastOpponentMove.add(titleMove);
        lastOpponentMove.add(lblLastOpponentMove);

        info.add(playerTurn);
        info.add(playerType);
        info.add(timeLeft);
        info.add(lastOpponentMove);

        add(info);


    }



    public void setPlayerTurnInfo(String info) {
        lblPlayerTurn.setText(info);
    }

    public void setPlayerTypeInfo(String info) {
        lblPlayerType.setText(info);
    }

    public void setTimeLeftInfo(String info) {
        lblTimeLeft.setText(info);
    }

    public void setLastMoveInfo(String info) {
        lblLastOpponentMove.setText(info);
    }

    public void setWinner(String info) {

        removeAll();

        setBackground(Color.BLACK);
        setLayout(new FlowLayout());

        JPanel GameOver = new JPanel();
        GameOver.setBackground(Color.BLACK);
        JLabel lblGameOver = new JLabel();
        lblGameOver.setFont(new Font("Arial", Font.BOLD, 30));
        lblGameOver.setForeground(Color.RED);
        lblGameOver.setText("Game is over !");
        GameOver.add(lblGameOver);

        JLabel titleWinner = new JLabel();
        titleWinner.setFont(new Font("Arial", Font.BOLD, TEXT_SIZE));
        titleWinner.setForeground(Color.WHITE);
        titleWinner.setText("The winner is ");
        JPanel winner = new JPanel();
        winner.setPreferredSize(new Dimension(350, 100));
        winner.setBackground(Color.BLACK);
        JLabel lblwinner = new JLabel();
        lblwinner.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        lblwinner.setForeground(Color.GREEN);
        lblwinner.setText(info);
        winner.add(titleWinner);
        winner.add(lblwinner);

        add(GameOver);
        add(winner);

        repaint();
        revalidate();

    }
}
