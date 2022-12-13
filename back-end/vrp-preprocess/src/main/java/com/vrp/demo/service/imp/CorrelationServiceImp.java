package com.vrp.demo.service.imp;

import com.google.common.collect.Lists;
import com.vrp.demo.entity.tenant.Correlation;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.CorrelationModel;
import com.vrp.demo.models.NodeModel;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.enu.NodeType;
import com.vrp.demo.models.search.CorrelationSearch;
import com.vrp.demo.models.search.CustomerSearch;
import com.vrp.demo.models.search.DepotSearch;
import com.vrp.demo.repository.CorrelationRepository;
import com.vrp.demo.service.CorrelationService;
import com.vrp.demo.service.CustomerService;
import com.vrp.demo.service.DepotService;
import com.vrp.demo.utils.CommonUtils;
import com.vrp.demo.utils.QueryTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("correlationService")
public class CorrelationServiceImp extends BaseServiceImp<CorrelationRepository, Correlation, Long> implements CorrelationService {

    private static Logger logger = LoggerFactory.getLogger(CorrelationServiceImp.class);

    @Autowired
    private CorrelationRepository correlationRepository;
    @Autowired
    private DistanceMatrixService distanceMatrixService;
    @Autowired
    @Lazy
    private CustomerService customerService;
    @Autowired
    @Lazy
    private DepotService depotService;

    private QueryTemplate buildQuery(CorrelationSearch search) {
        QueryTemplate queryTemplate = getBaseQuery(search);
        String query = queryTemplate.getQuery();
        HashMap<String, Object> params = queryTemplate.getParameterMap();
        if (search.getFromNodeCode() != null && !search.getFromNodeCode().isEmpty()) {
            query += " and e.fromNodeCode = :fromNodeCode ";
            params.put("fromNodeCode", search.getFromNodeCode());
        }
        if (search.getToNodeCode() != null && !search.getToNodeCode().isEmpty()) {
            query += " and e.toNodeCode = :toNodeCode ";
            params.put("toNodeCode", search.getToNodeCode());
        }
        if (search.getFromNodeName() != null && !search.getFromNodeName().isEmpty()) {
            query += " and e.fromNodeName = :fromNodeName ";
            params.put("fromNodeName", search.getFromNodeName());
        }
        if (search.getToNodeName() != null && !search.getToNodeName().isEmpty()) {
            query += " and e.toNodeName = :toNodeName ";
            params.put("toNodeName", search.getToNodeName());
        }
        queryTemplate.setQuery(query);
        queryTemplate.setParameterMap(params);
        return queryTemplate;
    }

    private void setCorrelationsFromNode() {

    }

    private void setCorrelationsFromNode(NodeModel originNode, List<NodeModel> destinationNodes) {
        List<Correlation> correlations = new ArrayList<>();
        List<String> originList = Arrays.asList(originNode.getLatitude()+", "+originNode.getLongitude());
        List<String> destinationList = new ArrayList<>();
        List<List<Long>> timeMatrix = new ArrayList<List<Long>>();
        List<List<Long>> distanceMatrix = new ArrayList<List<Long>>();
        for (NodeModel nodeModel : destinationNodes) {
            destinationList.add(nodeModel.getLatitude()+", "+nodeModel.getLongitude());
        }
        timeMatrix = distanceMatrixService.getDistTimeMatrix(originList, destinationList, distanceMatrix);
        for (int i = 0; i < destinationNodes.size(); i++) {
            NodeModel destinationNode = destinationNodes.get(i);
            Correlation correlation = new Correlation();
            correlation.setFromNodeId(originNode.getId());
            correlation.setFromNodeCode(originNode.getCode());
            correlation.setFromNodeName(originNode.getName());
            correlation.setFromNodeType(originNode.getCode().contains("C") ? NodeType.CUSTOMER : NodeType.DEPOT);
            correlation.setToNodeId(destinationNode.getId());
            correlation.setToNodeCode(destinationNode.getCode());
            correlation.setToNodeName(destinationNode.getName());
            correlation.setToNodeType(destinationNode.getCode().contains("C") ? NodeType.CUSTOMER : NodeType.DEPOT);
            correlation.setDistance(distanceMatrix.get(0).get(i).intValue());
            correlation.setTime(timeMatrix.get(0).get(i).intValue());
            correlation.setRiskProbability(0.01);
            correlations.add(correlation);
        }
        try {
            correlations = create(correlations);
            logger.info("insert " + correlations.size() + " correlations from " + originNode.getCode());
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private void setCorrelationsToNode(List<NodeModel> originNodes, NodeModel destinationNode) {
        List<Correlation> correlations = new ArrayList<>();
        List<String> destinationList = Arrays.asList(destinationNode.getLatitude()+", "+destinationNode.getLongitude());
        List<String> originList = originNodes.stream().map(node -> node.getLatitude()+", "+node.getLongitude()).collect(Collectors.toList());
        List<List<Long>> timeMatrix = new ArrayList<List<Long>>();
        List<List<Long>> distanceMatrix = new ArrayList<List<Long>>();
        timeMatrix = distanceMatrixService.getDistTimeMatrix(originList, destinationList, distanceMatrix);
        for (int i = 0; i < originList.size(); i++) {
            NodeModel originNode = originNodes.get(i);
            Correlation correlation = new Correlation();
            correlation.setToNodeId(destinationNode.getId());
            correlation.setToNodeCode(destinationNode.getCode());
            correlation.setToNodeName(destinationNode.getName());
            correlation.setToNodeType(destinationNode.getCode().contains("C") ? NodeType.CUSTOMER : NodeType.DEPOT);
            correlation.setFromNodeId(originNode.getId());
            correlation.setFromNodeCode(originNode.getCode());
            correlation.setFromNodeName(originNode.getName());
            correlation.setFromNodeType(originNode.getCode().contains("C") ? NodeType.CUSTOMER : NodeType.DEPOT);
            correlation.setDistance(distanceMatrix.get(i).get(0).intValue());
            correlation.setTime(timeMatrix.get(i).get(0).intValue());
            correlation.setRiskProbability(0.01);
            correlations.add(correlation);
        }
        try {
            correlations = create(correlations);
            logger.info("insert " + correlations.size() + " correlations to " + destinationNode.getCode());
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }

    private List<NodeModel> getAllNode() {
        List<NodeModel> nodeModels = new ArrayList<>();
        CustomerSearch customerSearch = new CustomerSearch();
        customerSearch.setPaged(false);
        nodeModels.addAll(customerService.find(customerSearch));
        DepotSearch depotSearch = new DepotSearch();
        depotSearch.setPaged(false);
        nodeModels.addAll(depotService.find(depotSearch));
        return nodeModels;
    }

    private void setCorrelationsForNode(NodeModel node, List<NodeModel> nodeModels) {
        setCorrelationsFromNode(node, nodeModels);
        List<NodeModel> fromNodes = new ArrayList<>(nodeModels);
        for (int i = 0; i < fromNodes.size(); i++) {
            NodeModel fromNode = fromNodes.get(i);
            if (node.getCode().equals(fromNode.getCode()))
                fromNodes.remove(i);
        }
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
        }
        setCorrelationsToNode(fromNodes, node);
    }

    @Override
    public List<CorrelationModel> find(CorrelationSearch search) {
        List<CorrelationModel> correlationModels = new ArrayList<>();
        QueryTemplate queryTemplate = buildQuery(search);
        List<Correlation> correlations = find(queryTemplate);
        for (Correlation correlation : correlations) {
            correlationModels.add(Correlation.convertToModel(correlation));
        }
        return correlationModels;
    }

    @Override
    public List<Correlation> findCorrelations(CorrelationSearch search) {
        QueryTemplate queryTemplate = buildQuery(search);
        return find(queryTemplate);
    }

    @Override
    @Transactional(readOnly = false)
    public CorrelationModel update(CorrelationModel correlationModel) throws CustomException {
        Correlation correlation = correlationRepository.find(correlationModel.getId());
        if (correlation == null)
            throw CommonUtils.createException(Code.CORRELATION_ID_NOT_EXISTED);
        correlation = correlation.updateCorrelation(correlationModel);
        correlation = update(correlation);
        correlationModel = Correlation.convertToModel(correlation);
        return correlationModel;
    }

    @Override
    @Transactional(readOnly = false)
    public List<Correlation> create(List<Correlation> correlations) throws CustomException {
        return getRepository().insert(correlations);
    }

    @Override
    @Transactional(readOnly = false)
    @Async
    public void createCorrelations(NodeModel node) {
        List<NodeModel> nodeModels = getAllNode();
        if (nodeModels.size() > 25) {
            List<List<NodeModel>> nodeModelsGroups = Lists.partition(nodeModels, 25);
            for (List<NodeModel> nodeModelsGroup : nodeModelsGroups) {
                setCorrelationsForNode(node, nodeModelsGroup);
                try {
                    Thread.sleep(20 * 1000);
                } catch (InterruptedException e) {
                }
            }
        } else {
            setCorrelationsForNode(node, nodeModels);
        }
//        setCorrelationsFromNode(node, nodeModels);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//        }
//        setCorrelationsToNode(nodeModels, node);
        return;
    }

    @Override
    @Transactional(readOnly = false)
    public void createCorrelationsData(NodeModel node) {
        List<NodeModel> nodeModels = getAllNode();
        if (nodeModels.size() > 25) {
            List<List<NodeModel>> nodeModelsGroups = Lists.partition(nodeModels, 25);
            for (List<NodeModel> nodeModelsGroup : nodeModelsGroups) {
                setCorrelationsForNode(node, nodeModelsGroup);
                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                }
            }
        } else {
            setCorrelationsForNode(node, nodeModels);
        }
        return;
    }

    @Override
    public CorrelationModel findOne(Long id) {
        Correlation Correlation = find(id);
        CorrelationModel CorrelationModel = Correlation.convertToModel(Correlation);
        return CorrelationModel;
    }

    @Override
    public CorrelationRepository getRepository() {
        return this.correlationRepository;
    }
}
