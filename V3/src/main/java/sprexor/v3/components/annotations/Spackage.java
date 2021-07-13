package sprexor.v3.components.annotations;

import sprexor.v3.components.SCommand;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented

/**
 * description()
 * packageName()
 * ref()
 */
public @interface Spackage {
	public String description() default "no description";
	public String packageName();
	public Class<? extends SCommand>[] ref();
}
