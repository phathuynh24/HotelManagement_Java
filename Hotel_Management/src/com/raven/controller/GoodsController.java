package com.raven.controller;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.raven.model.Model_Goods;
import com.raven.utils.MongoDBConfig;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class GoodsController {

    private final MongoCollection<Document> goodsCollection;

    public GoodsController() {
        MongoClient mongoClient = MongoClients.create(MongoDBConfig.MONGODB_URI);
        MongoDatabase database = mongoClient.getDatabase(MongoDBConfig.DATABASE_NAME);
        goodsCollection = database.getCollection(MongoDBConfig.COLLECTION_GOODS);
    }
    
    public String createId() {
        Document lastFloor = goodsCollection.find().sort(Sorts.descending("code")).first();
        
        if (lastFloor != null) {
            String lastId = lastFloor.getString("code");
            int numericPart = Integer.parseInt(lastId.substring(2));
            numericPart++;
            return String.format("HH%04d", numericPart);
        } else {
            return "HH0000";
        }
    }

    public void addGoods(Model_Goods goods) {
        Document goodsDocument = new Document()
                .append("code", goods.getId())
                .append("name", goods.getName())
                .append("unit", goods.getUnit())
                .append("importPrice", goods.getImportPrice())
                .append("sellPrice", goods.getSellPrice())
                .append("description", goods.getDescription())
                .append("status", goods.getStatus());

        goodsCollection.insertOne(goodsDocument);
    }

    public void deleteGoods(String goodsId) {
        goodsCollection.deleteOne(Filters.eq("code", goodsId));
    }

    public void updateGoods(String goodsId, Model_Goods updatedGoods) {
        Document updatedGoodsDocument = new Document()
                .append("code", updatedGoods.getId())
                .append("name", updatedGoods.getName())
                .append("unit", updatedGoods.getUnit())
                .append("importPrice", updatedGoods.getImportPrice())
                .append("sellPrice", updatedGoods.getSellPrice())
                .append("description", updatedGoods.getDescription())
                .append("status", updatedGoods.getStatus());

        goodsCollection.replaceOne(Filters.eq("code", goodsId), updatedGoodsDocument);
    }

    public Model_Goods getGoodsById(String goodsId) {
        Document goodsDocument = goodsCollection.find(Filters.eq("code", goodsId)).first();
        if (goodsDocument != null) {
            return new Model_Goods(
                goodsDocument.getString("code"),
                goodsDocument.getString("name"),
                goodsDocument.getString("unit"),
                goodsDocument.getInteger("importPrice"),
                goodsDocument.getInteger("sellPrice"),
                goodsDocument.getString("description"),
                goodsDocument.getString("status")
            );
        }
        return null;
    }

    public List<Model_Goods> getAllGoods() {
        List<Model_Goods> goodsList = new ArrayList<>();
        FindIterable<Document> goodsDocuments = goodsCollection.find();
        for (Document goodsDocument : goodsDocuments) {
            Model_Goods goods = new Model_Goods(
                goodsDocument.getString("code"),
                goodsDocument.getString("name"),
                goodsDocument.getString("unit"),
                goodsDocument.getInteger("importPrice"),
                goodsDocument.getInteger("sellPrice"),
                goodsDocument.getString("description"),
                goodsDocument.getString("status")
            );
            goodsList.add(goods);
        }
        return goodsList;
    }

    public boolean containsKeyword(Model_Goods goods, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        String id = goods.getId().toLowerCase();
        String name = goods.getName().toLowerCase();
        String unit = goods.getUnit().toLowerCase();
        String importPrice = String.valueOf(goods.getImportPrice()).toLowerCase();
        String sellPrice = String.valueOf(goods.getSellPrice()).toLowerCase();
        String description = goods.getDescription().toLowerCase();
        String status = goods.getStatus().toLowerCase();

        return id.contains(lowerCaseKeyword)
                || name.contains(lowerCaseKeyword)
                || unit.contains(lowerCaseKeyword)
                || importPrice.contains(lowerCaseKeyword)
                || sellPrice.contains(lowerCaseKeyword)
                || description.contains(lowerCaseKeyword)
                || status.contains(lowerCaseKeyword);
    }
}
