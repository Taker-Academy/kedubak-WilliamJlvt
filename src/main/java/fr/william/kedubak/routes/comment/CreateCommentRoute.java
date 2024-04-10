package fr.william.kedubak.routes.comment;

import com.google.gson.Gson;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import io.javalin.http.Context;
import org.bson.Document;

public class CreateCommentRoute implements KedubakRoute {

    private static final String COMMENTS_COLLECTION = "comments";

    @Override
    public void handle(Context ctx) {
        String commentJson = ctx.body();

        if (comment.content == null) {
            ctx.status(400).json(new ErrorResponse("Content must be provided"));
            return;
        }

        if (ctx.pathParam("id") == null) {
            ctx.status(400).json(new ErrorResponse("Id must be provided"));
            return;
        }

        String id = ctx.pathParam("id");

        Document newComment = new Document("content", comment.content)
                .append("postId", id);

        ctx.status(201).json(new SuccessResponse(newComment));
    }
}
