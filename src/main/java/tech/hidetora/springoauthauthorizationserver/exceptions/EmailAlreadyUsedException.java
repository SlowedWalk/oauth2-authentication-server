package tech.hidetora.springoauthauthorizationserver.exceptions;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException() {
        super("Email is already in use!");
    }
}
