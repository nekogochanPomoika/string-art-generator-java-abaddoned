package nekogochan.point;

@SuppressWarnings({"unchecked", "rawtypes"})
public interface M_VecDouble<It extends M_VecDouble<It>> {

  double _1();
  double _2();

  void _1(double _1);
  void _2(double _2);

  default It subtract(double _1, double _2) {
    _1(_1() - _1);
    _2(_2() - _2);
    return (It) this;
  }

  default It subtract(M_VecDouble vec) {
    return subtract(vec._1(), vec._2());
  }

  default It add(double _1, double _2) {
    _1(_1() + _1);
    _2(_2() + _2);
    return (It) this;
  }

  default It add(M_VecDouble dxy) {
    return add(dxy._1(), dxy._2());
  }
}
