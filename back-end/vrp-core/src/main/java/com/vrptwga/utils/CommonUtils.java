package com.vrptwga.utils;

import com.vrptwga.concepts.Customer;
import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.concepts.Request;
import com.vrptwga.evaluate.Evaluate;
import com.vrptwga.representation.Individual;
import com.vrptwga.representation.Population;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtils {

    private static final String resultDirectory = "experiment/ga-experiment";

    public static HashMap<Integer, Double> sortByValue(HashMap<Integer, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<Integer, Double>> list =
                new LinkedList<Map.Entry<Integer, Double>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<Integer, Double> temp = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static Integer getKeyOfMinValue(HashMap<Integer, Double> hm) {
        int keyOfMin = 0;
        double min = 99999999;
        for (Integer key : hm.keySet()) {
            double value = hm.get(key);
            if (value < min) {
                min = value;
                keyOfMin = key;
            }
        }
        return keyOfMin;
    }

    public static List<Customer> getDuplicateCustomer(List<Customer> customerInputList1, List<Customer> customerInputList2) {
        List<Customer> duplicateCustomers = new ArrayList<>();
        for (Customer customerInput1 : customerInputList1) {
            for (Customer customerInput2 : customerInputList2) {
                if (customerInput1.getId() == customerInput2.getId())
                    duplicateCustomers.add(customerInput1);
            }
        }
        return duplicateCustomers;
    }

    public static List<Request> getDuplicateRequest(List<Request> requestList1, List<Request> requestList2) {
        List<Request> duplicateRequests = new ArrayList<>();
        for (Request request1 : requestList1) {
            for (Request request2 : requestList2) {
                if (request1.getId() == request2.getId())
                    duplicateRequests.add(request1);
            }
        }
        return duplicateRequests;
    }

    public static List<Customer> removeCustomer(List<Customer> customerInputList, Customer removeCustomer) {
        for (int i = 0; i < customerInputList.size(); i++) {
            if (customerInputList.get(i).getId() == removeCustomer.getId())
                customerInputList.remove(i);
        }
        return customerInputList;
    }

    public static List<Customer> removeCustomers(List<Customer> customerInputList, List<Customer> removeCustomers) {
        for (Customer removeCustomer : removeCustomers) {
            removeCustomer(customerInputList, removeCustomer);
        }
        return customerInputList;
    }

    public static List<Request> removeRequest(List<Request> requestList, Request request) {
        for (int i = 0; i < requestList.size(); i++) {
            if (requestList.get(i).getId() == request.getId())
                requestList.remove(i);
        }
        return requestList;
    }

    public static List<Request> removeRequests(List<Request> requestList, List<Request> removeRequests) {
        for (Request removeRequest : removeRequests) {
            removeRequest(requestList, removeRequest);
        }
        return requestList;
    }

    public static String createExperimentDirectory() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date = new Date();
        String directoryName = dateFormat.format(date);
        String directoryExperimentPath = resultDirectory + File.separator + directoryName;
        File directoryExperiment = new File(directoryExperimentPath);
        if (!directoryExperiment.exists()) {
            directoryExperiment.mkdirs();
        }
        String algorithmLogDirectoryPath = directoryExperimentPath + File.separator + "experiment-result.xlsx";
        File algorithmLogDirectory = new File(algorithmLogDirectoryPath);
        if (!algorithmLogDirectory.exists()) {
            try {
                algorithmLogDirectory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writeHeader(algorithmLogDirectoryPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return algorithmLogDirectoryPath;
    }

    public static void writeHeader(String algorithmLogDirectoryPath) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Population evolution");
        XSSFRow headerRow = sheet.createRow(0);
        XSSFCell generationCell = headerRow.createCell(0);
        generationCell.setCellValue("Generation");
        XSSFCell minObjectiveValueCell = headerRow.createCell(1);
        minObjectiveValueCell.setCellValue("Min Objective Value");
        XSSFCell minFitnessCell = headerRow.createCell(2);
        minFitnessCell.setCellValue("Max Fitness");
        XSSFCell averageObjectiveCell = headerRow.createCell(3);
        averageObjectiveCell.setCellValue("Average Objective Value Of Elite");
        XSSFCell maxObjectiveValueCell = headerRow.createCell(4);
        maxObjectiveValueCell.setCellValue("Max Objective Value");
        XSSFCell maxFitnessCell = headerRow.createCell(5);
        maxFitnessCell.setCellValue("Min Fitness");

        FileOutputStream fileOut = new FileOutputStream(algorithmLogDirectoryPath);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    public static void writeValuePopulation(Population population, String pathFile) {
        int rowNumber = population.getCurrentGeneration() + 1;
        int generation = population.getCurrentGeneration();
        List<Individual> currentPopulation = population.getIndividuals();
        currentPopulation.sort(Comparator.comparingDouble(Individual::getObjectiveValue));
        Individual bestIndividual = currentPopulation.get(0);
        Individual worstIndividual = currentPopulation.get(currentPopulation.size() - 1);
        double averageObjectiveValueElite = population.getElites().stream()
                .mapToDouble(Individual::getObjectiveValue)
                .average().orElse(Double.NaN);
        try {
            InputStream inp = new FileInputStream(pathFile);
            Workbook workbook = WorkbookFactory.create(inp);
            XSSFSheet sheet = (XSSFSheet) workbook.getSheet("Population evolution");

            XSSFRow headerRow = sheet.createRow(rowNumber);
            XSSFCell generationCell = headerRow.createCell(0);
            generationCell.setCellValue(generation);
            XSSFCell minObjectiveValueCell = headerRow.createCell(1);
            minObjectiveValueCell.setCellValue(bestIndividual.getObjectiveValue());
            XSSFCell maxFitnessCell = headerRow.createCell(2);
            maxFitnessCell.setCellValue(bestIndividual.getFitness());
            XSSFCell averageObjectiveValue = headerRow.createCell(3);
            averageObjectiveValue.setCellValue(averageObjectiveValueElite);
            XSSFCell maxObjectiveValueCell = headerRow.createCell(4);
            maxObjectiveValueCell.setCellValue(worstIndividual.getObjectiveValue());
            XSSFCell minFitnessCell = headerRow.createCell(5);
            minFitnessCell.setCellValue(worstIndividual.getFitness());
            FileOutputStream fileOut = new FileOutputStream(pathFile);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void writeValue(Population population, String pathFile) {
        int rowNumber = 1;
        List<Individual> currentPopulation = population.getIndividuals();
        currentPopulation.sort(Comparator.comparingDouble(Individual::getObjectiveValue).reversed());
        InputStream inp = null;
        try {
            inp = new FileInputStream(pathFile);
            Workbook workbook = WorkbookFactory.create(inp);
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("data");
            for (int i = 0; i < currentPopulation.size(); i++) {
                Individual individual = currentPopulation.get(i);
                rowNumber = i+1;
                XSSFRow headerRow = sheet.createRow(rowNumber);
                XSSFCell generationCell = headerRow.createCell(0);
                generationCell.setCellValue(individual.getFitness());
                XSSFCell minObjectiveValueCell = headerRow.createCell(1);
                minObjectiveValueCell.setCellValue(individual.getObjectiveValue());
                FileOutputStream fileOut = new FileOutputStream(pathFile);
                workbook.write(fileOut);
                fileOut.flush();
                fileOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writeParameterConfigHeader(OptimizationScenario optimizationScenario, String pathFile) {
        try {
            InputStream inp = new FileInputStream(pathFile);
            Workbook workbook = WorkbookFactory.create(inp);
            XSSFSheet sheet = (XSSFSheet) workbook.createSheet("Parameter Configuration");
            XSSFRow headerRow = sheet.createRow(0);
            XSSFCell popSizeCell = headerRow.createCell(0);
            popSizeCell.setCellValue("population size");
            XSSFCell genNumCell = headerRow.createCell(1);
            genNumCell.setCellValue("max generation number");
            XSSFCell eliteRateCell = headerRow.createCell(2);
            eliteRateCell.setCellValue("elite rate");
            XSSFCell crossoverProbCell = headerRow.createCell(3);
            crossoverProbCell.setCellValue("crossover probability");
            XSSFCell mutationProbCell = headerRow.createCell(4);
            mutationProbCell.setCellValue("mutation probability");
            XSSFCell notImproveCell = headerRow.createCell(5);
            notImproveCell.setCellValue("not improve generation num");

            XSSFRow valueRow = sheet.createRow(1);
            XSSFCell popSizeValueCell = valueRow.createCell(0);
            popSizeValueCell.setCellValue(optimizationScenario.getPopSize());
            XSSFCell genNumValueCell = valueRow.createCell(1);
            genNumValueCell.setCellValue(optimizationScenario.getMaxGen());
            XSSFCell eliteRateValueCell = valueRow.createCell(2);
            eliteRateValueCell.setCellValue(optimizationScenario.getEliteRate());
            XSSFCell crossoverProbValueCell = valueRow.createCell(3);
            crossoverProbValueCell.setCellValue(optimizationScenario.getProbCrossover());
            XSSFCell mutationProbValueCell = valueRow.createCell(4);
            mutationProbValueCell.setCellValue(optimizationScenario.getProbMutation());
            XSSFCell notImproveValueCell = valueRow.createCell(5);
            notImproveValueCell.setCellValue(optimizationScenario.getImprove());

            FileOutputStream fileOut = new FileOutputStream(pathFile);
            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkOverTime(long startTime, long allowExecuteTime) {
        return System.currentTimeMillis() - startTime > allowExecuteTime;
    }

}
