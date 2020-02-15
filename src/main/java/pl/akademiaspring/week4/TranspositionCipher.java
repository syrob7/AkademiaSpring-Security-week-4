package pl.akademiaspring.week4;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TranspositionCipher implements PasswordEncoder {

    private static final char [] KEY = {'6', '7', '2', '3', '1', '5', '4'};

    @Override
    public String encode(CharSequence rawPassword) {
        return encodePassword(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String password = encodePassword(rawPassword);

        return password.equals(encodedPassword);
    }

    private String encodePassword(CharSequence rawPassword) {
        int size = BigDecimal.valueOf(rawPassword.length()/(1.0 * KEY.length))
                .setScale(0, RoundingMode.UP).intValue() + 1;
        char [][] table = new char[size] [KEY.length];

        //build cipher table
        table[0] = KEY;
        for (int row = 1, inx = 0; row < table.length; row++) {
            for (int col = 0; col < table[row].length; col++) {
                if (inx < rawPassword.length()) {
                    table[row][col] = rawPassword.charAt(inx++);
                } else {
                    table[row][col] = '#';
                }
            }
        }

        return buildPassword(table);
    }

    private String buildPassword(char[][] table) {
        char [][] tableSwaped = new char [KEY.length][table.length];
        StringBuilder stringBuilder = new StringBuilder();

        //swap table - columns and rows
        for (int row = 0; row < table.length; row++) {
            for (int col = 0; col < table[row].length; col++) {
                tableSwaped[col][row] = table[row][col];
            }
        }

        //build and sort list
        List<String> sortedList = Stream.of(tableSwaped)
                .map(e -> {
                    StringBuilder stringBuilder1 = new StringBuilder();
                    return stringBuilder1.append(e).toString();
                    })
                .sorted()
                .map(e -> e.substring(1).toLowerCase().replace(' ', '#'))
                .collect(Collectors.toList());

        //combine all list elements to build password
        sortedList.stream()
                .forEach(e -> stringBuilder.append(e));

        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        TranspositionCipher cipher = new TranspositionCipher();

        String encodedpassword = cipher.encode("To jest haslo do zaszyfrowania");

        boolean matches = cipher.matches("To jest haslo do zaszyfrowania", "elaw##a#r#jszo#t#zn#sosa#t#dyiohofa");
        System.out.println(encodedpassword);
        System.out.println(matches);
    }
}
