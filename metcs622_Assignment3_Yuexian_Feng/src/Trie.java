public class Trie {

    private final TrieNode root = new TrieNode();

    // Insert a word into the Trie
    public void insert(String word) {
        TrieNode current = root;

        for (char ch : word.toLowerCase().toCharArray()) {
            current = current.children.computeIfAbsent(
                    ch,
                    c -> new TrieNode()
            );
        }

        current.isEndOfWord = true;
        current.count++;
    }

    // Get the frequency of a word
    public int getFrequency(String word) {
        TrieNode current = root;

        for (char ch : word.toLowerCase().toCharArray()) {
            current = current.children.get(ch);

            if (current == null) {
                return 0;
            }
        }

        if (current.isEndOfWord) {
            return current.count;
        }

        return 0;
    }
}
