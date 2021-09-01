package nekogochan.stringart;

import lombok.SneakyThrows;
import lombok.val;
import nekogochan.FieldData;
import nekogochan.ref.Ref;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class Path {

    private final List<Nail> nails;
    private final FieldData fieldData;
    private final double subtractValue;
    private final int iterOffsetStraight;
    private final int iterOffsetCross;

    private final Map<Nail, Integer> nailIndexes = new HashMap<>();

    private boolean prevLeft = false;
    private Nail prev;

    public Path(List<Nail> nails, int iterOffsetStraight, int iterOffsetCross, FieldData field, double subtractValue) {
        this.nails = nails;
        this.iterOffsetStraight = iterOffsetStraight;
        this.fieldData = field;
        this.subtractValue = subtractValue;
        this.prev = nails.get(0);
        this.iterOffsetCross = iterOffsetCross;

        for (int i = 0; i < nails.size(); i++) {
            nailIndexes.put(nails.get(i), i);
        }
    }

    @SneakyThrows
    public void next(Consumer<IterationResult> action) {
        val fromIndex = nailIndexes.get(prev);
        val field = fieldData.data;

        val offsetsFrom = prevLeft
                          ? prev.offsetLeft()
                          : prev.offsetRight();

        val iterateResult = new ConcurrentLinkedQueue<IterationResult>();

        NextIterationHandler handler = (i, left) -> {
            val sum = new Ref.Double(0.0);
            val pathLength = new Ref.Int(0);

            val target = nails.get(i);

            val offsets = left
                          ? offsetsFrom.toLeft(target)
                          : offsetsFrom.toRight(target);

            offsets.from.pathTo(offsets.to, (x, y) -> {
                sum.val += field.get(x, y);
                pathLength.val++;
            });

            val power = sum.val /
                           (double) pathLength.val;

            iterateResult.add(new IterationResult(
                sum.val, power, target, offsets, left
            ));
        };

        // iteration offset left/right iter arg -> left/right handle arg
        val ioll = prevLeft ? iterOffsetStraight : iterOffsetCross;
        val iorl = prevLeft ? 0                  : iterOffsetCross;
        val iolr = prevLeft ? iterOffsetCross    : 0;
        val iorr = prevLeft ? iterOffsetCross    : iterOffsetStraight;

        CompletableFuture.allOf(
            CompletableFuture.runAsync(
                () -> Fn.iterateExcluding(fromIndex, nails.size(), ioll, iorl, (i) -> handler.call(i, true))),
            CompletableFuture.runAsync(
                () -> Fn.iterateExcluding(fromIndex, nails.size(), iolr, iorr, (i) -> handler.call(i, false)))
        ).get();

        IterationResult iterationResult = iterateResult.stream().max(Comparator.comparingDouble(a -> a.power)).get();

        val nextOffsets = iterationResult.path;
        val next = iterationResult.target;
        val nextLeft = iterationResult.left;

        val nextFrom = nextOffsets.from;
        val nextTo = nextOffsets.to;

        nextFrom.pathTo(nextTo, (x, y) -> field.subtract(x, y, subtractValue));

        prev = next;
        prevLeft = nextLeft;

        action.accept(iterationResult);
    }

    @FunctionalInterface
    private interface NextIterationHandler {
        void call(int i, boolean left);
    }

    public static class IterationResult {
        public final double sum;
        public final double power;
        public final Nail target;
        public final Nail.Pair path;
        public final boolean left;

        public IterationResult(double sum, double power, Nail target, Nail.Pair path, boolean left) {
            this.sum = sum;
            this.power = power;
            this.target = target;
            this.path = path;
            this.left = left;
        }
    }
}
