package tech.hidetora.springoauthauthorizationserver.utils;

import lombok.NoArgsConstructor;

/**
 * @author hidetora
 * @version 1.0.0
 * @since 2022/04/18
 */
@NoArgsConstructor
public class Constants {
    /**
     * Application constants.
     */

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "https://hidetora.tech";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    public static final String API_V1_AUTH = "/api/v1/auth";
    public static final String API_V1_USER = "/api/v1/users";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String ACTIVATE = "/activate";
    public static final String AUTHENTICATE = "/authenticate";
    public static final String AUTHORITIES = "/authorities";
    public static final String AUTHORITIES_KEY = "authorities";
//    public static final String CLIENT_REDIRECT_URI ="http://127.0.0.1:4200/login/oauth2";
    public static final String CLIENT_REDIRECT_URI ="http://127.0.0.1:80/login/oauth2/code";
}
