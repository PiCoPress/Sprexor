package sprexor.v3.components.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * name()
 * version()
 * targetVersion()
 */
public @interface CommandInfo {
	public String name();
	public String version() default "0.0.1";
	public String targetVersion() default "3.0.0";
}
