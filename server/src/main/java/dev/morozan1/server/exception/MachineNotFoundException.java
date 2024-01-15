package dev.morozan1.server.exception;

public class MachineNotFoundException extends RuntimeException {
    public MachineNotFoundException(Long id) {
        super("Could not find machine " + id);
    }
}