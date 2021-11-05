package nekogochan.stringart;

import nekogochan.fn.Unchecked;
import nekogochan.fn.ref.IntRef;
import nekogochan.point.Pair;
import nekogochan.point.RectPointInt;
import nekogochan.stringart.binds.BindedNailI;
import nekogochan.stringart.nail.NailI;

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
  private final List<BindedNailI> nails;
  private final double removeValue;
  private final Map<BindedNailI, Integer> indexes;
  private final ExecutorService executorService =
    Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  public StringArt(double[][] field, List<BindedNailI> nails, double removeValue) {
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

    from.getBinds()
        .forEach($ -> {
          iterationsResult.add(executorService.submit(() -> iterate($, false)));
          iterationsResult.add(executorService.submit(() -> iterate($, true)));
        });

    var result = iterationsResult.stream()
                                 .map($ -> Unchecked.call($::get))
                                 .max(Comparator.comparingDouble(IterationResult::val))
                                 .orElseThrow();

    var look = from.lookTo(result.target, fromLeft, result.left);
    look.path().forEach($ -> field[$.x()][$.y()] -= removeValue);

    fromIdx = indexes.get(result.target);
    fromLeft = result.left;
    return new NextResult(look, fromIdx);
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

  public record NextResult(Pair pair, int idx) {
  }
}
