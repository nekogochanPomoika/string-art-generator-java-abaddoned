package nekogochan.image;

public interface GrayscaleConverter {
  double[][] toArray();
  RgbConverter toRgb();
  GrayscaleConverter inverse();
}
