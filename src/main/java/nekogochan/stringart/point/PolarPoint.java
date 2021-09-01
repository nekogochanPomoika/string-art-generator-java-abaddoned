package nekogochan.stringart.point;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class PolarPoint {

    public final double a;
    public final double r;

    public PolarPoint(double r, double a) {
        this.a = a;
        this.r = r;
    }

    public RectPoint toRect() {
        return new RectPoint(
            cos(a) * r,
            sin(a) * r
        );
    }

    public PolarPoint subtract(double dr, double da) {
        return new PolarPoint(r - dr, a - da);
    }

    public PolarPoint multiply(double scalar) {
        return new PolarPoint(r * scalar, a);
    }
}
