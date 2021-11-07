package nekogochan.stringart.image.impl;

import nekogochan.stringart.fn.lambda.DoubleBiFunction;
import nekogochan.stringart.fn.lambda.DoubleBiPredicate;
import nekogochan.stringart.image.ImageConverter;
import nekogochan.stringart.image.RgbConverter;

import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageConverterImpl implements ImageConverter {

  private BufferedImage image;

  public ImageConverterImpl(BufferedImage image) {
    this.image = image;
  }

  @Override
  public ImageConverterImpl resize(int width, int height) {
    var scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    var scaledB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    scaledB.getGraphics().drawImage(scaled, 0, 0, null);
    image = scaledB;
    return this;
  }

  @Override
  public ImageConverterImpl resizeCropping(int width, int height) {
    return __resize(width, height, Math::max, (a, b) -> a < b);
  }

  @Override
  public ImageConverterImpl resizeSaving(int width, int height) {
    return __resize(width, height, Math::min, (a, b) -> a > b);
  }

  private ImageConverterImpl __resize(int width, int height, DoubleBiFunction mainRatioFunction, DoubleBiPredicate conditionPredicate) {
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
      x1 = (int) (ws - wp) / 2;
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

  @Override
  public ImageConverterImpl move(
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

  @Override
  public BufferedImage get() {
    return image;
  }

  @Override
  public ImageConverter set(BufferedImage image) {
    this.image = image;
    return this;
  }

  @Override
  public RgbConverter toRgb() {
    return RgbConverterImpl.fromImage(get());
  }
}
