package nekogochan.stringart.endpoint.client;

import nekogochan.stringart.StringArt;
import nekogochan.stringart.endpoint.StringArtHandler;
import nekogochan.stringart.endpoint.model.EndpointData;
import nekogochan.stringart.endpoint.socket.ClientSocket;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public class SingleClientHandler implements ClientHandler {

  private ClientSocket ws;
  private boolean handling;

  @Override
  public void acceptConnection(WebSocket ws, ClientHandshake handshake) {
    if (handling) {
      ws.close(1007, "handling");
    } else {
      this.ws = new ClientSocket(ws);
      handling = true;
    }
  }

  @Override
  public void removeConnection(WebSocket ws) {
    if (this.ws.getOrigin() == ws) {
      handling = false;
    }
  }

  @Override
  public void handle(WebSocket ignored, StringArt stringArt) {
    StringArtHandler.builder()
                    .bufferSize(50)
                    .iterations(10000)
                    .onProgress(results -> ws.send(new EndpointData().add("method", "PROGRESS")
                                                                     .add("data", results)))
                    .onComplete(results -> ws.send(new EndpointData().add("method", "END")
                                                                     .add("data", results)))
                    .build()
                    .start(stringArt);
  }
}
