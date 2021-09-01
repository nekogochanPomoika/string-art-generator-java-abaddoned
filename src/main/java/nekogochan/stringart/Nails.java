package nekogochan.stringart;

import nekogochan.stringart.point.PolarPoint;
import nekogochan.stringart.point.RectPoint;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.PI;
import static java.lang.Math.min;

public class Nails {
    public static Stream<Nail> circle(int count, double radius, double nailRadius) {
        return IntStream.range(0, count)
            .mapToObj((i) -> new PolarPoint(radius - nailRadius, (PI * 2.0 * i) / count)
                .toRect()
                .add(new RectPoint(radius, radius)))
            .map((rp) -> new Nail(rp, nailRadius));
    }

    public static Stream<Nail> rect(int count, double width, double height, double radius) {
        var perimeter = (width + height) * 2.0;
        var wRatio = width / perimeter;
        var hRatio = height / perimeter;

        var countOnWidth = (int) (wRatio * count);
        var countOnHeight = (int) (hRatio * count);

        var dy = width / countOnWidth;
        var dx = width / countOnHeight;

        var d = min(dx, dy);

        Supplier<DoubleStream> xAxis = () -> DoubleStream.iterate(0.0, (y) -> y + d).limit(countOnWidth);
        Supplier<DoubleStream> yAxis = () -> DoubleStream.iterate(0.0, (x) -> x + d).limit(countOnHeight);

        var top = xAxis.get().mapToObj((x) -> new RectPoint(x, 0.0));
        var bottom = xAxis.get().mapToObj((x) -> new RectPoint(x + d, height));
        var left = yAxis.get().mapToObj((y) -> new RectPoint(0.0, y + d));
        var right = yAxis.get().mapToObj((y) -> new RectPoint(width, y));

        return Stream.of(top, bottom, left, right)
            .flatMap(Function.identity())
            .map((p) -> new Nail(p, radius));
    }
}
