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

    public static Document getPostById(String postId) {
        for (Document doc : MongoDBConnection.getDatabase().getCollection(POSTS_COLLECTION).find()) {
            if (doc.getObjectId("_id").toString().equals(postId)) {
                return doc;
            }
        }
        return null;
    }

    @Override
    public void handle(Context ctx) {
        String id = ctx.pathParam("id");
        Document post = getPostById(id);

        if (post == null) {
            ctx.status(404).json(new ErrorResponse("Post not found"));
            return;
        }

        post.append("_id", post.getObjectId("_id").toString());
        ctx.status(200).json(new SuccessResponse(post));
    }
}
