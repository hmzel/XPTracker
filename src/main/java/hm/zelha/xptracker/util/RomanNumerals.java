package hm.zelha.xptracker.util;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RomanNumerals {
    private static final Map<Character, Integer> numeralMap = new HashMap<>();

    static {
        numeralMap.put('I', 1);
        numeralMap.put('V', 5);
        numeralMap.put('X', 10);
        numeralMap.put('L', 50);
        numeralMap.put('C', 100);
        numeralMap.put('D', 500);
        numeralMap.put('M', 1000);
    }

    private RomanNumerals() {}

    /**
     * Converts the roman numeral into a integer value.
     *
     * @param numeral The roman numeral to convert.
     * @return Integer value of the roman numeral,
     */
    public static int getIntFromNumeral(@Nullable String numeral) {
        if (numeral == null) return 0;

        numeral = numeral.replace("IV", "IIII");
        numeral = numeral.replace("IX", "VIIII");
        numeral = numeral.replace("XL", "XXXX");
        numeral = numeral.replace("XC", "LXXXX");
        numeral = numeral.replace("CD", "CCCC");
        numeral = numeral.replace("CM", "DCCC");
        int number = 0;

        for (char chara : numeral.toCharArray()) {
            number += numeralMap.get(chara);
        }

        return number;
    }
}
