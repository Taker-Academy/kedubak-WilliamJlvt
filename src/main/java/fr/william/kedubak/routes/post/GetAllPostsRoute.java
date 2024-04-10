package fr.william.kedubak.routes.post;

import com.google.gson.Gson;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.token.TokenManager;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import io.javalin.http.Context;
import org.bson.Document;
import com.mongodb.client.MongoCursor;

import java.util.ArrayList;
import java.util.List;

public class GetAllPostsRoute implements KedubakRoute {

    private static final String POSTS_COLLECTION = "posts";

    @Override
    public void handle(Context ctx) {
        String token = ctx.header("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            ctx.status(401).json(new ErrorResponse("Invalid token"));
            return;
        }

        String userId = TokenManager.getUserIdFromToken(token.substring(7));

        if (userId == null) {
            ctx.status(401).json(new ErrorResponse("Invalid token"));
            return;
        }

        MongoCursor<Document> cursor = MongoDBConnection.getDatabase().getCollection(POSTS_COLLECTION).find().iterator();

        List<Document> posts = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                posts.add(cursor.next());
            }
        } finally {
            cursor.close();
        }

        Document response = new Document()
                .append("ok", true)
                .append("data", posts);

        ctx.status(200).json(response);
    }
}
