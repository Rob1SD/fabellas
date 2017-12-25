package groupe_9.com.fabellas.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by thoma on 25/12/2017.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityAnnotation
{
    String title();

    int layout();

    boolean isBackEnable() default false;
}
