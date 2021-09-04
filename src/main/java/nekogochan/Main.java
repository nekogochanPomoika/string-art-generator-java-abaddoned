package nekogochan;

import lombok.SneakyThrows;
import lombok.val;
import nekogochan.stringart.Nails;
import nekogochan.stringart.PathHandler;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static java.lang.String.format;

public class Main {

    static int BUFFER_SIZE = 360;
    static double RESULT_MULTIPLIER = 2.0;
    static int NAILS_COUNT = 300;
    static int ITER_OFFSET_STRAIGHT = 0;
    static int ITER_OFFSET_CROSS = 0;
    static double NAILS_RADIUS = 1.0;
    static Path DESKTOP_FOLDER = Path.of("C:/Users/sgs08/Desktop");
    static Path SAVE_DIR = Path.of(DESKTOP_FOLDER.toString(), "result");
    static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH-mm-ss")
        .withLocale(Locale.forLanguageTag("ru"))
        .withZone(ZoneId.systemDefault());

    static {
        try {
            Files.createDirectory(SAVE_DIR);
        } catch (IOException ignored) {}
    }

    @SneakyThrows
    public static void main(String[] args) {
        withTimeLog(() -> convert(DESKTOP_FOLDER + "/16295385317441.jpg")).run();
    }

    static Runnable withTimeLog(Runnable runnable) {
        return () -> {
            val time = System.currentTimeMillis();
            runnable.run();
            System.out.println(System.currentTimeMillis() - time);
        };
    }

    @SneakyThrows
    static void convert(String imagePath) {
        val sourceImage = ImageIO.read(Path.of(imagePath).toFile());

        val sourceImageMinSide = min(sourceImage.getHeight(), sourceImage.getWidth());

        val sourceImageResizeRatio = (double) BUFFER_SIZE /
                                     (double) sourceImageMinSide;

        val bufferWidth = (int) (sourceImage.getWidth() * sourceImageResizeRatio);
        val bufferHeight = (int) (sourceImage.getHeight() * sourceImageResizeRatio);

        val nails = Nails.rect(NAILS_COUNT, bufferWidth, bufferHeight, NAILS_RADIUS)
            .collect(Collectors.toList());

        val pathHandler = new PathHandler(nails, ITER_OFFSET_STRAIGHT, ITER_OFFSET_CROSS);

        val imageConverter = new ImageConverter(bufferWidth, bufferHeight);
        val fieldData = imageConverter.convert(sourceImage);

        val subtractValue = 0.1;
        val STROKE_WIDTH = 0.15f;
        val iterations = 20000;

        pathHandler.prepare(fieldData, subtractValue);

        val image = new BufferedImage((int) (bufferWidth * RESULT_MULTIPLIER),
                                      (int) (bufferHeight * RESULT_MULTIPLIER),
                                      BufferedImage.TYPE_INT_RGB);
        val graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(STROKE_WIDTH));

        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        graphics.setColor(Color.BLACK);

        nails.forEach((n) -> {
            n = n.floor().multiply(RESULT_MULTIPLIER);
            val c = n.center;
            val r = n.radius;
            val r2 = r * 2;
            graphics.drawArc(
                (int) (c.x - r),
                (int) (c.y - r),
                (int) r2,
                (int) r2,
                0,
                360);
        });

        nails.forEach(System.out::println);

        val dirPath = Path.of(format("%s/%s", SAVE_DIR, DATE_FORMAT.format(Instant.now())));
        Files.createDirectory(dirPath);

        pathHandler.iterate(iterations, (nir, i) -> {
            val path = nir.path;
            val a = path.from.multiply(RESULT_MULTIPLIER);
            val b = path.to.multiply(RESULT_MULTIPLIER);
            graphics.drawLine(
                (int) a.x,
                (int) a.y,
                (int) b.x,
                (int) b.y
            );
            if (i % 500 == 0) {
                try {
                    val imgFile = Path.of(format("%s/%s.png", dirPath, i));
                    ImageIO.write(image, "png", imgFile.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(228);
                }
            }
        });
    }
}
