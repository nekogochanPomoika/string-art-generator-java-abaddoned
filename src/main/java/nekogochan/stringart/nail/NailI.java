package nekogochan.stringart.nail;

import nekogochan.point.Pair;
import nekogochan.point.RectPoint;

public interface NailI {
  RectPoint center();
  double radius();
  Pair lookTo(NailI nail, boolean fromLeft, boolean toLeft);
}
