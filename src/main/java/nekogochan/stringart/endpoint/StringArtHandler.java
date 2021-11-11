package nekogochan.stringart.endpoint;

import nekogochan.stringart.StringArt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StringArtHandler {
  private final int bufferSize;
  private final Consumer<List<StringArt.NextResult>> onProgress;
  private final Consumer<List<StringArt.NextResult>> onComplete;
  private int iterations;
  private boolean running = false;

  public StringArtHandler(int bufferSize, int iterations,
                          Consumer<List<StringArt.NextResult>> onProgress,
                          Consumer<List<StringArt.NextResult>> onComplete) {
    this.bufferSize = bufferSize;
    this.iterations = iterations;
    this.onProgress = onProgress;
    this.onComplete = onComplete;
  }

  public void start(StringArt stringArt) {
    running = true;
    var buffer = new ArrayList<StringArt.NextResult>(bufferSize);
    var data = new ArrayList<StringArt.NextResult>();
    while (iterations != 0 && running) {
      var nextResult = stringArt.next();
      buffer.add(nextResult);
      if (buffer.size() == bufferSize) {
        onProgress.accept(buffer);
        data.addAll(buffer);
        buffer.clear();
      }
      iterations--;
    }
    onComplete.accept(data);
  }

  public void stop() {
    running = false;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private int bufferSize;
    private Consumer<List<StringArt.NextResult>> onProgress;
    private Consumer<List<StringArt.NextResult>> onComplete;
    private int iterations;

    public Builder bufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
      return this;
    }

    public Builder onProgress(Consumer<List<StringArt.NextResult>> onProgress) {
      this.onProgress = onProgress;
      return this;
    }

    public Builder onComplete(Consumer<List<StringArt.NextResult>> onComplete) {
      this.onComplete = onComplete;
      return this;
    }

    public Builder iterations(int iterations) {
      this.iterations = iterations;
      return this;
    }

    public StringArtHandler build() {
      return new StringArtHandler(bufferSize,
                                  iterations,
                                  onProgress,
                                  onComplete);
    }
  }
}
