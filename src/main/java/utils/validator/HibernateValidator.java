package utils.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.Logger;

import utils.HibernateUtil;

public class HibernateValidator {
    protected static final Logger log = (Logger) LogManager.getLogger(HibernateUtil.class);

    protected static final Marker marker = MarkerManager.getMarker("CLASS");

    private static boolean status;

    private static String error = "";

    static ValidatorFactory factory;

    static Validator validator;

    public HibernateValidator() {
        status = false;
    }

    public static void buildValid() {
        log.debug(marker, "Setting up Validation factory");
        factory = Validation.buildDefaultValidatorFactory();
    }

    public static void closeValid() {
        log.debug(marker, "Closing Validation factory");
        factory.close();
    }

    public static <T> boolean validate(T object) {
        // validator
        log.debug(marker, "Validating Class");
        validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations;
        constraintViolations = validator.validate(object);

        if (!constraintViolations.isEmpty()) {
            error = constraintViolations.stream().map((constraintViolation) -> constraintViolation.getMessage() + "\n")
                    .reduce(error, String::concat);
            status = false;
        } else
            status = true;
        return status;
    }

    public static String getError() {
        return error;
    }

    public static void resetError() {
        error = "";
    }
}
