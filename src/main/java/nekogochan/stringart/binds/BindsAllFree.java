package nekogochan.stringart.binds;

import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.point.RectPoint;
import nekogochan.stringart.point.RectPointInt;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * free - means there's no barrier (another nail) from a to b
 */
public class BindsAllFree {
  private final List<BindNail> nails;

  private final int[]
    leftToLeft,
    leftToRight,
    rightToLeft,
    rightToRight;

  public List<BindNail> nails() {
    return nails;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public BindsAllFree(List<? extends Nail> nails) {
    var _nails = nails.stream()
                      .map(_Nail::new)
                      .toList();

    this.nails = (List<BindNail>) (List) _nails;

    leftToLeft = takeIndexesWhereFree(true, true);
    leftToRight = takeIndexesWhereFree(true, false);
    rightToLeft = takeIndexesWhereFree(false, true);
    rightToRight = takeIndexesWhereFree(false, false);

    for (int i = 0; i < _nails.size(); i++) {
      _nails.get(i).init(i);
    }
  }

  private boolean isIn(RectPointInt p, Nail nail) {
    var c = nail.center();
    return Math.hypot(p.x() - c.x(),
                      p.y() - c.y()) <= nail.radius();
  }

  private boolean hasBarrier(int idx, boolean fromLeft, boolean toLeft) {
    return nails.get(0).lookTo(nails.get(idx), fromLeft, toLeft)
                .path()
                .anyMatch(p -> IntStream.range(1, nails.size())
                                        .filter(i -> i != idx)
                                        .mapToObj(nails::get)
                                        .anyMatch(n -> isIn(p, n)));
  }

  // TODO (07.11.2021): fix the shit bug
  private int[] takeIndexesWhereFree(boolean fromLeft, boolean toLeft) {
    return IntStream.range(1, nails.size())
                    .filter(i -> !hasBarrier(i, fromLeft, toLeft))
                    .toArray();
  }

  private class _Nail implements BindNail {

    private final Nail it;

    private List<Nail>
      _leftToLeft,
      _leftToRight,
      _rightToLeft,
      _rightToRight;

    _Nail(Nail it) {
      this.it = it;
    }

    private void init(int idx) {
      this._leftToLeft = getSequence(idx, leftToLeft);
      this._leftToRight = getSequence(idx, leftToRight);
      this._rightToLeft = getSequence(idx, rightToLeft);
      this._rightToRight = getSequence(idx, rightToRight);
    }

    private List<Nail> getSequence(int idx, int[] availableNailsIndexes) {
      return Arrays.stream(availableNailsIndexes)
                   .map(i -> (i + idx) % nails.size())
                   .mapToObj(nails::get)
                   .collect(Collectors.toList());
    }

    @Override
    public List<Nail> leftToLeft() {
      return _leftToLeft;
    }

    @Override
    public List<Nail> leftToRight() {
      return _leftToRight;
    }

    @Override
    public List<Nail> rightToLeft() {
      return _rightToLeft;
    }

    @Override
    public List<Nail> rightToRight() {
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
