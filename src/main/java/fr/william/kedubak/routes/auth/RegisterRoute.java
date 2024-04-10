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

public class RegisterRoute implements KedubakRoute {

    private static final String USERS_COLLECTION = "users";
    private static final String SALT_ROUNDS = System.getenv("SALT_ROUNDS");

    class User {
        String email;
        String password;
        String firstName;
        String lastName;
    }

    @Override
    public void handle(Context ctx) {
        User user = new Gson().fromJson(ctx.body(), User.class);

        if (user.email == null || user.password == null || user.firstName == null || user.lastName == null) {
            ctx.status(400).json(new ErrorResponse("Tous les champs doivent être remplis"));
            return;
        }
        System.out.println("[DEBUG] form params: email=" + user.email + ", password=" + user.password + ", firstName=" + user.firstName + ", lastName=" + user.lastName);

        if (isEmailAlreadyUsed(user.email)) {
            ctx.status(400).json(new ErrorResponse("L'email est déjà utilisé"));
            return;
        }

        String hashedPassword = BCrypt.hashpw(user.password, SALT_ROUNDS);

        Document newUser = new Document()
                .append("email", user.email)
                .append("password", hashedPassword)
                .append("firstName", user.firstName)
                .append("lastName", user.lastName)
                .append("createdAt", System.currentTimeMillis())
                .append("lastUpVote", System.currentTimeMillis() - 60000);

        MongoDBConnection.getDatabase().getCollection(USERS_COLLECTION).insertOne(newUser);
        Document result = new Document()
                .append("token", TokenManager.generateToken(MongoDBConnection.getDatabase().getCollection(USERS_COLLECTION).find(newUser).first().getObjectId("_id").toString()))
                .append("user", new Document()
                        .append("email", user.email)
                        .append("firstName", user.firstName)
                        .append("lastName", user.lastName)
                );
        ctx.status(201).json(new SuccessResponse(result));
    }

    private static boolean isEmailAlreadyUsed(String email) {
        Document query = new Document("email", email);
        return MongoDBConnection.getDatabase().getCollection(USERS_COLLECTION).find(query).first() != null;
    }
}