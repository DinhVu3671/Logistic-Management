package com.vrp.demo.models.solution;

import com.vrp.demo.configuration.mongodb.CascadeSave;
import com.vrp.demo.models.*;
import com.vrp.demo.models.enu.NodeType;
import com.vrp.demo.utils.CommonUtils;
import com.vrptwga.concepts.OptimizationScenario;
import com.vrptwga.concepts.Request;
import com.vrptwga.concepts.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Document("route")
public class Route {

//    @Id
    private String id;
//    @DBRef
//    @CascadeSave
    private List<Arc> arcs = new ArrayList<>();
    private List<TimeWindow> timeline = new ArrayList<>();
    private Double totalCost;
    private Double totalDistance;
    private int currentVolume;
    private double maxCapacity;
    private double maxLoadWeight;
    private double totalAmount = 0;
    private double revenue = 0;
    private List<OrderModel> orders = new ArrayList<>();
    private List<String> ordinalLoadingOrders = new ArrayList<>();
    private Double currentLoadWeight;
    private Double currentCapacity;
    private Double fillRateLoadWeight;
    private Double fillRateCapacity;
    private DepotModel startDepot;
    private DepotModel endDepot;
    private List<Bill> bills;
    private double penaltyCost;
    private LinkedHashMap<String, Double> finedRequests = new LinkedHashMap<>();

    public void setArcsByCustomers(int startDepotId, List<CustomerModel> customers, int endDepotId, List<NodeModel> nodeModels) {
        List<Arc> arcs = new ArrayList<>();
        if (customers != null && !customers.isEmpty()) {
            Arc arcFirst = new Arc(NodeModel.findNode(nodeModels, (long) startDepotId, NodeType.DEPOT), NodeModel.findNode(nodeModels, customers.get(0).getId(), NodeType.CUSTOMER));
            arcs.add(arcFirst);
            for (int i = 0; i < customers.size() - 1; i++) {
                Arc arc = new Arc(NodeModel.findNode(nodeModels, customers.get(i).getId(), NodeType.CUSTOMER), NodeModel.findNode(nodeModels, customers.get(i + 1).getId(), NodeType.CUSTOMER));
                arcs.add(arc);
            }
            Arc arcLast = new Arc(NodeModel.findNode(nodeModels, customers.get(customers.size() - 1).getId(), NodeType.CUSTOMER), NodeModel.findNode(nodeModels, (long) endDepotId, NodeType.DEPOT));
            arcs.add(arcLast);
            this.arcs = arcs;
        }
    }

    public Route calculateTotalAmount(double loadingCostPerTon) {
        double totalAmount = 0;
        List<Bill> bills = new ArrayList<>();
        double capacityAfterNode = this.currentCapacity;
        double loadWeightAfterNode = this.currentLoadWeight;
        for (OrderModel order : orders) {
            double unloadingFee = loadingCostPerTon * order.getWeight();
            double orderValue = order.getOrderValue();
            double billValue = unloadingFee + orderValue;
            capacityAfterNode = Math.round((capacityAfterNode - order.getCapacity()) * 100.0) / 100.0;
            loadWeightAfterNode = Math.round((loadWeightAfterNode - order.getWeight()) * 100.0) / 100.0;
            double fillRateLoadWeightAfterNode = (Math.round((loadWeightAfterNode * 100 / this.getMaxLoadWeight()) * 100.0) / 100.0);
            double fillRateCapacityAfterNode = (Math.round((capacityAfterNode * 100 / this.getMaxCapacity()) * 100.0) / 100.0);
            double penaltyCost = finedRequests.get(order.getCode()) == null ? 0 : finedRequests.get(order.getCode());
            bills.add(new Bill(billValue, unloadingFee, orderValue, order, capacityAfterNode, loadWeightAfterNode,
                    fillRateLoadWeightAfterNode, fillRateCapacityAfterNode, penaltyCost));
            totalAmount += billValue;
        }
        this.totalAmount = totalAmount;
        this.revenue = totalAmount - totalCost;
        this.bills = bills;
        return this;
    }

    public List<TimeWindow> setTimeLine(List<com.vrptwga.concepts.TimeWindow> timeline) {
        List<TimeWindow> timeLine = new ArrayList<>();
        for (com.vrptwga.concepts.TimeWindow timeWindow : timeline) {
            timeLine.add(new TimeWindow(timeWindow));
        }
        this.timeline = timeLine;
        return this.timeline;
    }

//    public void setRiskOfCustomers(List<Double> riskOfCustomers) {
//        for (int i = 0; i < riskOfCustomers.size(); i++) {
//            this.customers.get(i).setRiskProbability(Math.round(riskOfCustomers.get(i) * 100.0) / 100.0);
//        }
//    }

    public List<OrderModel> setOrders(List<OrderModel> orders, List<Request> requests) {
        List<OrderModel> newOrders = new ArrayList<>();
        for (Request request : requests) {
            for (OrderModel order : orders) {
                if (request.getId() == order.getId()) {
//                    order.getCustomer().setCorrelations(null);
                    newOrders.add(order);
                }
            }
        }
        this.orders = newOrders;
        List<String> orderCodes = newOrders.stream()
                .map(OrderModel::getCode)
                .collect(Collectors.toList());
        Collections.reverse(orderCodes);
        this.ordinalLoadingOrders = orderCodes;
        return this.orders;
    }

    public static Route createRoute(com.vrptwga.representation.phenotype.Route route, ProblemAssumption problemAssumption, Vehicle vehicle) {
        Route routeInfo = new Route();
        routeInfo.setMaxCapacity(vehicle.getMaxCapacity());
        routeInfo.setMaxLoadWeight(vehicle.getMaxLoadWeight());
        routeInfo.setCurrentVolume(route.getCurrentVolume());
        routeInfo.setCurrentLoadWeight(route.getCurrentLoadWeight());
        routeInfo.setCurrentCapacity(route.getCurrentCapacity());
        routeInfo.setFillRateCapacity(Math.round((route.getCurrentCapacity() * 100 / route.getUsedVehicle().getMaxCapacity()) * 100.0) / 100.0);
        routeInfo.setFillRateLoadWeight(Math.round((route.getCurrentLoadWeight() * 100 / route.getUsedVehicle().getMaxLoadWeight()) * 100.0) / 100.0);
        List<CustomerModel> customerInRoute = CustomerModel.getListCustomerModelsByID(route.getServedCustomerIds(), problemAssumption.getCustomers());
        routeInfo.setTimeLine(route.getTimeline());
//        List<Customer> customers = new ArrayList<>();
//        for (CustomerModel customerModel : customerInRoute) {
//            customers.add(Customer.createCustomer(customerModel));
//        }
//        routeInfo.setCustomers(customers);
//        routeInfo.setRiskOfCustomers(route.getRiskOfCustomers());
        routeInfo.setArcsByCustomers(route.getStartDepot().getId(), customerInRoute, route.getEndDepot().getId(), problemAssumption.getNodes());
        routeInfo.setFinedRequests(route.getFinedRequests());
        routeInfo.setPenaltyCost(route.getPenaltyCost());
        routeInfo.setTotalCost(route.getTotalCost());
        routeInfo.setOrders(problemAssumption.getOrders(), route.getRequests());
        routeInfo.calculateTotalAmount(problemAssumption.getDepots().get(0).getUnloadingCost());
        DepotModel startDepot = (DepotModel) NodeModel.findNode(problemAssumption.getNodes(), (long) route.getStartDepot().getId(), NodeType.DEPOT);
//        startDepot.setCorrelations(null);
        routeInfo.setStartDepot(startDepot);
        DepotModel endDepot = (DepotModel) NodeModel.findNode(problemAssumption.getNodes(), (long) route.getEndDepot().getId(), NodeType.DEPOT);
//        endDepot.setCorrelations(null);
        routeInfo.setEndDepot(endDepot);
        return routeInfo;
    }

    public static com.vrptwga.representation.phenotype.Route createRoute(Route routeInfo, OptimizationScenario optimizationScenario, Vehicle vehicle) {
        com.vrptwga.representation.phenotype.Route route = new com.vrptwga.representation.phenotype.Route();
        route.setUsedVehicle(vehicle);
        route.setMaxCapacity(vehicle.getMaxCapacity());
        route.setCurrentVolume(routeInfo.getCurrentVolume());
        route.setCurrentLoadWeight(routeInfo.getCurrentLoadWeight());
        route.setCurrentCapacity(routeInfo.getCurrentCapacity());
        route.setTotalCost(route.getTotalCost());
        route.setRequests(CommonUtils.getRequests(optimizationScenario.getRequests(), routeInfo.getOrders()));
        route.setStartDepot(CommonUtils.findDepot(optimizationScenario.getDepots(), routeInfo.getStartDepot()));
        route.setEndDepot(CommonUtils.findDepot(optimizationScenario.getDepots(), routeInfo.getEndDepot()));
        route.updateRoute(optimizationScenario);
        return route;
    }

    public void setArcsId() {
        for (Arc arc : arcs) {
            arc.setObjectId();
        }
    }

    public void setObjectId() {
        setArcsId();
        String id = System.currentTimeMillis() + hashCode()+"";
        setId(getId() == null ? id : id + getId());
    }
}
