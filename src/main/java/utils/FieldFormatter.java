package utils;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.converter.DoubleStringConverter;

public class FieldFormatter {
    private static Pattern integerText = Pattern.compile(PatternFormat.DECIMAL.getValue());

    private static Pattern doubleText = Pattern.compile(PatternFormat.FLOAT.getValue());

    private TextFormatter<String> integer;

    private TextFormatter<Double> floatPoint;

    public FieldFormatter() {
        super();
    }

    public TextFormatter<Double> getFloatPoint() {
        return floatPoint;
    }

    public void setFloatPoint() {
        floatPoint = new TextFormatter<Double>(new DoubleStringConverter(), 0.0, change -> {
            String newText = change.getControlNewText();
            if (doubleText.matcher(newText).matches())
                return change;
            else
                return null;
        });
    }

    public TextFormatter<String> getInteger() {
        return integer;
    }

    public void setInteger() {
        integer = new TextFormatter<>((UnaryOperator<Change>) change -> {
            String text = change.getText();

            if (integerText.matcher(text).matches())
                return change;

            return null;
        });
    }
}
