package nekogochan.stringart.usagelocal;

import nekogochan.stringart.binds.BindsAllFree;
import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.nail.NailImpl;
import nekogochan.stringart.point.RectPoint;

import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class BindsRect extends BindsAllFree {
  public BindsRect(int count, double width, double height, double radius) {
    super(generateNails(count, width, height, radius));
  }

  private static List<? extends Nail> generateNails(int count, double width, double height, double radius) {
    var _width = width - radius * 2 - 2;
    var _height = height - radius * 2 - 2;
    var offset = new RectPoint(radius + 1, radius + 1);

    var perimeter = (_width + _height) * 2.0;
    var wRatio = _width / perimeter;

    var countOnWidth = (int) (wRatio * count);
    var countOnHeight = count / 2 - countOnWidth;

    var dw = _width / (double) (countOnWidth + 1);
    var dh = _height / (double) (countOnHeight + 1);

    Supplier<DoubleStream> xAxis = () -> DoubleStream.iterate(dw, (x) -> x + dw).limit(countOnWidth);
    Supplier<DoubleStream> yAxis = () -> DoubleStream.iterate(dh, (y) -> y + dh).limit(countOnHeight);

    var left = side(yAxis, (y) -> nail(0.0, y, radius), offset);
    var top = side(xAxis, (x) -> nail(x, 0.0, radius), offset);
    var right = side(yAxis, (y) -> nail(_width, y, radius), offset);
    var bottom = side(xAxis, (x) -> nail(x, _height, radius), offset);

    return Stream.of(left, top, right, bottom)
                 .flatMap(s -> s)
                 .toList();
  }

  private static Stream<Nail> side(Supplier<DoubleStream> axis, DoubleFunction<Nail> generator, RectPoint offset) {
    return axis.get().mapToObj(generator).peek(n -> n.center().add(offset));
  }

  private static Nail nail(double x, double y, double radius) {
    return new NailImpl(new RectPoint(x, y), radius);
  }
}
