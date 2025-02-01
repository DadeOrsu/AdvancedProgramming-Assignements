import java.util.stream.Stream;

public abstract class AbstractEmitStrategy<K, V> {
    public abstract Stream<AJob<K, V>> emit();
}
