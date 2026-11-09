package me.darragh.jcvipe.desktop.configuration;

import lombok.Data;

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
    }
}
