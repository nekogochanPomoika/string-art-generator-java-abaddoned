package nekogochan.stringart.binds;

import nekogochan.stringart.fn.Unchecked;
import nekogochan.stringart.fn.container.IfElse;
import nekogochan.stringart.fn.ref.IntRef;
import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.point.RectPoint;
import nekogochan.stringart.point.RectPointInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static nekogochan.stringart.fn.Fn.noneSame;

/**
 * пиздец какая медленная хуйня, т.к. проверяет все гвозди со всеми
 * время n^2 от количества гвоздей * n от размера поля
 * по возможности не использовать эту хуйню
 * либо создавать на старте приложения и сохранять куда-нибудь
 */
public class BindsAllFree {
  private final List<? extends BindNail> nails;

  public List<? extends BindNail> nails() {
    return nails;
  }

  public BindsAllFree(List<? extends Nail> nails) {
    var _nails = nails.stream().map(_Nail::new).toList();

    Map<_Nail, IfElse<IfElse<Map<_Nail, Boolean>>>> bindsMap =
      _nails.stream()
            .collect(Collectors.toMap(
              n -> n,
              n -> new IfElse<>(
                new IfElse<>(
                  new ConcurrentHashMap<>(),
                  new ConcurrentHashMap<>()),
                new IfElse<>(
                  new ConcurrentHashMap<>(),
                  new ConcurrentHashMap<>()))));

    var es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    var tasks = new ArrayList<Future<?>>();
    var idx = new IntRef(0);

    _nails.forEach(
      from -> tasks.add(es.submit(() -> {
        _nails.stream()
              .filter(to -> to != from)
              .forEach(to -> {
                bind(bindsMap, from, to, true, true);
                bind(bindsMap, from, to, true, false);
                bind(bindsMap, from, to, false, true);
                bind(bindsMap, from, to, false, false);
              });
        System.out.println("idx = " + idx.getAndIncrement());
      })));

    tasks.forEach(f -> Unchecked.call(f::get));

    _nails.forEach(n -> n.init(
      getFree(bindsMap, n, true, true),
      getFree(bindsMap, n, true, false),
      getFree(bindsMap, n, false, true),
      getFree(bindsMap, n, false, false)
    ));

    this.nails = _nails;
  }

  private static void bind(Map<_Nail, IfElse<IfElse<Map<_Nail, Boolean>>>> bindsMap,
                           _Nail from, _Nail to, boolean fromLeft, boolean toLeft) {
    var bind = bindsMap.get(from).get(fromLeft).get(toLeft).get(to);
    if (bind == null) {
      var value = isFree(bindsMap.keySet(), from, to, fromLeft, toLeft);
      bindsMap.get(from).get(fromLeft).get(toLeft).put(to, value);
      bindsMap.get(to).get(!toLeft).get(!fromLeft).put(from, value);
    }
  }

  private static boolean isFree(Collection<? extends Nail> nails,
                                Nail from, Nail to, boolean fromLeft, boolean toLeft) {
    return nails.stream()
                .filter(n -> noneSame(n, from, to))
                .noneMatch(n -> from.lookTo(to, fromLeft, toLeft).path()
                                    .anyMatch(p -> isIn(p, n)));
  }

  private static List<_Nail> getFree(Map<_Nail, IfElse<IfElse<Map<_Nail, Boolean>>>> bindsMap,
                                     _Nail from, boolean fromLeft, boolean toLeft) {
    return bindsMap.get(from).get(fromLeft).get(toLeft).entrySet().stream()
                   .filter(Map.Entry::getValue)
                   .map(Map.Entry::getKey)
                   .toList();
  }

  private static boolean isIn(RectPointInt p, Nail nail) {
    var c = nail.center();
    return Math.hypot(p.x() - c.x(),
                      p.y() - c.y()) <= nail.radius();
  }

  private static class _Nail implements BindNail {
    final Nail it;
    List<_Nail>
      leftToLeft,
      leftToRight,
      rightToLeft,
      rightToRight;

    _Nail(Nail it) {
      this.it = it;
    }

    void init(List<_Nail> leftToLeft,
              List<_Nail> leftToRight,
              List<_Nail> rightToLeft,
              List<_Nail> rightToRight) {
      this.leftToLeft = leftToLeft;
      this.leftToRight = leftToRight;
      this.rightToLeft = rightToLeft;
      this.rightToRight = rightToRight;
    }

    @Override
    public RectPoint center() {
      return it.center();
    }

    @Override
    public double radius() {
      return it.radius();
    }

    @Override
    public Pair lookTo(Nail nail, boolean fromLeft, boolean toLeft) {
      return it.lookTo(nail, fromLeft, toLeft);
    }

    @Override
    public List<? extends Nail> leftToLeft() {
      return leftToLeft;
    }

    @Override
    public List<? extends Nail> leftToRight() {
      return leftToRight;
    }

    @Override
    public List<? extends Nail> rightToLeft() {
      return rightToLeft;
    }

    @Override
    public List<? extends Nail> rightToRight() {
      return rightToRight;
    }
  }
}
