package nekogochan.stringart.fn;

import java.util.HashSet;
import java.util.Set;

public class SplitSet<T> {

  private final Set<T> left;
  private final Set<T> cross;
  private final Set<T> right;

  public SplitSet(Set<T> left, Set<T> right) {
    var all = new HashSet<>(left);
    all.addAll(right);

    this.left = new HashSet<>(all);
    this.right = new HashSet<>(all);
    this.cross = new HashSet<>(all);

    this.left.removeAll(right);
    this.right.removeAll(left);
    this.cross.removeAll(this.left);
    this.cross.removeAll(this.right);
  }

  public Set<T> getLeft() {
    return left;
  }

  public Set<T> getCross() {
    return cross;
  }

  public Set<T> getRight() {
    return right;
  }
}
