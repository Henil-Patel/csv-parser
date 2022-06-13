import edu.duke.*;
import java.io.File;
import org.apache.commons.csv.*;

/**
 * Keys
 * TimeEST,TemperatureF,Dew PointF,Humidity,Sea Level PressureIn,
 * VisibilityMPH, Wind Direction,Wind SpeedMPH,Gust SpeedMPH,
 * PrecipitationIn,Events,Conditions, WindDirDegrees,DateUTC
 */


public class Part1
{
    public void tester(){
        //testColdestHourInFile();
        testFileWithColdestTemperature();
        //testLowestHumidityInFile();
        //testLowestHumidityInManyFiles();
        //testAverageTemperatureInFile();
        //testAverageTemperatureWithHighHumidityInFile();
    }
    
    public void testColdestHourInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord coldestHour = coldestHourInFile(parser);
        String temp = coldestHour.get("TemperatureF");
        String time = coldestHour.get("DateUTC");
        System.out.println("Coldest temperature was " + temp + " at " + time);
    }
    
    public void testFileWithColdestTemperature(){
        String fileLocation = fileWithColdestTemperature();
        int separatorIndex = fileLocation.lastIndexOf("/");
        String fileName = fileLocation.substring(separatorIndex + 1, fileLocation.length());
        System.out.println("Coldest day was in file " + fileName);
        FileResource fr = new FileResource(fileLocation);
        CSVParser parser = fr.getCSVParser();
        CSVRecord coldestRecord = coldestHourInFile(parser);
        String coldestTemp = coldestRecord.get("TemperatureF");
        System.out.println("Coldest temperature on that day was " + coldestTemp);
        System.out.println("All the Temperatures on the coldest day were: ");
        CSVParser reuse = fr.getCSVParser();
        for (CSVRecord record: reuse){
            String time = record.get("TimeEST");
            String date = record.get("DateUTC");
            String temp = record.get("TemperatureF");
            System.out.println(date + " " + time + " " + temp);
        }
        
    }
    
    public void testLowestHumidityInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord lowestHumidityRecord = lowestHumidityInFile(parser);
        String lowestHumidity = lowestHumidityRecord.get("Humidity");
        String dateUTC = lowestHumidityRecord.get("DateUTC");
        System.out.println("Lowest humidity was " + lowestHumidity + " at " + dateUTC);
    }
    
    public void testLowestHumidityInManyFiles(){
        String fileLocation = lowestHumidityInManyFiles();
        int separatorIndex = fileLocation.lastIndexOf("/");
        String fileName = fileLocation.substring(separatorIndex + 1, fileLocation.length());
        FileResource fr = new FileResource(fileLocation);
        CSVParser parser = fr.getCSVParser();
        CSVRecord lowestHumidityRecord = lowestHumidityInFile(parser);
        String lowestHumidity = lowestHumidityRecord.get("Humidity");
        String dateUTC = lowestHumidityRecord.get("DateUTC");
        System.out.println("Lowest humidity was " + lowestHumidity + " at " + dateUTC);
    }
    
    public void testAverageTemperatureInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double averageTemperature = averageTemperatureInFile(parser);
        System.out.println("Average temperature in file is : " + averageTemperature);
    }
    
    public void testAverageTemperatureWithHighHumidityInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        double averageTemperatureWithHumidity = averageTemperatureWithHighHumidityInFile(parser, 80);
        System.out.println("Average temp when high humidity is " + averageTemperatureWithHumidity);
    }
    
    public CSVRecord coldestHourInFile(CSVParser parser){
        CSVRecord coldestHour = null;
        for (CSVRecord currentRecord: parser){
            coldestHour = compareRecords(coldestHour, currentRecord, "TemperatureF");
        }
        return coldestHour;
    }
    public double averageTemperatureInFile(CSVParser parser){
        double averageTemperature = 0.0;
        int sampleSize = 0;
        double runningTotal = 0.0;
        for (CSVRecord record: parser){
            double tempRecord = Double.parseDouble(record.get("TemperatureF"));
            if (tempRecord != -9999){
                sampleSize = sampleSize + 1;
                runningTotal = runningTotal + tempRecord; 
            }
        }
        averageTemperature = (double) runningTotal / sampleSize;
        return averageTemperature; 
    }
    public String fileWithColdestTemperature(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord globalColdestHour = null;
        String coldestFile = null;
        for (File f: dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser();
            CSVRecord localColdestHour = coldestHourInFile(parser);
            if (globalColdestHour == null){
                globalColdestHour = localColdestHour;
            }
            else{
                double localTemp = Double.parseDouble(localColdestHour.get("TemperatureF"));
                double globalTemp = Double.parseDouble(globalColdestHour.get("TemperatureF"));
                if (localTemp < globalTemp && localTemp != -9999 && globalTemp != -9999){
                    globalColdestHour = localColdestHour;
                    coldestFile = f.toString();
                }
            }
            
        }
        System.out.println(coldestFile);
        return coldestFile;
    }
    public String lowestHumidityInManyFiles(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord globalLowestHumidity = null;
        String lowestHumidityFile = null;
        for (File f: dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVParser parser = fr.getCSVParser();
            CSVRecord localLowestHumidity = lowestHumidityInFile(parser);
            if (globalLowestHumidity == null){
                globalLowestHumidity = localLowestHumidity;
            }
            else{
                double localHumidity = Double.parseDouble(localLowestHumidity.get("Humidity"));
                double globalHumidity = Double.parseDouble(globalLowestHumidity.get("Humidity"));
                if (localHumidity < globalHumidity){
                    globalLowestHumidity = localLowestHumidity;
                    lowestHumidityFile = f.toString();
                }
            }
        }
        return lowestHumidityFile;
    }
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowestHumidity = null;
        for (CSVRecord currHumidity: parser){
            lowestHumidity = compareRecords(lowestHumidity, currHumidity, "Humidity");
        }
        return lowestHumidity;
    }
    
    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
        double averageTemperatureWithHumidity = 0.0;
        int sampleSize = 0;
        double runningTotal = 0.0;
        for (CSVRecord record: parser){
            String humidityString = record.get("Humidity");
            //System.out.println("Humidity: " + humidityString);
            if (humidityString != "N/A"){
                double humidityVal = Double.parseDouble(humidityString);
                if (humidityVal >= (double)value){
                    double tempVal = Double.parseDouble(record.get("TemperatureF"));
                    runningTotal = runningTotal + tempVal;
                    sampleSize = sampleSize + 1;
                }
            }
        }
        if (sampleSize == 0){
            System.out.println("No temperatures with that humidity");
        }
        averageTemperatureWithHumidity = (double) runningTotal / sampleSize;
        return averageTemperatureWithHumidity;
    }
    
    public CSVRecord compareRecords(CSVRecord global, CSVRecord local, String key){
        if (global == null){
                global = local;
        }
        else{
            
            String localRecString = local.get(key);
            String globalRecString = global.get(key);
            
            if (globalRecString.contains("N/A") || localRecString.contains("N/A")){
                System.out.println("PASS");
                
            }
            else{
                System.out.println(localRecString);
                double localRec = Double.parseDouble(localRecString);
                double globalRec = Double.parseDouble(globalRecString);
                if (localRec < globalRec){
                    global = local;
                }
            }
        }
        return global;
    }
    
    public static void main(String[] args){
        Part1 p1 = new Part1();
        p1.tester();
    }
}
