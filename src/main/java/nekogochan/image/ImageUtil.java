package nekogochan.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageUtil {
  public static BufferedImage copy(BufferedImage source) {
    BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics g = copy.createGraphics();
    g.drawImage(source, 0, 0, null);
    return copy;
  }
}
