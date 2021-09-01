package nekogochan;

import nekogochan.stringart.Field;

import static java.lang.Math.min;

public class FieldData {
    public final Field data;
    public final double avg;
    public final double min;
    public final double sum;

    public FieldData(double[][] data) {
        this.data = new Field(data);

        var _sum = 0.0;
        var _min = Double.MIN_VALUE;

        for (var row : data) {
            for (var e : row) {
                _sum += e;
                _min = min(_min, e);
            }
        }

        this.avg = _sum / (double) (data.length * data[0].length);
        this.sum = _sum;
        this.min = _min;
    }
}
