package nekogochan.stringart.fn.ref;

public class DoubleRef {
  private double it;

  public DoubleRef(double it) {
    this.it = it;
  }

  public double get() {
    return it;
  }

  public void set(double it) {
    this.it = it;
  }

  public void add(double v) {
    it += v;
  }
}
