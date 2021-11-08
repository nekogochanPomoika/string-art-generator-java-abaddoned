package nekogochan.stringart.binds;

import nekogochan.stringart.nail.Nail;

public interface BindNail extends Nail {
  Iterable<? extends Nail> leftToLeft();
  Iterable<? extends Nail> leftToRight();
  Iterable<? extends Nail> rightToLeft();
  Iterable<? extends Nail> rightToRight();
}
