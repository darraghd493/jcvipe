package me.darragh.jcvipe.extension.input;

import org.intellij.lang.annotations.MagicConstant;

/**
 * @author darraghd493
 * @since 0.0.1
 */
public interface InputExtension {
    boolean isPressed(@MagicConstant(intValues = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'
    }) char key);
}
