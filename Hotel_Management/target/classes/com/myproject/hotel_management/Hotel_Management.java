package com.myproject.hotel_management;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.myproject.dashboard.Dashboard;
import org.bson.Document;

public class Hotel_Management {

    public static void main(String[] args) {
        Dashboard DashboardFrame = new Dashboard();
        DashboardFrame.setVisible(true);
//        NewJFrame DashboardFrame = new NewJFrame();
//        DashboardFrame.setVisible(true);
        MongoClient client = MongoClients.create("mongodb+srv://HotelGroup:xfwl2Y6oahXJugda@cluster0.awr6sf9.mongodb.net/");
        
        MongoDatabase db = client.getDatabase("Hotel_Management");
        
        MongoCollection col = db.getCollection("User");
        
        /*
        _id: 65e6a3a7b478cbf0f31db114
        name: "Duy"
        */
        
        FindIterable<Document> documents = col.find();

        for (Document document : documents) {
            System.out.println(document.toJson());
        }
    }
}
