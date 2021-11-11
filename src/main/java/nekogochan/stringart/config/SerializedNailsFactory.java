package nekogochan.stringart.config;

import io.vavr.control.Try;
import nekogochan.stringart.binds.BindNail;
import nekogochan.stringart.binds.SerializableBindNailInfo;
import nekogochan.stringart.fn.Fn;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class SerializedNailsFactory {
  public static NailsConfig nailsConfig = Factory.Config.nails();

  public static Optional<List<? extends BindNail>> optional() {
    return Try.of(() -> Files.readAllBytes(nailsConfig.serializedNailsPath()))
              .flatMap(data -> Try.withResources(() -> new ObjectInputStream(new ByteArrayInputStream(data)))
                                  .of(ObjectInputStream::readObject))
              .map(Fn::<List<SerializableBindNailInfo>>autoCast)
              .map(l -> l.stream().map(SerializableBindNailInfo::toStandardBindNail).toList())
              .toJavaOptional()
              .map(Fn::autoCast);
  }
}
