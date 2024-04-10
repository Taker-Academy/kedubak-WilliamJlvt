package fr.william.kedubak.routes;

import fr.william.kedubak.KedubakRoute;
import io.javalin.http.Context;

public class HelloWorldRoute implements KedubakRoute {

    @Override
    public void handle(Context ctx) {
        ctx.result("Hello World \uD83C\uDF0D");
    }

}
