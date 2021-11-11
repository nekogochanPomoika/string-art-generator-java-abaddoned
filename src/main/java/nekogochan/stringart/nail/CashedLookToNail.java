package nekogochan.stringart.nail;

import nekogochan.stringart.fn.container.IfElse;
import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.point.RectPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

  private final Map<Nail, IfElse<IfElse<Pair>>> cash = new HashMap<>();

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CashedLookToNail that = (CashedLookToNail) o;
    return Objects.equals(it, that.it) && Objects.equals(cash, that.cash);
  }

  @Override
  public int hashCode() {
    return Objects.hash(it, cash);
  }
}
