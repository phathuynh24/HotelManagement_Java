package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.raven.model.Model_Floor;
import com.raven.utils.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class FloorController {

    private final MongoCollection<Document> floorCollection;
    private final RoomController roomController = new RoomController();

    public FloorController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        floorCollection = database.getCollection(MongoDBConfig.COLLECTION_FLOOR);
    }
    
    public String createId() {
        Document lastFloor = floorCollection.find().sort(Sorts.descending("code")).first();
        
        if (lastFloor != null) {
            String lastId = lastFloor.getString("code");
            int numericPart = Integer.parseInt(lastId.substring(1));
            numericPart++;
            return String.format("T%03d", numericPart);
        } else {
            return "T001";
        }
    }

    public void addFloor(Model_Floor floor) {
        Document floorDocument = new Document()
                .append("code", floor.getId())
                .append("name", floor.getName())
                .append("totalRoom", 0);
        floorCollection.insertOne(floorDocument);
    }

    public void deleteFloor(String floorId) {
        floorCollection.deleteOne(Filters.eq("code", floorId));
    }

    public void updateFloor(Model_Floor updatedFloor) {
        Document updatedFloorDocument = new Document()
                .append("code", updatedFloor.getId())
                .append("name", updatedFloor.getName());
        floorCollection.replaceOne(Filters.eq("code", updatedFloor.getId()), updatedFloorDocument);
    }

    public Model_Floor getFloorById(int floorId) {
        Document floorDocument = floorCollection.find(Filters.eq("code", floorId)).first();
        if (floorDocument != null) {
            return new Model_Floor(
                    floorDocument.getString("code"),
                    floorDocument.getString("name"),
                    roomController.getTotalRoom(floorDocument.getString("name")));
        }
        return null;
    }

    public List<Model_Floor> getAllFloors() {
        List<Model_Floor> floors = new ArrayList<>();
        // Thực hiện truy vấn và sắp xếp theo trường "code"
        FindIterable<Document> floorDocuments = floorCollection.find().sort(Sorts.ascending("code"));
        for (Document floorDocument : floorDocuments) {
            floors.add(new Model_Floor(
                    floorDocument.getString("code"),
                    floorDocument.getString("name"),
                    roomController.getTotalRoom(floorDocument.getString("name"))));
        }
        return floors;
    }

    public List<Model_Floor> getAllNameFloors() {
        List<Model_Floor> floors = new ArrayList<>();
        // Thực hiện truy vấn và sắp xếp theo trường "code"
        FindIterable<Document> floorDocuments = floorCollection.find().sort(Sorts.ascending("code"));
        for (Document floorDocument : floorDocuments) {
            floors.add(new Model_Floor(
                    floorDocument.getString("code"),
                    floorDocument.getString("name"),
                    -1));
        }
        return floors;
    }

    public boolean containsKeyword(Model_Floor floor, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        String floorId = floor.getId().toLowerCase();
        String floorName = floor.getName().toLowerCase();
        String totalRoom = String.valueOf(floor.getTotalRoom()).toLowerCase();

        String[] fieldsToCheck = {floorId, floorName, totalRoom};

        for (String field : fieldsToCheck) {
            if (field.contains(lowerCaseKeyword)) {
                return true;
            }
        }
        return false;
    }

    public int countFloors() {
        return (int) floorCollection.countDocuments();
    }
}
