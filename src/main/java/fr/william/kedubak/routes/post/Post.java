package fr.william.kedubak.routes.post;

import org.bson.Document;
import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Post {

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

    public Post(long createdAt, String userId, String firstName, String title, String content, List<Comment> comments, List<String> upVotes) {
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

    public void editPost(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
    }

    Document toDocument() {
        JSONArray commentsArray = new JSONArray();
        for (Comment comment : comments) {
            commentsArray.put(comment.toDocument());
        }

        return new Document()
                .append("createdAt", createdAt)
                .append("userId", userId)
                .append("firstName", firstName)
                .append("title", title)
                .append("content", content)
                .append("comments", commentsArray)
                .append("upVotes", upVotes);
    }

    static Post fromDocument(Document document) {
        Document[] commentsDocuments = (Document[]) document.get("comments");
        Comment[] comments = new Comment[commentsDocuments.length];
        for (int i = 0; i < commentsDocuments.length; i++) {
            comments[i] = Comment.fromDocument(commentsDocuments[i]);
        }

        return new Post(
                document.getLong("createdAt"),
                document.getString("userId"),
                document.getString("firstName"),
                document.getString("title"),
                document.getString("content"),
                List.of(comments),
                document.getList("upVotes", String.class)
        );
    }
}
