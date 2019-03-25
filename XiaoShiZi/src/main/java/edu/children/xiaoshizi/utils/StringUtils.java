package edu.children.xiaoshizi.utils;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class StringUtils {
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String UTF8 = "UTF-8";
    public static final String USASCII = "US-ASCII";
    public static final String QUOTE_ENCODE = "&quot;";
    public static final String APOS_ENCODE = "&apos;";
    public static final String AMP_ENCODE = "&amp;";
    public static final String LT_ENCODE = "&lt;";
    public static final String GT_ENCODE = "&gt;";
    public static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    private static final ThreadLocal<Random> randGen = new ThreadLocal<Random>() {
        protected Random initialValue() {
            return new Random();
        }
    };
    private static final char[] numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final ThreadLocal<SecureRandom> SECURE_RANDOM = new ThreadLocal<SecureRandom>() {
        protected SecureRandom initialValue() {
            return new SecureRandom();
        }
    };

    public StringUtils() {
    }

    public static CharSequence escapeForXml(CharSequence input) {
        return escapeForXml(input, StringUtils.XmlEscapeMode.safe);
    }

    public static CharSequence escapeForXmlAttribute(CharSequence input) {
        return escapeForXml(input, StringUtils.XmlEscapeMode.forAttribute);
    }

    public static CharSequence escapeForXmlAttributeApos(CharSequence input) {
        return escapeForXml(input, StringUtils.XmlEscapeMode.forAttributeApos);
    }

    public static CharSequence escapeForXmlText(CharSequence input) {
        return escapeForXml(input, StringUtils.XmlEscapeMode.forText);
    }

    private static CharSequence escapeForXml(CharSequence input, StringUtils.XmlEscapeMode xmlEscapeMode) {
        if (input == null) {
            return null;
        } else {
            int len = input.length();
            StringBuilder out = new StringBuilder((int)((double)len * 1.3D));
            int last = 0;
            int i = 0;

            while(i < len) {
                String toAppend;
                toAppend = null;
                char ch = input.charAt(i);
                label60:
                switch(xmlEscapeMode) {
                    case safe:
                        switch(ch) {
                            case '"':
                                toAppend = "&quot;";
                                break label60;
                            case '&':
                                toAppend = "&amp;";
                                break label60;
                            case '\'':
                                toAppend = "&apos;";
                                break label60;
                            case '<':
                                toAppend = "&lt;";
                                break label60;
                            case '>':
                                toAppend = "&gt;";
                            default:
                                break label60;
                        }
                    case forAttribute:
                        switch(ch) {
                            case '"':
                                toAppend = "&quot;";
                                break label60;
                            case '&':
                                toAppend = "&amp;";
                                break label60;
                            case '\'':
                                toAppend = "&apos;";
                                break label60;
                            case '<':
                                toAppend = "&lt;";
                            default:
                                break label60;
                        }
                    case forAttributeApos:
                        switch(ch) {
                            case '&':
                                toAppend = "&amp;";
                                break label60;
                            case '\'':
                                toAppend = "&apos;";
                                break label60;
                            case '<':
                                toAppend = "&lt;";
                            default:
                                break label60;
                        }
                    case forText:
                        switch(ch) {
                            case '&':
                                toAppend = "&amp;";
                                break;
                            case '<':
                                toAppend = "&lt;";
                        }
                }

                if (toAppend != null) {
                    if (i > last) {
                        out.append(input, last, i);
                    }

                    out.append(toAppend);
                    ++i;
                    last = i;
                } else {
                    ++i;
                }
            }

            if (last == 0) {
                return input;
            } else {
                if (i > last) {
                    out.append(input, last, i);
                }

                return out;
            }
        }
    }

    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for(int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 15];
        }

        return new String(hexChars);
    }

    public static byte[] toUtf8Bytes(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalStateException("UTF-8 encoding not supported by platform", var2);
        }
    }

    public static String insecureRandomString(int length) {
        return randomString(length, (Random)randGen.get());
    }

    public static String randomString(int length) {
        return randomString(length, (Random)SECURE_RANDOM.get());
    }

    private static String randomString(int length, Random random) {
        if (length < 1) {
            return null;
        } else {
            byte[] randomBytes = new byte[length];
            random.nextBytes(randomBytes);
            char[] randomChars = new char[length];

            for(int i = 0; i < length; ++i) {
                randomChars[i] = getPrintableChar(randomBytes[i]);
            }

            return new String(randomChars);
        }
    }

    private static char getPrintableChar(byte indexByte) {
        assert numbersAndLetters.length < 254;

        int index = indexByte & 255;
        return numbersAndLetters[index % numbersAndLetters.length];
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isNullOrEmpty(cs);
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        return cs == null || isEmpty(cs);
    }

    public static boolean isNotEmpty(CharSequence... css) {
        CharSequence[] var1 = css;
        int var2 = css.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CharSequence cs = var1[var3];
            if (isNullOrEmpty(cs)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isNullOrEmpty(CharSequence... css) {
        CharSequence[] var1 = css;
        int var2 = css.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CharSequence cs = var1[var3];
            if (isNotEmpty(cs)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs.length() == 0;
    }

    public static String collectionToString(Collection<? extends Object> collection) {
        return toStringBuilder(collection, " ").toString();
    }

    public static StringBuilder toStringBuilder(Collection<? extends Object> collection, String delimiter) {
        StringBuilder sb = new StringBuilder(collection.size() * 20);
        Iterator it = collection.iterator();

        while(it.hasNext()) {
            Object cs = it.next();
            sb.append(cs);
            if (it.hasNext()) {
                sb.append(delimiter);
            }
        }

        return sb;
    }

    public static String returnIfNotEmptyTrimmed(String string) {
        if (string == null) {
            return null;
        } else {
            String trimmedString = string.trim();
            return trimmedString.length() > 0 ? trimmedString : null;
        }
    }

    public static boolean nullSafeCharSequenceEquals(CharSequence csOne, CharSequence csTwo) {
        return nullSafeCharSequenceComparator(csOne, csTwo) == 0;
    }

    public static int nullSafeCharSequenceComparator(CharSequence csOne, CharSequence csTwo) {
        if (csOne == null ^ csTwo == null) {
            return csOne == null ? -1 : 1;
        } else {
            return csOne == null && csTwo == null ? 0 : csOne.toString().compareTo(csTwo.toString());
        }
    }

    public static <CS extends CharSequence> CS requireNotNullOrEmpty(CS cs, String message) {
        if (isNullOrEmpty(cs)) {
            throw new IllegalArgumentException(message);
        } else {
            return cs;
        }
    }

    public static <CS extends CharSequence> CS requireNullOrNotEmpty(CS cs, String message) {
        if (cs == null) {
            return null;
        } else if (cs.toString().isEmpty()) {
            throw new IllegalArgumentException(message);
        } else {
            return cs;
        }
    }

    public static String maybeToString(CharSequence cs) {
        return cs == null ? null : cs.toString();
    }

    private static enum XmlEscapeMode {
        safe,
        forAttribute,
        forAttributeApos,
        forText;

        private XmlEscapeMode() {
        }
    }
}