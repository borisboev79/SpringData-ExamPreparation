package softuni.exam.util;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtilImpl implements ValidatorUtil {

    @Autowired
    private final Validator validator;

    @Autowired
    public ValidatorUtilImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public <E> boolean isValid(E entity) {
        try {
            return this.validator.validate(entity).isEmpty();
        } catch (Exception exception) {
            throw new NotYetImplementedException("Not implemented");
        }
    }
    @Override
    public <E> Set<ConstraintViolation<E>> violations(E entity) {
        try{
            return this.validator.validate(entity);
        } catch (Exception exception) {
            throw new NotYetImplementedException("Not implemented");
        }
    }


}
