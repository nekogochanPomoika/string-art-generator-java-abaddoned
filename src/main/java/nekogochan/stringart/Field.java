package nekogochan.stringart;

public class Field {

    private final double[][] field;
    private final int width;
    private final int height;

    public Field(int width, int height) {
        this(new double[width][height]);
    }

    public Field(double[][] field) {
        this.field = field;
        this.width = field.length;
        this.height = field[0].length;
    }

    public double get(int x, int y) {
        return field[x][y];
    }

    public void set(int x, int y, double v) {
        field[x][y] = v;
    }

    public void add(int x, int y, double v) {
        field[x][y] += v;
    }

    public void subtract(int x, int y, double v) {
        add(x, y, -v);
    }

    public int width() {return width;}

    public int height() {return height;}
}
