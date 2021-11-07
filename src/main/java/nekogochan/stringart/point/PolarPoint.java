package nekogochan.stringart.point;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PolarPoint extends VecDouble<PolarPoint> {

  public PolarPoint(double r, double a) {
    r(r);
    a(a);
  }

  public PolarPoint r(double r) {
    this._1 = r;
    return this;
  }

  public PolarPoint a(double a) {
    this._2 = a;
    return this;
  }

  public double r() {
    return _1;
  }

  public double a() {
    return _2;
  }

  public PolarPoint aAdd(double a) {
    a(a() + a);
    return this;
  }

  public PolarPoint copy() {
    return new PolarPoint(r(), a());
  }

  public RectPoint toRect() {
    return new RectPoint(
        cos(a()) * r(),
        sin(a()) * r()
    );
  }
}
