package tech.hidetora.springoauthauthorizationserver.exceptions;

public class AuthorityNotFoundException extends RuntimeException {
    public AuthorityNotFoundException() {
        super("Authority not found!");
    }

    public AuthorityNotFoundException(String message) {
        super(message);
    }
}
