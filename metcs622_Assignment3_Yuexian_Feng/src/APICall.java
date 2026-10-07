import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class APICall {

    public static void main(String[] args) {

        System.out.println("Fetching data from Data USA API...");

        // Fetch data from Data USA API
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = null;

            try {
                request = HttpRequest.newBuilder()
                        .uri(new URI(
                                "https://api.datausa.io/tesseract/data.jsonrecords?cube=acs_yg_total_population_5&drilldowns=State,Year&measures=Population&include=Year:2023&limit=100,0"
                        ))
                        .GET()
                        .build();

            } catch (URISyntaxException e) {
                System.out.println(
                        "There was a problem with your URL: " + e
                );
            }

            try {
                HttpResponse<String> response =
                        client.send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

                if (response.statusCode() > 299) {

                    System.out.println(
                            "The URL is correct but there is no data, because: "
                                    + response.statusCode()
                    );

                } else {

                    System.out.println(
                            "This is the data in that API:"
                                    + response.body()
                    );

                    // Save the API response body to data.json
                    String file = "data.json";

                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(response.body());
                    }
                }

            } catch (InterruptedException | IOException e) {

                System.out.println(
                        "The server connection was interrupted" + e
                );
            }
        }

        // Read data from data.json
        try {

            String content =
                    Files.readString(Path.of("data.json"));

            // Use the same three search words from Homework 2
            String[] words = {
                    "State",
                    "Year",
                    "Population"
            };

            // Store RegEx search results
            Map<String, Integer> wordCounts =
                    new HashMap<>();

            Map<String, LocalDateTime> searchHistory =
                    new HashMap<>();

            Map<String, Long> regexTimes =
                    new HashMap<>();

            // Search for words using RegEx
            for (String word : words) {

                Pattern pattern = Pattern.compile(
                        "\\b" + word + "\\b",
                        Pattern.CASE_INSENSITIVE
                );

                Matcher matcher =
                        pattern.matcher(content);

                int count = 0;

                // Start RegEx search time
                long startTime =
                        System.nanoTime();

                while (matcher.find()) {
                    count++;
                }

                // End RegEx search time
                long endTime =
                        System.nanoTime();

                wordCounts.put(
                        word,
                        count
                );

                regexTimes.put(
                        word,
                        endTime - startTime
                );

                searchHistory.put(
                        word,
                        LocalDateTime.now()
                );
            }

            // Create a Trie
            Trie trie = new Trie();

            // Split the JSON content into words
            String[] allWords =
                    content.toLowerCase()
                            .split("[^a-z]+");

            // Insert all words into the Trie
            for (String currentWord : allWords) {

                if (!currentWord.isEmpty()) {
                    trie.insert(currentWord);
                }
            }

            // Store Trie search results
            Map<String, Integer> trieCounts =
                    new HashMap<>();

            Map<String, Long> trieTimes =
                    new HashMap<>();

            // Search for the same words using Trie
            for (String word : words) {

                // Start Trie search time
                long startTime =
                        System.nanoTime();

                int count =
                        trie.getFrequency(word);

                // End Trie search time
                long endTime =
                        System.nanoTime();

                trieCounts.put(
                        word,
                        count
                );

                trieTimes.put(
                        word,
                        endTime - startTime
                );
            }

            // Display RegEx search results
            System.out.println("\nRegEx Search Results:");

            for (String word : words) {

                System.out.println(
                        word
                                + " | Count: "
                                + wordCounts.get(word)
                                + " | Time: "
                                + regexTimes.get(word)
                                + " ns"
                );
            }

            // Display Trie search results
            System.out.println("\nTrie Search Results:");

            for (String word : words) {

                System.out.println(
                        word
                                + " | Count: "
                                + trieCounts.get(word)
                                + " | Time: "
                                + trieTimes.get(word)
                                + " ns"
                );
            }

            // Compare RegEx and Trie search results
            System.out.println("\nSearch Comparison:");

            for (String word : words) {

                System.out.println(
                        word
                                + " | RegEx Count: "
                                + wordCounts.get(word)
                                + " | RegEx Time: "
                                + regexTimes.get(word)
                                + " ns"
                                + " | Trie Count: "
                                + trieCounts.get(word)
                                + " | Trie Time: "
                                + trieTimes.get(word)
                                + " ns"
                );
            }

            // Display search history
            System.out.println("\nSearch History:");

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "MM/dd/yy"
                    );

            for (Map.Entry<String, LocalDateTime> entry
                    : searchHistory.entrySet()) {

                System.out.println(
                        entry.getKey()
                                + ": "
                                + entry.getValue()
                                .format(formatter)
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error reading the file: " + e
            );
        }
    }
}


