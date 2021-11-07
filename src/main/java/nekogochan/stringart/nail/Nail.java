package nekogochan.stringart.nail;

import nekogochan.stringart.point.RectPoint;
import nekogochan.stringart.pair.Pair;

public interface Nail {
  RectPoint center();
  double radius();
  Pair lookTo(Nail nail, boolean fromLeft, boolean toLeft);
}
