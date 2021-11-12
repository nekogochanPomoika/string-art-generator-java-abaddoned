package nekogochan.stringart.config;

public class EndpointMethods {

  public static final String
    METHOD = "method",
    DATA = "data";

  public static class Server {
    public static final String
      DONT_UNDERSTAND = "dont_understand",
      HANDLE = "handle",
      INVALID_DATA = "invalid_data",
      INVALID_DATA_FORMAT = "invalid_data_format";
  }

  public static class Client {
    public static final String
      PROGRESS = "progress",
      END = "end",
      HANDLING = "handling";
  }
}
