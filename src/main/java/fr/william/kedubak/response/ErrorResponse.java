package fr.william.kedubak.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonProperty
    private final boolean ok;

    private final String error;

    @JsonCreator
    public ErrorResponse(@JsonProperty("error") String error) {
        this.ok = false;
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
