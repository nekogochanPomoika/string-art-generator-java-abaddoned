package nekogochan.ref;

public class Ref<T> {
    public T val;

    public Ref(T val) {
        this.val = val;
    }

    public Ref() {}

    public static class Int {
        public int val;

        public Int(int val) {
            this.val = val;
        }
    }

    public static class Double {
        public double val;

        public Double(double val) {
            this.val = val;
        }
    }

    public static class Boolean {
        public boolean val;

        public Boolean(boolean val) {
            this.val = val;
        }
    }
}
