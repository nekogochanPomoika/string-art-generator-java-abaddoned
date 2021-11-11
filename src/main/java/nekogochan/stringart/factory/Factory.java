package nekogochan.stringart.factory;

import com.google.gson.Gson;
import nekogochan.stringart.MultithreadingOptimizedStringArt;
import nekogochan.stringart.StringArt;
import nekogochan.stringart.binds.BindNail;
import nekogochan.stringart.binds.BindsCircle;
import nekogochan.stringart.endpoint.StringArtEndpoint;
import nekogochan.stringart.endpoint.client.ClientHandler;
import nekogochan.stringart.endpoint.client.SingleClientHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class Factory {
  private static final Gson gson = new Gson();

  public static Gson JSON() {
    return gson;
  }

  private static final List<? extends BindNail> nails = new BindsCircle(200, 499.0, 2.5).nails();

  public static StringArt stringArt(double[][] data) {
    return new MultithreadingOptimizedStringArt(data, nails, 0.2);
  }

  public static ClientHandler clientHandler() {
    return new SingleClientHandler();
  }

  public static StringArtEndpoint stringArtEndpoint() {
    return new StringArtEndpoint(new InetSocketAddress(5500), clientHandler());
  }
}
