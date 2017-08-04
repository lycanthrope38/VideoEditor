package com.freelancer.videoeditor.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import net.margaritov.preference.colorpicker.BuildConfig;

public class FilenameUtils {
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = new Character(EXTENSION_SEPARATOR).toString();
    private static final char OTHER_SEPARATOR;
    private static final char SYSTEM_SEPARATOR = File.separatorChar;
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';

    static {
        if (isSystemWindows()) {
            OTHER_SEPARATOR = UNIX_SEPARATOR;
        } else {
            OTHER_SEPARATOR = WINDOWS_SEPARATOR;
        }
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

    private static boolean isSeparator(char ch) {
        return ch == UNIX_SEPARATOR || ch == WINDOWS_SEPARATOR;
    }

    public static String normalize(String filename) {
        return doNormalize(filename, true);
    }

    public static String normalizeNoEndSeparator(String filename) {
        return doNormalize(filename, false);
    }

    private static String doNormalize(String filename, boolean keepSeparator) {
        if (filename == null) {
            return null;
        }
        int length = filename.length();
        if (length == 0) {
            return filename;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        int i;
        char[] array = new char[(length + 2)];
        filename.getChars(0, filename.length(), array, 0);
        for (i = 0; i < array.length; i++) {
            if (array[i] == OTHER_SEPARATOR) {
                array[i] = SYSTEM_SEPARATOR;
            }
        }
        boolean lastIsDirectory = true;
        if (array[length - 1] != SYSTEM_SEPARATOR) {
            int size = length + 1;
            array[length] = SYSTEM_SEPARATOR;
            lastIsDirectory = false;
            length = size;
        }
        i = prefix + 1;
        while (i < length) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == SYSTEM_SEPARATOR) {
                System.arraycopy(array, i, array, i - 1, length - i);
                length--;
                i--;
            }
            i++;
        }
        i = prefix + 1;
        while (i < length) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == EXTENSION_SEPARATOR && (i == prefix + 1 || array[i - 2] == SYSTEM_SEPARATOR)) {
                if (i == length - 1) {
                    lastIsDirectory = true;
                }
                System.arraycopy(array, i + 1, array, i - 1, length - i);
                length -= 2;
                i--;
            }
            i++;
        }
        i = prefix + 2;
        while (i < length) {
            if (array[i] == SYSTEM_SEPARATOR && array[i - 1] == EXTENSION_SEPARATOR && array[i - 2] == EXTENSION_SEPARATOR && (i == prefix + 2 || array[i - 3] == SYSTEM_SEPARATOR)) {
                if (i == prefix + 2) {
                    return null;
                }
                if (i == length - 1) {
                    lastIsDirectory = true;
                }
                for (int j = i - 4; j >= prefix; j--) {
                    if (array[j] == SYSTEM_SEPARATOR) {
                        System.arraycopy(array, i + 1, array, j + 1, length - i);
                        length -= i - j;
                        i = j + 1;
                        break;
                    }
                }
                System.arraycopy(array, i + 1, array, prefix, length - i);
                length -= (i + 1) - prefix;
                i = prefix + 1;
            }
            i++;
        }
        if (length <= 0) {
            return BuildConfig.FLAVOR;
        }
        if (length <= prefix) {
            return new String(array, 0, length);
        }
        if (lastIsDirectory && keepSeparator) {
            return new String(array, 0, length);
        }
        return new String(array, 0, length - 1);
    }

    public static String concat(String basePath, String fullFilenameToAdd) {
        int prefix = getPrefixLength(fullFilenameToAdd);
        if (prefix < 0) {
            return null;
        }
        if (prefix > 0) {
            return normalize(fullFilenameToAdd);
        }
        if (basePath == null) {
            return null;
        }
        int len = basePath.length();
        if (len == 0) {
            return normalize(fullFilenameToAdd);
        }
        if (isSeparator(basePath.charAt(len - 1))) {
            return normalize(basePath + fullFilenameToAdd);
        }
        return normalize(basePath + UNIX_SEPARATOR + fullFilenameToAdd);
    }

    public static String separatorsToUnix(String path) {
        return (path == null || path.indexOf(92) == -1) ? path : path.replace(WINDOWS_SEPARATOR, UNIX_SEPARATOR);
    }

    public static String separatorsToWindows(String path) {
        return (path == null || path.indexOf(47) == -1) ? path : path.replace(UNIX_SEPARATOR, WINDOWS_SEPARATOR);
    }

    public static String separatorsToSystem(String path) {
        if (path == null) {
            return null;
        }
        if (isSystemWindows()) {
            return separatorsToWindows(path);
        }
        return separatorsToUnix(path);
    }

    public static int getPrefixLength(String filename) {
        if (filename == null) {
            return -1;
        }
        int len = filename.length();
        if (len == 0) {
            return 0;
        }
        char ch0 = filename.charAt(0);
        if (ch0 == ':') {
            return -1;
        }
        int posUnix;
        int posWin;
        if (len == 1) {
            if (ch0 == '~') {
                return 2;
            }
            if (isSeparator(ch0)) {
                return 1;
            }
            return 0;
        } else if (ch0 == '~') {
            posUnix = filename.indexOf(47, 1);
            posWin = filename.indexOf(92, 1);
            if (posUnix == -1 && posWin == -1) {
                return len + 1;
            }
            if (posUnix == -1) {
                posUnix = posWin;
            }
            if (posWin == -1) {
                posWin = posUnix;
            }
            return Math.min(posUnix, posWin) + 1;
        } else {
            char ch1 = filename.charAt(1);
            if (ch1 == ':') {
                ch0 = Character.toUpperCase(ch0);
                if (ch0 < 'A' || ch0 > 'Z') {
                    return -1;
                }
                if (len == 2 || !isSeparator(filename.charAt(2))) {
                    return 2;
                }
                return 3;
            } else if (isSeparator(ch0) && isSeparator(ch1)) {
                posUnix = filename.indexOf(47, 2);
                posWin = filename.indexOf(92, 2);
                if ((posUnix == -1 && posWin == -1) || posUnix == 2 || posWin == 2) {
                    return -1;
                }
                if (posUnix == -1) {
                    posUnix = posWin;
                }
                if (posWin == -1) {
                    posWin = posUnix;
                }
                return Math.min(posUnix, posWin) + 1;
            } else if (isSeparator(ch0)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static int indexOfLastSeparator(String filename) {
        if (filename == null) {
            return -1;
        }
        return Math.max(filename.lastIndexOf(47), filename.lastIndexOf(92));
    }

    public static int indexOfExtension(String filename) {
        if (filename == null) {
            return -1;
        }
        int extensionPos = filename.lastIndexOf(46);
        if (indexOfLastSeparator(filename) > extensionPos) {
            extensionPos = -1;
        }
        return extensionPos;
    }

    public static String getPrefix(String filename) {
        if (filename == null) {
            return null;
        }
        int len = getPrefixLength(filename);
        if (len < 0) {
            return null;
        }
        if (len > filename.length()) {
            return filename + UNIX_SEPARATOR;
        }
        return filename.substring(0, len);
    }

    public static String getPath(String filename) {
        return doGetPath(filename, 1);
    }

    public static String getPathNoEndSeparator(String filename) {
        return doGetPath(filename, 0);
    }

    private static String doGetPath(String filename, int separatorAdd) {
        if (filename == null) {
            return null;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        int index = indexOfLastSeparator(filename);
        if (prefix >= filename.length() || index < 0) {
            return BuildConfig.FLAVOR;
        }
        return filename.substring(prefix, index + separatorAdd);
    }

    public static String getFullPath(String filename) {
        return doGetFullPath(filename, true);
    }

    public static String getFullPathNoEndSeparator(String filename) {
        return doGetFullPath(filename, false);
    }

    private static String doGetFullPath(String filename, boolean includeSeparator) {
        if (filename == null) {
            return null;
        }
        int prefix = getPrefixLength(filename);
        if (prefix < 0) {
            return null;
        }
        if (prefix < filename.length()) {
            int index = indexOfLastSeparator(filename);
            if (index < 0) {
                return filename.substring(0, prefix);
            }
            int i;
            if (includeSeparator) {
                i = 1;
            } else {
                i = 0;
            }
            return filename.substring(0, index + i);
        } else if (includeSeparator) {
            return getPrefix(filename);
        } else {
            return filename;
        }
    }

    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        return filename.substring(indexOfLastSeparator(filename) + 1);
    }

    public static String getBaseName(String filename) {
        return removeExtension(getName(filename));
    }

    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        if (index == -1) {
            return BuildConfig.FLAVOR;
        }
        return filename.substring(index + 1);
    }

    public static String removeExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int index = indexOfExtension(filename);
        return index != -1 ? filename.substring(0, index) : filename;
    }

    public static boolean equals(String filename1, String filename2) {
        return equals(filename1, filename2, false, IOCase.SENSITIVE);
    }

    public static boolean equalsOnSystem(String filename1, String filename2) {
        return equals(filename1, filename2, false, IOCase.SYSTEM);
    }

    public static boolean equalsNormalized(String filename1, String filename2) {
        return equals(filename1, filename2, true, IOCase.SENSITIVE);
    }

    public static boolean equalsNormalizedOnSystem(String filename1, String filename2) {
        return equals(filename1, filename2, true, IOCase.SYSTEM);
    }

    public static boolean equals(String filename1, String filename2, boolean normalized, IOCase caseSensitivity) {
        if (filename1 != null && filename2 != null) {
            if (normalized) {
                filename1 = normalize(filename1);
                filename2 = normalize(filename2);
                if (filename1 == null || filename2 == null) {
                    throw new NullPointerException("Error normalizing one or both of the file names");
                }
            }
            if (caseSensitivity == null) {
                caseSensitivity = IOCase.SENSITIVE;
            }
            return caseSensitivity.checkEquals(filename1, filename2);
        } else if (filename1 == filename2) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isExtension(String filename, String extension) {
        if (filename == null) {
            return false;
        }
        if (extension != null && extension.length() != 0) {
            return getExtension(filename).equals(extension);
        }
        if (indexOfExtension(filename) == -1) {
            return true;
        }
        return false;
    }

    public static boolean isExtension(String filename, String[] extensions) {
        boolean z = true;
        if (filename == null) {
            return false;
        }
        if (extensions == null || extensions.length == 0) {
            if (indexOfExtension(filename) != -1) {
                z = false;
            }
            return z;
        }
        String fileExt = getExtension(filename);
        for (Object equals : extensions) {
            if (fileExt.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExtension(String filename, Collection extensions) {
        boolean z = true;
        if (filename == null) {
            return false;
        }
        if (extensions == null || extensions.isEmpty()) {
            if (indexOfExtension(filename) != -1) {
                z = false;
            }
            return z;
        }
        String fileExt = getExtension(filename);
        for (Object equals : extensions) {
            if (fileExt.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public static boolean wildcardMatch(String filename, String wildcardMatcher) {
        return wildcardMatch(filename, wildcardMatcher, IOCase.SENSITIVE);
    }

    public static boolean wildcardMatchOnSystem(String filename, String wildcardMatcher) {
        return wildcardMatch(filename, wildcardMatcher, IOCase.SYSTEM);
    }

    public static boolean wildcardMatch(String filename, String wildcardMatcher, IOCase caseSensitivity) {
        if (filename == null && wildcardMatcher == null) {
            return true;
        }
        if (filename == null || wildcardMatcher == null) {
            return false;
        }
        if (caseSensitivity == null) {
            caseSensitivity = IOCase.SENSITIVE;
        }
        filename = caseSensitivity.convertCase(filename);
        String[] wcs = splitOnTokens(caseSensitivity.convertCase(wildcardMatcher));
        boolean anyChars = false;
        int textIdx = 0;
        int wcsIdx = 0;
        Stack backtrack = new Stack();
        do {
            if (backtrack.size() > 0) {
                int[] array = (int[]) backtrack.pop();
                wcsIdx = array[0];
                textIdx = array[1];
                anyChars = true;
            }
            while (wcsIdx < wcs.length) {
                if (wcs[wcsIdx].equals("?")) {
                    textIdx++;
                    anyChars = false;
                } else if (wcs[wcsIdx].equals("*")) {
                    anyChars = true;
                    if (wcsIdx == wcs.length - 1) {
                        textIdx = filename.length();
                    }
                } else {
                    if (!anyChars) {
                        if (!filename.startsWith(wcs[wcsIdx], textIdx)) {
                            break;
                        }
                    }
                    textIdx = filename.indexOf(wcs[wcsIdx], textIdx);
                    if (textIdx == -1) {
                        break;
                    }
                    if (filename.indexOf(wcs[wcsIdx], textIdx + 1) >= 0) {
                        backtrack.push(new int[]{wcsIdx, filename.indexOf(wcs[wcsIdx], textIdx + 1)});
                    }
                    textIdx += wcs[wcsIdx].length();
                    anyChars = false;
                }
                wcsIdx++;
            }
            if (wcsIdx == wcs.length && textIdx == filename.length()) {
                return true;
            }
        } while (backtrack.size() > 0);
        return false;
    }

    static String[] splitOnTokens(String text) {
        if (text.indexOf("?") == -1 && text.indexOf("*") == -1) {
            return new String[]{text};
        }
        char[] array = text.toCharArray();
        ArrayList list = new ArrayList();
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        while (i < array.length) {
            if (array[i] == '?' || array[i] == '*') {
                if (buffer.length() != 0) {
                    list.add(buffer.toString());
                    buffer.setLength(0);
                }
                if (array[i] == '?') {
                    list.add("?");
                } else if (list.size() == 0 || (i > 0 && !list.get(list.size() - 1).equals("*"))) {
                    list.add("*");
                }
            } else {
                buffer.append(array[i]);
            }
            i++;
        }
        if (buffer.length() != 0) {
            list.add(buffer.toString());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
