package nekogochan.stringart.endpoint.client;

import nekogochan.stringart.StringArt;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public interface ClientHandler {
  void acceptConnection(WebSocket ws, ClientHandshake handshake);
  void removeConnection(WebSocket ws);
  void handle(WebSocket ws, StringArt stringArt);
}
