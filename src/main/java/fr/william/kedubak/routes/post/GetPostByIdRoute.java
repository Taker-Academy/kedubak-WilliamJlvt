package fr.william.kedubak.routes.post;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import io.javalin.http.Context;
import org.bson.Document;
import org.bson.types.ObjectId;

public class GetPostByIdRoute implements KedubakRoute {

    private static final String POSTS_COLLECTION = "posts";

    @Override
    public void handle(Context ctx) {
        String id = ctx.pathParam("id");

        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection(POSTS_COLLECTION);

        Document post = collection.find(new Document("_id", new ObjectId(id))).first();

        if (post == null) {
            ctx.status(404).json(new ErrorResponse("Post not found"));
            return;
        }

        ctx.status(200).json(new SuccessResponse(post));
    }
}
