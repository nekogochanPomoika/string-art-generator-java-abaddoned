package nekogochan.point;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PolarPoint extends Vec2<PolarPoint> {
    private PolarPoint(double r, double a) {
        super(r, a);
    }

    public static PolarPoint of(double r, double a) {
        return new PolarPoint(r, a);
    }

    public double r() {
        return _x;
    }

    public double a() {
        return _y;
    }

    public RectPoint toRect() {
        return RectPoint.of(
            cos(a() * r()),
            sin(a() * r())
        );
    }
}
