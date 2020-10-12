package Domain;

import Presentation.Checker;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {
    private static final String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
    private static final String upperCaseChars = lowerCaseChars.toUpperCase();
    private static final String numbers = "0123456789";
    private static final String passwordAllowedBase = lowerCaseChars + upperCaseChars + numbers;
    private static final String passwordAllow = shuffleString(passwordAllowedBase);
    private static final SecureRandom random = new SecureRandom();



    private static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }

    public static String generateRandPassword(int length) {
        String password;
        StringBuilder sb;
        do{
            password = "";
            sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                int rndCharAt = random.nextInt(passwordAllow.length());
                char rndChar = passwordAllow.charAt(rndCharAt);
                sb.append(rndChar);
            }
            password = sb.toString();
        }
        while(!Checker.isValidPassword(password));

        return password;
    }

}
