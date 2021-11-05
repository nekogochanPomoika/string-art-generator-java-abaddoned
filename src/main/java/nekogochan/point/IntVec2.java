package nekogochan.point;

public class IntVec2 {

  private int _x, _y;

  public IntVec2(int x, int y) {
    _x = x;
    _y = y;
  }

  public int x() {
    return _x;
  }

  public int y() {
    return _y;
  }

  public IntVec2 x(int x) {
    _x = x;
    return this;
  }

  public IntVec2 y(int y) {
    _y = y;
    return this;
  }
}
