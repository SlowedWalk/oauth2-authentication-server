package tech.hidetora.springoauthauthorizationserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringOauthAuthorizationServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringOauthAuthorizationServerApplication.class, args);
    }

    // steps to generate the pkce code verifier and challenge
    // 1. generate a random string of 43 characters
    // 2. encode the string using base64url encoding
    // 3. remove the padding characters '='
    // 4. replace the characters '-' and '_' with '+' and '/'
    // 5. remove all line breaks
    // 6. generate the sha256 hash of the string
    // 7. encode the hash using base64url encoding
    // 8. remove the padding characters '='
    // 9. replace the characters '-' and '_' with '+' and '/'
    // 10. remove all line breaks
    // 11. the code verifier is the string from step 3
    // 12. the code challenge is the string from step 9

}
