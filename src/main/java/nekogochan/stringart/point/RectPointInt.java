package nekogochan.stringart.point;

public class RectPointInt extends VecInt<RectPointInt> {

  public RectPointInt(int x, int y) {
    x(x);
    y(y);
  }

  public int x() {
    return _1;
  }

  public RectPointInt x(int x) {
    this._1 = x;
    return this;
  }

  public int y() {
    return _2;
  }

  public RectPointInt y(int y) {
    this._2 = y;
    return this;
  }

  public RectPoint toDouble() {
    return new RectPoint(x(), y());
  }

  public RectPointInt copy() {
    return new RectPointInt(x(), y());
  }
}
