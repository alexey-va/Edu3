package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.spi.DecimalFormatSymbolsProvider;

@Data
@AllArgsConstructor
public class Point {

    private static final DecimalFormat format = new DecimalFormat("##.#") {{
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        setDecimalFormatSymbols(symbols);
    }};
    double x, y;

    public double distanceTo(Point point) {
        return Math.sqrt((this.x - point.x) * (this.x - point.x) + (this.y - point.y) * (this.y - point.y));
    }

    @Override
    public String toString() {
        return "{" +
                format.format(x) +
                "," + format.format(y) +
                '}';
    }
}
