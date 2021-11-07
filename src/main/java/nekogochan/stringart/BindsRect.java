package nekogochan.stringart;

import nekogochan.stringart.point.RectPoint;
import nekogochan.stringart.binds.BindNailI;
import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.nail.NailI;
import nekogochan.stringart.pair.PairI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class BindsRect {

  private final List<BindNailI> left, top, right, bottom;

  public BindsRect(List<? extends NailI> left,
                   List<? extends NailI> top,
                   List<? extends NailI> right,
                   List<? extends NailI> bottom) {
    var _left = left.stream().map(_Nail::new).toList();
    var _top = top.stream().map(_Nail::new).toList();
    var _right = right.stream().map(_Nail::new).toList();
    var _bottom = bottom.stream().map(_Nail::new).toList();
    this.left = new ArrayList<>(_left);
    this.top = new ArrayList<>(_top);
    this.right = new ArrayList<>(_right);
    this.bottom = new ArrayList<>(_bottom);
    _left.forEach($ -> $.init(Side.LEFT));
    _top.forEach($ -> $.init(Side.TOP));
    _right.forEach($ -> $.init(Side.RIGHT));
    _bottom.forEach($ -> $.init(Side.BOTTOM));
  }

  public List<BindNailI> nails() {
    return Stream.of(left, top, right, bottom)
                 .flatMap(Collection::stream)
                 .toList();
  }

  public class _Nail implements BindNailI {
    private List<BindNailI> accessibleLeft;
    private List<BindNailI> accessibleRight;
    private List<BindNailI> accessibleBoth;

    private final NailI nail;

    public _Nail(NailI nail) {
      this.nail = nail;
    }

    private void init(Side side) {
      accessibleLeft = nailsBySide(side.prev());
      accessibleRight = nailsBySide(side.next());
      accessibleBoth = nailsBySide(side.opposite());
    }

    private List<BindNailI> nailsBySide(Side side) {
      return switch (side) {
        case RIGHT -> right;
        case TOP -> top;
        case LEFT -> left;
        case BOTTOM -> bottom;
      };
    }

    @Override
    public List<? extends NailI> accessibleLeft() {
      return accessibleLeft;
    }

    @Override
    public List<? extends NailI> accessibleRight() {
      return accessibleRight;
    }

    @Override
    public List<? extends NailI> accessibleBoth() {
      return accessibleBoth;
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
    public PairI lookTo(NailI nail, boolean fromLeft, boolean toLeft) {
      return this.nail.lookTo(nail, fromLeft, toLeft);
    }
  }

  public enum Side {
    LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);

    private final int idx;

    Side(int idx) {
      this.idx = idx;
    }

    static Side fromIdx(int idx) {
      return switch (idx) {
        case 0 -> LEFT;
        case 1 -> TOP;
        case 2 -> RIGHT;
        case 3 -> BOTTOM;
        default -> throw new IllegalStateException("Unexpected value: " + idx);
      };
    }

    Side next() {
      return fromIdx((idx + 1) % 4);
    }

    Side prev() {
      return fromIdx((idx + 3) % 4);
    }

    Side opposite() {
      return fromIdx((idx + 2) % 4);
    }
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
    return new Nail(new RectPoint(x, y), radius);
  }
}
