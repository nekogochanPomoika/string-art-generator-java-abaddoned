package nekogochan.point;

@SuppressWarnings("unchecked")
public class Vec2<This extends Vec2<This>> {
    
    protected double _x, _y;

    public Vec2(double x, double y) {
        this._x = x;
        this._y = y;
    }
    
    public This add(double x, double y) {
        _x += x;
        _y += y;
        return (This) this;
    }

    public This add(double v) {
        return add(v, v);
    }

    public This add(This vec) {
        return add(vec._x, vec._y);
    }
    
    public This subtract(double x, double y) {
        _x -= x;
        _y -= y;
        return (This) this;
    }

    public This subtract(double v) {
        return subtract(v, v);
    }

    public This subtract(This vec) {
        return subtract(vec._x, vec._y);
    }

    public This multiply(double x, double y) {
        _x *= x;
        _y *= y;
        return (This) this;
    }

    public This multiply(double v) {
        return multiply(v, v);
    }

    public This multiply(This vec) {
        return multiply(vec._x, vec._y);
    }

    public This divide(double x, double y) {
        _x /= x;
        _y /= y;
        return (This) this;
    }

    public This divide(double v) {
        return divide(v, v);
    }

    public This divide(This vec) {
        return divide(vec._x, vec._y);
    }

    public double x() {
        return _x;
    }

    public double y() {
        return _y;
    }

    public This x(double x) {
        _x = x;
        return (This) this;
    }

    public This y(double y) {
        _y = y;
        return (This) this;
    }

    @Override
    public String toString() {
        return "%s: (%.2f, %.2f)".formatted(getClass().getName(), _x, _y);
    }
}
