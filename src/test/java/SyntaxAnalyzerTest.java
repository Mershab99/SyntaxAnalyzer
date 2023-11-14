import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mershab.SyntaxAnalyzer;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SyntaxAnalyzerTest {

    private static String TEST_INPUT_FILE_LOCATION = "./test_resources/";
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    public void analyzerTest(int input) throws Exception {
        String inputFilename = TEST_INPUT_FILE_LOCATION + "input" + (input == 1 ? "" : input) + ".txt";
        String expectedOutputFilename = TEST_INPUT_FILE_LOCATION + "expected_output" + (input == 1 ? "" : input) + ".txt";

        String expectedOutput = new String(Files.readAllBytes(Paths.get(expectedOutputFilename)));

        String debugPhrase = "Test " + input + ":\n" + "inputFileName: " + inputFilename + "\n" + "expectedOutput: " + expectedOutput + "\n";

        System.out.println(debugPhrase);

        // Assuming your analyzer has a method analyze that takes a string input and returns the output
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(new FileReader(inputFilename));
        String actualOutput = "";
        try {
            actualOutput = syntaxAnalyzer.parse();
        } catch (SyntaxAnalyzer.SyntaxException e) {
            actualOutput = e.getMessage();
        }

        assertEquals(expectedOutput, actualOutput, "Failed for input" + input + ".txt");    }
}

