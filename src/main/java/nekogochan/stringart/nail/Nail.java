package nekogochan.stringart.nail;

import nekogochan.point.Pair;
import nekogochan.point.RectPoint;
import nekogochan.point.RectPointInt;

import java.util.stream.Stream;

import static java.lang.Math.PI;

public class Nail implements NailI {
  private final RectPoint center;
  private final double radius;

  public Nail(RectPoint center, double radius) {
    this.center = center;
    this.radius = radius;
  }

  public RectPoint center() {
    return center;
  }

  public double radius() {
    return radius;
  }

  public Pair lookTo(NailI nail, boolean fromLeft, boolean toLeft) {
    var fromC = this.center();
    var toC = nail.center();

    var dxy = toC.copy()
                 .subtract(fromC)
                 .toPolar()
                 .r(nail.radius())
                 .aAdd(PI / 2)
                 .toRect();

    return new Pair((fromLeft ? fromC.copy().subtract(dxy)
                              : fromC.copy().add(dxy)).toInt(),
                    (toLeft ? toC.copy().subtract(dxy)
                            : toC.copy().add(dxy)).toInt());
  }
}