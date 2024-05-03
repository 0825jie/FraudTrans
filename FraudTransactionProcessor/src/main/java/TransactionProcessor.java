import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionProcessor {
    private static final String PATH = "/src/main/java/files/";
    private static String[] input_headers = { "User ID", "Timestamp", "Merchant", "Amount"};
    private static String[] output_headers = { "User ID", "Timestamp", "Merchant", "Amount", "Result" };

    public static void main(String[] args) throws IOException {
        String filePath = new File("").getAbsolutePath();
        String csvFile = filePath.concat(PATH + "test.csv");
        // Step1: prepare data
        populateDate(csvFile);

        Map<Integer, User> users = new HashMap<>();
        // Step2: read CSV file and process data
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] nextLine;
            boolean headerRead = false;
            while ((nextLine = reader.readNext()) != null) {
                if (!headerRead) {
                    headerRead = true;
                    continue;
                }
                // if data is invalid, skip this line
                if (!Util.isValid(nextLine)) {
                    continue;
                }
                Transaction transaction = new Transaction(nextLine);
                int userID = Integer.valueOf(nextLine[0]);
                User user = users.getOrDefault(userID, new User(userID));
                user.insertTransaction(transaction);
                users.put(userID, user);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        String outputPath = filePath.concat(PATH + (new Date()).getTime() + "-result.csv");
        FileWriter fileWriter = new FileWriter(outputPath);
        CSVWriter writer = new CSVWriter(fileWriter);
        writer.writeNext(output_headers);
        // Step 3: calculate risk for each transaction, write result to result.csv
        for (User user : users.values()) {
            for (Transaction transaction : user.getHistory().values()) {
                RiskCalculator.calculateRisk(transaction, user);
                writer.writeNext(transaction.result());
            }
        }
        writer.close();
    }

    private static void populateDate(String csvFile) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(csvFile, false));
        writer.writeNext(input_headers);
        List<String[]> data = Util.generateData(10000);
        for(String[] line : data) {
            writer.writeNext(line);
        }
        writer.close();
    }
}
