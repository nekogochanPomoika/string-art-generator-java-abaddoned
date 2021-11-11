package nekogochan.stringart.config;

import com.google.gson.Gson;
import nekogochan.stringart.MultithreadingOptimizedStringArt;
import nekogochan.stringart.StringArt;
import nekogochan.stringart.binds.BindNail;
import nekogochan.stringart.binds.BindsCircle;
import nekogochan.stringart.endpoint.StringArtEndpoint;
import nekogochan.stringart.endpoint.client.ClientHandler;
import nekogochan.stringart.endpoint.client.SingleClientHandler;

import java.net.InetSocketAddress;
import java.util.List;

public class Factory {
  public static class Endpoint {
    private static final Gson gson = new Gson();

    public static Gson JSON() {
      return gson;
    }

    public static ClientHandler clientHandler() {
      return new SingleClientHandler();
    }

    public static StringArtEndpoint stringArtEndpoint() {
      return new StringArtEndpoint(new InetSocketAddress(5500), clientHandler());
    }
  }

  public static class Config {
    private static final NailsConfig nails = new NailsConfig();
    private static final FieldConfig field = new FieldConfig();

    public static NailsConfig nails() {
      return nails;
    }

    public static FieldConfig field() {
      return field;
    }
  }

  public static class Model {

    public static StringArt stringArt(double[][] data) {
      return new MultithreadingOptimizedStringArt(data, nails(), Config.field().removeValue());
    }

    public static List<? extends BindNail> nails() {
      return SerializedNailsFactory.optional()
                                   .orElseGet(() -> new BindsCircle(200, 499.0, 2.5).nails());
    }
  }
}
