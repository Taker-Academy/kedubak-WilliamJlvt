package fr.william.kedubak.routes.user;

import com.google.gson.Gson;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.token.TokenManager;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import io.javalin.http.Context;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import static com.mongodb.client.model.Updates.*;

public class EditUserProfileRoute implements KedubakRoute {

    private static final String USERS_COLLECTION = "users";

    class User {
        String email;
        String firstName;
        String lastName;
        String password;
    }

    @Override
    public void handle(Context ctx) {
        String token = ctx.header("Authorization");

        if (token == null || !token.startsWith("Bearer ") || !TokenManager.isValidToken(token.substring(7))) {
            ctx.status(401).json(new ErrorResponse("Invalid token"));
            return;
        }

        String userId = TokenManager.getUserIdFromToken(token.substring(7));

        if (userId == null) {
            ctx.status(401).json(new ErrorResponse("Invalid token"));
            return;
        }

        User user = new Gson().fromJson(ctx.body(), User.class);

        Bson update = combine(
                set("email", user.email),
                set("firstName", user.firstName),
                set("lastName", user.lastName),
                set("password", BCrypt.hashpw(user.password, BCrypt.gensalt()))
        );

        Document query = new Document("_id", new ObjectId(userId));
        MongoDBConnection.getDatabase().getCollection(USERS_COLLECTION).updateOne(query, update);

        Document result = new Document()
                .append("email", user.email)
                .append("firstName", user.firstName)
                .append("lastName", user.lastName);

        ctx.status(200).json(new SuccessResponse(result));
    }
}