package ru.various.sdp2partpost.cases;

import java.util.Properties;

public class CaseProperties extends Properties {

    private CaseProperties(Properties properties) {
        super(properties);
    }

    public static class Builder extends Properties {

        public CaseProperties build() {
            return new CaseProperties(this);
        }

        public Builder add(String key, String value) {
            put(key, value);
            return this;
        }
    }

    public static Builder builder() {

        return
            new Builder()
                    .add("case_id", "case_id")
                    .add("name", "name")
                    .add("address", "address")
                    .add("post_address", "post_address")
                    .add("gender", "comments")
                    .add("id", "id")
                    .add("full_number", "full_number");

    }
}
