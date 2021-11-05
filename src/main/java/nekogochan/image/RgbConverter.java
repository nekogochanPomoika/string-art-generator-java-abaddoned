package nekogochan.image;

import java.util.List;

public interface RgbConverter {

  interface MapOperator {
    RGB apply(RGB rgb, List<List<RGB>> data, int x, int y);
  }

  RgbConverter map(MapOperator operator);

  ImageConverter toImage();

  GrayscaleConverter toGrayscale();
}
