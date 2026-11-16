package me.darragh.jcvipe.desktop.configuration;

import lombok.Data;
import org.intellij.lang.annotations.MagicConstant;

/**
 * @author darraghd493
 * @since 0.0.1
 */
@Data
public final class Configuration {
    private Bindings bindings;

    public record Bindings(
            char in1, char in2, char in3, char inC,
            char in4, char in5, char in6, char inD,
            char in7, char in8, char in9, char inE,
            char inA, char in0, char inB, char inF
    ) {
        public static final Bindings ORIGINAL_BINDINGS = new Bindings(
                '1', '2', '3', 'C',
                '4', '5', '6', 'D',
                '7', '8', '9', 'E',
                'A', '0', 'B', 'F'
        );
        public static final Bindings QWERTY_BINDINGS = new Bindings(
                '1', '2', '3', '4',
                'Q', 'W', 'E', 'R',
                'A', 'S', 'D', 'F',
                'Z', 'X', 'C', 'V'
        );

        public char map(@MagicConstant(intValues = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'
        }) char key) {
            return switch (key) {
                case '1' -> this.in1;
                case '2' -> this.in2;
                case '3' -> this.in3;
                case 'C' -> this.inC;
                case '4' -> this.in4;
                case '5' -> this.in5;
                case '6' -> this.in6;
                case 'D' -> this.inD;
                case '7' -> this.in7;
                case '8' -> this.in8;
                case '9' -> this.in9;
                case 'E' -> this.inE;
                case 'A' -> this.inA;
                case '0' -> this.in0;
                case 'B' -> this.inB;
                case 'F' -> this.inF;
                default -> throw new IllegalArgumentException("Invalid key: " + key);
            };
        }
    }
}
