package MVCView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TexturePanel extends JPanel {
    private TexturePaint texture;

    public TexturePanel(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));
            this.texture = new TexturePaint(image, new Rectangle(0, 0, image.getWidth(), image.getHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(texture);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}