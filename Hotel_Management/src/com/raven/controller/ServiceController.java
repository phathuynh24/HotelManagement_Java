package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.raven.model.Model_Service;
import com.raven.utils.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ServiceController {

    private final MongoCollection<Document> serviceCollection;

    public ServiceController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        serviceCollection = database.getCollection(MongoDBConfig.COLLECTION_SERVICE);
    }

    public String createId() {
        Document lastService = serviceCollection.find().sort(Sorts.descending("code")).first();

        if (lastService != null) {
            String lastId = lastService.getString("code");
            int numericPart = Integer.parseInt(lastId.substring(2));
            numericPart++;
            return String.format("DV%03d", numericPart);
        } else {
            return "DV000";
        }
    }

    public void addService(Model_Service service) {
        Document serviceDocument = new Document()
                .append("code", service.getId())
                .append("name", service.getName())
                .append("price", service.getPrice())
                .append("unit", service.getUnit())
                .append("status", service.getStatus());

        serviceCollection.insertOne(serviceDocument);
    }

    public void deleteService(String serviceId) {
        serviceCollection.deleteOne(Filters.eq("code", serviceId));
    }

    public void updateService(String serviceId, Model_Service updatedService) {
        Document updatedServiceDocument = new Document()
                .append("code", updatedService.getId())
                .append("name", updatedService.getName())
                .append("price", updatedService.getPrice())
                .append("unit", updatedService.getUnit())
                .append("status", updatedService.getStatus());

        serviceCollection.replaceOne(Filters.eq("code", serviceId), updatedServiceDocument);
    }

    public Model_Service getServiceById(String serviceId) {
        Document serviceDocument = serviceCollection.find(Filters.eq("code", serviceId)).first();
        if (serviceDocument != null) {
            return documentToModel(serviceDocument);
        }
        return null;
    }

    public List<Model_Service> getAllServices() {
        List<Model_Service> servicesList = new ArrayList<>();
        FindIterable<Document> serviceDocuments = serviceCollection.find();
        for (Document serviceDocument : serviceDocuments) {
            Model_Service service = documentToModel(serviceDocument);
            servicesList.add(service);
        }

        // Sắp xếp danh sách dịch vụ theo ID
        servicesList.sort((service1, service2) -> {
            // Lấy phần số sau chữ cái "DV" từ mã dịch vụ
            int serviceNumber1 = Integer.parseInt(service1.getId().substring(2));
            int serviceNumber2 = Integer.parseInt(service2.getId().substring(2));
            // So sánh các số dịch vụ
            return serviceNumber1 - serviceNumber2;
        });

        return servicesList;
    }

    private Model_Service documentToModel(Document serviceDocument) {
        String id = serviceDocument.getString("code");
        String name = serviceDocument.getString("name");
        int price = getPriceFromDocument(serviceDocument);
        String unit = serviceDocument.getString("unit");
        String status = serviceDocument.getString("status");

        return new Model_Service(id, name, price, unit, status);
    }

    private int getPriceFromDocument(Document serviceDocument) {
        Object priceObj = serviceDocument.get("price");
        if (priceObj instanceof Integer) {
            return (int) priceObj;
        } else if (priceObj instanceof Double) {
            return ((Double) priceObj).intValue();
        } else {
            throw new IllegalStateException("Unexpected price type in MongoDB document.");
        }
    }

    public List<Model_Service> searchServices(String keyword) {
        List<Model_Service> servicesList = new ArrayList<>();
        FindIterable<Document> serviceDocuments = serviceCollection.find();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (Document serviceDocument : serviceDocuments) {
            Model_Service service = documentToModel(serviceDocument);
            if (containsKeyword(service, lowerCaseKeyword)) {
                servicesList.add(service);
            }
        }
        return servicesList;
    }

    public boolean containsKeyword(Model_Service service, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        String id = service.getId().toLowerCase();
        String name = service.getName().toLowerCase();
        String price = String.valueOf(service.getPrice()).toLowerCase();
        String unit = service.getUnit().toLowerCase();
        String status = service.getStatus().toLowerCase();

        return id.contains(lowerCaseKeyword)
                || name.contains(lowerCaseKeyword)
                || price.contains(lowerCaseKeyword)
                || unit.contains(lowerCaseKeyword)
                || status.contains(lowerCaseKeyword);
    }
}