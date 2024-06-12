package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.raven.model.Model_RoomType;
import com.raven.utils.MongoDBConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bson.Document;

public class RoomTypeController {

    private final MongoCollection<Document> roomTypeCollection;
    private static final String ID_PREFIX = "LP";
    private static final int ID_LENGTH = 5;

    public RoomTypeController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        roomTypeCollection = database.getCollection(MongoDBConfig.COLLECTION_ROOM_TYPE);
    }

    public String createId() {
        String latestId = getLatestInvoiceId();
        int sequenceNumber = Integer.parseInt(latestId.substring(2)) + 1;
        String newId = ID_PREFIX + String.format("%0" + (ID_LENGTH - ID_PREFIX.length()) + "d", sequenceNumber);
        return newId;
    }

    private String getLatestInvoiceId() {
        Document sortQuery = new Document("code", -1);
        Document latestInvoice = roomTypeCollection.find().sort(sortQuery).limit(1).first();

        if (latestInvoice != null) {
            String latestId = latestInvoice.getString("code");
            return latestId;
        }

        return ID_PREFIX + "000";
    }
    
    public void addRoomType(Model_RoomType roomType) {
        Document roomTypeDocument = new Document()
                .append("code", roomType.getId())
                .append("name", roomType.getName())
                .append("price", roomType.getPrice())
                .append("description", roomType.getDescription())
                .append("type", roomType.getType());

        roomTypeCollection.insertOne(roomTypeDocument);
    }

    public void deleteRoomType(String roomTypeId) {
        roomTypeCollection.deleteOne(new Document("code", roomTypeId));
    }

    public void updateRoomType(String roomTypeId, Model_RoomType updatedRoomType) {
        Document updatedRoomTypeDocument = new Document()
                .append("code", updatedRoomType.getId())
                .append("name", updatedRoomType.getName())
                .append("price", updatedRoomType.getPrice())
                .append("description", updatedRoomType.getDescription())
                .append("type", updatedRoomType.getType());

        roomTypeCollection.replaceOne(new Document("code", roomTypeId), updatedRoomTypeDocument);
    }

    public Model_RoomType getRoomType(String propertyName, Object propertyValue) {
        Document roomTypeDocument = roomTypeCollection.find(Filters.eq(propertyName, propertyValue)).first();
        if (roomTypeDocument != null) {
            Model_RoomType roomType = new Model_RoomType();
            roomType.setId(roomTypeDocument.getString("code"));
            roomType.setName(roomTypeDocument.getString("name"));
            roomType.setPrice(roomTypeDocument.getInteger("price"));
            roomType.setDescription(roomTypeDocument.getString("description"));
            roomType.setType(roomTypeDocument.getString("type"));
            return roomType;
        }
        return null;
    }

    public List<Model_RoomType> getAllRoomTypes() {
        List<Model_RoomType> roomTypes = new ArrayList<>();
        FindIterable<Document> roomTypeDocuments = roomTypeCollection.find();
        for (Document roomTypeDocument : roomTypeDocuments) {
            Model_RoomType roomType = new Model_RoomType();
            roomType.setId(roomTypeDocument.getString("code"));
            roomType.setName(roomTypeDocument.getString("name"));
            roomType.setPrice(roomTypeDocument.getInteger("price", 0));
            roomType.setDescription(roomTypeDocument.getString("description"));
            roomType.setType(roomTypeDocument.getString("type"));
            roomTypes.add(roomType);
        }

        // Sắp xếp danh sách loại phòng theo mã (code)
        Collections.sort(roomTypes, (Model_RoomType roomType1, Model_RoomType roomType2) -> {
            // Lấy phần số sau chữ cái "LP" từ mã loại phòng
            int roomTypeNumber1 = Integer.parseInt(roomType1.getId().substring(2));
            int roomTypeNumber2 = Integer.parseInt(roomType2.getId().substring(2));
            // So sánh các số loại phòng
            return roomTypeNumber1 - roomTypeNumber2;
        });

        return roomTypes;
    }

    public boolean containsKeyword(Model_RoomType roomType, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        String roomTypeId = roomType.getId().toLowerCase();
        String roomTypeName = roomType.getName().toLowerCase();
        String price = String.valueOf(roomType.getPrice()).toLowerCase();
        String description = roomType.getDescription().toLowerCase();
        String type = roomType.getType().toLowerCase();

        return roomTypeId.contains(lowerCaseKeyword)
                || roomTypeName.contains(lowerCaseKeyword)
                || price.contains(lowerCaseKeyword)
                || description.contains(lowerCaseKeyword)
                || type.contains(lowerCaseKeyword);
    }
}
