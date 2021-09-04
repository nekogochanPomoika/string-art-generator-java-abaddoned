package nekogochan.stringart;

import nekogochan.stringart.point.PolarPoint;
import nekogochan.stringart.point.RectPoint;

import java.util.function.DoubleFunction;
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

        var _width = width - radius * 2 - 2;
        var _height = height - radius * 2 - 2;
        var offset = new RectPoint(radius + 1, radius + 1);

        var perimeter = (_width + _height) * 2.0;
        var wRatio = _width / perimeter;

        var countOnWidth = (int) (wRatio * count) - 1;
        var countOnHeight = count / 2 - countOnWidth - 2;

        var dw = _width / (double) (countOnWidth + 1);
        var dh = _height / (double) (countOnHeight + 1);

        Supplier<DoubleStream> xAxis = () -> DoubleStream.iterate(dw, (x) -> x + dw).limit(countOnWidth);
        Supplier<DoubleStream> yAxis = () -> DoubleStream.iterate(dh, (y) -> y + dh).limit(countOnHeight);

        var top = xAxis.get().mapToObj((x) -> rp(x, 0.0));
        var bottom = xAxis.get().mapToObj((x) -> rp(x, _height));
        var left = yAxis.get().mapToObj((y) -> rp(0.0, y));
        var right = yAxis.get().mapToObj((y) -> rp(_width, y));
        var angles = Stream.of(
            rp(0.0, 0.0),
            rp(0.0, _height),
            rp(_width, 0.0),
            rp(_width, _height)
        );

        return Stream.of(top, bottom, left, right, angles)
            .flatMap(Function.identity())
            .map((p) -> new Nail(p.add(offset), radius));
    }

    private static RectPoint rp(double x, double y) {
        return new RectPoint(x, y);
    }
}
