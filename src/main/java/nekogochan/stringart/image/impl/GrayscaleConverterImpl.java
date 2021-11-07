package nekogochan.stringart.image.impl;

import nekogochan.stringart.image.GrayscaleConverter;
import nekogochan.stringart.image.RGB;
import nekogochan.stringart.image.RgbConverter;

import java.util.Arrays;
import java.util.List;

public class GrayscaleConverterImpl implements GrayscaleConverter {

  private double[][] data;

  public GrayscaleConverterImpl(double[][] data) {
    this.data = data;
  }

  public static GrayscaleConverterImpl fromRgb(List<List<RGB>> data) {
    var _data = new double[data.size()][data.get(0).size()];
    for (int i = 0; i < data.size(); i++) {
      for (int j = 0; j < data.get(0).size(); j++) {
        _data[i][j] = data.get(i).get(j).grayscale();
      }
    }
    return new GrayscaleConverterImpl(_data);
  }

  @Override
  public double[][] toArray() {
    return data;
  }

  @Override
  public RgbConverter toRgb() {
    return new RgbConverterImpl(Arrays.stream(data)
                                      .map($ -> Arrays.stream($)
                                                      .mapToObj(RGB::ofGrayscale)
                                                      .toList())
                                      .toList());
  }

  @Override
  public GrayscaleConverter inverse() {
    for (int i = 0; i < data.length; i++) {
      for (int j = 0; j < data[0].length; j++) {
        data[i][j] = 1.0 - data[i][j];
      }
    }
    return this;
  }
}
