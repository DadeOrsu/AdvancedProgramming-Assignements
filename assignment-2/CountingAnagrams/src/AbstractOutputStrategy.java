import java.util.List;
import java.util.stream.Stream;
public abstract class AbstractOutputStrategy<K, V> {
    public abstract void output(Stream<Pair<K, List<V>>> out);
}
