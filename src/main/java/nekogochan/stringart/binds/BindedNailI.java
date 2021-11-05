package nekogochan.stringart.binds;

import nekogochan.stringart.nail.NailI;

import java.util.stream.Stream;

public interface BindedNailI extends NailI {
  Stream<? extends NailI> getBinds();
}
