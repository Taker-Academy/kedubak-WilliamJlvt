package fr.william.kedubak.routes.post;

import com.google.gson.Gson;
import com.mongodb.client.MongoDatabase;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import fr.william.kedubak.routes.user.GetUserProfileRoute;
import fr.william.kedubak.token.TokenManager;
import io.javalin.http.Context;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

public class CreatePostRoute implements KedubakRoute {

    private static final String POSTS_COLLECTION = "posts";

    class RequestPost {
        String title;
        String content;
    }

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

        RequestPost requestPost = new Gson().fromJson(ctx.body(), RequestPost.class);

        if (requestPost.title == null || requestPost.content == null) {
            ctx.status(400).json(new ErrorResponse("Missing title or content"));
            return;
        }

        Document user = GetUserProfileRoute.getUserById(userId);
        Post post = new Post(userId, user.getString("firstName"), requestPost.title, requestPost.content);
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection(POSTS_COLLECTION);
        collection.insertOne(post.toDocument());
        // set post id to the last inserted post id in the collection
        post.set_id(collection.find().sort(new Document("createdAt", -1)).first().getObjectId("_id").toString());
        ctx.status(201).json(new SuccessResponse(post.toDocumentWithId()));
    }
}
