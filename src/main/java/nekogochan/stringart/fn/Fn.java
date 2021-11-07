package nekogochan.stringart.fn;

public class Fn {
  public static boolean anySame(Object target, Object ...rest) {
    for (var r : rest) {
      if (target == r) return true;
    }
    return false;
  }

  public static boolean noneSame(Object target, Object ...rest) {
    return !anySame(target, rest);
  }
}
