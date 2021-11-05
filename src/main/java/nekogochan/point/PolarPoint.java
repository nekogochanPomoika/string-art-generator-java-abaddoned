package nekogochan.point;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PolarPoint implements M_VecDouble<PolarPoint> {

  private double r;
  private double a;

  public PolarPoint(double r, double a) {
    this.r = r;
    this.a = a;
  }

  public PolarPoint r(double r) {
    this.r = r;
    return this;
  }

  public PolarPoint a(double a) {
    this.a = a;
    return this;
  }

  public PolarPoint aAdd(double a) {
    this.a += a;
    return this;
  }

  public double r() {
    return r;
  }

  public double a() {
    return a;
  }

  public RectPoint toRect() {
    return new RectPoint(
        cos(a()) * r(),
        sin(a()) * r()
    );
  }

  @Override
  public double _1() {
    return a();
  }

  @Override
  public double _2() {
    return r();
  }

  @Override
  public void _1(double _1) {
    a(_1);
  }

  @Override
  public void _2(double _2) {
    r(_2);
  }
}
