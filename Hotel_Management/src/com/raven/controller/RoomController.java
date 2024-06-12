package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.regex;
import com.mongodb.client.model.Sorts;
import com.raven.model.Model_Room;
import com.raven.utils.MongoDBConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bson.Document;

public class RoomController {

    private final MongoCollection<Document> roomCollection;

    public RoomController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        roomCollection = database.getCollection(MongoDBConfig.COLLECTION_ROOM);
    }
    
    public int getTotalRoom(String floorName) {
        int totalRoom = 0;
        FindIterable<Document> roomDocuments = roomCollection.find(Filters.eq("floor", floorName));
        for (Document roomDocument : roomDocuments) {
            totalRoom++;
        }
        return totalRoom;
    }
    
    public String getLastCodeInFloor(String floor) {
        String floorNumber = floor.replaceAll("[^0-9]", "");
        Document roomDocument = roomCollection.find(regex("code", "^P" + floorNumber))
                .sort(Sorts.descending("code"))
                .first();
        return roomDocument != null ? roomDocument.getString("code") : null;
    }

    public String createId(String floor) {
        String floorNumber = floor.replaceAll("[^0-9]", "");

        String lastCode = getLastCodeInFloor(floor);

        if (lastCode == null) {
            return "P" + floorNumber + "01";
        } else {
            int lastNumber = Integer.parseInt(lastCode.substring(2));
            int newNumber = lastNumber + 1;
            return "P" + floorNumber + String.format("%02d", newNumber);
        }
    }

    public void addRoom(Model_Room room) {
        Document roomDocument = new Document()
                .append("code", room.getId())
                .append("name", room.getName())
                .append("type", room.getType())
                .append("floor", room.getFloor())
                .append("capacity", room.getCapacity());

        roomCollection.insertOne(roomDocument);
    }

    public void deleteRoom(String roomId) {
        roomCollection.deleteOne(new Document("code", roomId));
    }

    public void updateRoom(String roomId, Model_Room updatedRoom) {
        Document updatedRoomDocument = new Document()
                .append("code", updatedRoom.getId())
                .append("name", updatedRoom.getName())
                .append("type", updatedRoom.getType())
                .append("floor", updatedRoom.getFloor())
                .append("capacity", updatedRoom.getCapacity());

        roomCollection.replaceOne(new Document("code", roomId), updatedRoomDocument);
    }

    public Model_Room getRoom(String propertyName, Object propertyValue) {
        Document roomDocument = roomCollection.find(Filters.eq(propertyName, propertyValue)).first();
        if (roomDocument != null) {
            Model_Room room = new Model_Room();
            room.setId(roomDocument.getString("code"));
            room.setName(roomDocument.getString("name"));
            room.setType(roomDocument.getString("type"));
            room.setFloor(roomDocument.getString("floor"));
            room.setCapacity(roomDocument.getInteger("capacity"));
            return room;
        }
        return null;
    }

    public List<Model_Room> getAllRooms() {
        List<Model_Room> rooms = new ArrayList<>();
        FindIterable<Document> roomDocuments = roomCollection.find();
        for (Document roomDocument : roomDocuments) {
            Model_Room room = new Model_Room();
            room.setId(roomDocument.getString("code"));
            room.setName(roomDocument.getString("name"));
            room.setType(roomDocument.getString("type"));
            room.setFloor(roomDocument.getString("floor"));
            room.setCapacity(roomDocument.getInteger("capacity"));
            rooms.add(room);
        }

        // Sắp xếp danh sách phòng theo mã (code)
        Collections.sort(rooms, (Model_Room room1, Model_Room room2) -> {
            // Lấy phần số sau chữ cái "P" từ mã phòng
            int roomNumber1 = Integer.parseInt(room1.getId().substring(1));
            int roomNumber2 = Integer.parseInt(room2.getId().substring(1));
            // So sánh các số phòng
            return roomNumber1 - roomNumber2;
        });

        return rooms;
    }

    public Model_Room getRoomById(String roomId) {
        Document roomDocument = roomCollection.find(Filters.eq("code", roomId)).first();
        if (roomDocument != null) {
            Model_Room room = new Model_Room();
            room.setId(roomDocument.getString("code"));
            room.setName(roomDocument.getString("name"));
            room.setType(roomDocument.getString("type"));
            room.setFloor(roomDocument.getString("floor"));
            room.setCapacity(roomDocument.getInteger("capacity"));
            return room;
        }
        return null;
    }

    public List<Model_Room> getRoomsByType(String roomType) {
        List<Model_Room> rooms = new ArrayList<>();
        FindIterable<Document> roomDocuments = roomCollection.find(Filters.eq("type", roomType));
        for (Document roomDocument : roomDocuments) {
            Model_Room room = new Model_Room();
            room.setId(roomDocument.getString("code"));
            room.setName(roomDocument.getString("name"));
            room.setType(roomDocument.getString("type"));
            room.setFloor(roomDocument.getString("floor"));
            room.setCapacity(roomDocument.getInteger("capacity"));
            rooms.add(room);
        }
        return rooms;
    }

    public List<Model_Room> getRoomsByStatus(String roomStatus) {
        List<Model_Room> rooms = new ArrayList<>();
        FindIterable<Document> roomDocuments = roomCollection.find(Filters.eq("status", roomStatus));
        for (Document roomDocument : roomDocuments) {
            Model_Room room = new Model_Room();
            room.setId(roomDocument.getString("code"));
            room.setName(roomDocument.getString("name"));
            room.setType(roomDocument.getString("type"));
            room.setFloor(roomDocument.getString("floor"));
            room.setCapacity(roomDocument.getInteger("capacity"));
            rooms.add(room);
        }
        return rooms;
    }

    public List<String> getAllRoomIds() {
        return roomCollection.find().map(room -> room.getString("code")).into(new ArrayList<>());
    }

    public boolean containsKeyword(Model_Room room, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        String roomId = room.getId().toLowerCase();
        String roomName = room.getName().toLowerCase();
        String roomType = room.getType().toLowerCase();
        String floor = room.getFloor().toLowerCase();
        String capacity = String.valueOf(room.getCapacity()).toLowerCase();

        return roomId.contains(lowerCaseKeyword)
                || roomName.contains(lowerCaseKeyword)
                || roomType.contains(lowerCaseKeyword)
                || floor.contains(lowerCaseKeyword)
                || capacity.contains(lowerCaseKeyword);
    }
}
