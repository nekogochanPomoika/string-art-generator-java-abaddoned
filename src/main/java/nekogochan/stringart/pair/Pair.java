package nekogochan.stringart.pair;

import nekogochan.fn.ref.DoubleRef;
import nekogochan.point.RectPointInt;

import java.util.function.BooleanSupplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.round;

public class Pair implements PairI {
  private final RectPointInt from;
  private final RectPointInt to;

  public Pair(RectPointInt from, RectPointInt to) {
    this.from = from;
    this.to = to;
  }

  public Stream<RectPointInt> path() {
    var dxy = to.copy()
                .subtract(from)
                .toDouble();

    DoubleRef secondaryCoord;
    double secondaryCoordStep;
    int mainCoordStep;
    BooleanSupplier predicate;
    UnaryOperator<RectPointInt> nextOperator;
    var point = new RectPointInt(from.x(), from.y());

    if (abs(dxy.y()) > abs(dxy.x())) {
      secondaryCoord = new DoubleRef(from.x());
      secondaryCoordStep = dxy.x() / abs(dxy.y());
      if (dxy.y() > 0) {
        predicate = () -> point.y() <= to.y();
        mainCoordStep = 1;
      } else {
        predicate = () -> point.y() >= to.y();
        mainCoordStep = -1;
      }
      nextOperator = (rp) -> {
        secondaryCoord.add(secondaryCoordStep);
        rp.x((int) secondaryCoord.get());
        rp.y(rp.y() + mainCoordStep);
        return rp;
      };
    } else {
      secondaryCoord = new DoubleRef(from.y());
      secondaryCoordStep = dxy.y() / abs(dxy.x());
      if (dxy.x() > 0) {
        predicate = () -> point.x() <= to.x();
        mainCoordStep = 1;
      } else {
        predicate = () -> point.x() >= to.x();
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

  public RectPointInt from() {
    return from;
  }

  public RectPointInt to() {
    return to;
  }
}
