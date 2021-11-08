package nekogochan.stringart;

import nekogochan.stringart.binds.BindsAllFree;
import nekogochan.stringart.nail.NailImpl;
import nekogochan.stringart.point.PolarPoint;

import java.util.stream.IntStream;

import static java.lang.Math.PI;

public class BindsCircle extends BindsAllFree {
  public BindsCircle(int count, double radius, double nailRadius) {
    super(IntStream.range(0, count)
                   .mapToObj((i) -> new PolarPoint(radius - nailRadius,
                                                   (PI * 2.0 * i) / count)
                     .toRect()
                     .add(radius, radius))
                   .map((rp) -> new NailImpl(rp, nailRadius))
                   .toList());
  }
}
