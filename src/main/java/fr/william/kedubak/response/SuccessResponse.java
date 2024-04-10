package fr.william.kedubak.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.Document;

public class SuccessResponse {

    @JsonProperty
    private final boolean ok;

    private final Document data;

    @JsonCreator
    public SuccessResponse(@JsonProperty("data") Document data) {
        this.ok = true;
        this.data = data;
    }

    public Document getData() {
        return data;
    }
}
