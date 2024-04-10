package fr.william.kedubak;

import io.javalin.http.Context;

public interface KedubakRoute {

    void handle(Context ctx);

}
