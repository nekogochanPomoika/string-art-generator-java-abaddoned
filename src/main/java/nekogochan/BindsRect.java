package nekogochan;

import nekogochan.point.Pair;
import nekogochan.point.RectPoint;
import nekogochan.stringart.binds.BindedNailI;
import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.nail.NailI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class BindsRect {

  private final List<BindedNailI>
    left = new ArrayList<>(),
    top = new ArrayList<>(),
    right = new ArrayList<>(),
    bottom = new ArrayList<>();

  public BindsRect(List<? extends NailI> left, List<? extends NailI> top, List<? extends NailI> right, List<? extends NailI> bottom) {
    left.stream().map($ -> new _Nail(Side.LEFT, $)).forEach(this.left::add);
    top.stream().map($ -> new _Nail(Side.TOP, $)).forEach(this.top::add);
    right.stream().map($ -> new _Nail(Side.RIGHT, $)).forEach(this.right::add);
    bottom.stream().map($ -> new _Nail(Side.BOTTOM, $)).forEach(this.bottom::add);
  }

  public List<BindedNailI> nails() {
    return Stream.of(left, top, right, bottom)
                 .flatMap(Collection::stream)
                 .toList();
  }

  public class _Nail implements BindedNailI {
    private final List<List<BindedNailI>> boundSides;
    private final NailI nail;

    public _Nail(Side side, NailI nail) {
      this.nail = nail;
      this.boundSides = (switch (side) {
        case LEFT -> Stream.of(top, right, bottom);
        case TOP -> Stream.of(left, right, bottom);
        case RIGHT -> Stream.of(left, top, bottom);
        case BOTTOM -> Stream.of(left, top, right);
      }).toList();
    }

    @Override
    public Stream<? extends NailI> getBinds() {
      return boundSides.stream().flatMap(Collection::stream);
    }

    @Override
    public RectPoint center() {
      return nail.center();
    }

    @Override
    public double radius() {
      return nail.radius();
    }

    @Override
    public Pair lookTo(NailI nail, boolean fromLeft, boolean toLeft) {
      return this.nail.lookTo(nail, fromLeft, toLeft);
    }
  }

  public enum Side {
    LEFT, TOP, RIGHT, BOTTOM
  }

  static BindsRect generate(int count, double width, double height, double radius) {
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

    return new BindsRect(
      yAxis.get().mapToObj((y) -> nail(0.0, y, radius)).peek(($) -> $.center().add(offset)).toList(),
      xAxis.get().mapToObj((x) -> nail(x, 0.0, radius)).peek(($) -> $.center().add(offset)).toList(),
      yAxis.get().mapToObj((y) -> nail(_width, y, radius)).peek(($) -> $.center().add(offset)).toList(),
      xAxis.get().mapToObj((x) -> nail(x, _height, radius)).peek(($) -> $.center().add(offset)).toList()
    );
  }

  private static NailI nail(double x, double y, double radius) {
    return (new Nail(new RectPoint(x, y), radius));
  }
}
