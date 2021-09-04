package nekogochan.stringart;

import lombok.val;
import nekogochan.FieldData;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PathHandler {

    private final List<Nail> nails;
    private final int iterOffsetPlain;
    private final int iterOffsetCross;
    private Path path;

    public PathHandler(List<Nail> nails, int iterOffsetPlain, int iterOffsetCross) {
        this.nails = nails.stream().map(Nail::floor).collect(Collectors.toList());
        this.iterOffsetPlain = iterOffsetPlain;
        this.iterOffsetCross = iterOffsetCross;
    }

    public void prepare(FieldData fieldData, double subtractValue) {
        this.path = new Path(nails, iterOffsetPlain, iterOffsetCross, fieldData, subtractValue);
    }

    public void iterate(int iterations, BiConsumer<Path.IterationResult, Integer> action) {
        Fn.repeat(iterations, (i) -> path.next((nir) -> action.accept(nir, i)));
    }
}
