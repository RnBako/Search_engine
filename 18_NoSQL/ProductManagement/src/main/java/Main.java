import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Введите команду (доступны ДОБАВИТЬ_МАГАЗИН, ДОБАВИТЬ_ТОВАР, ВЫСТАВИТЬ_ТОВАР , СТАТИСТИКА_ТОВАРОВ, ВЫХОД): ");
            String command = scanner.nextLine();

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase db = mongoClient.getDatabase("Markets");
            MongoCollection<Document> marketCollection = db.getCollection("Market");
            MongoCollection<Document> productCollection = db.getCollection("Product");

            if (command.matches("ВЫХОД")) {
                break;
            } else if (command.matches("СТАТИСТИКА_ТОВАРОВ")) {
                Iterable<Document> markets = marketCollection.find();
                markets.forEach(market -> {
                    System.out.println("Товары магазина \"" + market.get("name") +"\": " + market.get("products"));
                });
                System.out.println("/-------------------------------------------------------------/");

                AggregateIterable<Document> avgPrices = productCollection.aggregate(Arrays.asList(
                        new Document("$lookup", new Document("from", "Market").append("localField", "name").append("foreignField", "products").append("as", "market_list")),
                        new Document("$unwind", new Document("path", "$market_list")),
                        new Document("$group", new Document("_id",new Document("name", "$market_list.name")).
                                                     append("avgPrice", new Document("$avg", "$price")).
                                                     append("minPrice", new Document("$min", "$price")).
                                                     append("maxPrice", new Document("$max", "$price")).
                                                     append("cntPrice", new Document("$count", new Document())))
                ));
                for (Document avgPrice : avgPrices) {
                    Document market = (Document) avgPrice.get("_id");
                    System.out.println("Минимальная цена товаров для \"" + market.get("name") + "\": " + avgPrice.get("minPrice"));
                    System.out.println("Средняя цена товаров для \"" + market.get("name") + "\": " + avgPrice.get("avgPrice"));
                    System.out.println("Максимальная цена товаров для \"" + market.get("name") + "\": " + avgPrice.get("maxPrice"));
                    System.out.println("Количество товаров для \"" + market.get("name") + "\": " + avgPrice.get("cntPrice"));
                    System.out.println("<----->");
                }
                System.out.println("/-------------------------------------------------------------/");

                AggregateIterable<Document> cntPrices = productCollection.aggregate(Arrays.asList(
                        new Document("$lookup", new Document("from", "Market").append("localField", "name").append("foreignField", "products").append("as", "market_list")),
                        new Document("$match", new Document("price", new Document("$lt", 100))),
                        new Document("$unwind", new Document("path", "$market_list")),
                        new Document("$group", new Document("_id",new Document("name", "$market_list.name")).
                                append("cntPrice", new Document("$count", new Document())))
                ));
                for (Document cntPrice : cntPrices) {
                    Document market = (Document) cntPrice.get("_id");
                    System.out.println("Количество товаров дешевле 100 рублей для \"" + market.get("name") + "\": " + cntPrice.get("cntPrice"));
                }
                System.out.println("/-------------------------------------------------------------/");
            } else if (command.matches("ДОБАВИТЬ_МАГАЗИН [0-ё]+")) {
                Document market = new Document();
                String marketName = command.substring(command.indexOf(" ")).trim();
                market.put("name", marketName);
                market.put("products", new ArrayList<>());
                marketCollection.insertOne(market);
                System.out.println(marketName + " добавлен.");
            } else if (command.matches("ДОБАВИТЬ_ТОВАР [0-ё\\s]+[0-9]")) {
                Document product = new Document();
                String productName = command.substring(command.indexOf(" "), command.lastIndexOf(" ")).trim();
                int productPrice = Integer.parseInt(command.substring(command.indexOf(productName) + productName.length()).trim());
                product.put("name", productName);
                product.put("price", productPrice);
                productCollection.insertOne(product);
                System.out.println(productName + " добавлен.");
            } else if (command.matches("ВЫСТАВИТЬ_ТОВАР [0-ё]+\\s[0-ё]+")) {
                String productName = command.substring(command.indexOf(" "), command.lastIndexOf(" ")).trim();
                String marketName = command.substring(command.lastIndexOf(" ")).trim();

                Document product = productCollection.find(new Document("name", productName)).first();
                Document market = marketCollection.find(new Document("name", marketName)).first();
                if (product.isEmpty() || market.isEmpty()) {
                    System.out.println("Магазин или продукт не найдены!");
                } else {
                    ArrayList<String> products = (ArrayList<String>) market.get("products");
                    products.add(productName);
                    market.append("products", products);
                    marketCollection.updateOne(new Document("name", marketName), new Document("$set", market));
                    System.out.println("В " + marketName + " выставлено - " + productName);
                }
            } else {
                System.out.println("Введена не верная команда, попробуйте еще раз.");
            }
        }
    }
}
