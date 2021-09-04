package nekogochan;

import lombok.SneakyThrows;
import nekogochan.stringart.Fn;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class ImageConverter {

    private final int width;
    private final int height;

    public ImageConverter(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @SneakyThrows
    public FieldData convert(BufferedImage sourceImg) {
        var scaledImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        var sw = sourceImg.getWidth();
        var sh = sourceImg.getHeight();

        var wRatio = (double) width / (double) sw;
        var hRatio = (double) height / (double) sh;

        var mainRatio = min(wRatio, hRatio);

        int sx1, sy1, sx2, sy2;

        if (wRatio > hRatio) {
            sy1 = 0;
            sy2 = sh;
            var swProjection = width / mainRatio;
            var sProjectionDw = (sw - swProjection) / 2;
            sx1 = (int) (sProjectionDw);
            sx2 = (int) (sw - sProjectionDw);
        } else {
            sx1 = 0;
            sx2 = sw;
            var shProjection = height / mainRatio;
            var sProjectionDh = (sh - shProjection) / 2;
            sy1 = (int) (sProjectionDh);
            sy2 = (int) (sh - sProjectionDh);
        }

        var graphics = scaledImg.getGraphics();
        graphics.drawImage(sourceImg, 0, 0, width, height,
                           sx1, sy1, sx2, sy2, null);

        var result = new double[width][height];

        Fn.repeat(width, (x) -> {
            Fn.repeat(height, (y) -> {
                var rgb = new RGB(scaledImg.getRGB(x, y));
                result[x][y] = 1.0 - (double) (rgb.r + rgb.g + rgb.b) / 765.0;
            });
        });

        return new FieldData(result);
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
