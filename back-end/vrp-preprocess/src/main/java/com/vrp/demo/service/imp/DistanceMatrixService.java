package com.vrp.demo.service.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;
import com.vrp.demo.entity.tenant.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DistanceMatrixService {

//    @Autowired
//    private CustomerService customerService;
//    @Autowired
//    private DepotService depotService;
//    @Autowired
//    @Lazy
//    private CorrelationService correlationService;

    private static Logger logger = LoggerFactory.getLogger(DistanceMatrixService.class);

    private static final String API_KEY = "AIzaSyAmoJa0osaR-pbmamp2GTxdAzE-rBKF1hs";
    static GeoApiContext context = new GeoApiContext.Builder()
            .apiKey(API_KEY)
            .build();

    public static String listToString(List<Long> row) {
        String rowString = "";
        for (Long value : row) {
            rowString = rowString + "\t" + String.valueOf(value);
        }
        return rowString;
    }

//    @Async
//    public void createCorrelations(NodeModel node) {
//        List<NodeModel> nodeModels = getAllNode();
//        setCorrelation(node, nodeModels);
//        return;
//    }

//    public List<NodeModel> getAllNode() {
//        List<NodeModel> nodeModels = new ArrayList<>();
//        CustomerSearch customerSearch = new CustomerSearch();
//        customerSearch.setPaged(false);
//        nodeModels.addAll(customerService.find(customerSearch));
//        DepotSearch depotSearch = new DepotSearch();
//        depotSearch.setPaged(false);
//        nodeModels.addAll(depotService.find(depotSearch));
//        return nodeModels;
//    }

//    public void createCorrelations() {
//        List<NodeModel> nodeModels = getAllNode();
//        for (NodeModel nodeModel : nodeModels) {
//            setCorrelation(nodeModel, nodeModels);
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                System.out.println(e);
//            }
//        }
//    }

//    public void setCorrelation(NodeModel originNode, List<NodeModel> destinationNodes) {
//        List<Correlation> correlations = new ArrayList<>();
//        List<String> originList = Arrays.asList(originNode.getName());
//        List<String> destinationList = new ArrayList<>();
//        List<List<Long>> timeMatrix = new ArrayList<List<Long>>();
//        List<List<Long>> distanceMatrix = new ArrayList<List<Long>>();
//        for (NodeModel nodeModel : destinationNodes) {
//            destinationList.add(nodeModel.getName());
//        }
//        timeMatrix = getDistTimeMatrix(originList, destinationList, distanceMatrix);
//        for (int i = 0; i < destinationNodes.size(); i++) {
//            NodeModel destinationNode = destinationNodes.get(i);
//            Correlation correlation = new Correlation();
//            correlation.setFromNodeId(originNode.getId());
//            correlation.setFromNodeCode(originNode.getCode());
//            correlation.setFromNodeName(originNode.getName());
//            correlation.setFromNodeType(originNode.getCode().contains("C") ? NodeType.CUSTOMER : NodeType.DEPOT);
//            correlation.setToNodeId(destinationNode.getId());
//            correlation.setToNodeCode(destinationNode.getCode());
//            correlation.setToNodeName(destinationNode.getName());
//            correlation.setToNodeType(destinationNode.getCode().contains("C") ? NodeType.CUSTOMER : NodeType.DEPOT);
//            correlation.setDistance(distanceMatrix.get(0).get(i).intValue());
//            correlation.setTime(timeMatrix.get(0).get(i).intValue());
//            correlation.setRiskProbability(0.01);
//            correlations.add(correlation);
//        }
//        try {
//            correlations = correlationService.create(correlations);
//        } catch (CustomException e) {
//            e.printStackTrace();
//        }
//    }

    private void waitMatrixResponse(DistanceMatrix distanceMatrix, DistanceMatrixApiRequest req, String[] origins, String[] destinations) {
        while (distanceMatrix == null) {
            try {
                Thread.sleep(10 * 1000);
                logger.info("Resend Request");
                distanceMatrix = req.origins(origins).destinations(destinations).mode(TravelMode.DRIVING).await();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        logger.info("Response Success");
    }

    public List<List<Long>> getDistTimeMatrix(List<String> originList, List<String> destinationList, List<List<Long>> distanceMatrix) {
        String[] origins = new String[originList.size()];
        String[] destinations = new String[destinationList.size()];

        origins = originList.toArray(origins);
        destinations = destinationList.toArray(destinations);

        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
        List<List<Long>> distanceTimeMatrix = new ArrayList<List<Long>>();
        DistanceMatrix t = null;
//        DistanceMatrix t2 = null;
        try {
            t = req.origins(origins).destinations(destinations).mode(TravelMode.DRIVING).await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
//            waitMatrixResponse(t2, req, origins, destinations);
        }

        for (int i = 0; i < origins.length; i++) {
            List<Long> rowTime = new ArrayList<>();
            List<Long> rowDistance = new ArrayList<>();
//            if (t2 == null) {
                for (int j = 0; j < destinations.length; j++) {
                    try {
                        rowTime.add(t.rows[i].elements[j].duration.inSeconds);
                        rowDistance.add(t.rows[i].elements[j].distance.inMeters);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
//            } else {
//                for (int j = 0; j < destinations.length; j++) {
//                    rowTime.add(t2.rows[i].elements[j].duration.inSeconds);
//                    rowDistance.add(t2.rows[i].elements[j].distance.inMeters);
//                }
//            }
            distanceTimeMatrix.add(rowTime);
            distanceMatrix.add(rowDistance);
        }
        return distanceTimeMatrix;
    }

    public Node getCoordinate(Node node) {
        String address = node.getName();
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.newRequest(context).address(address).await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (results.length < 1) {
            System.err.println(results);
        }
        System.out.println(gson.toJson(results[0].geometry.location));
        node.setLatitude(results[0].geometry.location.lat);
        node.setLongitude(results[0].geometry.location.lng);
        return node;
    }

    public static void writeOutput(List<String> originList, List<String> destinationList, FileWriter fileWriter, List<List<Long>> distanceMatrix) {
        try {
            for (String destination : destinationList) {
                fileWriter.write("\t" + destination);
            }
            fileWriter.write("\n");
            for (int i = 0; i < originList.size(); i++) {
                fileWriter.write(originList.get(i) + listToString(distanceMatrix.get(i)) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ;

    public static void main(String[] args) {

        List<String> originList = new ArrayList<String>(Arrays.asList("Hanoi", "HoChiMinh City", "Nghe An", "Da Nang", "Can tho"));
        List<String> destinationList = new ArrayList<String>(Arrays.asList("Hanoi", "HoChiMinh City", "Nghe An", "Da Nang", "Can tho"));

        String[] origins = new String[originList.size()];
        String[] destinations = new String[destinationList.size()];

        origins = originList.toArray(origins);
        destinations = destinationList.toArray(destinations);

        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
        try {
            DistanceMatrix t = req.origins(origins).destinations(destinations).mode(TravelMode.DRIVING).await();
            System.out.println(t);
            File file = new File("Matrix.txt");
            FileWriter outFile = new FileWriter(file);
            List<List<Long>> distanceMatrix = new ArrayList<List<Long>>();
            for (int i = 0; i < origins.length; i++) {
                List<Long> row = new ArrayList<>();
                for (int j = 0; j < destinations.length; j++) {
                    row.add(t.rows[i].elements[j].distance.inMeters);
                }
                distanceMatrix.add(row);
            }
            writeOutput(originList, destinationList, outFile, distanceMatrix);
            outFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
