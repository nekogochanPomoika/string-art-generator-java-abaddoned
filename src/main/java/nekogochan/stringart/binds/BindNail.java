package nekogochan.stringart.binds;

import nekogochan.stringart.nail.Nail;

public interface BindNail extends Nail {
  Iterable<Nail> leftToLeft();
  Iterable<Nail> leftToRight();
  Iterable<Nail> rightToLeft();
  Iterable<Nail> rightToRight();
}
