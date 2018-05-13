/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package graphics.gui;

import org.lwjgl.*;
import org.lwjgl.nuklear.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.*;

/**
 * Java port of
 * <a href="https://github.com/vurtun/nuklear/blob/master/demo/glfw_opengl3/main.c">https://github.com/vurtun/nuklear/blob/master/demo/glfw_opengl3/main.c</a>.
 */
class Demo {

    private static final int EASY = 0;
    private static final int HARD = 1;

    private int op = EASY;

    private IntBuffer compression = BufferUtils.createIntBuffer(1).put(0, 20);

    Demo() {
    }

    void layout(NkContext ctx, int x, int y) {
        try (MemoryStack stack = stackPush()) {
            NkRect rect = NkRect.mallocStack(stack);

            if (nk_begin(
                    ctx,
                    "Demo",
                    nk_rect(x, y, 230, 250, rect),
                    NK_WINDOW_BORDER | NK_WINDOW_MOVABLE | NK_WINDOW_SCALABLE | NK_WINDOW_MINIMIZABLE | NK_WINDOW_TITLE
            )) {
                nk_layout_row_static(ctx, 30, 80, 1);
                if (nk_button_label(ctx, "button")) {
                    System.out.println("button pressed");
                }

                nk_layout_row_dynamic(ctx, 30, 2);
                if (nk_option_label(ctx, "easy", op == EASY)) {
                    op = EASY;
                }
                if (nk_option_label(ctx, "hard", op == HARD)) {
                    op = HARD;
                }

                nk_layout_row_dynamic(ctx, 25, 1);
                nk_property_int(ctx, "Compression:", 0, compression, 100, 10, 1);

                nk_layout_row_dynamic(ctx, 20, 1);
                nk_label(ctx, "background:", NK_TEXT_LEFT);
                nk_layout_row_dynamic(ctx, 25, 1);

            }
            nk_end(ctx);
        }
    }

}