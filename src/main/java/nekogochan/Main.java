package nekogochan;

import nekogochan.image.ImageConverter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Main {

    static String DESKTOP = "C:/Users/sgs08/Desktop";

    static Path SOURCE_PATH = Path.of(DESKTOP, "16295385317441.jpg");
    static Path SAVE_DIR = Path.of(DESKTOP, "result");

    static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd - HH-mm-ss")
        .withLocale(Locale.forLanguageTag("ru"))
        .withZone(ZoneId.systemDefault());

    public static void main(String[] args) throws IOException {

        ImageConverter.readFrom(SOURCE_PATH)
            .proceedImage(Main::showImage)
            .toRgb()
            .map((rgb, data, x, y) -> rgb.inverse())
            .toImage()
            .proceedImage(Main::showImage);
    }

    static void showImage(BufferedImage image) {
        var frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(), image.getHeight() + 44);
        var label = new JLabel();
        label.setIcon(new ImageIcon(image));
        frame.add(label);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
