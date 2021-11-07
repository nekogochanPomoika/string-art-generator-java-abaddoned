package nekogochan.stringart.point;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class VecInt<It extends VecInt<It>> {

  protected int _1;
  protected int _2;

  public It add(int _1, int _2) {
    this._1 += _1;
    this._2 += _2;
    return (It) this;
  }

  public It add(VecInt vec) {
    return add(vec._1, vec._2);
  }

  public It subtract(int _1, int _2) {
    this._1 -= _1;
    this._2 -= _2;
    return (It) this;
  }

  public It subtract(VecInt vec) {
    return subtract(vec._1, vec._2);
  }
}
