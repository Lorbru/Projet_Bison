package MVCView;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image image;

    public ImagePanel(String imagePath) {

        this.image = new ImageIcon(imagePath).getImage();
        this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
        this.setMinimumSize(new Dimension(image.getWidth(null), image.getHeight(null)));
        this.setMaximumSize(new Dimension(image.getWidth(null), image.getHeight(null)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}