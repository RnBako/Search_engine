import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.opencsv.CSVReader;
import org.bson.Document;

import java.io.FileReader;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        try {
            CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\AVK\\Documents\\Java\\java_basics\\18_NoSQL\\MongoUpload\\src\\main\\resources\\mongo.csv"));
            List<String[]> lines = csvReader.readAll();

            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase db = mongoClient.getDatabase("Students");
            MongoCollection<Document> collection = db.getCollection("Students");

            lines.forEach(element -> {
                Document document = new Document();
                document.put("name", element[0]);
                document.put("age", element[1]);
                document.put("courses", element[2]);
                collection.insertOne(document);
            });

            System.out.println("Общее количество студентов в базе - " + collection.countDocuments());

            Iterable<Document> documentsMoreThanForty = collection.find(new Document("age", new Document("$gt", "40")));
            AtomicInteger countMoreThanForty = new AtomicInteger();
            documentsMoreThanForty.forEach(document -> {
                countMoreThanForty.getAndIncrement();
            });
            System.out.println("Количество студентов старше 40 лет - " + countMoreThanForty);

            Iterable<Document> documentsYounger = collection.find().sort(new Document("age",1)).limit(1);
            documentsYounger.forEach(document -> {
                System.out.println("Имя самого молодого студента - " + document.get("name"));
            });

            Iterable<Document> documentsOldest = collection.find().sort(new Document("age",-1)).limit(1);
            documentsOldest.forEach(document -> {
                System.out.println("Список курсов самого старого студента: " + document.get("courses"));
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
