package nekogochan.point;

import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class RectPoint implements M_VecDouble<RectPoint> {

  private double x;
  private double y;

  public double x() {
    return x;
  }

  public RectPoint x(double x) {
    this.x = x;
    return this;
  }

  public double y() {
    return y;
  }

  public RectPoint y(double y) {
    this.y = y;
    return this;
  }

  public RectPoint(double x, double y) {
    x(x);
    y(y);
  }

  public PolarPoint toPolar() {
    return new PolarPoint(
        sqrt(pow(x(), 2) + pow(y(), 2)),
        atan2(y(), x())
    );
  }

  public RectPointInt toInt() {
    return new RectPointInt((int) x,
                            (int) y);
  }

  @Override
  public double _1() {
    return x();
  }

  @Override
  public double _2() {
    return y();
  }

  @Override
  public void _1(double _1) {
    x(_1);
  }

  @Override
  public void _2(double _2) {
    y(_2);
  }

  public RectPoint copy() {
    return new RectPoint(x(), y());
  }
}
