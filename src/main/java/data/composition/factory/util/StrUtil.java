package data.composition.factory.util;

/**
 * @author zhangjinyu
 * @since 2023-09-07
 */
public interface StrUtil {
    static boolean isBlank(CharSequence str) {
        final int length;
        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!isBlankChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static boolean isBlankChar(char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a' || c == '\u0000'
                // issue#I5UGSQ，Hangul Filler
                || c == '\u3164'
                // Braille Pattern Blank
                || c == '\u2800'
                // MONGOLIAN VOWEL SEPARATOR
                || c == '\u180e';
    }
}
