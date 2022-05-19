package huffmancoding;

import java.util.List;

public interface huffmann {
    void set_alphabet(char[] alphabet, float[] probabilities);

    List<List<String>> get_codes();

    String encode(String plain_text);

    String decode(String huffman_text);
}
