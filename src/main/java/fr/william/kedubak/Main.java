package fr.william.kedubak;

import com.google.gson.GsonBuilder;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.token.TokenManager;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.eclipse.jetty.http.HttpMethod;

public class Main {

    public static void main(String[] args) {
        MongoDBConnection.connect();
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(CorsPluginConfig.CorsRule::anyHost);
            });
        });

        for (KedubakRoutes route : KedubakRoutes.values())
            route.register(app);

        app.events(event -> {
            event.serverStopping(MongoDBConnection::close);
        });
        app.start(8080);
    }
}
