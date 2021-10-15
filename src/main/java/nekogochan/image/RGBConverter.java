package nekogochan.image;

import nekogochan.fn.Fn;

import java.awt.image.BufferedImage;
import java.util.List;

public class RGBConverter {

    List<List<RGB>> data;

    public static RGBConverter fromImage(BufferedImage image) {
        var _this = new RGBConverter();
        _this.data = Fn.generateBIList(0, image.getWidth(),
                                       0, image.getHeight(),
                                       (x, y) -> RGB.ofPlain(image.getRGB(x, y)));
        return _this;
    }

    public interface MapOperator {
        RGB apply(RGB rgb, List<List<RGB>> data, int x, int y);
    }
    public RGBConverter map(MapOperator operator) {
        data = Fn.generateBIList(0, data.size(),
                                 0, data.get(0).size(),
                                 (x, y) -> operator.apply(data.get(x).get(y), data, x, y));
        return this;
    }

    public ImageConverter toImage() {
        var image = new BufferedImage(data.size(), data.get(0).size(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < data.size(); i++) {
            var row = data.get(i);
            for (int j = 0; j < row.size(); j++) {
                var cell = row.get(j);
                image.setRGB(i, j, cell.toPlain());
            }
        }
        return ImageConverter.of(image);
    }
}
