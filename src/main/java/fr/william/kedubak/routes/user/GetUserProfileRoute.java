package fr.william.kedubak.routes.user;

import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.token.TokenManager;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import io.javalin.http.Context;
import org.bson.Document;

public class GetUserProfileRoute implements KedubakRoute {

    private static final String USERS_COLLECTION = "users";

    public static Document getUserById(String userId) {
        for (Document doc : MongoDBConnection.getDatabase().getCollection(USERS_COLLECTION).find()) {
            if (doc.getObjectId("_id").toString().equals(userId)) {
                return doc;
            }
        }
        return null;
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


        Document foundUser = getUserById(userId);

        if (foundUser == null) {
            ctx.status(404).json(new ErrorResponse("User not found"));
            return;
        }

        Document result = new Document()
                .append("email", foundUser.getString("email"))
                .append("firstName", foundUser.getString("firstName"))
                .append("lastName", foundUser.getString("lastName"));

        ctx.status(200).json(new SuccessResponse(result));
    }
}
