package nekogochan.image;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public interface ImageConverter {

  ImageConverter resize(int width, int height);

  ImageConverter resizeCropping(int width, int height);

  ImageConverter resizeSaving(int width, int height);

  ImageConverter move(int dx1, int dy1, int dx2, int dy2,
                      int sx1, int sy1, int sx2, int sy2);

  BufferedImage get();

  ImageConverter set(BufferedImage image);

  RgbConverter toRgb();

  default BufferedImage getCopy() {
    return ImageUtil.copy(get());
  }

  default ImageConverter setCopy(BufferedImage image) {
    return set(ImageUtil.copy(image));
  }

  default ImageConverter proceed(Consumer<BufferedImage> consumer) {
    consumer.accept(get());
    return this;
  }
}
