package me.ogali.xenithlibrary.conditions.evaluator;

public enum Evaluator {
    EQUAL,
    NOT_EQUAL,
    LESS_THAN,
    GREATER_THAN,
    LESS_OR_EQUAL,
    GREATER_OR_EQUAL;

    /**
     * Evaluates two string values based on the evaluator type.
     *
     * @param value1 The first value to compare
     * @param value2 The second value to compare
     * @return true if the comparison is satisfied, false otherwise
     */
    public boolean evaluate(String value1, String value2) {
        if (value1 == null || value2 == null) {
            return false;
        }

        return switch (this) {
            case EQUAL -> value1.equals(value2);
            case NOT_EQUAL -> !value1.equals(value2);
            case LESS_THAN -> {
                try {
                    yield Double.parseDouble(value1) < Double.parseDouble(value2);
                } catch (NumberFormatException e) {
                    yield value1.compareTo(value2) < 0;
                }
            }
            case GREATER_THAN -> {
                try {
                    yield Double.parseDouble(value1) > Double.parseDouble(value2);
                } catch (NumberFormatException e) {
                    yield value1.compareTo(value2) > 0;
                }
            }
            case LESS_OR_EQUAL -> {
                try {
                    yield Double.parseDouble(value1) <= Double.parseDouble(value2);
                } catch (NumberFormatException e) {
                    yield value1.compareTo(value2) <= 0;
                }
            }
            case GREATER_OR_EQUAL -> {
                try {
                    yield Double.parseDouble(value1) >= Double.parseDouble(value2);
                } catch (NumberFormatException e) {
                    yield value1.compareTo(value2) >= 0;
                }
            }
        };
    }
}