package nekogochan.stringart.point;

import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class RectPoint extends VecDouble<RectPoint> {

  public double x() {
    return _1;
  }

  public RectPoint x(double x) {
    this._1 = x;
    return this;
  }

  public double y() {
    return _2;
  }

  public RectPoint y(double y) {
    this._2 = y;
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
    return new RectPointInt((int) x(),
                            (int) y());
  }
  public RectPoint copy() {
    return new RectPoint(x(), y());
  }
}
