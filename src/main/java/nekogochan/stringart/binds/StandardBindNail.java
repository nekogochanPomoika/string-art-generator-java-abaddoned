package nekogochan.stringart.binds;

import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.point.RectPoint;

import java.util.List;
import java.util.Objects;

public class StandardBindNail implements BindNail {
  final Nail it;
  List<? extends BindNail>
    leftToLeft,
    leftToRight,
    rightToLeft,
    rightToRight;

  StandardBindNail(Nail it) {
    this.it = it;
  }

  void bind(List<? extends BindNail> leftToLeft,
            List<? extends BindNail> leftToRight,
            List<? extends BindNail> rightToLeft,
            List<? extends BindNail> rightToRight) {
    this.leftToLeft = leftToLeft;
    this.leftToRight = leftToRight;
    this.rightToLeft = rightToLeft;
    this.rightToRight = rightToRight;
  }

  @Override
  public RectPoint center() {
    return it.center();
  }

  @Override
  public double radius() {
    return it.radius();
  }

  @Override
  public Pair lookTo(Nail nail, boolean fromLeft, boolean toLeft) {
    return it.lookTo(nail, fromLeft, toLeft);
  }

  @Override
  public List<? extends Nail> leftToLeft() {
    return leftToLeft;
  }

  @Override
  public List<? extends Nail> leftToRight() {
    return leftToRight;
  }

  @Override
  public List<? extends Nail> rightToLeft() {
    return rightToLeft;
  }

  @Override
  public List<? extends Nail> rightToRight() {
    return rightToRight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StandardBindNail that = (StandardBindNail) o;
    return Objects.equals(it, that.it);
  }

  @Override
  public int hashCode() {
    return Objects.hash(it);
  }
}