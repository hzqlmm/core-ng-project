package app.web;

import core.framework.api.web.Request;
import core.framework.api.web.Response;

/**
 * @author neo
 */
public class WildcardController {
    public Response wildcard(Request request) {
        return Response.text("catch all url", "text/html");
    }
}
