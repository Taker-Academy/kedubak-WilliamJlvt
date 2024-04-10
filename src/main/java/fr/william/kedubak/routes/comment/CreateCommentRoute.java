package fr.william.kedubak.routes.comment;

import com.google.gson.Gson;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import fr.william.kedubak.routes.post.Comment;
import fr.william.kedubak.routes.post.GetPostByIdRoute;
import fr.william.kedubak.routes.post.Post;
import io.javalin.http.Context;
import org.bson.Document;
import org.bson.types.ObjectId;

public class CreateCommentRoute implements KedubakRoute {

    private static final String COMMENTS_COLLECTION = "comments";

    private class CommentRequest {
        String content;
    }

    @Override
    public void handle(Context ctx) {
        CommentRequest commentRequest = new Gson().fromJson(ctx.body(), CommentRequest.class);

        if (commentRequest.content == null || commentRequest.content.isEmpty()) {
            ctx.status(400).json(new ErrorResponse("Content must be provided"));
            return;
        }

        if (ctx.pathParam("id") == null) {
            ctx.status(400).json(new ErrorResponse("Id must be provided"));
            return;
        }

        String id = ctx.pathParam("id");

        Document postDocument = GetPostByIdRoute.getPostById(id);
        if (postDocument == null) {
            ctx.status(404).json(new ErrorResponse("Post not found"));
            return;
        }

        Post post = Post.fromDocument(postDocument);
        Comment co = new Comment("comment-" + new ObjectId(), "William", commentRequest.content);
        post.getComments().add(co);
        System.out.println(post.toDocument());
        MongoDBConnection.getDatabase().getCollection("posts").updateOne(new Document("_id", new ObjectId(id)), new Document("$set", post.toDocument()));

        ctx.status(201).json(new SuccessResponse(co.toDocument()));
    }
}
