package nekogochan.stringart.fn.container;

public class IfElse<T> {
  private T _if;
  private T _else;

  public IfElse() {
  }

  public IfElse(T _if, T _else) {
    this._if = _if;
    this._else = _else;
  }

  public T get(boolean cond) {
    return cond ? _if
                : _else;
  }

  public void set(boolean cond, T value) {
    if (cond) {
      _if = value;
    } else {
      _else = value;
    }
  }
}
