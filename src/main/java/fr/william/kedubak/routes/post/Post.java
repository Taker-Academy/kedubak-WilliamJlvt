package fr.william.kedubak.routes.post;

import org.bson.Document;
import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Post {

    private String _id;
    private final long createdAt;
    private final String userId;
    private final String firstName;
    private String title;
    private String content;
    private List<Comment> comments;
    private List<String> upVotes;

    public Post(String userId, String firstName, String title, String content) {
        this.createdAt = System.currentTimeMillis();
        this.userId = userId;
        this.firstName = firstName;
        this.title = title;
        this.content =  content;
        this.comments = new ArrayList<>();
        this.upVotes = new ArrayList<>();
    }

    public Post(String _id, long createdAt, String userId, String firstName, String title, String content, List<Comment> comments, List<String> upVotes) {
        this._id = _id;
        this.createdAt = createdAt;
        this.userId = userId;
        this.firstName = firstName;
        this.title = title;
        this.content = content;
        this.comments = comments;
        this.upVotes = upVotes;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public List<String> getUpVotes() {
        return upVotes;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void editPost(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
    }

    public Document toDocument() {
        List<Document> commentsList = new ArrayList<>();
        for (Comment comment : comments) {
            commentsList.add(comment.toDocument());
        }

        return new Document()
                .append("createdAt", createdAt)
                .append("userId", userId)
                .append("firstName", firstName)
                .append("title", title)
                .append("content", content)
                .append("comments", commentsList)
                .append("upVotes", upVotes);
    }

    public Document toDocumentWithId() {
        return toDocument().append("_id", _id);
    }

    public static Post fromDocument(Document document) {
        List<Comment> comments = new ArrayList<>();
        for (Document commentDocument : document.getList("comments", Document.class)) {
            comments.add(Comment.fromDocument(commentDocument));
        }

        return new Post(
                document.getObjectId("_id").toString(),
                document.getLong("createdAt"),
                document.getString("userId"),
                document.getString("firstName"),
                document.getString("title"),
                document.getString("content"),
                comments,
                document.getList("upVotes", String.class)
        );
    }
}
