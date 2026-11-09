package me.darragh.jcvipe;

import lombok.RequiredArgsConstructor;
import me.darragh.jcvipe.extension.ExtensionCollection;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Stack;

/**
 * The "orchestrator" for the COSMAC VIP emulator.
 * <br/>
 * Instead of operating with shorts, we operate with integers.
 * This is because the COSMAC VIP used unsigned integers,
 * whereas short for instance is bound to [-32768,32767].
 *
 * @author darraghd493
 * @since 0.0.1
 */
@RequiredArgsConstructor
public final class Emulator {
    //region Constants
    //region Memory Locations
    private static final int INTERPRETER_MEMORY_LOCATION_FROM = 0x000;
    private static final int INTERPRETER_MEMORY_LOCATION_TO = 0x1FF;

    private static final int FONT_MEMORY_LOCATION_FROM = 0x050;
    private static final int FONT_MEMORY_LOCATION_TO = 0x09F;

    private static final int PROGRAM_MEMORY_LOCATION_FROM = 0x200;
    //endregion
    //region Font Data
    private static final int[] FONT_DATA = new int[] {
            0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
            0x20, 0x60, 0x20, 0x20, 0x70, // 1
            0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
            0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
            0x90, 0x90, 0xF0, 0x10, 0x10, // 4
            0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
            0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
            0xF0, 0x10, 0x20, 0x40, 0x40, // 7
            0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
            0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
            0xF0, 0x90, 0xF0, 0x90, 0x90, // A
            0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
            0xF0, 0x80, 0x80, 0x80, 0xF0, // C
            0xE0, 0x90, 0x90, 0x90, 0xE0, // D
            0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
            0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };
    //endregion
    //endregion

    private final ExtensionCollection extensions;

    //region Machine State
    private final int[] memory /* 16-bits */;
    private final Stack<Integer> stack; // expected to support up to 12 successive subroutine calls
    private int indexRegister /* 16-bits */, programCounter /* 12-bits */;

    // each timer is updates @ 60hz
    private int soundTimerRegister /* 16-bits */, delayTimerRegister /* 16-bits */;
    //endregion

    private boolean running;

    public Emulator(
            @NotNull ExtensionCollection extensions,
            @MagicConstant(intValues = {
                    (2 * 1024) / 16, // 2kb
                    (4 * 1024) / 16, // 4kb
                    (32 * 1024) / 16 // 32kb
            }) int allocatedMemory
    ) {
        this.extensions = extensions;

        // prepare memory fields
        this.memory = new int[allocatedMemory];
        this.stack = new Stack<>();
        this.indexRegister = 0;
        this.programCounter = 0;
        this.soundTimerRegister = 0;
        this.delayTimerRegister = 0;
        this.running = false;

        // load the font into memory
        System.arraycopy(FONT_DATA, 0, this.memory, FONT_MEMORY_LOCATION_FROM, FONT_DATA.length);
    }

    //region Operations
    public void reset() {
        this.stack.clear();
        this.indexRegister = 0;
        this.programCounter = 0;
        this.soundTimerRegister = 0;
        this.delayTimerRegister = 0;

        Arrays.fill(this.memory, 0);
        System.arraycopy(FONT_DATA, 0, this.memory, FONT_MEMORY_LOCATION_FROM, FONT_DATA.length);

        this.running = false;
    }

    public void loadProgram(int[] program) {
        this.reset();
        System.arraycopy(program, 0, this.memory, PROGRAM_MEMORY_LOCATION_FROM, program.length);
    }
    //endregion

    //region Machine
    public void tick() {

    }
    //endregion
}
