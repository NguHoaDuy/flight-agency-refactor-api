package vn.nhd.flightagency.account.annotation;

import vn.nhd.flightagency.account.validation.UniqueEmailValidator;
import vn.nhd.flightagency.account.validation.UniqueUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserValidator.class)
public @interface UniqueUser {
    String message() default "username is already signed up.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
