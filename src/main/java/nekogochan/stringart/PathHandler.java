package nekogochan.stringart;

import lombok.val;
import nekogochan.FieldData;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PathHandler {

    private final List<Nail> nails;
    private final int iterOffsetPlain;
    private final int iterOffsetCross;

    public PathHandler(List<Nail> nails, int iterOffsetPlain, int iterOffsetCross) {
        this.nails = nails.stream().map(Nail::floor).collect(Collectors.toList());
        this.iterOffsetPlain = iterOffsetPlain;
        this.iterOffsetCross = iterOffsetCross;
    }

    public void handle(FieldData fieldData, double subtractValue, Consumer<Path.IterationResult> action) {

        val path = new Path(nails, iterOffsetPlain, iterOffsetCross, fieldData, subtractValue);

        Fn.repeat(10000, (i) -> {
            if (i % 200 == 0) System.out.println(i);
            path.next(action);
        });
        /*
        var power = new Ref.Double(1.0);
        var i = 0;

        while (power.val > -0.5) {
            i++;
            path.next((nir) -> {
                action.accept(nir);
                power.val = nir.power;
            });
            if (i % 100 == 0)
            System.out.format("%s: %s\n", i, power.val);
        }*/
    }
}
