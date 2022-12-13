package com.vrp.demo.utils;

import com.vrp.demo.entity.BaseEntity;
import com.vrp.demo.entity.tenant.Customer;
import com.vrp.demo.entity.tenant.Order;
import com.vrp.demo.entity.tenant.Product;
import com.vrp.demo.exception.CustomException;
import com.vrp.demo.models.*;
import com.vrp.demo.models.enu.Code;
import com.vrp.demo.models.search.BaseSearch;
import com.vrptwga.concepts.Depot;
import com.vrptwga.concepts.Request;
import com.vrptwga.concepts.Vehicle;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUtils {

    public static final String dateFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String timeZone = "GMT+7";
    public static final int PAGE_DEFAULT = 1;
    public static final int PAGE_SIZE_DEFAULT = 5;

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

    public static String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

    public static Timestamp getCurrentTime() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return currentTime;
    }

    public static void setDefaultPageIfNull(BaseSearch search) {
        if (search.getPage() == null)
            search.setPage(CommonUtils.PAGE_DEFAULT);
        if (search.getPageSize() == null)
            search.setPageSize(CommonUtils.PAGE_SIZE_DEFAULT);
    }

    public static SimpleDateFormat getSimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return simpleDateFormat;
    }

    public static CustomException createException(Code errorCode) {
        return new CustomException(errorCode);
    }

    public static Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    public static List<Locale> getSupportedLocale() {
        return Arrays.asList(new Locale("vi"), new Locale("en"), new Locale("jp"));
    }


    public static VehicleModel getVehicleById(List<VehicleModel> vehicleModels, long idVehicle) {
        for (VehicleModel vehicleModel : vehicleModels) {
            if (vehicleModel.getId() == idVehicle)
                return vehicleModel;
        }
        return null;
    }

    public static CustomerModel getCustomerByCode(List<CustomerModel> customerModels, String customerCode) {
        for (CustomerModel customerModel : customerModels) {
            if (customerModel.getCode().equals(customerCode))
                return customerModel;
        }
        return null;
    }

    public static OrderModel getOrderById(List<OrderModel> orderModels, long orderId) {
        for (OrderModel orderModel : orderModels) {
            if (orderModel.getId() == orderId)
                return orderModel;
        }
        return null;
    }

    public static OrderModel getOrderByCode(List<OrderModel> orderModels, String orderCode) {
        for (OrderModel orderModel : orderModels) {
            if (orderModel.getCode().equals(orderCode))
                return orderModel;
        }
        return null;
    }

    public static com.vrp.demo.entity.tenant.Depot getDepotByCode(List<com.vrp.demo.entity.tenant.Depot> depots, String depotCode) {
        for (com.vrp.demo.entity.tenant.Depot depot : depots) {
            if (depot.getCode().equals(depotCode))
                return depot;
        }
        return null;
    }

    public static List<Request> getRequests(List<Request> requests, List<OrderModel> orders) {
        List<Request> requestList = new ArrayList<>();
        for (Request request : requests) {
            for (OrderModel order : orders) {
                if (request.getId() == order.getId())
                    requestList.add(request);
            }
        }
        return requestList;
    }

    public static Request findRequest(List<Request> requests, OrderModel order) {
        for (Request request : requests) {
            if (request.getId() == order.getId())
                return request;
        }
        return null;
    }

    public static Depot findDepot(List<Depot> depots, DepotModel depotModel) {
        Depot depotInput = null;
        for (Depot depot : depots) {
            if (depot.getId() == depotModel.getId())
                depotInput = depot;
        }
        return depotInput;
    }

    public static Vehicle getVehicle(List<Vehicle> vehicleInputs, VehicleModel vehicleModel) {
        for (Vehicle vehicleInput : vehicleInputs) {
            if (vehicleInput.getId() == vehicleModel.getId())
                return vehicleInput;
        }
        return null;
    }

    public static List<Product> getProductsByCustomer(List<Order> orders, Customer customer) {
        return orders.stream().filter(order -> order.getCustomer().getId().equals(customer.getId())).flatMap(order -> order.getProducts().stream()).distinct().collect(Collectors.toList());
    }

    public static List<com.vrp.demo.entity.tenant.Depot> getAvailableDepotsByCustomer(List<Order> orders, Customer customer, List<com.vrp.demo.entity.tenant.Depot> depots) {
        List<com.vrp.demo.entity.tenant.Depot> availableDepots = new ArrayList<>();
        for (com.vrp.demo.entity.tenant.Depot depot : depots) {
            if (checkDepotAvailableToCustomer(orders,customer,depot))
               availableDepots.add(depot) ;
        }
        return availableDepots;
    }

    public static boolean checkDepotAvailableToCustomer(List<Order> orders, Customer customer , com.vrp.demo.entity.tenant.Depot depot){
        List<Long> depotProductIds = depot.getProducts().stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<Long> customerProductIds = getProductsByCustomer(orders,customer).stream().map(BaseEntity::getId).collect(Collectors.toList());
        return depotProductIds.containsAll(customerProductIds);
    }
}
