package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.raven.model.*;
import com.raven.utils.MongoDBConfig;
import com.raven.utils.RoomPrice;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceController {

    public final MongoCollection<Document> invoiceCollection;
    private static final String ID_PREFIX = "HD";
    private static final int ID_LENGTH = 9;

    public InvoiceController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        invoiceCollection = database.getCollection(MongoDBConfig.COLLECTION_INVOICE);
    }

    public String createId() {
        String latestId = getLatestInvoiceId();
        int sequenceNumber = Integer.parseInt(latestId.substring(2)) + 1;
        String newId = ID_PREFIX + String.format("%0" + (ID_LENGTH - ID_PREFIX.length()) + "d", sequenceNumber);
        return newId;
    }

    private String getLatestInvoiceId() {
        Document sortQuery = new Document("code", -1);
        Document latestInvoice = invoiceCollection.find().sort(sortQuery).limit(1).first();

        if (latestInvoice != null) {
            String latestId = latestInvoice.getString("code");
            return latestId;
        }

        return ID_PREFIX + "0000000";
    }

    public void addInvoice(Model_Invoice invoice) {
        Document invoiceDocument = new Document()
                .append("code", this.createId())
                .append("hotelName", invoice.getHotelName())
                .append("hotelAddress", invoice.getHotelAddress())
                .append("hotelPhone", invoice.getHotelPhone())
                .append("hotelEmail", invoice.getHotelEmail())
                .append("customer", customerToDocument(invoice.getCustomer()))
                .append("numberOfGuests", invoice.getNumberOfGuests())
                .append("note", invoice.getNote())
                .append("bookingDate", invoice.getBookingDate())
                .append("checkInDate", invoice.getCheckInDate())
                .append("checkOutDate", invoice.getCheckOutDate())
                .append("status", invoice.getStatus())
                .append("roomList", invoice.getRoomList().stream().map(this::roomToDocument).collect(Collectors.toList()))
                .append("productList", invoice.getProductList().stream().map(this::servicesAndGoodsToDocument).collect(Collectors.toList()))
                .append("serviceList", invoice.getServiceList().stream().map(this::servicesAndGoodsToDocument).collect(Collectors.toList()))
                .append("totalAmount", invoice.getTotalAmount());

        invoiceCollection.insertOne(invoiceDocument);
    }

    public void deleteInvoice(String invoiceId) {
        invoiceCollection.deleteOne(new Document("code", invoiceId));
    }

    public void updateInvoice(String invoiceId, Model_Invoice updatedInvoice) {
        Document updatedInvoiceDocument = new Document()
                .append("code", updatedInvoice.getId())
                .append("hotelName", updatedInvoice.getHotelName())
                .append("hotelAddress", updatedInvoice.getHotelAddress())
                .append("hotelPhone", updatedInvoice.getHotelPhone())
                .append("hotelEmail", updatedInvoice.getHotelEmail())
                .append("customer", customerToDocument(updatedInvoice.getCustomer()))
                .append("numberOfGuests", updatedInvoice.getNumberOfGuests())
                .append("note", updatedInvoice.getNote())
                .append("bookingDate", updatedInvoice.getBookingDate())
                .append("checkInDate", updatedInvoice.getCheckInDate())
                .append("checkOutDate", updatedInvoice.getCheckOutDate())
                .append("status", updatedInvoice.getStatus())
                .append("roomList", updatedInvoice.getRoomList().stream().map(this::roomToDocument).collect(Collectors.toList()))
                .append("productList", updatedInvoice.getProductList().stream().map(this::servicesAndGoodsToDocument).collect(Collectors.toList()))
                .append("serviceList", updatedInvoice.getServiceList().stream().map(this::servicesAndGoodsToDocument).collect(Collectors.toList()))
                .append("totalAmount", updatedInvoice.getTotalAmount());

        invoiceCollection.replaceOne(new Document("code", invoiceId), updatedInvoiceDocument);
    }

    public void swapRoom(Model_Room currentRoom, Model_Room selectedRoom, Model_Invoice invoice, RoomPrice roomPrice) {
        invoice.setRoomById(currentRoom.getName(), selectedRoom, roomPrice.getPrice(selectedRoom.getType()));
        updateInvoice(invoice.getId(), invoice);
    }

    public Model_Invoice getInvoiceById(String invoiceId) {
        Document invoiceDocument = invoiceCollection.find(Filters.eq("code", invoiceId)).first();
        if (invoiceDocument != null) {
            return documentToInvoice(invoiceDocument);
        }
        return null;
    }

    public List<Model_Invoice> getAllInvoices() {
        List<Model_Invoice> invoices = new ArrayList<>();
        FindIterable<Document> invoiceDocuments = invoiceCollection.find();
        for (Document invoiceDocument : invoiceDocuments) {
            invoices.add(documentToInvoice(invoiceDocument));
        }
        return invoices;
    }

    public List<Model_Invoice> getInvoicesByCustomer(String customerName) {
        List<Model_Invoice> invoices = new ArrayList<>();
        FindIterable<Document> invoiceDocuments = invoiceCollection.find(Filters.eq("customer.name", customerName));
        for (Document invoiceDocument : invoiceDocuments) {
            invoices.add(documentToInvoice(invoiceDocument));
        }
        return invoices;
    }

    public List<Model_Invoice> getInvoicesByStatus(String status) {
        List<Model_Invoice> invoices = new ArrayList<>();
        FindIterable<Document> invoiceDocuments = invoiceCollection.find(Filters.eq("status", status));
        for (Document invoiceDocument : invoiceDocuments) {
            invoices.add(documentToInvoice(invoiceDocument));
        }
        return invoices;
    }

    public List<Model_Invoice> getInvoicesByDateRange(Date startDate, Date endDate) {
        List<Model_Invoice> invoices = new ArrayList<>();
        FindIterable<Document> invoiceDocuments = invoiceCollection.find(
                Filters.and(
                        Filters.gte("bookingDate", startDate),
                        Filters.lte("bookingDate", endDate)
                )
        );
        for (Document invoiceDocument : invoiceDocuments) {
            invoices.add(documentToInvoice(invoiceDocument));
        }
        return invoices;
    }

    public boolean containsKeyword(Model_Invoice invoice, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        String customerName = invoice.getCustomer().getName().toLowerCase();
        String note = invoice.getNote().toLowerCase();
        String status = invoice.getStatus().toLowerCase();
        String bookingDate = invoice.getBookingDate().toString().toLowerCase();
        String checkInDate = invoice.getCheckInDate().toString().toLowerCase();
        String checkOutDate = invoice.getCheckOutDate().toString().toLowerCase();
        String totalAmount = String.valueOf(invoice.getTotalAmount()).toLowerCase();

        return customerName.contains(lowerCaseKeyword)
                || note.contains(lowerCaseKeyword)
                || status.contains(lowerCaseKeyword)
                || bookingDate.contains(lowerCaseKeyword)
                || checkInDate.contains(lowerCaseKeyword)
                || checkOutDate.contains(lowerCaseKeyword)
                || totalAmount.contains(lowerCaseKeyword);
    }

    private Document customerToDocument(Model_Customer customer) {
        return new Document("idCard", customer.getIdCard())
                .append("name", customer.getName())
                .append("gender", customer.getGender())
                .append("phone", customer.getPhone())
                .append("email", customer.getEmail())
                .append("address", customer.getAddress());
    }

    private Model_Customer documentToCustomer(Object customerObject) {
        Document customerDocument = (Document) customerObject;
        String idCard = customerDocument.getString("idCard");
        String name = customerDocument.getString("name");
        String gender = customerDocument.getString("gender");
        String phone = customerDocument.getString("phone");
        String email = customerDocument.getString("email");
        String address = customerDocument.getString("address");

        return new Model_Customer(idCard, name, gender, phone, email, address);
    }

    private Model_Invoice documentToInvoice(Document invoiceDocument) {
        String id = invoiceDocument.getString("code");
        Object customerObj = invoiceDocument.get("customer");
        Model_Customer customer = documentToCustomer(customerObj);
        int numberOfGuests = invoiceDocument.getInteger("numberOfGuests");
        String note = invoiceDocument.getString("note");
        Date bookingDate = invoiceDocument.getDate("bookingDate");
        Date checkInDate = invoiceDocument.getDate("checkInDate");
        Date checkOutDate = invoiceDocument.getDate("checkOutDate");
        String status = invoiceDocument.getString("status");
        int totalAmount = invoiceDocument.getInteger("totalAmount");

        // Convert roomList, productList, serviceList from Document
        List<Document> roomDocs = (List<Document>) invoiceDocument.get("roomList");
        List<Model_Invoice_Rooms> roomList = roomDocs.stream()
                .map(this::documentToRoom)
                .collect(Collectors.toList());

        List<Document> productDocs = (List<Document>) invoiceDocument.get("productList");
        List<Model_Invoice_ServicesAndGoods> productList = productDocs.stream()
                .map(doc -> documentToServicesAndGoods(doc, "HH"))
                .collect(Collectors.toList());

        List<Document> serviceDocs = (List<Document>) invoiceDocument.get("serviceList");
        List<Model_Invoice_ServicesAndGoods> serviceList = serviceDocs.stream()
                .map(doc -> documentToServicesAndGoods(doc, "DV"))
                .collect(Collectors.toList());

        return new Model_Invoice(
                id, customer, numberOfGuests, note, bookingDate, checkInDate, checkOutDate, status,
                roomList, productList, serviceList, totalAmount
        );
    }

    private Document roomToDocument(Model_Invoice_Rooms room) {
        return new Document("code", room.getCode())
                .append("name", room.getName())
                .append("capacity", room.getCapacity())
                .append("type", room.getType())
                .append("floor", room.getFloor())
                .append("pricePerNight", room.getPricePerNight())
                .append("numberOfNights", room.getNumberOfNights())
                .append("totalRoomPrice", room.getTotalRoomPrice());
    }

    private Model_Invoice_Rooms documentToRoom(Document roomDoc) {
        String id = roomDoc.getString("code");
        String name = roomDoc.getString("name");
        int capacity = roomDoc.getInteger("capacity");
        String type = roomDoc.getString("type");
        String floor = roomDoc.getString("floor");
        int pricePerNight = roomDoc.getInteger("pricePerNight");
        int numberOfNights = roomDoc.getInteger("numberOfNights");
        int totalRoomPrice = roomDoc.getInteger("totalRoomPrice");

        return new Model_Invoice_Rooms(id, name, capacity, type, floor, pricePerNight, numberOfNights, totalRoomPrice);
    }

    private Document servicesAndGoodsToDocument(Model_Invoice_ServicesAndGoods product) {
        return new Document("room", product.getRoomName())
                .append("name", product.getName())
                .append("quantity", product.getQuantity())
                .append("unitPrice", product.getUnitPrice())
                .append("totalPrice", product.getTotalPrice())
                .append("unit", product.getUnit());
    }

    private Model_Invoice_ServicesAndGoods documentToServicesAndGoods(Document productDoc, String t) {

        String roomName = productDoc.getString("room");
        String name = productDoc.getString("name");
        int quantity = productDoc.getInteger("quantity");
        int unitPrice = productDoc.getInteger("unitPrice");
        int totalPrice = productDoc.getInteger("totalPrice");
        String unit = productDoc.getString("unit");
        String type = t;

        return new Model_Invoice_ServicesAndGoods(roomName, name, quantity, unitPrice, totalPrice, unit, type);
    }

    public List<Model_Invoice> getInvoicesWithinTimePeriod(Date checkInDate, Date checkOutDate) {
        List<Model_Invoice> invoices = new ArrayList<>();

        // Create query filters for check-in and check-out dates
        Document checkInFilter = new Document("$gte", checkInDate);
        Document checkOutFilter = new Document("$lt", checkOutDate);

        // Combine the filters using the "$and" operator
        Document dateFilter = new Document("$and", Arrays.asList(
                new Document("checkInDate", checkInFilter),
                new Document("checkOutDate", checkOutFilter)
        ));

        // Find invoices within the specified time period
        FindIterable<Document> invoiceDocuments = invoiceCollection.find(dateFilter);
        for (Document invoiceDocument : invoiceDocuments) {
            invoices.add(documentToInvoice(invoiceDocument));
        }

        return invoices;
    }
}
