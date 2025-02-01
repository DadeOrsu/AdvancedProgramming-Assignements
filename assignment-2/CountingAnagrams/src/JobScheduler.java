import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JobScheduler<K, V> {

    private AbstractEmitStrategy<K, V> emitStrategy;
    private AbstractOutputStrategy<K, V> outputStrategy;
    /**
     * Constructor.
     * @param emitStrategy The emit strategy.
     * @param outputStrategy The output strategy.
     */
    public JobScheduler(AbstractEmitStrategy<K, V> emitStrategy, AbstractOutputStrategy<K, V> outputStrategy) {
        this.emitStrategy = emitStrategy;
        this.outputStrategy = outputStrategy;
    }

    /**
     * Sets the emit strategy.
     * @param emitStrategy The new emit strategy.
     */
    public void setEmitStrategy(AbstractEmitStrategy<K, V> emitStrategy) {
        this.emitStrategy = emitStrategy;
    }

    /**
     * Sets the output strategy.
     * @param outputStrategy The new output strategy.
     */
    public void setOutputStrategy(AbstractOutputStrategy<K, V> outputStrategy) {
        this.outputStrategy = outputStrategy;
    }
    /**
     * Entry point of the framework.
     * It is a frozen spot, since the user should not change the order of the
     * phases.
     */
    public final void executePhases() {
        output(collect(compute(emit())));
    }

    /**
     * Executes the jobs received from emit by invoking execute on them.
     *
     * @return A single stream of key/value pairs obtained by concatenating the
     *         output of the jobs.
     */
    public Stream<AJob<K, V>> emit(){
        return emitStrategy.emit();
    }

    public final Stream<Pair<K, V>> compute(Stream<AJob<K, V>> stream) {
        return stream.flatMap(AJob::execute);
    }

    /**
     * Groups the output of compute by key, keeping a list of all the values.
     *
     * @param computed Output of emit.
     * @return A stream of pairs grouped as described.
     */
    public final Stream<Pair<K, List<V>>> collect(Stream<Pair<K, V>> computed) {
        return computed
                .collect(Collectors.groupingBy(Pair::getKey, Collectors.mapping(Pair::getValue, Collectors.toList())))
                .entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue()));
    }

    /**
     * Outputs the result of collect.
     * @param out Output of collect.
     */
    public void output(Stream<Pair<K, List<V>>> out){
        outputStrategy.output(out);
    }
}