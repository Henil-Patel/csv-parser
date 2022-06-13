import edu.duke.*;
import java.io.File;
import org.apache.commons.csv.*;

public class Part1
{
    public void tester(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        //String info = countryInfo(parser, "Nauru");
        //System.out.println(info);
        //listExportersTwoProducts(parser, "cotton", "flowers");
        //int numFound = numberOfExporters(parser, "cocoa");
        //System.out.println(numFound);
        bigExporters(parser, "$999,999,999,999");
    }
    
    public String countryInfo(CSVParser parser, String country){
        String info = "";
        for (CSVRecord record: parser){
            String storedCountry = record.get("Country");
            if (storedCountry.equals(country)){
                info = record.get("Country") + ": " + record.get("Exports") + ": " + record.get("Value (dollars)");
                break;
            }
            else{
                info = "NOT FOUND";
            }
        }
        return info;
    }
    
    public void listExportersTwoProducts(CSVParser parser, 
                                         String exportitem1,
                                         String exportitem2){
        for (CSVRecord entry: parser){
            if (entry.get("Exports").contains(exportitem1) && entry.get("Exports").contains(exportitem2)){
                System.out.println(entry.get("Country"));
            }
            
        }
    }
    
    public int numberOfExporters(CSVParser parser, String exportitem){
        int count = 0;
        for (CSVRecord data: parser){
            if (data.get("Exports").contains(exportitem)){
                count = count + 1;
            }
        }
        return count;
    }
    
    public void bigExporters(CSVParser parser, String amount){
        for (CSVRecord record: parser){
            if (record.get("Value (dollars)").length() > amount.length()){
                System.out.println(record.get("Country") + " " + record.get("Value (dollars)"));
            }
        }
    }
    public static void main(String[] args){
        Part1 p1 = new Part1();
        p1.tester();
    }
}
