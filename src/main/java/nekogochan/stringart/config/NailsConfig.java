package nekogochan.stringart.config;

import java.nio.file.Path;

public class NailsConfig {
  private final FieldConfig fieldConfig = Factory.Config.field();

  private final Path serializedNailsPath = Path.of("nails.ser");

  public int nailsCount() {
    return 200;
  }

  public double nailRadius() {
    return 2.5;
  }

  public Path serializedNailsPath() {
    return serializedNailsPath;
  }
}
