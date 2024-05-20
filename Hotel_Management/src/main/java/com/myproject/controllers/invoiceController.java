package com.myproject.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.myproject.commons.Constants;
import com.myproject.models.Model_Invoice;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import com.myproject.models.Model_Customer;
import com.myproject.models.Model_Product;
import com.myproject.models.Model_Room;
import com.myproject.models.Model_Service;

public class invoiceController {

    private final MongoDatabase database;
    private final MongoCollection<Document> invoiceCollection;

    public invoiceController() {
        MongoClient mongoClient = MongoClients.create(Constants.MONGODB_URI);
        this.database = mongoClient.getDatabase(Constants.DATABASE_NAME);
        this.invoiceCollection = database.getCollection(Constants.COLLECTION_INVOICE);
    }

    // Create a new invoice
    public void createInvoice(Model_Invoice invoice) {
        Document document = new Document("hotelName", invoice.getHotelName())
                .append("hotelAddress", invoice.getHotelAddress())
                .append("customer", invoice.getCustomer())
                .append("numberOfGuests", invoice.getNumberOfGuests())
                .append("note", invoice.getNote())
                .append("bookingDate", invoice.getBookingDate())
                .append("checkInDate", invoice.getCheckInDate())
                .append("checkOutDate", invoice.getCheckOutDate())
                .append("status", invoice.getStatus())
                .append("roomList", invoice.getRoomList())
                .append("productList", invoice.getProductList())
                .append("serviceList", invoice.getServiceList())
                .append("totalAmount", invoice.getTotalAmount());
        invoiceCollection.insertOne(document);
    }

    // Read all invoices
    public List<Model_Invoice> getAllInvoices() {
        List<Model_Invoice> invoices = new ArrayList<>();
        for (Document doc : invoiceCollection.find()) {
            invoices.add(documentToInvoice(doc));
        }
        return invoices;
    }

    // Read a single invoice by ID
    public Model_Invoice getInvoiceById(String id) {
        Document doc = invoiceCollection.find(eq("_id", new ObjectId(id))).first();
        if (doc != null) {
            return documentToInvoice(doc);
        }
        return null;
    }

    // Update an invoice
    public void updateInvoice(String id, Model_Invoice updatedInvoice) {
        invoiceCollection.updateOne(eq("_id", new ObjectId(id)),
                new Document("$set", new Document("hotelName", updatedInvoice.getHotelName())
                        .append("hotelAddress", updatedInvoice.getHotelAddress())
                        .append("customer", updatedInvoice.getCustomer())
                        .append("numberOfGuests", updatedInvoice.getNumberOfGuests())
                        .append("note", updatedInvoice.getNote())
                        .append("bookingDate", updatedInvoice.getBookingDate())
                        .append("checkInDate", updatedInvoice.getCheckInDate())
                        .append("checkOutDate", updatedInvoice.getCheckOutDate())
                        .append("status", updatedInvoice.getStatus())
                        .append("roomList", updatedInvoice.getRoomList())
                        .append("productList", updatedInvoice.getProductList())
                        .append("serviceList", updatedInvoice.getServiceList())
                        .append("totalAmount", updatedInvoice.getTotalAmount())));
    }

    // Delete an invoice
    public void deleteInvoice(String id) {
        invoiceCollection.deleteOne(eq("_id", new ObjectId(id)));
    }

    // Helper method to convert Document to Model_Invoice
    private Model_Invoice documentToInvoice(Document doc) {
        return new Model_Invoice(
                doc.getString("hotelName"),
                doc.getString("hotelAddress"),
                doc.get("customer", Model_Customer.class),
                doc.getInteger("numberOfGuests"),
                doc.getString("note"),
                doc.getDate("bookingDate"),
                doc.getDate("checkInDate"),
                doc.getDate("checkOutDate"),
                doc.getString("status"),
                doc.getList("roomList", Model_Room.class),
                doc.getList("productList", Model_Product.class),
                doc.getList("serviceList", Model_Service.class),
                doc.getDouble("totalAmount")
        );
    }
}
