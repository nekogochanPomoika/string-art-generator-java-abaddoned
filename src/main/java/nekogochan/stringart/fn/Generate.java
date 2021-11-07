package nekogochan.stringart.fn;

import nekogochan.stringart.fn.lambda.IntBiFunction;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Generate {
  public static <T> Stream<Stream<T>> biStream(
    int xFrom, int xTo,
    int yFrom, int yTo,
    IntBiFunction<T> function
  ) {
    return IntStream.range(xFrom, xTo)
                    .mapToObj((x) -> IntStream.range(yFrom, yTo)
                                              .mapToObj((y) -> function.apply(x, y)));
  }

  public static <T> List<List<T>> biList(
    int xFrom, int xTo,
    int yFrom, int yTo,
    IntBiFunction<T> function
  ) {
    return Generate.biStream(xFrom, xTo, yFrom, yTo, function)
      .map(Stream::toList)
      .toList();
  }
}
