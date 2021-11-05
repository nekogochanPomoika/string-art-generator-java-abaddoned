package nekogochan;

import nekogochan.fn.Unchecked;
import nekogochan.fn.ref.Ref;
import nekogochan.image.impl.ImageConverterImpl;
import nekogochan.stringart.StringArt;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.function.Supplier;

public class Main {

  static String SOURCE = "волчары.jpg";

  static Path DESKTOP = Path.of("C:/Users/sgs08/Desktop");
  static Path SOURCE_PATH = DESKTOP.resolve(SOURCE);
  static Path SAVE_DIR = DESKTOP.resolve("result");

  static int WIDTH = 1000;
  static int HEIGHT = 1000;
  static double RADIUS = 10.0;
  static double REMOVE_VALUE = 0.3;
  static double DRAW_MP = 3.0;
  static int ITERATIONS = 10000;
  static int SHOW_PERIOD = 250;
  static int NAILS_COUNT = 400;

  static int DRAW_WIDTH = (int) (WIDTH * DRAW_MP);
  static int DRAW_HEIGHT = (int) (HEIGHT * DRAW_MP);

  public static void main(String[] args) throws Exception {
    var img = ImageIO.read(SOURCE_PATH.toFile());
    var data = new ImageConverterImpl(img)
      .resizeCropping(WIDTH, HEIGHT)
      .proceed(Main::showImage)
      .toRgb()
      .toGrayscale()
      .inverse()
      .toArray();

    var nails = BindsRect.generate(NAILS_COUNT, WIDTH, HEIGHT, RADIUS).nails();
    var stringArt = new StringArt(data, nails, REMOVE_VALUE);

    var image = new BufferedImage(DRAW_WIDTH, DRAW_HEIGHT, BufferedImage.TYPE_INT_RGB);
    var gx = image.createGraphics();
    gx.fillRect(0, 0, image.getWidth(), image.getHeight());
    gx.setColor(Color.BLACK);
    gx.setStroke(new BasicStroke(0.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

    Supplier<BufferedImage> imageForShow = () -> new ImageConverterImpl(image).resize(WIDTH, HEIGHT).get();
    var prev = new Ref<Frame>();
    Runnable showResult = () -> {
      var next = showImage(imageForShow.get());
      if (prev.contains()) prev.get().dispose();
      prev.set(next);
    };
    Runnable saveResult = () -> saveImage(image, SAVE_DIR.resolve(SOURCE));

    var time = System.currentTimeMillis();
    for (int i = 1; i <= ITERATIONS; i++) {
      var path = stringArt.next().pair();
      gx.drawLine(
        (int) DRAW_MP * path.from().x(),
        (int) DRAW_MP * path.from().y(),
        (int) DRAW_MP * path.to().x(),
        (int) DRAW_MP * path.to().y()
      );
      if (i % SHOW_PERIOD == 0) {
        showResult.run();
        System.out.println("i = " + i);
      }
    }
    System.out.println("time: " + (System.currentTimeMillis() - time));

    showResult.run();
    saveResult.run();
  }

  static Frame showImage(BufferedImage image) {
    var frame = new JFrame();
    frame.setLayout(new FlowLayout());
    frame.setSize(image.getWidth(), image.getHeight() + 44);
    var label = new JLabel();
    label.setIcon(new ImageIcon(image));
    frame.add(label);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    return frame;
  }

  static void saveImage(BufferedImage image, Path path) {
    System.out.println(path);
    Unchecked.call(() -> ImageIO.write(image, "png", path.toFile()));
  }
}
