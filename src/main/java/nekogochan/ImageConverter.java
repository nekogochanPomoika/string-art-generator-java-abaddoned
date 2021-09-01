package nekogochan;

import nekogochan.stringart.Fn;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import static java.lang.Math.min;

public class ImageConverter {

    private final int size;

    public ImageConverter(int size) {
        this.size = size;
    }

    public FieldData convert(String path) {
        try {
            var img = ImageIO.read(new File(path));
            var w = img.getWidth();
            var h = img.getHeight();

            var minSide = min(w, h);
            var scaleFactor = (double) size / (double) minSide;

            w *= scaleFactor;
            h *= scaleFactor;

            var wOffset = (size - w) / 2;
            var hOffset = (size - h) / 2;

            var scaledImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            var graphics = scaledImg.createGraphics();

            graphics.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), wOffset, hOffset, size, size, null);
            graphics.dispose();

            var result = new double[size][size];

            Fn.repeat(size, (x) -> {
                Fn.repeat(size, (y) -> {
                    var rgb = new RGB(scaledImg.getRGB(x, y));
                    result[x][y] = 1.0 - (double) (rgb.r + rgb.g + rgb.b) / 765.0;
                });
            });

            return new FieldData(result);
        } catch (Exception ignored) {
            throw new RuntimeException("ИДА НУ И ХУЙНЯ С ИЗОБРАЖЕНИЕМ: " + path);
        }
    }

    private static class RGB {
        public final int r;
        public final int g;
        public final int b;

        public RGB(int rgb) {
            r = rgb & R_MASK;
            g = (rgb & G_MASK) >> 8;
            b = (rgb & B_MASK) >> 16;
        }

        private static final int R_MASK = getMask(8);
        private static final int G_MASK = getMask(16) ^ getMask(8);
        private static final int B_MASK = getMask(24) ^ getMask(16);

        private static int getMask(int offset) {
            return (2 << offset - 1) - 1;
        }
    }
}
