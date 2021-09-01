package nekogochan.stringart;

import java.util.function.IntConsumer;

public class Fn {
    public static void repeat(int times, IntConsumer consumer) {
        for (int i = 0; i < times; i++) {
            consumer.accept(i);
        }
    }

    public static void iterateExcluding(int root, int top, int excludeLeft, int excludeRight, IntConsumer consumer) {
        var last = top - excludeRight;

        for (int i = excludeLeft; i < last; i++) {
            consumer.accept((i + root) % top);
        }
    }

    @FunctionalInterface
    public interface DoubleBiPredicate {
        boolean check(double a, double b);
    }
}
