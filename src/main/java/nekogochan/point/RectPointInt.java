package nekogochan.point;

import nekogochan.fn.ref.DoubleRef;

import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class RectPointInt implements M_VecInt<RectPointInt> {

  private int x;
  private int y;

  public RectPointInt(int x, int y) {
    x(x);
    y(y);
  }

  public int x() {
    return x;
  }

  public RectPointInt x(int x) {
    this.x = x;
    return this;
  }

  public int y() {
    return y;
  }

  public RectPointInt y(int y) {
    this.y = y;
    return this;
  }

  public RectPoint toDouble() {
    return new RectPoint(x, y);
  }

  public Stream<RectPointInt> pathTo(RectPointInt target) {
    var dxy = target.copy()
                    .subtract(this)
                    .toDouble();

    DoubleRef secondaryCoord;
    double secondaryCoordStep;
    int mainCoordStep;
    BooleanSupplier predicate;
    UnaryOperator<RectPointInt> nextOperator;
    var point = new RectPointInt(x(), y());

    if (abs(dxy.y()) > abs(dxy.x())) {
      secondaryCoord = new DoubleRef(x());
      secondaryCoordStep = dxy.x() / abs(dxy.y());
      if (dxy.y() > 0) {
        predicate = () -> point.y <= target.y;
        mainCoordStep = 1;
      } else {
        predicate = () -> point.y >= target.y;
        mainCoordStep = -1;
      }
      nextOperator = (rp) -> {
        secondaryCoord.add(secondaryCoordStep);
        rp.x((int) secondaryCoord.get());
        rp.y(rp.y() + mainCoordStep);
        return rp;
      };
    } else {
      secondaryCoord = new DoubleRef(y());
      secondaryCoordStep = dxy.y() / abs(dxy.x());
      if (dxy.x() > 0) {
        predicate = () -> point.x <= target.x;
        mainCoordStep = 1;
      } else {
        predicate = () -> point.x >= target.x;
        mainCoordStep = -1;
      }
      nextOperator = (rp) -> {
        secondaryCoord.add(secondaryCoordStep);
        rp.x(rp.x() + mainCoordStep);
        rp.y((int) secondaryCoord.get());
        return rp;
      };
    }

    secondaryCoord.set(round(secondaryCoord.get()));
    return Stream.iterate(point, ($) -> predicate.getAsBoolean(), nextOperator);
  }

  public RectPointInt copy() {
    return new RectPointInt(x(), y());
  }

  @Override
  public int _1() {
    return x();
  }

  @Override
  public int _2() {
    return y();
  }

  @Override
  public void _1(int _1) {
    x(_1);
  }

  @Override
  public void _2(int _2) {
    y(_2);
  }
}
