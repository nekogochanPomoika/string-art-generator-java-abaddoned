package nekogochan.stringart;

import nekogochan.stringart.config.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    log.info("Application started");
    var server = Factory.Endpoint.stringArtEndpoint();
    server.start();
  }
}
