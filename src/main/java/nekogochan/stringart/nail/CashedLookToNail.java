package nekogochan.stringart.nail;

import nekogochan.stringart.fn.container.IfElse;
import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.point.RectPoint;

import java.util.Map;
import java.util.WeakHashMap;

public class CashedLookToNail implements Nail {
  private final Nail it;

  public CashedLookToNail(Nail it) {
    this.it = it;
  }

  @Override
  public RectPoint center() {
    return it.center();
  }

  @Override
  public double radius() {
    return it.radius();
  }

  Map<Nail, IfElse<IfElse<Pair>>> cash = new WeakHashMap<>();

  @Override
  public Pair lookTo(Nail nail, boolean fromLeft, boolean toLeft) {
    var ifElse = cash.get(nail);
    if (ifElse == null) {
      ifElse = new IfElse<>(new IfElse<>(),
                            new IfElse<>());
      cash.put(nail, ifElse);
    }

    var value = ifElse.get(fromLeft).get(toLeft);
    if (value == null) {
      value = it.lookTo(nail, fromLeft, toLeft);
      ifElse.get(fromLeft).set(toLeft, value);
    }

    return value;
  }
}
