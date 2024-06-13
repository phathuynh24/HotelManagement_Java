package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.raven.model.Model_Customer;
import com.raven.utils.MongoDBConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bson.Document;

public class CustomerController {

    private final MongoCollection<Document> customerCollection;

    public CustomerController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        customerCollection = database.getCollection(MongoDBConfig.COLLECTION_CUSTOMER);
    }
    
    public void addCustomer(Model_Customer customer) {
        Document customerDocument = new Document()
                .append("idCard", customer.getIdCard())
                .append("name", customer.getName())
                .append("gender", customer.getGender())
                .append("phone", customer.getPhone())
                .append("email", customer.getEmail())
                .append("address", customer.getAddress());

        customerCollection.insertOne(customerDocument);
    }

    public void deleteCustomer(String idCard) {
        customerCollection.deleteOne(new Document("idCard", idCard));
    }

    public void updateCustomer(String idCard, Model_Customer updatedCustomer) {
        Document updatedCustomerDocument = new Document()
                .append("idCard", updatedCustomer.getIdCard())
                .append("name", updatedCustomer.getName())
                .append("gender", updatedCustomer.getGender())
                .append("phone", updatedCustomer.getPhone())
                .append("email", updatedCustomer.getEmail())
                .append("address", updatedCustomer.getAddress());

        customerCollection.replaceOne(new Document("idCard", idCard), updatedCustomerDocument);
    }

    public Model_Customer getCustomerByIdCard(String idCard) {
        Document customerDocument = customerCollection.find(Filters.eq("idCard", idCard)).first();
        if (customerDocument != null) {
            return new Model_Customer(
                    customerDocument.getString("idCard"),
                    customerDocument.getString("name"),
                    customerDocument.getString("gender"),
                    customerDocument.getString("phone"),
                    customerDocument.getString("email"),
                    customerDocument.getString("address")
            );
        }
        return null;
    }

    public boolean isCCCDExist(String idCard) {
        Document customerDocument = customerCollection.find(Filters.eq("idCard", idCard)).first();
        return customerDocument != null;
    }

    public List<Model_Customer> getAllCustomers() {
        List<Model_Customer> customersList = new ArrayList<>();
        FindIterable<Document> customerDocuments = customerCollection.find();
        for (Document customerDocument : customerDocuments) {
            Model_Customer customer = new Model_Customer(
                    customerDocument.getString("idCard"),
                    customerDocument.getString("name"),
                    customerDocument.getString("gender"),
                    customerDocument.getString("phone"),
                    customerDocument.getString("email"),
                    customerDocument.getString("address")
            );
            customersList.add(customer);
        }

        // Sắp xếp danh sách khách hàng theo ID Card
        Collections.sort(customersList, (Model_Customer customer1, Model_Customer customer2) -> {
            return customer1.getIdCard().compareTo(customer2.getIdCard());
        });

        return customersList;
    }

    public String[] getAllNameCustomers(List<Model_Customer> customerList) {
        String[] nameList = new String[customerList.size()];

        for (int i = 0; i < customerList.size(); i++) {
            Model_Customer customer = customerList.get(i);
            String name = customer.getName();
            nameList[i] = name;
        }

        return nameList;
    }

    public Model_Customer getCustomersByName(String name, List<Model_Customer> customerList) {
        Model_Customer customer = null;
        for (Model_Customer c : customerList) {
            if (c.getName().equals(name)) {
                customer = c;
                break;
            }
        }
        return customer;
    }

    public List<Model_Customer> searchCustomers(String keyword) {
        List<Model_Customer> customersList = new ArrayList<>();
        FindIterable<Document> customerDocuments = customerCollection.find();
        String lowerCaseKeyword = keyword.toLowerCase();
        for (Document customerDocument : customerDocuments) {
            Model_Customer customer = new Model_Customer(
                    customerDocument.getString("idCard"),
                    customerDocument.getString("name"),
                    customerDocument.getString("gender"),
                    customerDocument.getString("phone"),
                    customerDocument.getString("email"),
                    customerDocument.getString("address")
            );
            if (containsKeyword(customer, lowerCaseKeyword)) {
                customersList.add(customer);
            }
        }
        return customersList;
    }

    public boolean containsKeyword(Model_Customer customer, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        String idCard = customer.getIdCard().toLowerCase();
        String name = customer.getName().toLowerCase();
        String gender = customer.getGender().toLowerCase();
        String phone = customer.getPhone().toLowerCase();
        String email = customer.getEmail().toLowerCase();
        String address = customer.getAddress().toLowerCase();

        return idCard.contains(lowerCaseKeyword)
                || name.contains(lowerCaseKeyword)
                || gender.contains(lowerCaseKeyword)
                || phone.contains(lowerCaseKeyword)
                || email.contains(lowerCaseKeyword)
                || address.contains(lowerCaseKeyword);
    }

    public Model_Customer documentToCustomer(Document customerDocument) {
        String name = customerDocument.getString("name");
        String gender = customerDocument.getString("gender");
        String phone = customerDocument.getString("phone");
        String idCard = customerDocument.getString("idCard");
        String address = customerDocument.getString("address");
        String email = customerDocument.getString("email");

        return new Model_Customer(name, gender, phone, idCard, address, email);
    }
}
