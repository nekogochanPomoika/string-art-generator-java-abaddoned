package nekogochan.point;

import nekogochan.fn.DoubleBiPredicate;

import java.util.stream.Stream;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class RectPoint extends Vec2<RectPoint> {
    private RectPoint(double x, double y) {
        super(x, y);
    }

    public static RectPoint of(double x, double y) {
        return new RectPoint(x, y);
    }

    public RectPoint copy() {
        return of(x(), y());
    }

    public PolarPoint toPolar() {
        return PolarPoint.of(
            sqrt(pow(x(), 2) + pow(y(), 2)),
            atan2(y(), x())
        );
    }

    public Stream<RectPoint> pathTo(RectPoint target) {
        var dxy = this.copy().subtract(this);

        double dx, dy;

        DoubleBiPredicate whilePredicate;

        if (abs(dxy.y()) > abs(dxy.x())) {
            dy = dxy.y() > 0 ? 1 : -1;
            dx = dxy.x() / abs(dxy.y());
            whilePredicate = dxy.y() > 0
                             ? (_x, _y) -> _y < target.y()
                             : (_x, _y) -> _y > target.y();
        } else {
            dx = dxy.x() > 0 ? 1 : -1;
            dy = dxy.y() / abs(dxy.x());
            whilePredicate = dxy.x() > 0
                             ? (_x, _y) -> _x < target.x()
                             : (_x, _y) -> _x > target.x();
        }

        return Stream.iterate(
            RectPoint.of(x(), y()),
            (rp) -> whilePredicate.test(rp.x(), rp.y()),
            (rp) -> rp.add(dx, dy)
        );
    }
}
