package nekogochan.stringart.binds;

import nekogochan.stringart.nail.NailI;

import java.util.List;

public interface BindNailI extends NailI {
  List<? extends NailI> accessibleLeft();
  List<? extends NailI> accessibleRight();
  List<? extends NailI> accessibleBoth();
}
