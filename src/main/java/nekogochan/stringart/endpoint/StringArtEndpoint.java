package nekogochan.stringart.endpoint;

import com.google.gson.JsonParseException;
import io.vavr.control.Try;
import nekogochan.stringart.endpoint.client.ClientHandler;
import nekogochan.stringart.endpoint.model.EndpointData;
import nekogochan.stringart.config.Factory;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Optional;

import static nekogochan.stringart.endpoint.EndpointMethods.DATA;
import static nekogochan.stringart.endpoint.EndpointMethods.METHOD;
import static nekogochan.stringart.endpoint.EndpointMethods.Server.DONT_UNDERSTAND;
import static nekogochan.stringart.endpoint.EndpointMethods.Server.HANDLE;
import static nekogochan.stringart.endpoint.EndpointMethods.Server.INVALID_DATA;
import static nekogochan.stringart.endpoint.EndpointMethods.Server.INVALID_DATA_FORMAT;

public class StringArtEndpoint extends WebSocketServer {
  private static final Logger log = LoggerFactory.getLogger(StringArtEndpoint.class);

  private final ClientHandler clientHandler;

  public StringArtEndpoint(InetSocketAddress address, ClientHandler clientHandler) {
    super(address);
    this.clientHandler = clientHandler;
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    log.info("open connection: {}", conn.getRemoteSocketAddress());
    clientHandler.acceptConnection(conn, handshake);
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    log.info("connection closed: {}", conn.getRemoteSocketAddress());
    clientHandler.removeConnection(conn);
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    var data = new EndpointData(message);
    data.optional(METHOD)
        .ifPresentOrElse(method -> onMethod(conn, method, data),
                         () -> conn.close(4000, DONT_UNDERSTAND));
  }

  private void onMethod(WebSocket conn, String method, EndpointData data) {
    if (method.equals(HANDLE)) {
      Try.of(() -> data.optional(DATA, double[][].class))
         .onFailure(JsonParseException.class, (ex) -> onJsonParseError(conn, ex))
         .onSuccess(imageData -> imageData.flatMap(this::toEmptyIfInvalid)
                                          .ifPresentOrElse(d -> onHandle(conn, d),
                                                           () -> conn.close(4000, INVALID_DATA)));
    } else {
      conn.close(4000, DONT_UNDERSTAND);
    }
  }

  private void onHandle(WebSocket conn, double[][] data) {
    clientHandler.handle(conn, Factory.Model.stringArt(data));
  }

  private Optional<double[][]> toEmptyIfInvalid(double[][] data) {
    return isValid(data) ? Optional.of(data)
                         : Optional.empty();
  }

  private boolean isValid(double[][] data) {
    if (data.length != 1000) return false;
    for (var row : data) {
      if (row.length != 1000) return false;
    }
    return true;
  }

  private void onJsonParseError(WebSocket conn, JsonParseException ex) {
    log.error("client send invalid data: {} {}", conn.getRemoteSocketAddress(), ex);
    conn.close(4000, INVALID_DATA_FORMAT);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    log.error("error occurred", ex);
    clientHandler.removeConnection(conn);
  }

  @Override
  public void onStart() {
    log.info("socket server is started");
  }
}
