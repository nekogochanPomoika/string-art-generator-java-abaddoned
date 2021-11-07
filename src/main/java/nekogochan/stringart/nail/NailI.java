package nekogochan.stringart.nail;

import nekogochan.stringart.point.RectPoint;
import nekogochan.stringart.pair.PairI;

public interface NailI {
  RectPoint center();
  double radius();
  PairI lookTo(NailI nail, boolean fromLeft, boolean toLeft);
}
