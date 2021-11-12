package nekogochan.stringart.endpoint.client;

import nekogochan.stringart.main.StringArt;
import nekogochan.stringart.endpoint.StringArtHandler;
import nekogochan.stringart.endpoint.model.EndpointData;
import nekogochan.stringart.endpoint.socket.ClientSocket;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import static nekogochan.stringart.config.EndpointMethods.Client.END;
import static nekogochan.stringart.config.EndpointMethods.Client.HANDLING;
import static nekogochan.stringart.config.EndpointMethods.Client.PROGRESS;
import static nekogochan.stringart.config.EndpointMethods.DATA;
import static nekogochan.stringart.config.EndpointMethods.METHOD;

public class SingleClientHandler implements ClientHandler {

  private ClientSocket ws;
  private boolean handling;
  private final StringArtHandler stringArtHandler = StringArtHandler
    .builder()
    .bufferSize(50)
    .iterations(10000)
    .onProgress(results -> ws.send(new EndpointData().add(METHOD, PROGRESS).add(DATA, results)))
    .onComplete(results -> {
      ws.send(new EndpointData().add(METHOD, END).add(DATA, results));
      ws.close();
    })
    .build();

  @Override
  public void acceptConnection(WebSocket ws, ClientHandshake handshake) {
    if (handling) {
      ws.close(4000, HANDLING);
    } else {
      this.ws = new ClientSocket(ws);
      handling = true;
    }
  }

  @Override
  public void removeConnection(WebSocket ws) {
    if (this.ws.getOrigin() == ws) {
      handling = false;
      stringArtHandler.stop();
    }
  }

  @Override
  public void handle(WebSocket ignored, StringArt stringArt) {
    stringArtHandler.start(stringArt);
  }
}
