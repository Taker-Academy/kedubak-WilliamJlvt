package fr.william.kedubak.routes.post;

import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.token.TokenManager;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import io.javalin.http.Context;
import org.bson.Document;
import org.bson.types.ObjectId;

public class DeletePostRoute implements KedubakRoute {

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

        String postId = ctx.pathParam("id");
        Document query = new Document("_id", new ObjectId(postId));
        Document foundPost = MongoDBConnection.getDatabase().getCollection(POSTS_COLLECTION).findOneAndDelete(query);

        if (foundPost == null) {
            ctx.status(404).json(new ErrorResponse("Post not found"));
            return;
        }

        ctx.status(200).json(new SuccessResponse(new Document()
                .append("removed", true)
                .append("email", foundPost.getString("email"))
                .append("firstName", foundPost.getString("firstName"))
                .append("lastName", foundPost.getString("lastName"))));
    }
}
