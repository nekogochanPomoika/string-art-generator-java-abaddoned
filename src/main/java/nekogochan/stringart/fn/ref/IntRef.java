package nekogochan.stringart.fn.ref;

public class IntRef {
  private int it;

  public IntRef(int it) {
    this.it = it;
  }

  public int get() {
    return it;
  }

  public void set(int value) {
    it = value;
  }

  public void add(int value) {
    it += value;
  }

  public int getAndIncrement() {
    return it++;
  }

  public void increment() {
    it++;
  }
}
