package nekogochan.image;

import nekogochan.fn.DoubleBiFunction;
import nekogochan.fn.DoubleBiPredicate;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class ImageConverter {

    private BufferedImage image;

    private ImageConverter(BufferedImage image) {
        this.image = image;
    }

    public static ImageConverter readFrom(Path path) throws IOException {
        return ImageConverter.of(
            ImageIO.read(path.toFile()));
    }

    public static ImageConverter of(BufferedImage image) {
        return new ImageConverter(image);
    }

    public static ImageConverter withCopyOf(BufferedImage image) {
        return ImageConverter.of(copyImage(image));
    }

    public ImageConverter resize(int width, int height) {
        var scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        var scaledB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        scaledB.getGraphics().drawImage(scaled, 0, 0, null);
        image = scaledB;
        return this;
    }

    public ImageConverter resizeCropping(int width, int height) {
        return __resize(width, height, Math::max, (a, b) -> a < b);
    }

    public ImageConverter resizeSaving(int width, int height) {
        return __resize(width, height, Math::min, (a, b) -> a > b);
    }

    private ImageConverter __resize(int width, int height, DoubleBiFunction mainRatioFunction, DoubleBiPredicate conditionPredicate) {
        var ws = image.getWidth();
        var hs = image.getHeight();

        int x1, x2, y1, y2;

        var wRatio = (double) width / (double) ws;
        var hRatio = (double) height / (double) hs;

        var mainRatio = mainRatioFunction.apply(wRatio, hRatio);

        if (conditionPredicate.test(wRatio, hRatio)) {
            var wp = width / mainRatio;
            y1 = 0;
            y2 = hs;
            x1 = (int) (width - wp) / 2;
            x2 = ws - x1;
        } else {
            var hp = height / mainRatio;
            x1 = 0;
            x2 = ws;
            y1 = (int) (hs - hp) / 2;
            y2 = hs - y1;
        }

        return move(
            0, 0, width, height,
            x1, y1, x2, y2
        );
    }

    public ImageConverter move(
        int dx1, int dy1, int dx2, int dy2,
        int sx1, int sy1, int sx2, int sy2
    ) {
        var scaledImg = new BufferedImage(dx2 - dx1, dy2 - dy1, BufferedImage.TYPE_INT_RGB);
        scaledImg.getGraphics().drawImage(image,
                                          dx1, dy1, dx2, dy2,
                                          sx1, sy1, sx2, sy2,
                                          null);
        image = scaledImg;
        return this;
    }

    public ImageConverter saveTo(Path dir, String fileName, String fileType) throws IOException {
        Files.createDirectory(dir);
        ImageIO.write(image, fileType, dir.resolve("%s.%s".formatted(fileName, fileType)).toFile());
        return this;
    }

    public ImageConverter proceedImage(Consumer<BufferedImage> consumer) {
        consumer.accept(image);
        return this;
    }

    public BufferedImage get() {
        return image;
    }

    public BufferedImage getCopy() {
        return copyImage(image);
    }

    public ImageConverter set(BufferedImage image) {
        this.image = image;
        return this;
    }

    public ImageConverter setCopy(BufferedImage image) {
        this.image = copyImage(image);
        return this;
    }

    public RGBConverter toRgb() {
        return RGBConverter.fromImage(image);
    }

    private static BufferedImage copyImage(BufferedImage source) {
        BufferedImage copy = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copy.createGraphics();
        g.drawImage(source, 0, 0, null);
        return copy;
    }
}
