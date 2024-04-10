package fr.william.kedubak.routes.auth;

import com.google.gson.Gson;
import fr.william.kedubak.KedubakRoute;
import fr.william.kedubak.token.TokenManager;
import fr.william.kedubak.database.MongoDBConnection;
import fr.william.kedubak.response.ErrorResponse;
import fr.william.kedubak.response.SuccessResponse;
import io.javalin.http.Context;
import org.mindrot.jbcrypt.BCrypt;
import org.bson.Document;

public class LoginRoute implements KedubakRoute {

    private static final String USERS_COLLECTION = "users";

    class User {
        String email;
        String password;
    }

    @Override
    public void handle(Context ctx) {
        User user = new Gson().fromJson(ctx.body(), User.class);

        if (user.email == null || user.password == null) {
            ctx.status(400).json(new ErrorResponse("Email and password must be provided"));
            return;
        }

        Document query = new Document("email", user.email);
        Document foundUser = MongoDBConnection.getDatabase().getCollection(USERS_COLLECTION).find(query).first();

        if (foundUser == null) {
            ctx.status(401).json(new ErrorResponse("Invalid email"));
            return;
        }

        if (!BCrypt.checkpw(user.password, foundUser.getString("password"))) {
            ctx.status(401).json(new ErrorResponse("Invalid password"));
            return;
        }

        Document result = new Document()
                .append("token", TokenManager.generateToken(foundUser.getObjectId("_id").toString()))
                .append("user", new Document()
                        .append("email", foundUser.getString("email"))
                        .append("firstName", foundUser.getString("firstName"))
                        .append("lastName", foundUser.getString("lastName"))
                );
        ctx.status(200).json(new SuccessResponse(result));
    }
}
