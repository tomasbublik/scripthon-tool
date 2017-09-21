package cz.ctu.scripthon.compiler.lexical;

import java.util.Arrays;
import java.util.List;

public class SupportedCharacters {

    private static final List<String> LIST_OF_CONCATENATED_CHARACTERS = Arrays.asList("==", "<=", ">=", "<>");

    private static final char[] SUPPORTED_CHARACTERS = new char[]{'(', ')', '=', '"', ';', '.', '[', ']', ','};


    public static boolean containsConcatenatedString(String sequence) {
        return compare(sequence);
    }


    public static boolean containsConcatenatedString(CharSequence sequence) {
        return compare(sequence);
    }

    private static boolean compare(CharSequence sequence) {
        for (String listOfCharacter : LIST_OF_CONCATENATED_CHARACTERS) {
            if (listOfCharacter.equals(sequence)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsConcatenatedString(Character... characters) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Character character : characters) {
            stringBuilder.append(character);
        }
        String sequence = stringBuilder.toString();
        return compare(sequence);
    }

    private static boolean containedInSupportedCharacters(char character) {
        for (char SUPPORTED_CHARACTER : SUPPORTED_CHARACTERS) {
            if (character == SUPPORTED_CHARACTER) {
                return true;
            }
        }
        return false;
    }

    static boolean isThisCharacterSupported(char character) {
        return Character.isLetterOrDigit(character) || containedInSupportedCharacters(character);
    }
}
