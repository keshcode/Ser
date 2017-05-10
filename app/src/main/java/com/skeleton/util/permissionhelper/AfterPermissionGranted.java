package com.skeleton.util.permissionhelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Developer: Saurabh Verma
 * Dated: 08/03/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterPermissionGranted {

    /**
     * Value int.
     *
     * @return
     */
    int value();

}
