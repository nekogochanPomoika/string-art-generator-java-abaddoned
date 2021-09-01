package nekogochan.stringart;

import nekogochan.stringart.point.PolarPoint;
import nekogochan.stringart.point.RectPoint;

public class Nail {

    public final RectPoint center;
    public final double radius;

    public Nail(RectPoint center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Nail multiply(double scalar) {
        return multiply(scalar, scalar, scalar);
    }

    public Nail multiply(double scalarX, double scalarY, double scalarR) {
        return new Nail(
            center.multiply(scalarX, scalarY),
            radius * scalarR
        );
    }

    public Nail floor() {
        return new Nail(center.floor(), radius);
    }

    public OffsetFluent offsetLeft() {
        return new OffsetFluent(this, true);
    }
    public OffsetFluent offsetRight() {
        return new OffsetFluent(this, false);
    }

    public static class OffsetFluent {
        private final Nail from;
        private final boolean fromLeft;

        public OffsetFluent(Nail from, boolean fromLeft) {
            this.from = from;
            this.fromLeft = fromLeft;
        }

        public Pair toLeft(Nail to) {
            return Nail.offset(from, to, fromLeft, true);
        }
        public Pair toRight(Nail to) {
            return Nail.offset(from, to, fromLeft, false);
        }
    }

    private static Pair offset(Nail from, Nail to, boolean fromLeft, boolean toLeft) {

        var fromC = from.center;
        var toC = to.center;

        var polarOffset = toC.subtract(fromC).toPolar();

        polarOffset = new PolarPoint(
            to.radius, polarOffset.a + Math.PI / 2
        );

        var dxy = polarOffset.toRect();

        var fromOffset = fromLeft
                         ? fromC.subtract(dxy)
                         : fromC.add(dxy);

        var toOffset = toLeft
                       ? toC.subtract(dxy)
                       : toC.add(dxy);

        return new Pair(fromOffset, toOffset);
    }

    public static class Pair {
        public final RectPoint from;
        public final RectPoint to;

        public Pair(RectPoint from, RectPoint to) {
            this.from = from;
            this.to = to;
        }
    }

    @Override
    public String toString() {
        return center.toString();
    }
}
