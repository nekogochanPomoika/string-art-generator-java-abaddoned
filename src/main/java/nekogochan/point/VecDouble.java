package nekogochan.point;

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class VecDouble<It extends VecDouble<It>> {
  protected double _1;
  protected double _2;

  public It subtract(double _1, double _2) {
    this._1 -= _1;
    this._2 -= _2;
    return (It) this;
  }

  public It subtract(VecDouble vec) {
    return subtract(vec._1, vec._2);
  }

  public It add(double _1, double _2) {
    this._1 += _1;
    this._2 += _2;
    return (It) this;
  }

  public It add(VecDouble vec) {
    return add(vec._1, vec._2);
  }
}
