package nekogochan.stringart;

import nekogochan.stringart.binds.BindNail;
import nekogochan.stringart.fn.Unchecked;
import nekogochan.stringart.fn.ref.IntRef;
import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.pair.Pair;
import nekogochan.stringart.point.RectPointInt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StringArt {

  private final double[][] field;
  private final List<? extends BindNail> nails;
  private final double removeValue;
  private final Map<Nail, Integer> indexes;
  private final ExecutorService es =
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public StringArt(double[][] field, List<? extends BindNail> nails, double removeValue) {
    this.field = field;
    this.nails = nails;
    this.removeValue = removeValue;
    indexes = new HashMap<>();
    for (int i = 0; i < nails.size(); i++) {
      indexes.put(nails.get(i), i);
    }
  }

  private int fromIdx = 0;
  private boolean fromLeft = false;

  public NextResult next() {
    var from = nails.get(fromIdx);
    var tasks = new ArrayList<Future<IterationResult>>();

    if (fromLeft) {
      from.leftToLeft().forEach(n -> tasks.add(es.submit(() -> iterateLeft(n))));
      from.leftToRight().forEach(n -> tasks.add(es.submit(() -> iterateRight(n))));
    } else {
      from.rightToLeft().forEach(n -> tasks.add(es.submit(() -> iterateLeft(n))));
      from.rightToRight().forEach(n -> tasks.add(es.submit(() -> iterateRight(n))));
    }

    var result = tasks.stream()
                                 .map(f -> Unchecked.call(f::get))
                                 .max(Comparator.comparingDouble(IterationResult::val))
                                 .orElseThrow();

    var look = from.lookTo(result.target, fromLeft, result.left);
    look.path().forEach($ -> field[$.x()][$.y()] -= removeValue);

    fromIdx = indexes.get(result.target);
    fromLeft = result.left;
    return new NextResult(look, fromIdx);
  }

  private IterationResult iterateLeft(Nail to) {
    return iterate(to, true);
  }

  private IterationResult iterateRight(Nail to) {
    return iterate(to, false);
  }

  private IterationResult iterate(Nail to, boolean toLeft) {
    var from = nails.get(fromIdx);
    var count = new IntRef(0);
    var sum = from.lookTo(to, fromLeft, toLeft)
                  .path()
                  .mapToDouble(this::getFieldValue)
                  .reduce(0, (a, b) -> {
                    count.increment();
                    return a + b;
                  });
    return new IterationResult(to, toLeft, sum / count.get());
  }

  private double getFieldValue(RectPointInt point) {
    return field[point.x()][point.y()];
  }

  private record IterationResult(Nail target, boolean left, double val) {
  }

  public record NextResult(Pair pair, int idx) {
  }
}
