package nekogochan.point;

@SuppressWarnings({"unchecked", "rawtypes"})
public interface M_VecInt<It extends M_VecInt<It>> {
  int _1();
  int _2();

  void _1(int _1);
  void _2(int _2);

  default It add(int _1, int _2) {
    _1(_1 + _1());
    _2(_2 + _2());
    return (It) this;
  }

  default It add(M_VecInt vec) {
    return add(vec._1(), vec._2());
  }

  default It add(int v) {
    return add(v, v);
  }

  default It subtract(int _1, int _2) {
    _1(_1() - _1);
    _2(_2() - _2);
    return (It) this;
  }

  default It subtract(M_VecInt vec) {
    return subtract(vec._1(), vec._2());
  }

  default It subtract(int v) {
    return subtract(v, v);
  }
}
