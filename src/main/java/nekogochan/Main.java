package nekogochan;

import com.google.gson.Gson;
import lombok.val;
import nekogochan.ref.Ref;
import nekogochan.stringart.Nail;
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
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.min;
import static java.lang.String.format;

public class Main {
    public static void main(String[] args) throws IOException {

        val JSON = new Gson();
        val RESULT_SIZE = 1000;
        val BUFFER_WIDTH = 500;
        val BUFFER_HEIGHT = 500;
        val MULTIPLIER_X = RESULT_SIZE / BUFFER_HEIGHT;
        val MULTIPLIER_Y = RESULT_SIZE / BUFFER_WIDTH;
        val NAILS_COUNT = 350;
        val ITER_OFFSET_STRAIGHT = 70;
        val ITER_OFFSET_CROSS = 35;
        val NAILS_RADIUS = 0.7;
        val SUBTRACT_VALUE = 0.1;
        val STROKE_WIDTH = 0.2f;
        val IMAGE_PATH = "C:/Users/sgs08/Desktop/16295385317441.jpg";
        val SAVE_DIR = "C:/Users/sgs08/Desktop/result";

        val nails = Nails.circle(NAILS_COUNT, (BUFFER_HEIGHT / 2.0), NAILS_RADIUS)
            .collect(Collectors.toList());

//        val nails = Nails.rect(NAILS_COUNT, BUFFER_WIDTH, BUFFER_HEIGHT, NAILS_RADIUS)
//            .collect(Collectors.toList());

        val imageConverter = new ImageConverter(BUFFER_HEIGHT);
        val fieldData = imageConverter.convert(IMAGE_PATH);

        val image = new BufferedImage(RESULT_SIZE, RESULT_SIZE, BufferedImage.TYPE_INT_RGB);
        val graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                  RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(STROKE_WIDTH));

        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        graphics.setColor(Color.BLACK);

        nails.forEach((n) -> {
            n = n.floor().multiply(MULTIPLIER_X, MULTIPLIER_Y, min(MULTIPLIER_X, MULTIPLIER_Y));
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

        val pathHandler = new PathHandler(nails, ITER_OFFSET_STRAIGHT, ITER_OFFSET_CROSS);
        val stringPath = new ArrayList<Nail.Pair>();

        val iter = new Ref.Int(0);

        val dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd - hh-mm-ss")
            .withLocale(Locale.forLanguageTag("ru"))
            .withZone(ZoneId.systemDefault());

        val dirPath = Path.of(format("%s/%s", SAVE_DIR, dateFormat.format(Instant.now())));
        Files.createDirectory(dirPath);

        pathHandler.handle(fieldData, SUBTRACT_VALUE, (nir) -> {
            val path = nir.path;
            val a = path.from.multiply(MULTIPLIER_X, MULTIPLIER_Y);
            val b = path.to.multiply(MULTIPLIER_X, MULTIPLIER_Y);
            graphics.drawLine(
                (int) a.x,
                (int) a.y,
                (int) b.x,
                (int) b.y
            );
            stringPath.add(path);
            if (++iter.val % 500 == 0) {
                try {
                    val imgFile = Path.of(format("%s/%s.png", dirPath, iter.val));
                    ImageIO.write(image, "png", imgFile.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(228);
                }
            }
        });

        Files.writeString(Path.of(format("%s/data.json", dirPath)), JSON.toJson(Map.of(
            "stringPath", stringPath,
            "strokeWidth", STROKE_WIDTH,
            "bufferWidth", BUFFER_WIDTH,
            "bufferHeight", BUFFER_HEIGHT
        )));
    }
}
