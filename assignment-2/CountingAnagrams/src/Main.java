public class Main {
    /**
     * Given the absolute path of a directory, print the number of anagrams
     * of all the words contained in a set of documents in that directory.
     * It could have been done in a separate Main class,
     * but for the sake of the exercise, I decided to put it here.
     */
    public static void main(String[] args) {
        JobScheduler<String, String> scheduler = new JobScheduler<>(
                new ConcreteEmitStrategy(),
                new ConcreteOutputStrategy()
        );
        scheduler.executePhases();
    }
}