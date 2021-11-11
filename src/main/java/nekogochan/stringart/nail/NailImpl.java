package nekogochan.stringart.nail;

import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.pair.PairImpl;
import nekogochan.stringart.point.RectPoint;

import java.util.Objects;

import static java.lang.Math.PI;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.hypot;

@SuppressWarnings("DuplicatedCode")
public class NailImpl implements Nail {
  private final RectPoint center;
  private final double radius;

  public NailImpl(RectPoint center, double radius) {
    this.center = center;
    this.radius = radius;
  }

  public RectPoint center() {
    return center;
  }

  public double radius() {
    return radius;
  }

  public Pair lookTo(Nail nail, boolean fromLeft, boolean toLeft) {
    return fromLeft == toLeft ? lookOuter(nail, fromLeft)
                              : lookInner(nail, fromLeft);
  }

  private Pair lookInner(Nail nail, boolean fromLeft) {
    var _1 = (Nail) this;
    var _2 = nail;

    if (_2.radius() > _2.radius()) {
      var tmp = _2;
      _2 = _1;
      _1 = tmp;
    }

    var c1 = _1.center();
    var c2 = _2.center();
    var r1 = _1.radius();
    var r2 = _2.radius();

    var H = hypot(c1.x() - c2.x(),
                  c1.y() - c2.y());

    var O = r1 + r2;

    var phi = (PI / 2) - asin(O / H);

    if (fromLeft) {
      phi = -phi;
    }

    var dra = c1.copy()
                .subtract(c2)
                .toPolar()
                .aAdd(phi);

    var dxy1 = dra.r(r1)
                  .toRect();

    var dxy2 = dra.r(r2)
                  .toRect();

    var p1 = c1.copy().subtract(dxy1);
    var p2 = c2.copy().add(dxy2);

    return new PairImpl(p1.toInt(), p2.toInt());
  }

  private Pair lookOuter(Nail nail, boolean fromLeft) {
    var _1 = (Nail) this;
    var _2 = nail;

    if (_2.radius() > _1.radius()) {
      var tmp = _2;
      _2 = _1;
      _1 = tmp;
    }

    var c1 = _1.center();
    var c2 = _2.center();
    var r1 = _1.radius();
    var r2 = _2.radius();

    var H = hypot(c1.x() - c2.x(),
                  c1.y() - c2.y());

    var A = r1 - r2;

    var phi = acos(A / H);

    if (fromLeft) {
      phi = -phi;
    }

    var dra = c1.copy()
                .subtract(c2)
                .toPolar()
                .aAdd(phi);

    var dxy1 = dra.r(r1)
                  .toRect();

    var dxy2 = dra.r(r2)
                  .toRect();

    var p1 = c1.copy().subtract(dxy1);
    var p2 = c2.copy().subtract(dxy2);

    return new PairImpl(p1.toInt(), p2.toInt());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NailImpl nail = (NailImpl) o;
    return Double.compare(nail.radius, radius) == 0 && Objects.equals(center, nail.center);
  }

  @Override
  public int hashCode() {
    return Objects.hash(center, radius);
  }
}
