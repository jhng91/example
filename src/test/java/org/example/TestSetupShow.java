package org.example;

import org.example.controllers.ShowController;
import org.example.exceptions.InvalidArgumentException;
import org.example.exceptions.ShowAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class TestSetupShow {
    ShowController controller;
    @Before
    public void init() {
        controller = new ShowController();
    }
    @Test
    public void testSetupShowValidSeatAndRowLower() throws InvalidArgumentException, ShowAlreadyExistsException {
        boolean result = controller.setupShow(1, 1, 1, 1);
        assertTrue(result);
    }
    @Test
    public void testSetupShowValidSeatAndRowUpper() throws InvalidArgumentException, ShowAlreadyExistsException {
        boolean result = controller.setupShow(1, 26, 10, 1);
        assertTrue(result);
    }
    @Test
    public void testSetupShowInvalidRowLower() {
        assertThrows(InvalidArgumentException.class, () ->
                controller.setupShow(1, 0, 1, 1));
    }
    @Test
    public void testSetupShowInvalidRowUpper() {
        assertThrows(InvalidArgumentException.class, () ->
                controller.setupShow(1, 27, 1, 1));
    }

    @Test
    public void testSetupShowInvalidSeatLower() {
        assertThrows(InvalidArgumentException.class, () ->
                controller.setupShow(1, 1, 0, 1));
    }
    @Test
    public void testSetupShowInvalidSeatUpper() {
        assertThrows(InvalidArgumentException.class, () ->
                controller.setupShow(1, 1, 11, 1));
    }
    @Test
    public void testSetupShowAlreadyExists() throws InvalidArgumentException, ShowAlreadyExistsException {
        controller.setupShow(1, 1, 1, 1);
        assertThrows(ShowAlreadyExistsException.class, () ->
                controller.setupShow(1, 1, 1, 1));
    }
    @Test
    public void testSetupShowInvalidCancellationWindowLower() {
        assertThrows(InvalidArgumentException.class, () ->
                controller.setupShow(1, 1, 10, -1));
    }
    @Test
    public void testSetupShowValidCancellationWindowUpper() {
        assertThrows(InvalidArgumentException.class, () ->
                controller.setupShow(1, 1, 10, Integer.MAX_VALUE));
    }
}
