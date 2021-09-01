package nekogochan.stringart;

import lombok.Setter;
import lombok.experimental.Accessors;

public class Settings {
    public final int nailsCount;
    public final double nailRadius;
    public final int iterationOffsetLeft;
    public final int iterationOffsetRight;

    public Settings(int nailsCount, double nailRadius, int iterationOffsetLeft, int iterationOffsetRight) {
        this.nailsCount = nailsCount;
        this.nailRadius = nailRadius;
        this.iterationOffsetLeft = iterationOffsetLeft;
        this.iterationOffsetRight = iterationOffsetRight;
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class Builder {
        private int nailsCount;
        private double nailRadius;
        private int iterationOffsetLeft;
        private int iterationOffsetRight;

        public Settings build() {
            return new Settings(nailsCount, nailRadius, iterationOffsetLeft, iterationOffsetRight);
        }
    }
}
