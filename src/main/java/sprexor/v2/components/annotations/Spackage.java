package sprexor.v2.components.annotations;

import sprexor.v2.components.SCommand;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Spackage {
	public String description() default "no description";
	public String packageName();
	public Class<? extends SCommand>[] ref();
}
