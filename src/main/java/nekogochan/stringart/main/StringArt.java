package nekogochan.stringart.main;

import nekogochan.stringart.pair.Pair;

public interface StringArt {
  NextResult next();

  record NextResult(Pair pair, int idx) {}
}
