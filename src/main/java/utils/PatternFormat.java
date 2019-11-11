package utils;

public enum PatternFormat {

    DECIMAL {

        @Override
        public String getValue() {
            return "[0-9]*";
        }
    },
    FLOAT {

        @Override
        public String getValue() {
            return "-?((\\d{0,7})|(\\d+\\.\\d{0,4}))";
        }
    };

    public abstract String getValue();

}
