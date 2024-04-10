package fr.william.kedubak;

import fr.william.kedubak.routes.HelloWorldRoute;
import fr.william.kedubak.routes.auth.LoginRoute;
import fr.william.kedubak.routes.auth.RegisterRoute;
import fr.william.kedubak.routes.comment.CreateCommentRoute;
import fr.william.kedubak.routes.post.*;
import fr.william.kedubak.routes.user.EditUserProfileRoute;
import fr.william.kedubak.routes.user.GetUserProfileRoute;
import fr.william.kedubak.routes.user.RemoveUserRoute;
import io.javalin.Javalin;
import org.eclipse.jetty.http.HttpMethod;

public enum KedubakRoutes {

    HELLO_WORLD(HttpMethod.GET, "/hello", new HelloWorldRoute()),

    REGISTER(HttpMethod.POST, "/auth/register", new RegisterRoute()),
    LOGIN(HttpMethod.POST, "/auth/login", new LoginRoute()),

    GET_USER_PROFILE(HttpMethod.GET, "/user/me", new GetUserProfileRoute()),
    EDIT_USER_PROFILE(HttpMethod.PUT, "/user/edit", new EditUserProfileRoute()),
    REMOVE_USER(HttpMethod.DELETE, "/user/remove", new RemoveUserRoute()),

    GET_ALL_POSTS(HttpMethod.GET, "/post", new GetAllPostsRoute()),
    CREATE_POST(HttpMethod.POST, "/post", new CreatePostRoute()),
    GET_USER_POSTS(HttpMethod.GET, "/post/me", new GetUserPostsRoute()),
    GET_POST_BY_ID(HttpMethod.GET, "/post/{id}", new GetPostByIdRoute()),
    DELETE_POST(HttpMethod.DELETE, "/post/{id}", new DeletePostRoute()),
    VOTE_POST(HttpMethod.POST, "/post/vote/{id}", new VotePostRoute()),

    CREATE_COMMENT(HttpMethod.POST, "/comment/{id}", new CreateCommentRoute());

    private final HttpMethod method;
    private final String path;
    private final KedubakRoute route;

    KedubakRoutes(HttpMethod method, String path, KedubakRoute route) {
        this.method = method;
        this.path = path;
        this.route = route;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public KedubakRoute getRoute() {
        return route;
    }

    public void register(Javalin app) {
        switch (this.method) {
            case GET:
                app.get(this.path, this.route::handle);
                break;
            case POST:
                app.post(this.path, this.route::handle);
                break;
            case PUT:
                app.put(this.path, this.route::handle);
                break;
            case DELETE:
                app.delete(this.path, this.route::handle);
                break;
            default:
                break;
        }
    }
}
