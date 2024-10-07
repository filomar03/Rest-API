package it.unimib.sd2024.models;

import java.io.StringReader;
import java.time.Instant;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

public class Domain {
    private String name;
    private String state;
    private String owner;
    private Instant start;
    private Instant end;

    public Domain(String name, String state, String owner, Instant periodStart, Instant periodEnd) {
        this.name = name;
        this.state = state;
        this.owner = owner;
        this.start = periodStart;
        this.end = periodEnd;
    }

    public Domain() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public String toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        System.out.println(this.name);
        System.out.println(this.owner);
        System.out.println(this.start);
        System.out.println(this.owner);

        builder.add("name", this.name);
            
        if (this.state != null) {
            builder.add("state", this.state);
        } else {
            builder.addNull("state");
        }

        if (this.owner != null) {
            builder.add("owner", this.owner);
        } else {
            builder.addNull("owner");
        }

        if (this.start != null) {
            builder.add("start", this.start.toString());
        } else {
            builder.addNull("start");
        }

        if (this.end != null) {
            builder.add("end", this.end.toString());
        } else {
            builder.addNull("end");
        }

        return builder.build().toString();
    }

    public static Domain fromJson(String jsonStr) {
        JsonObject jsonObject;
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonStr))) {
            jsonObject = jsonReader.readObject();
        }

        Domain domain = new Domain();

        jsonObject.forEach((key, value) -> {
                System.out.println(key + " : " + value);
                switch (key) {
                    case "name" -> domain.setName(Domain.trim(value.toString()));
                    case "state" -> domain.setState(Domain.trim(value.toString()));
                    case "owner" -> domain.setOwner(Domain.trim(value.toString()));
                    case "start" -> {
                        String startString = value.toString();
                        domain.setStart(startString.equals("0") ? Instant.EPOCH : Instant.parse(startString.substring(1, startString.length() - 1)));
                    }
                    case "end" -> {
                        String endString = value.toString();
                        domain.setEnd(endString.equals("0") ? Instant.EPOCH : Instant.parse(endString.substring(1, endString.length() - 1)));
                    }
                }
        });
    
        return domain;
    }

    private static String trim(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length()-1);
        } else {
            return str;
        }
    }

    @Override
    public String toString() {
        return toJson();
    }
}
