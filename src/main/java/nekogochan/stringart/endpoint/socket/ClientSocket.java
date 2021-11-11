package nekogochan.stringart.endpoint.socket;

import nekogochan.stringart.endpoint.model.EndpointData;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.Framedata;
import org.java_websocket.protocols.IProtocol;

import javax.net.ssl.SSLSession;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Collection;

public class ClientSocket implements WebSocket {
  private final WebSocket it;

  public ClientSocket(WebSocket it) {
    this.it = it;
  }

  public WebSocket getOrigin() {
    return it;
  }

  public void send(EndpointData data) {
    send(data.toString());
  }

  @Override
  public void close(int code, String message) {it.close(code, message);}

  @Override
  public void close(int code) {it.close(code);}

  @Override
  public void close() {it.close();}

  @Override
  public void closeConnection(int code, String message) {it.closeConnection(code, message);}

  @Override
  public void send(String text) {it.send(text);}

  @Override
  public void send(ByteBuffer bytes) {it.send(bytes);}

  @Override
  public void send(byte[] bytes) {it.send(bytes);}

  @Override
  public void sendFrame(Framedata framedata) {it.sendFrame(framedata);}

  @Override
  public void sendFrame(Collection<Framedata> frames) {it.sendFrame(frames);}

  @Override
  public void sendPing() {it.sendPing();}

  @Override
  public void sendFragmentedFrame(Opcode op, ByteBuffer buffer, boolean fin) {it.sendFragmentedFrame(op, buffer, fin);}

  @Override
  public boolean hasBufferedData() {return it.hasBufferedData();}

  @Override
  public InetSocketAddress getRemoteSocketAddress() {return it.getRemoteSocketAddress();}

  @Override
  public InetSocketAddress getLocalSocketAddress() {return it.getLocalSocketAddress();}

  @Override
  public boolean isOpen() {return it.isOpen();}

  @Override
  public boolean isClosing() {return it.isClosing();}

  @Override
  public boolean isFlushAndClose() {return it.isFlushAndClose();}

  @Override
  public boolean isClosed() {return it.isClosed();}

  @Override
  public Draft getDraft() {return it.getDraft();}

  @Override
  public ReadyState getReadyState() {return it.getReadyState();}

  @Override
  public String getResourceDescriptor() {return it.getResourceDescriptor();}

  @Override
  public <T> void setAttachment(T attachment) {it.setAttachment(attachment);}

  @Override
  public <T> T getAttachment() {return it.getAttachment();}

  @Override
  public boolean hasSSLSupport() {return it.hasSSLSupport();}

  @Override
  public SSLSession getSSLSession() throws IllegalArgumentException {return it.getSSLSession();}

  @Override
  public IProtocol getProtocol() {return it.getProtocol();}
}
