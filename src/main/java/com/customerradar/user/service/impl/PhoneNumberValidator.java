package com.customerradar.user.service.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.customerradar.user.service.PhoneNumber;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            return true;
        }
        return phoneField.matches("[0-9]*") && phoneField.length() > 8 && phoneField.length() < 14;
    }
    
}
