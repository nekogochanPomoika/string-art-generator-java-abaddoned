package nekogochan.stringart.fn;

import nekogochan.stringart.fn.lambda.IntBiFunction;

import java.util.List;
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
}
