package fr.william.kedubak.routes.post;

import com.mongodb.client.MongoCursor;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.token.TokenManager;
import io.javalin.http.Context;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class GetUserPostsRoute implements KedubakRoute {

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
                Document post = cursor.next();
                if (!post.getString("userId").equals(userId)) {
                    continue;
                }
                post.append("_id", post.getObjectId("_id").toString());
                posts.add(post);
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
