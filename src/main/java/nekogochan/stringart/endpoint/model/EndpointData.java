package nekogochan.stringart.endpoint.model;

import com.google.gson.Gson;
import nekogochan.stringart.factory.Factory;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class EndpointData {
  private static final Gson JSON = Factory.JSON();
  private final Map<String, String> data;

  public EndpointData(Map<String, String> data) {
    this.data = data;
  }

  public EndpointData(String data) {
    this(Arrays.stream(data.split("&"))
               .map(s -> s.split("="))
               .collect(Collectors.toMap(
                 s -> s[0],
                 s -> s[1]
               )));
  }

  public EndpointData() {
    this(new HashMap<>());
  }

  @Nullable
  public <T> T get(String key, Class<T> clazz) {
    var _data = data.get(key);
    return _data == null
           ? null
           : JSON.fromJson(_data, clazz);
  }

  @Nullable
  public String get(String key) {
    return data.get(key);
  }

  public <T> Optional<T> optional(String key, Class<T> clazz) {
    return Optional.ofNullable(get(key, clazz));
  }

  public Optional<String> optional(String key) {
    return Optional.ofNullable(get(key));
  }

  public EndpointData add(String key, Object obj) {
    data.put(key, JSON.toJson(obj));
    return this;
  }

  @Override
  public String toString() {
    return data.entrySet().stream()
               .map(e -> e.getKey() + "=" + e.getValue())
               .collect(Collectors.joining("&"));
  }
}
