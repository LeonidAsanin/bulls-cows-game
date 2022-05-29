package org.leonidasanin.bullscowsgame;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Servlet Initializer for proper running into Tomcat servlet-container.
 *
 * @since 1.0.0
 * @author Leonid Asanin
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BullsAndCowsGameApplication.class);
	}

}
