package nekogochan.stringart.binds;

import nekogochan.stringart.fn.Unchecked;
import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.point.RectPoint;
import nekogochan.stringart.point.RectPointInt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static nekogochan.stringart.fn.Fn.noneSame;

/**
 * пиздец какая медленная хуйня, т.к. проверяет все гвозди со всеми
 * время n^2 от количества гвоздей * n от размера поля
 * на размер поля 500 на 500 и 240 гвоздями по периметру занимет 5.5 минут
 */
public class BindsAllFree {
  private final List<? extends BindNail> nails;

  public List<? extends BindNail> nails() {
    return nails;
  }

  public BindsAllFree(List<? extends Nail> nails) {
    var _nails = nails.stream()
                      .map(_Nail::new)
                      .toList();
    this.nails = _nails;
    var es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    var tasks = new ArrayList<Future<?>>();
    for (int i = 0; i < _nails.size(); i++) {
      int _i = i;
      tasks.add(es.submit(() -> _nails.get(_i).init(_i)));
    }
    tasks.forEach(t -> Unchecked.call(t::get));
  }

  private boolean isIn(RectPointInt p, Nail nail) {
    var c = nail.center();
    return Math.hypot(p.x() - c.x(),
                      p.y() - c.y()) <= nail.radius();
  }

  private boolean isFree(Nail from, Nail to, boolean fromLeft, boolean toLeft) {
    return nails.stream()
                .filter(n -> noneSame(n, from, to))
                .noneMatch(n -> from.lookTo(to, fromLeft, toLeft).path()
                                    .anyMatch(p -> isIn(p, n)));
  }

  private List<? extends Nail> takeWhereFree(int idx, boolean fromLeft, boolean toLeft) {
    var root = nails.get(idx);
    return nails.stream()
                .filter(n -> n != root)
                .filter(n -> isFree(root, n, fromLeft, toLeft))
                .toList();
  }

  private class _Nail implements BindNail {

    private final Nail it;

    private List<? extends Nail>
      _leftToLeft,
      _leftToRight,
      _rightToLeft,
      _rightToRight;

    _Nail(Nail it) {
      this.it = it;
    }

    private void init(int idx) {
      this._leftToLeft = takeWhereFree(idx, true, true);
      this._leftToRight = takeWhereFree(idx, true, false);
      this._rightToLeft = takeWhereFree(idx, false, true);
      this._rightToRight = takeWhereFree(idx, false, false);
      System.out.println("idx = " + idx);
    }

    @Override
    public List<? extends Nail> leftToLeft() {
      return _leftToLeft;
    }

    @Override
    public List<? extends Nail> leftToRight() {
      return _leftToRight;
    }

    @Override
    public List<? extends Nail> rightToLeft() {
      return _rightToLeft;
    }

    @Override
    public List<? extends Nail> rightToRight() {
      return _rightToRight;
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
  }
}
