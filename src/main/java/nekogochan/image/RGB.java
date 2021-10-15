package nekogochan.image;


public class RGB {
    private int r;
    private int g;
    private int b;

    private RGB() {
    }

    static RGB of(int r, int g, int b) {
        return new RGB().rgb(r, g, b);
    }

    static RGB ofPlain(int plainRgb) {
        return new RGB()
            .r((plainRgb & R_MASK) >> 16)
            .g((plainRgb & G_MASK) >> 8)
            .b((plainRgb & B_MASK));
    }

    static RGB ofGrayscale(double grayscale) {
        var value = (int) (255 * grayscale);
        return of(value, value, value);
    }

    public int r() {
        return r;
    }

    public int g() {
        return g;
    }

    public int b() {
        return b;
    }

    public RGB r(int r) {
        this.r = r;
        return this;
    }

    public RGB g(int g) {
        this.g = g;
        return this;
    }

    public RGB b(int b) {
        this.b = b;
        return this;
    }

    public RGB rgb(int r, int g, int b) {
        r(r);
        g(g);
        b(b);
        return this;
    }

    public int[] asArray() {
        return new int[]{r, g, b};
    }

    public double grayscale() {
        return (r + g + b) /
               765.0;
    }

    public RGB inverse() {
        r(255 - r);
        g(255 - g);
        b(255 - b);
        return this;
    }

    public int toPlain() {
        return (r << 16)
               + (g << 8)
               + (b);
    }

    private static final int R_MASK = getMask(24) ^ getMask(16);
    private static final int G_MASK = getMask(16) ^ getMask(8);
    private static final int B_MASK = getMask(8);

    private static int getMask(int offset) {
        return (2 << offset - 1) - 1;
    }
}
