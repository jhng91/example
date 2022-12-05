package org.example.utils.validators;

import java.util.Set;

public class RowValidator {
    public boolean validateNumberOfRows(int numberOfRows) {
        return numberOfRows > 0 && numberOfRows <= 26;
    }
    public boolean validateRowInShow(String row, Set<String> rows) { return rows.contains(row); }
}
