# metcs622_Assignment3_Yuexian_Feng

## API Data Search Project

This project uses Java to fetch population data from the Data USA API.

The purpose of this project is to compare two different search approaches: RegEx search and Trie search. The program searches for the same three words used in Homework 2, counts their frequencies, and compares the execution time of the two approaches.

The program retrieves 2023 population data by state and saves the API response to a JSON file.

## Project Structure

The project includes the following files:

1. `APICall.java`
   - Sends a GET request to the Data USA API.
   - Handles possible exceptions using try/catch.
   - Saves the API response body to `data.json`.
   - Reads the saved JSON file.
   - Searches for selected words using RegEx.
   - Searches for the same words using Trie.
   - Measures execution time using `System.nanoTime()`.
   - Compares the RegEx and Trie search results.

2. `Trie.java`
   - Implements the Trie search algorithm.
   - Inserts words into the Trie.
   - Uses a Lambda function with `computeIfAbsent()`.
   - Returns the frequency of a searched word.

3. `TrieNode.java`
   - Represents each node in the Trie.
   - Stores child nodes using a `Map`.
   - Stores whether the node is the end of a word.
   - Stores the frequency count of a word.

4. `data.json`
   - Stores the response returned by the Data USA API.

5. `HW3_Search_Comparison.xlsx`
   - Contains the RegEx and Trie execution times.
   - Includes a chart comparing the two search approaches.

## Search Words

The program searches for the same three words from Homework 2:

- `State`
- `Year`
- `Population`

## RegEx Search

The first approach uses Java regular expressions.

The program uses `Pattern` and `Matcher` to search the JSON content. `Pattern.CASE_INSENSITIVE` is used so capitalization does not affect the search results.

The frequency is counted using the `find()` method.

## Trie Search

The second approach uses a Trie data structure.

The JSON content is converted to lowercase and split into individual words. Each word is inserted into the Trie.

When inserting a word, the program uses the following Lambda function:

```java
current.children.computeIfAbsent(ch, c -> new TrieNode());
```

If a character does not already exist in the current node, the Lambda function creates a new `TrieNode`.

The `getFrequency()` method searches through the Trie and returns the frequency of the requested word.

## Frequency Results

Both search approaches returned the same frequencies:

| Search Word | RegEx Frequency | Trie Frequency |
|-------------|----------------:|---------------:|
| State | 106 | 106 |
| Year | 55 | 55 |
| Population | 54 | 54 |

This confirms that the Trie search produced the same frequency results as the RegEx search.

## Execution Time Comparison

The execution time of each search was measured using `System.nanoTime()`.

| Search Word | RegEx Time (ns) | Trie Time (ns) |
|-------------|----------------:|---------------:|
| State | 1,453,625 | 2,625 |
| Year | 223,000 | 583 |
| Population | 197,709 | 1,167 |

In this execution, the Trie lookup was faster than the RegEx search for all three search words.

The Trie construction time is not included in the Trie lookup measurements. The comparison measures the search time after the Trie has already been built.

## Big-O Comparison

For this assignment, the RegEx search is treated as a tree traversal DFS with a worst-case runtime of:

`O(n * m)`

The Trie search has a lookup runtime of:

`O(m)`

where `m` is the length of the search word.

The Trie also requires preprocessing to build the data structure before searching.

## Search Comparison Chart

The file `HW3_Search_Comparison.xlsx` contains a chart comparing the execution times of RegEx and Trie searches for State, Year, and Population.

The chart uses the execution times measured in nanoseconds.

## How to Compile and Run

This project uses Java 21.

From the project directory, compile the source files with:

```bash
javac -d out src/APICall.java src/Trie.java src/TrieNode.java
```

Run the program with:

```bash
java -cp out APICall
```

## Sample Output

```text
RegEx Search Results:
State | Count: 106 | Time: 1453625 ns
Year | Count: 55 | Time: 223000 ns
Population | Count: 54 | Time: 197709 ns

Trie Search Results:
State | Count: 106 | Time: 2625 ns
Year | Count: 55 | Time: 583 ns
Population | Count: 54 | Time: 1167 ns
```

Execution times may be different each time the program is run.