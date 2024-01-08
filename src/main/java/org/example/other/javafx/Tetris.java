package org.example.other.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Log4j2
public class Tetris implements Initializable {

    @FXML
    Canvas canvas;
    @FXML
    Text textTetris;
    @FXML
    Text scoreText;
    @FXML
    Text levelText;
    @FXML
    Canvas rightPane;
    Color bgColor = Color.rgb(39, 47, 50);
    Color fgColor = Color.rgb(38, 57, 61);

    @Setter
    Scene scene;

    int rows;
    int cols;

    Field field;
    Block block;
    Block nextBlock;

    int blockWidth = 30;
    int counter = 0;
    boolean shiftPressed = false;
    boolean paused = false;
    boolean gameOver = false;

    int score = 0;
    int level = 1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.rows = (int) (canvas.getHeight() / blockWidth);
        this.cols = (int) (canvas.getWidth() / blockWidth);

        this.field = new Field(rows, cols);

        placeNewBlock();
        draw();
        setupTimeline();
        textTetris.setVisible(false);
    }

    private void setupTimeline() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(5), event -> {
                    if (paused || gameOver) return;
                    counter++;

                    if ((shiftPressed && counter % Math.min((100 - level), 10) == 0) || counter % (100 - level) == 0) {
                        doStep();
                        draw();
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    void setupKeyHandlers() {
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.LEFT) {
            if (field.fit(block, block.x - 1, block.y)) {
                block.x--;
                draw();
            }
        } else if (event.getCode() == KeyCode.RIGHT) {
            if (field.fit(block, block.x + 1, block.y)) {
                block.x++;
                draw();
            }
        } else if (event.getCode() == KeyCode.DOWN) {
            shiftPressed = true;
        } else if (event.getCode() == KeyCode.ESCAPE) {
            paused = !paused;
            textTetris.setVisible(paused || gameOver);
            textTetris.setText(paused ? "PAUSED" : "GAME OVER");
        } else if (event.getCode() == KeyCode.SPACE) {
            Block rotated = block.getRotated();
            if (field.fit(rotated, block.x, block.y)) {
                block = rotated;
                draw();
            } else if (field.fit(rotated, block.x + 1, block.y)) {
                block = rotated;
                block.x++;
                draw();
            } else if (field.fit(rotated, block.x - 1, block.y)) {
                block = rotated;
                block.x--;
                draw();
            }
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.DOWN) {
            shiftPressed = false;
        }
    }

    private void placeNewBlock() {
        block = nextBlock == null ? Block.generate() : nextBlock;
        nextBlock = Block.generate();
        List<Integer> places = IntStream.range(0, cols).boxed().toList();
        while (!places.isEmpty()) {
            int x = places.get(new Random().nextInt(places.size()));
            if (field.fit(block, x, 0)) {
                block.x = x;
                return;
            }
            places = places.stream().filter(i -> i != x).toList();
        }

    }

    private void draw() {
        drawBg(canvas.getGraphicsContext2D());
        drawBgColor(rightPane.getGraphicsContext2D());
        drawBlock(canvas.getGraphicsContext2D(), block);
        drawNextBlock();
    }

    private void doStep() {
        counter = 0;
        if (field.fit(block, block.x, block.y + 1)) {
            block.y++;

        } else {
            field.placeBlock(block);
            placeNewBlock();
            if (!field.fit(block, block.x, block.y)) {
                gameOver = true;
                textTetris.setVisible(true);
                textTetris.setText("GAME OVER");
                return;
            }
            List<Integer> toRemove = field.clearRows();
            score += toRemove.size() * 100;
            scoreText.setText("Score " + score);
            if (level != (score / 1000 + 1)) {
                level = score / 1000 + 1;
                levelText.setText("Level " + level);
                if (level == 100) winGame();
            }
        }
    }

    private void winGame() {
        gameOver = true;
        textTetris.setVisible(true);
        textTetris.setText("YOU WON");
    }


    private void drawBg(GraphicsContext gc) {
        drawBgColor(gc);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (field.grid[i][j] == null)
                    drawBlock(gc, j * blockWidth, i * blockWidth, blockWidth, fgColor);
                else
                    drawBlock(gc, j * blockWidth, i * blockWidth, blockWidth, field.grid[i][j]);
            }
        }
    }

    private void drawBgColor(GraphicsContext gc) {
        gc.setFill(bgColor);
        gc.fillRect(0, 0, 1000, 1000);
    }

    private void drawBlock(GraphicsContext gc, Block block) {
        drawBlockProjection(gc, block);

        for (int i = 0; i < block.grid.length; i++) {
            for (int j = 0; j < block.grid[0].length; j++) {
                if (block.grid[i][j] != 0) {
                    drawBlock(gc, (block.x + j) * blockWidth, (block.y + i) * blockWidth, blockWidth, block.color);
                }
            }
        }
    }

    private void drawBlockProjection(GraphicsContext gc, Block block) {
        gc.setFill(Color.BLUE.deriveColor(0.0, 0.7, 2.0, 1.0));
        int maxY = this.block.y;

        while (field.fit(this.block, this.block.x, maxY + 1)) {
            maxY++;
        }

        int yStart = (block.y+block.grid.length) * blockWidth;
        int yEnd = maxY * blockWidth;
        int xStart = block.x * blockWidth;
        int xEnd = (block.x + block.grid[0].length) * blockWidth;
        Color projectionColor = block.color.deriveColor(0.0, 1.0, 1.5, 0.5);

        //gc.fillRect(xStart, yStart, xEnd - xStart, 1);
        //gc.fillRect(xStart, yEnd, xEnd - xStart, 1);
        gc.fillRect(xStart, yStart, 1, yEnd - yStart);
        gc.fillRect(xEnd, yStart, 1, yEnd - yStart);

        for (int i = 0; i < block.grid.length; i++) {
            for (int j = 0; j < block.grid[0].length; j++) {
                if (block.grid[i][j] != 0) {
                    drawBlock(gc, (block.x + j) * blockWidth, (maxY + i) * blockWidth, blockWidth, projectionColor);
                }
            }
        }
    }

    private void drawBlock(GraphicsContext gc, int x, int y, int width, Color bg) {
        gc.setFill(bg);
        gc.fillRect(x + 2, y + 2, width - 4, width - 4);
        Color accent1 = bg.brighter();
        Color accent2 = accent1.brighter();
        gc.setFill(accent1);
        gc.fillRect(x + 2, y + 3, width - 4, 1);
        gc.setFill(accent2);
        gc.fillRect(x + 2, y + 2, width - 4, 1);
    }

    private void drawNextBlock() {
        GraphicsContext gc = rightPane.getGraphicsContext2D();
        gc.setFill(Color.GRAY);
        gc.setLineWidth(1);

        int x1 = 9;
        int y1 = 149;

        int myBlockWidth = 30;


        gc.fillRect(x1, y1, myBlockWidth * 4 + 20, myBlockWidth * 2 + 20);
        gc.setFill(bgColor);
        gc.fillRect(x1 + 2, y1 + 2, myBlockWidth * 4 + 16, myBlockWidth * 2 + 16);

        int shiftX = (4 - nextBlock.grid[0].length) * myBlockWidth / 2;
        int shiftY = (2 - nextBlock.grid.length) * myBlockWidth / 2;

        for (int i = 0; i < nextBlock.grid.length; i++) {
            for (int j = 0; j < nextBlock.grid[0].length; j++) {
                if (nextBlock.grid[i][j] != 0) {
                    drawBlock(gc, shiftX + 10 + x1 + j * myBlockWidth, shiftY + y1 + 10 + i * myBlockWidth, myBlockWidth, nextBlock.color);
                }
            }
        }
    }
}

@Log4j2
class Field {
    Color[][] grid;


    public Field(int rows, int cols) {
        this.grid = new Color[rows][cols];
    }

    public void placeBlock(Block block) {
        for (int i = 0; i < block.grid.length; i++) {
            for (int j = 0; j < block.grid[0].length; j++) {
                if (block.grid[i][j] != 0) {
                    grid[block.y + i][block.x + j] = block.color;
                }
            }
        }
    }

    public void removeRow(int row) {
        for (int i = 0; i < grid[0].length; i++) {
            grid[row][i] = null;
        }
    }

    public void moveRowsDown(int row) {
        for (int i = row; i > 0; i--) {
            System.arraycopy(grid[i - 1], 0, grid[i], 0, grid[0].length);
        }
        Arrays.fill(grid[0], null);
    }

    public List<Integer> clearRows() {
        List<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            boolean allFilled = true;
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == null) {
                    allFilled = false;
                    break;
                }
            }
            if (allFilled) toRemove.add(i);
        }


        for (int a : toRemove) {
            removeRow(a);
            moveRowsDown(a);
        }

        return toRemove;
    }

    public boolean fit(Block block, int x, int y) {
        int xMax = x + block.grid[0].length;
        int yMax = y + block.grid.length;

        if (x < 0 || y < 0)
            return false;

        for (int i = x; i < xMax; i++) {
            for (int j = y; j < yMax; j++) {
                if (block.grid[j - y][i - x] == 0) continue;
                if (i >= grid[0].length || j >= grid.length || grid[j][i] != null) return false;
            }
        }

        return true;
    }
}

@AllArgsConstructor
class Block {
    private static final List<Color> colors = List.of(
            Color.BLUE.deriveColor(0.0, 0.8, 0.8, 1.0),
            Color.RED.deriveColor(0.0, 1.0, 0.8, 1.0),
            Color.GREEN.deriveColor(0.0, 1.0, 0.8, 1.0),
            Color.ORANGE.deriveColor(0.0, 1.0, 0.8, 1.0));

    int[][] grid;
    Color color;
    int x = 0, y = 0;

    public Block(int id, Color color) {
        this.grid = generateById(id);
        this.color = color;
    }

    public static Block generate() {
        return new Block(new Random().nextInt(0, 5), colors.get(new Random().nextInt(0, colors.size())));
    }

    public Block getRotated() {
        return new Block(rotate90Degrees(grid), color, x, y);
    }

    public static int[][] rotate90Degrees(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // Create a new array to store the rotated elements
        int[][] rotatedMatrix = new int[cols][rows];

        // Populate the rotated matrix
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                rotatedMatrix[i][j] = matrix[rows - j - 1][i];
            }
        }

        return rotatedMatrix;

    }

    private int[][] generateById(int id) {
        switch (id) {
            case 0 -> {
                return new int[][]{
                        {1, 1},
                        {1, 1}
                };
            }
            case 1 -> {
                return new int[][]{
                        {1, 1, 1},
                        {0, 1, 0}
                };
            }
            case 2 -> {
                return new int[][]{
                        {1, 1, 1},
                        {1, 0, 0}
                };
            }
            case 3 -> {
                return new int[][]{
                        {1, 1, 1},
                        {0, 0, 1}
                };
            }
            case 4 -> {
                return new int[][]{
                        {1, 1, 1, 1}
                };
            }
        }
        return null;
    }
}
