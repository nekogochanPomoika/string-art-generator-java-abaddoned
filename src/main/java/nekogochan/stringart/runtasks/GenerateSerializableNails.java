package nekogochan.stringart.runtasks;

import nekogochan.stringart.binds.BindsCircle;
import nekogochan.stringart.binds.SerializableBindNailInfo;
import nekogochan.stringart.config.Factory;
import nekogochan.stringart.config.SerializedNailsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateSerializableNails {
  private final static Logger log = LoggerFactory.getLogger(GenerateSerializableNails.class);

  public static void main(String[] args) throws IOException {
    log.info("start");

    var nailsConfig = Factory.Config.nails();
    var fieldConfig = Factory.Config.field();

    log.info("generate nails");
    var nails = new BindsCircle(
      nailsConfig.nailsCount(),
      fieldConfig.fieldWidth() / 2.0 - 1.0,
      nailsConfig.nailRadius()
    ).nails();

    log.info("map to serializable data");
    var serializableNails = nails.stream()
                                 .map(SerializableBindNailInfo::fromBindNail)
                                 .collect(Collectors.toCollection(ArrayList::new));

    log.info("convert to data");
    var objectData = new ByteArrayOutputStream();
    var objectStream = new ObjectOutputStream(objectData);
    objectStream.writeObject(serializableNails);

    log.info("write to file");
    var path = nailsConfig.serializedNailsPath();
    Files.write(path, objectData.toByteArray());

    log.info("test to read generated nails");
    SerializedNailsFactory.optional()
                          .map(List::size)
                          .ifPresentOrElse(
                            s -> { if (s != nails.size()) throw new Error(); },
                            () -> { throw new Error(); }
                          );

    log.info("end");
  }
}
