package com.terturl.MMO.Framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface CommandMeta {

	/**
	 * Can be used to show what the command does when doing /help
	 * @return
	 */
	String description() default "";

	/**
	 * Can be used to have shortcuts to commands (/TestCommand -> /tc)
	 * @return
	 */
	String[] aliases() default {};

	/**
	 * Can be used to show how you'd perform the command (/{command} {player} {ban/kick})
	 * @return
	 */
	String usage() default "";
}
