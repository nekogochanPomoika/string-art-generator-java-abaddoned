package nekogochan.fn;

import nekogochan.fn.lambda.IntBiFunction;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Fn {
  public static <T> Stream<Stream<T>> generateBiStream(
      int xFrom, int xTo,
      int yFrom, int yTo,
      IntBiFunction<T> function
  ) {
    return IntStream.range(xFrom, xTo)
        .mapToObj((x) -> IntStream.range(yFrom, yTo)
            .mapToObj((y) -> function.apply(x, y)));
  }

  public static <T> List<List<T>> generateBIList(
      int xFrom, int xTo,
      int yFrom, int yTo,
      IntBiFunction<T> function
  ) {
    return generateBiStream(xFrom, xTo, yFrom, yTo, function)
        .map(Stream::toList)
        .toList();
  }

  public static class FnIterator<T> implements Iterator<T> {

    private T it;
    private final Predicate<T> predicate;
    private final UnaryOperator<T> nextGenerator;

    public FnIterator(T initValue, Predicate<T> predicate, UnaryOperator<T> nextGenerator) {
      this.it = initValue;
      this.predicate = predicate;
      this.nextGenerator = nextGenerator;
    }

    @Override
    public boolean hasNext() {
      return predicate.test(it);
    }

    @Override
    public T next() {
      var prev = it;
      it = nextGenerator.apply(it);
      return prev;
    }
  }

  public static <T> Iterator<T> iterator(T initValue, Predicate<T> predicate, UnaryOperator<T> nextGenerator) {
    return new FnIterator<>(initValue, predicate, nextGenerator);
  }
}
