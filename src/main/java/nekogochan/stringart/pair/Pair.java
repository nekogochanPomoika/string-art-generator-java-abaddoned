package nekogochan.stringart.pair;

import nekogochan.stringart.point.RectPointInt;

import java.util.stream.Stream;

public interface Pair {
  Stream<RectPointInt> path();
  RectPointInt from();
  RectPointInt to();
}
