package fr.william.kedubak.routes.post;

import org.bson.Document;

public class Comment {

    private final long createdAt;
    private final String id;
    private final String firstName;
    private final String content;

    public Comment(String id, String firstName, String content) {
        this.createdAt = System.currentTimeMillis();
        this.id = id;
        this.firstName = firstName;
        this.content = content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getContent() {
        return content;
    }

    Document toDocument() {
        return new Document()
                .append("createdAt", createdAt)
                .append("id", id)
                .append("firstName", firstName)
                .append("content", content);
    }

    static Comment fromDocument(Document document) {
        return new Comment(
                document.getString("id"),
                document.getString("firstName"),
                document.getString("content")
        );
    }
}
