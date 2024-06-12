package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.raven.model.Model_Employee;
import com.raven.utils.MongoDBConfig;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;

public class EmployeeController {

    private final MongoCollection<Document> employeeCollection;

    public EmployeeController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        employeeCollection = database.getCollection(MongoDBConfig.COLLECTION_EMPLOYEE);
    }
    
    public String createId() {
        Document lastFloor = employeeCollection.find().sort(Sorts.descending("code")).first();
        
        if (lastFloor != null) {
            String lastId = lastFloor.getString("code");
            int numericPart = Integer.parseInt(lastId.substring(2));
            numericPart++;
            return String.format("NV%04d", numericPart);
        } else {
            return "NV0000";
        }
    }

    public void addEmployee(Model_Employee employee) {
        Document employeeDocument = new Document()
                .append("code", employee.getId())
                .append("name", employee.getName())
                .append("phone", employee.getPhone())
                .append("position", employee.getPosition())
                .append("dob", employee.getDob())
                .append("gender", employee.getGender())
                .append("email", employee.getEmail())
                .append("idCard", employee.getIdCard())
                .append("status", employee.getStatus())
                .append("userName", employee.getUserName())
                .append("passWord", employee.getPassword())
                .append("permissions", employee.getPermissions());

        employeeCollection.insertOne(employeeDocument);
    }

    public void deleteEmployee(String employeeId) {
        employeeCollection.deleteOne(new Document("code", employeeId));
    }

    public void updateEmployee(String employeeId, Model_Employee updatedEmployee) {
        Document updatedEmployeeDocument = new Document()
                .append("code", updatedEmployee.getId())
                .append("name", updatedEmployee.getName())
                .append("phone", updatedEmployee.getPhone())
                .append("position", updatedEmployee.getPosition())
                .append("dob", updatedEmployee.getDob())
                .append("gender", updatedEmployee.getGender())
                .append("email", updatedEmployee.getEmail())
                .append("idCard", updatedEmployee.getIdCard())
                .append("status", updatedEmployee.getStatus())
                .append("userName", updatedEmployee.getUserName())
                .append("passWord", updatedEmployee.getPassword())
                .append("permissions", updatedEmployee.getPermissions());

        employeeCollection.replaceOne(new Document("code", employeeId), updatedEmployeeDocument);
    }

    public Model_Employee getEmployee(String propertyName, Object propertyValue) {
        Document employeeDocument = employeeCollection.find(Filters.eq(propertyName, propertyValue)).first();
        if (employeeDocument != null) {
            Model_Employee employee = new Model_Employee();
            employee.setId(employeeDocument.getString("code"));
            employee.setName(employeeDocument.getString("name"));
            employee.setPhone(employeeDocument.getString("phone"));
            employee.setPosition(employeeDocument.getString("position"));
            employee.setDob(employeeDocument.getString("dob"));
            employee.setGender(employeeDocument.getString("gender"));
            employee.setEmail(employeeDocument.getString("email"));
            employee.setIdCard(employeeDocument.getString("idCard"));
            employee.setStatus(employeeDocument.getString("status"));
            employee.setUserName(employeeDocument.getString("userName"));
            employee.setPassword(employeeDocument.getString("passWord"));
            employee.setPermissionsFromDoc(employeeDocument.get("permissions"));
            return employee;
        }
        return null;
    }

    public List<Model_Employee> getAllEmployees() {
        List<Model_Employee> employees = new ArrayList<>();
        FindIterable<Document> employeeDocuments = employeeCollection.find();
        for (Document employeeDocument : employeeDocuments) {
            Model_Employee employee = new Model_Employee();
            employee.setId(employeeDocument.getString("code"));
            employee.setName(employeeDocument.getString("name"));
            employee.setPhone(employeeDocument.getString("phone"));
            employee.setPosition(employeeDocument.getString("position"));
            employee.setDob(employeeDocument.getString("dob"));
            employee.setGender(employeeDocument.getString("gender"));
            employee.setEmail(employeeDocument.getString("email"));
            employee.setIdCard(employeeDocument.getString("idCard"));
            employee.setStatus(employeeDocument.getString("status"));
            employee.setUserName(employeeDocument.getString("userName"));
            employee.setPassword(employeeDocument.getString("passWord"));
            employee.setPermissionsFromDoc(employeeDocument.get("permissions"));
            employees.add(employee);
        }
        return employees;
    }

    public Model_Employee getEmployeeById(String employeeId) {
        return getEmployee("code", employeeId);
    }

    public List<Model_Employee> getEmployeesByName(String name) {
        List<Model_Employee> employees = new ArrayList<>();
        FindIterable<Document> employeeDocuments = employeeCollection.find(Filters.eq("name", name));
        for (Document employeeDocument : employeeDocuments) {
            Model_Employee employee = new Model_Employee();
            employee.setId(employeeDocument.getString("code"));
            employee.setName(employeeDocument.getString("name"));
            employee.setPhone(employeeDocument.getString("phone"));
            employee.setPosition(employeeDocument.getString("position"));
            employee.setDob(employeeDocument.getString("dob"));
            employee.setGender(employeeDocument.getString("gender"));
            employee.setEmail(employeeDocument.getString("email"));
            employee.setIdCard(employeeDocument.getString("idCard"));
            employee.setStatus(employeeDocument.getString("status"));
            employee.setUserName(employeeDocument.getString("userName"));
            employee.setPassword(employeeDocument.getString("passWord"));
            employee.setPermissionsFromDoc(employeeDocument.get("permissions"));
            employees.add(employee);
        }
        return employees;
    }

    public List<Model_Employee> getEmployeesByPosition(String position) {
        List<Model_Employee> employees = new ArrayList<>();
        FindIterable<Document> employeeDocuments = employeeCollection.find(Filters.eq("position", position));
        for (Document employeeDocument : employeeDocuments) {
            Model_Employee employee = new Model_Employee();
            employee.setId(employeeDocument.getString("code"));
            employee.setName(employeeDocument.getString("name"));
            employee.setPhone(employeeDocument.getString("phone"));
            employee.setPosition(employeeDocument.getString("position"));
            employee.setDob(employeeDocument.getString("dob"));
            employee.setGender(employeeDocument.getString("gender"));
            employee.setEmail(employeeDocument.getString("email"));
            employee.setIdCard(employeeDocument.getString("idCard"));
            employee.setStatus(employeeDocument.getString("status"));
            employee.setUserName(employeeDocument.getString("userName"));
            employee.setPassword(employeeDocument.getString("passWord"));
            employee.setPermissionsFromDoc(employeeDocument.get("permissions"));
            employees.add(employee);
        }
        return employees;
    }

    public boolean containsKeyword(Model_Employee employee, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return employee.getId().toLowerCase().contains(lowerCaseKeyword)
                || employee.getName().toLowerCase().contains(lowerCaseKeyword)
                || employee.getPhone().toLowerCase().contains(lowerCaseKeyword)
                || employee.getPosition().toLowerCase().contains(lowerCaseKeyword)
                || employee.getDob().toLowerCase().contains(lowerCaseKeyword)
                || employee.getGender().toLowerCase().contains(lowerCaseKeyword)
                || employee.getEmail().toLowerCase().contains(lowerCaseKeyword)
                || employee.getIdCard().toLowerCase().contains(lowerCaseKeyword)
                || employee.getStatus().toLowerCase().contains(lowerCaseKeyword)
                || employee.getUserName().toLowerCase().contains(lowerCaseKeyword);
    } 
    
    public void updatePermissions(String userId, Map<String, String> permissions) {
            Document updateDocument = new Document("$set", new Document("permissions", permissions));
            employeeCollection.updateOne(Filters.eq("code", new ObjectId(userId)), updateDocument, new UpdateOptions().upsert(true));
    }
}
