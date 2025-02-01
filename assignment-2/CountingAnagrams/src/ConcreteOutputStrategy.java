import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;


public class ConcreteOutputStrategy extends AbstractOutputStrategy<String,String> {
    /**
     * Output the results to a file
     * @param collection the collection of pairs to output
     */
    @Override
    public void output(Stream<Pair<String, List<String>>> collection) {
        // Open the file using a BufferedWriter for efficiency
        File outFile = new File("count_anagrams.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            // Write to file
            collection.forEach(pair -> {
                try {
                    String ciao = pair.getKey();
                    long anagramsCount = pair.getValue().size();

                    writer.write(ciao + " - " + anagramsCount);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Output written to " + outFile.getAbsolutePath());
        }
    }
}
