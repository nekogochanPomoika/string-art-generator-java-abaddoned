package nekogochan.stringart;

import nekogochan.stringart.binds.BindNail;
import nekogochan.stringart.fn.Unchecked;
import nekogochan.stringart.nail.Nail;
import nekogochan.stringart.point.RectPointInt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultithreadingOptimizedStringArt implements StringArt {

  private final double[][] field;
  private final List<? extends BindNail> nails;
  private final double removeValue;
  private final Map<Nail, Integer> indexes;
  private final ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public MultithreadingOptimizedStringArt(double[][] field, List<? extends BindNail> nails, double removeValue) {
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

  @Override
  public StringArt.NextResult next() {
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
    return new StringArt.NextResult(look, fromIdx);
  }

  private IterationResult iterateLeft(Nail to) {
    return iterate(to, true);
  }

  private IterationResult iterateRight(Nail to) {
    return iterate(to, false);
  }

  private IterationResult iterate(Nail to, boolean toLeft) {
    var from = nails.get(fromIdx);
    var avg = from.lookTo(to, fromLeft, toLeft)
                  .path()
                  .mapToDouble(this::getFieldValue)
                  .collect(() -> new double[2],
                           (countAndSum, value) -> {
                             countAndSum[0]++;
                             countAndSum[1] += value;
                           },
                           (d1, d2) -> {
                             d1[0] += d2[0];
                             d1[1] += d2[1];
                           });
    return new IterationResult(to, toLeft, avg[1] / avg[0]);
  }

  private double getFieldValue(RectPointInt point) {
    return field[point.x()][point.y()];
  }

  private record IterationResult(Nail target, boolean left, double val) {
  }
}
