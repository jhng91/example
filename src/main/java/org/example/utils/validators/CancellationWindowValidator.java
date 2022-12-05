package org.example.utils.validators;

public class CancellationWindowValidator {
    public boolean validate(int cancellationWindow) {
        return cancellationWindow >= 0 && cancellationWindow < Integer.MAX_VALUE;
    }
}
