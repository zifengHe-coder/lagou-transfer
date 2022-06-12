package com.lagou.edu.anno;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
@Inherited //可继承
public @interface MyService {
    String value() default "";
}
