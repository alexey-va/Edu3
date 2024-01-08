package org.example.other.javafx;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {


    @Test
    void test() {

        int[][] res = Block.rotate90Degrees(new int[][]{
                {1, 1, 1},
                {0, 1, 0}
        });

        assertArrayEquals(new int[][]{
                {0, 1},
                {1, 1},
                {0, 1}
        }, res);

        res = Block.rotate90Degrees(new int[][]{
                {1, 1, 1},
                {1, 0, 0}
        });

        assertArrayEquals(new int[][]{
                {1, 1},
                {0, 1},
                {0, 1}
        }, res);

        res = Block.rotate90Degrees(new int[][]{
                {1, 1, 1},
                {0, 0, 1}
        });

        assertArrayEquals(new int[][]{
                {0, 1},
                {0, 1},
                {1, 1}
        }, res);

        res = Block.rotate90Degrees(new int[][]{
                {1, 1, 1, 1}
        });

        assertArrayEquals(new int[][]{
                {1},
                {1},
                {1},
                {1}
        }, res);

        res = Block.rotate90Degrees(new int[][]{
                {1, 1},
                {1, 1}
        });

        assertArrayEquals(new int[][]{
                {1, 1},
                {1, 1}
        }, res);

        res = Block.rotate90Degrees(new int[][]{
                {1, 1, 1},
                {1, 0, 1}
        });

        assertArrayEquals(new int[][]{
                {1, 1},
                {0, 1},
                {1, 1}
        }, res);

        res = Block.rotate90Degrees(new int[][]{
                {1, 1, 1},
                {1, 1, 0}
        });

        assertArrayEquals(new int[][]{
                {1, 1},
                {1, 1},
                {0, 1}
        }, res);
    }

}