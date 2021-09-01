package nekogochan.stringart.point;

import nekogochan.stringart.Fn;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class RectPoint {

    public final double x;
    public final double y;

    public RectPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public RectPoint add(RectPoint dxy) {
        return new RectPoint(
            x + dxy.x,
            y + dxy.y
        );
    }

    public RectPoint subtract(RectPoint dxy) {
        return new RectPoint(
            x - dxy.x,
            y - dxy.y
        );
    }

    public RectPoint multiply(double scalar) {
        return multiply(scalar, scalar);
    }

    public RectPoint multiply(double scalarX, double scalarY) {
        return new RectPoint(
            x * scalarX,
            y * scalarY
        );
    }

    public RectPoint floor() {
        return new RectPoint(
            Math.floor(x),
            Math.floor(y)
        );
    }

    @FunctionalInterface
    public interface PathToCallback {
        void call(int x, int y);
    }
    public void pathTo(RectPoint target, PathToCallback callback) {
        var dxy = target.subtract(this);

        double dx, dy,
            x = this.x,
            y = this.y;

        Fn.DoubleBiPredicate whilePredicate;

        if (abs(dxy.y) > abs(dxy.x)) {
            dy = dxy.y > 0 ? 1 : -1;
            dx = dxy.x / abs(dxy.y);
            whilePredicate = dxy.y > 0
                             ? (_x, _y) -> _y < target.y
                             : (_x, _y) -> _y > target.y;
        } else {
            dx = dxy.x > 0 ? 1 : -1;
            dy = dxy.y / abs(dxy.x);
            whilePredicate = dxy.x > 0
                             ? (_x, _y) -> _x < target.x
                             : (_x, _y) -> _x > target.x;
        }

        do {
            callback.call((int) x, (int) y);
            x += dx;
            y += dy;
        } while (whilePredicate.check(x, y));
    }

    public PolarPoint toPolar() {
        return new PolarPoint(
            sqrt(pow(y, 2) + pow(x, 2)), atan2(y, x)
        );
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}
