package nekogochan.stringart;

import nekogochan.fn.Unchecked;
import nekogochan.fn.ref.IntRef;
import nekogochan.point.RectPointInt;
import nekogochan.stringart.binds.BindNailI;
import nekogochan.stringart.nail.NailI;
import nekogochan.stringart.pair.PairI;

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
  private final List<BindNailI> nails;
  private final double removeValue;
  private final Map<BindNailI, Integer> indexes;
  private final ExecutorService executorService =
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public StringArt(double[][] field, List<BindNailI> nails, double removeValue) {
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
    var iterationsResult = new ArrayList<Future<IterationResult>>();

    from.accessibleBoth().forEach(n -> {
      iterationsResult.add(executorService.submit(() -> iterateLeft(n)));
      iterationsResult.add(executorService.submit(() -> iterateRight(n)));
    });

    if (fromLeft) {
      from.accessibleLeft().forEach(n -> iterationsResult.add(executorService.submit(() -> iterateLeft(n))));
    } else {
      from.accessibleRight().forEach(n -> iterationsResult.add(executorService.submit(() -> iterateRight(n))));
    }

    var result = iterationsResult.stream()
                                 .map(f -> Unchecked.call(f::get))
                                 .max(Comparator.comparingDouble(IterationResult::val))
                                 .orElseThrow();

    var look = from.lookTo(result.target, fromLeft, result.left);
    look.path().forEach($ -> field[$.x()][$.y()] -= removeValue);

    fromIdx = indexes.get(result.target);
    fromLeft = result.left;
    return new NextResult(look, fromIdx);
  }

  private IterationResult iterateLeft(NailI to) {
    return iterate(to, true);
  }

  private IterationResult iterateRight(NailI to) {
    return iterate(to, false);
  }

  private IterationResult iterate(NailI to, boolean toLeft) {
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

  private record IterationResult(NailI target, boolean left, double val) {
  }

  public record NextResult(PairI pair, int idx) {
  }
}
