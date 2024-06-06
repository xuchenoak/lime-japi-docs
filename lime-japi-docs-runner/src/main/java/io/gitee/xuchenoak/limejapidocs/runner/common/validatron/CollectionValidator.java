package io.gitee.xuchenoak.limejapidocs.runner.common.validatron;

import io.gitee.xuchenoak.limejapidocs.runner.util.ListUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class CollectionValidator implements ConstraintValidator<CollectionValid, Collection<?>> {

    @Override
    public boolean isValid(Collection<?> v, ConstraintValidatorContext context) {
        return ListUtils.isNotBlank(v);
    }

}
