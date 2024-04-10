package fr.william.kedubak.routes.post;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import fr.william.kedubak.token.TokenManager;
import io.javalin.http.Context;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class VotePostRoute implements KedubakRoute {

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

        String id = ctx.pathParam("id");
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection(POSTS_COLLECTION);

        Document post = collection.find(new Document("_id", new ObjectId(id))).first();

        if (post == null) {
            ctx.status(404).json(new ErrorResponse("Post not found"));
            return;
        }

        ArrayList<String> upVotes = (ArrayList<String>) post.get("upVotes");

        if (upVotes.contains(userId)) {
            ctx.status(409).json(new ErrorResponse("You have already voted for this post"));
            return;
        }

        upVotes.add(userId);
        collection.updateOne(new Document("_id", new ObjectId(id)), new Document("$set", new Document("upVotes", upVotes)));

        Document response = new Document().append("ok", true).append("message", "Post upvoted");
        ctx.status(200).json(new SuccessResponse(response));
    }
}
