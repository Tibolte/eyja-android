package fr.northborders.eyja.util;

/**
 * Created by thibaultguegan on 11/01/15.
 */
public final class Preconditions {
    private Preconditions() {
    }

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static <T> T checkNotNull(T reference, String errorMessage, Object... args) {
        if (reference == null) {
            throw new NullPointerException(String.format(errorMessage, args));
        }

        return reference;
    }

    /**
     * @throws java.lang.IllegalArgumentException if condition is false.
     */
    public static void checkArgument(boolean condition, String errorMessage, Object... args) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(errorMessage, args));
        }
    }

    /**
     * Ensures that a String is not null and contains at least one non-whitespace char.
     *
     * @param value an String
     * @return the String, trimmed of any leading/trailing whitespace
     * @throws java.lang.NullPointerException if {@code value} is null
     * @throws java.lang.IllegalArgumentException if {@code value} is empty
     */
    public static String checkNotNullOrBlank(String value, String errorMessage, Object... args) {
        checkNotNull(value, errorMessage, args);
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(String.format(errorMessage, args));
        }
        return trimmed;
    }

    public static void checkState(boolean expression, String errorMessage, Object... args) {
        if (!expression) {
            throw new IllegalStateException(String.format(errorMessage, args));
        }
    }
}
