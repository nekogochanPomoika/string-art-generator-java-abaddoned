package nekogochan.fn.ref;

public class Ref<T> {
  private T it;

  public T get() {
    return it;
  }

  public void set(T value) {
    it = value;
  }

  public boolean contains() {
    return it != null;
  }

  public boolean empty() {
    return it == null;
  }
}
