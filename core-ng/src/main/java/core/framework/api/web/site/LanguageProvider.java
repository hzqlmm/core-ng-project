package core.framework.api.web.site;

import core.framework.api.web.Request;

import java.util.Optional;

/**
 * @author neo
 */
@FunctionalInterface
public interface LanguageProvider {
    Optional<String> get(Request request);
}
