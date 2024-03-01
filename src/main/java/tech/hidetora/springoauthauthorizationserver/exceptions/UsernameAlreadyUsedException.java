package tech.hidetora.springoauthauthorizationserver.exceptions;

public class UsernameAlreadyUsedException extends RuntimeException {
    public UsernameAlreadyUsedException() {
        super("Login name already used!");
    }
}
