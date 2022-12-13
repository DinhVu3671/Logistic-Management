package com.vrp.demo.service.imp;

import com.vrp.demo.models.*;
import com.vrp.demo.models.enu.DeliveryMode;
import com.vrp.demo.models.enu.NodeType;
import com.vrp.demo.models.solution.ProblemAssumption;
import com.vrp.demo.models.solution.Solution;
import com.vrp.demo.models.search.CustomerSearch;
import com.vrp.demo.models.search.DepotSearch;
import com.vrp.demo.models.search.OrderSearch;
import com.vrp.demo.models.search.VehicleSearch;
import com.vrp.demo.models.solution.SolutionDTO;
import com.vrp.demo.service.CustomerService;
import com.vrp.demo.service.DepotService;
import com.vrp.demo.service.OrderService;
import com.vrp.demo.service.VehicleService;
import com.vrptwga.MainAlgorithm;
import com.vrptwga.concepts.Customer;
import com.vrptwga.concepts.Depot;
import com.vrptwga.concepts.Vehicle;
import com.vrptwga.representation.Individual;
import com.vrptwga.concepts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SolutionRouteService {

    private static Logger logger = LoggerFactory.getLogger(SolutionRouteService.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private DepotService depotService;
    @Autowired
    private VehicleService vehicleService;

//    private static String createFile(HttpServletRequest request, MultipartFile fileInput, String description) {
//        Random random = new Random(System.currentTimeMillis());
//        String uploadedFileDir = null;
//        String uploadRootPath = request.getServletContext().getRealPath("upload");
//        File uploadRootDir = new File(uploadRootPath);
//        String tail = "" + System.currentTimeMillis() + "-" + random.nextInt() + description;
//        if (!uploadRootDir.exists()) {
//            uploadRootDir.mkdirs();
//        }
//        String name = fileInput.getOriginalFilename();
//        if (name != null && name.length() > 0) {
//            try {
//                String folder = uploadRootPath + File.separator + tail;
//                File directory = new File(folder);
//                if (!directory.exists()) {
//                    directory.mkdirs();
//                }
//                // Tạo file tại Server.
//                uploadedFileDir = directory.getAbsolutePath() + File.separator + name;
//                File serverFile = new File(uploadedFileDir);
//                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
//                stream.write(fileInput.getBytes());
//                stream.close();
//                //
//                System.out.println("Write file: " + serverFile);
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Error Write file: " + name);
//            }
//        }
//        return uploadedFileDir;
//    }

//    private List<Order> readOrders(String fileExcel, List<Customer> customers, List<Depot> depots) throws IOException, InvalidFormatException {
//        List<Order> orders = new ArrayList<>();
//        FileInputStream inputStream = new FileInputStream(new File(fileExcel));
//        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//        XSSFSheet sheet = wb.getSheetAt(0);
//        Iterator<Row> rowIterator = sheet.iterator();
//        rowIterator.next();         // đọc từ dòng thứ 2
//        while (rowIterator.hasNext()) {
//            Order order = new Order();
//            Row row = rowIterator.next();
//            Cell cell = row.getCell(0);
//            // lấy Id
//            Double column1 = Double.parseDouble(cell.getStringCellValue().substring(1));
//            if (column1 == null)
//                break;
//            int id = column1.intValue();
//            order.setId(id);
//            // lấy mã kho
//            cell = row.getCell(1);
//            Double depotId = Double.parseDouble(cell.getStringCellValue().substring(1));
//            order.setDepot(depotId.intValue(), depots);
//            //lấy mã khách hàng
//            cell = row.getCell(2);
//            Double customerId = Double.parseDouble(cell.getStringCellValue().substring(1));
//            order.setCustomer(customerId.intValue(), customers);
//            //khối lượng hàng
//            cell = row.getCell(3);
//            Double demand = cell.getNumericCellValue();
//            order.setDemand(demand.intValue());
//            //thể tích
//            cell = row.getCell(4);
//            Double volume = cell.getNumericCellValue();
//            order.setVolume(volume.intValue());
//            // tổng tiền
//            cell = row.getCell(5);
//            Double value = cell.getNumericCellValue();
//            order.setValue(value);
//            // chế độ giao hàng
//            cell = row.getCell(6);
//            String mode = cell.getStringCellValue();
//            order.setMode(mode);
//            // loại hàng
//            cell = row.getCell(7);
//            String type = cell.getStringCellValue();
//            order.setType(type);
//            if (order.getMode() > 1) {
//                // giao hàng trước thời điểm
//                cell = row.getCell(8);
//                if (cell != null) {
//                    Double endTime = cell.getNumericCellValue();
//                    if (endTime > 0)
//                        order.setSpecialTimeWindowEnd(endTime.intValue() * 3600);
//                }
//                // giao hàng sau thời điểm
//                cell = row.getCell(9);
//                if (cell != null) {
//                    Double startTime = cell.getNumericCellValue();
//                    if (startTime > 0)
//                        order.setSpecialTimeWindowStart(startTime.intValue() * 3600);
//                }
//            }
//            orders.add(order);
//        }
//
//        return orders;
//    }

//    private List<Depot> readDepots(String fileExcel) throws IOException, InvalidFormatException {
//        List<Depot> depots = new ArrayList<>();
//        FileInputStream inputStream = new FileInputStream(new File(fileExcel));
//        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//        XSSFSheet sheet = wb.getSheetAt(0);
//        Iterator<Row> rowIterator = sheet.iterator();
//        rowIterator.next();         // đọc từ dòng thứ 2
//        while (rowIterator.hasNext()) {
//            Depot depot = new Depot();
//            Row row = rowIterator.next();
//            Cell cell = row.getCell(0);
//            // lấy Id theo STT
//            Double column1 = Double.parseDouble(cell.getStringCellValue().substring(1));
//            if (column1 == null)
//                break;
//            int id = column1.intValue();
//            depot.setId(id);
//            // lấy tên
//            cell = row.getCell(1);
//            String name = cell.getStringCellValue();
//            depot.setName(name);
//            //đọc vĩ độ
//            cell = row.getCell(2);
//            Double lat = cell.getNumericCellValue();
//            depot.setLat(lat);
//            //đọc kinh độ
//            cell = row.getCell(3);
//            Double lng = cell.getNumericCellValue();
//            depot.setLng(lng);
//            //time-window-start
//            cell = row.getCell(4);
//            Double startTime = cell.getNumericCellValue();
//            //time-window-end
//            cell = row.getCell(5);
//            Double endTime = cell.getNumericCellValue();
//            TimeWindow timeWindow = new TimeWindow((int) Math.round(startTime * 3600), (int) Math.round(endTime * 3600));
//            depot.setTimeWindow(timeWindow);
//            //phí dỡ hàng
//            cell = row.getCell(6);
//            Double loadingFeePerTon = cell.getNumericCellValue();
//            depot.setLoadingCostPerTon(loadingFeePerTon);
//            depots.add(depot);
//        }
//        return depots;
//    }

//    private List<Vehicle> readVehicles(String fileExcel, List<Depot> depots) throws IOException, InvalidFormatException {
//        List<Vehicle> vehicles = new ArrayList<>();
//        FileInputStream inputStream = new FileInputStream(new File(fileExcel));
//        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//        XSSFSheet sheet = wb.getSheetAt(0);
//        Iterator<Row> rowIterator = sheet.iterator();
//        rowIterator.next();         // đọc từ dòng thứ 2
//        while (rowIterator.hasNext()) {
//            Vehicle vehicle = new Vehicle();
//            Row row = rowIterator.next();
//            Cell cell = row.getCell(0);
//            // lấy Id
//            Double column1 = Double.parseDouble(cell.getStringCellValue().substring(1));
//            if (column1 == null)
//                break;
//            int id = column1.intValue();
//            vehicle.setId(id);
//            // lấy tên
//            cell = row.getCell(1);
//            String name = cell.getStringCellValue();
//            vehicle.setName(name);
//            //đọc sức chứa tải trọng-thể tích
//            cell = row.getCell(2);
//            Double capacity = cell.getNumericCellValue();
//            vehicle.setCapacity(capacity.intValue());
//            cell = row.getCell(3);
//            Double volume = cell.getNumericCellValue();
//            vehicle.setVolume(volume.intValue());
//            //đọc phí di chuyển
//            cell = row.getCell(4);
//            Double gasConsume = cell.getNumericCellValue();
//            cell = row.getCell(5);
//            Double gasCost = cell.getNumericCellValue();
//            vehicle.setFuelCost(gasConsume * gasCost);
//            // vận tốc
//            cell = row.getCell(6);
//            Double minVelocity = cell.getNumericCellValue();
//            cell = row.getCell(7);
//            Double maxVelocity = cell.getNumericCellValue();
//            vehicle.setVelocity((minVelocity + maxVelocity) / 2);
//            // mã kho
//            cell = row.getCell(8);
//            Double depotId = Double.parseDouble(cell.getStringCellValue().substring(1));
//            ;
//            vehicle.setDepot(depotId.intValue(), depots);
//            // mã kho
//            cell = row.getCell(9);
//            String ready = cell.getStringCellValue();
//            if (ready.equals("có"))
//                vehicle.setReady(true);
//            vehicles.add(vehicle);
//        }
//        return vehicles;
//    }

//    private List<Customer> readCustomers(String fileExcel) throws IOException {
//        List<Customer> customers = new ArrayList<>();
//        FileInputStream inputStream = new FileInputStream(new File(fileExcel));
//        XSSFWorkbook wb = new XSSFWorkbook(inputStream);
//        XSSFSheet sheet = wb.getSheetAt(0);
//        Iterator<Row> rowIterator = sheet.iterator();
//        rowIterator.next();         // đọc từ dòng thứ 2
//        while (rowIterator.hasNext()) {
//            Customer customer = new Customer();
//            Row row = rowIterator.next();
//            Cell cell = row.getCell(0);
//            // lấy Id
//            Double column1 = Double.parseDouble(cell.getStringCellValue().substring(1));
//            ;
//            if (column1 == null)
//                break;
//            int id = column1.intValue();
//            customer.setId(id);
//            // lấy tên
//            cell = row.getCell(1);
//            String name = cell.getStringCellValue();
//            customer.setName(name);
//            //đọc vĩ độ
//            cell = row.getCell(2);
//            Double lat = cell.getNumericCellValue();
//            customer.setLat(lat);
//            //đọc kinh độ
//            cell = row.getCell(3);
//            Double lng = cell.getNumericCellValue();
//            customer.setLng(lng);
//            //time-window-start
//            cell = row.getCell(4);
//            Double startTime = cell.getNumericCellValue();
//            customer.setReadyTime((int) Math.round(startTime * 3600));
//            //time-window-end
//            cell = row.getCell(5);
//            Double endTime = cell.getNumericCellValue();
//            customer.setDueTime((int) Math.round(endTime * 3600));
//            customers.add(customer);
//        }
//
//        return customers;
//    }

//    public List<Customer> preProcessDataCustomers(HttpServletRequest request, MultipartFile fileInput) throws IOException, InvalidFormatException {
//        String uploadedFileDir = createFile(request, fileInput, "customer");
//        List<Customer> customers = readCustomers(uploadedFileDir);
//        //delete File
//        File uploadedFile = new File(uploadedFileDir);
//        FileUtils.deleteDirectory(new File(uploadedFile.getParent()));
//        return customers;
//    }

//    public List<Order> preProcessDataOrders(HttpServletRequest request, MultipartFile fileInput, List<Depot> depots, List<Customer> customers) throws IOException, InvalidFormatException {
//        String uploadedFileDir = createFile(request, fileInput, "order");
//        List<Order> orderList = readOrders(uploadedFileDir, customers, depots);
//        //delete File
//        File uploadedFile = new File(uploadedFileDir);
//        FileUtils.deleteDirectory(new File(uploadedFile.getParent()));
//        return orderList;
//    }

//    public List<Depot> preProcessDataDepots(HttpServletRequest request, MultipartFile fileInputDepot) throws IOException, InvalidFormatException {
//        String uploadedFileDir = createFile(request, fileInputDepot, "depot");
//        List<Depot> depots = readDepots(uploadedFileDir);
//        //delete File
//        File uploadedFile = new File(uploadedFileDir);
//        FileUtils.deleteDirectory(new File(uploadedFile.getParent()));
//        return depots;
//    }

//    public List<Vehicle> preProcessDataVehicles(HttpServletRequest request, MultipartFile fileInputDepot, List<Depot> depots) throws IOException, InvalidFormatException {
//        String uploadedFileDir = createFile(request, fileInputDepot, "vehicle");
//        List<Vehicle> vehicles = readVehicles(uploadedFileDir, depots);
//        //delete File
//        File uploadedFile = new File(uploadedFileDir);
//        FileUtils.deleteDirectory(new File(uploadedFile.getParent()));
//        return vehicles;
//    }


    private List<Customer> getListCustomers() {
        List<CustomerModel> customerModels = customerService.findAllWithCorrelations();
        List<Customer> customerInputs = new ArrayList<>();
        for (CustomerModel customerModel : customerModels) {
            customerInputs.add(CustomerModel.getCustomerInput(customerModel));
        }
        return customerInputs;
    }

    private List<Depot> getListDepots() {
        List<DepotModel> depotModels = depotService.find(new DepotSearch());
        List<Depot> depotInputs = new ArrayList<>();
        for (DepotModel depotModel : depotModels) {
            depotModel = depotService.findOne(depotModel.getId());
            depotInputs.add(DepotModel.getDepotInput(depotModel));
        }
        return depotInputs;
    }

    private List<Vehicle> getExcludeVehicles(OrderModel orderModel, List<Vehicle> vehicleInputs, List<VehicleModel> vehicleModels) {
        List<Vehicle> excludeVehicles = new ArrayList<>();
        for (VehicleModel vehicleModel : vehicleModels) {
            if (orderModel.isExcludeVehicle(vehicleModel)) {
                Vehicle excludeVehicle = Vehicle.getById(vehicleModel.getId().intValue(), vehicleInputs);
                if (excludeVehicle != null)
                    excludeVehicles.add(excludeVehicle);
            }
        }
        return excludeVehicles;
    }

    private List<Request> getListRequests(List<Customer> customerInputs, List<Depot> depotInputs, List<Vehicle> vehicleInputs, List<CustomerModel> clusterCustomers) {
        List<OrderModel> orderModels = orderService.find(new OrderSearch());
        List<VehicleModel> vehicleModels = vehicleService.find(new VehicleSearch());
        List<Request> requests = new ArrayList<>();
        for (OrderModel orderModel : orderModels) {
            orderModel.setFromDepot(clusterCustomers);
            Request request = new Request();
            request.setId(orderModel.getId().intValue());
            for (Customer customerInput : customerInputs) {
                if (customerInput.getId() == orderModel.getCustomer().getId().intValue()) {
                    request.setCustomer(customerInput);
                }
            }
            request.setDemand((int) orderModel.getWeight());
            request.setCode(orderModel.getCode());
            request.setTimeService(orderModel.getTimeService());
            request.setTimeLoading(orderModel.getTimeLoading());
            request.setCapacity(orderModel.getCapacity());
            request.setWeight(orderModel.getWeight());
            request.setOrderValue(orderModel.getOrderValue());
            request.setAntiStackingArea(orderModel.getAntiStackingArea());
            request.setExcludeVehicles(getExcludeVehicles(orderModel, vehicleInputs, vehicleModels));
            request.setExcludingRequestIds(orderModel.getExcludeRequestIds(orderModels));
            if (!orderModel.getDeliveryMode().equals(DeliveryMode.STANDARD)) {
                request.setSpecialTimeWindow(orderModel.getDeliveryAfterTime().intValue(), orderModel.getDeliveryBeforeTime().intValue());
            }
            request.setDepot(Depot.getById(orderModel.getDepot().getId().intValue(), depotInputs));
            requests.add(request);
        }

        for (Vehicle vehicleInput : vehicleInputs) {
            vehicleInput.setServedAbleRequests(vehicleInput.getServedAbleRequests(requests));
        }
        return requests;
    }

    private List<Vehicle> getListVehicles() {
        VehicleSearch search = new VehicleSearch();
        search.setAvailable(true);
        List<VehicleModel> vehicleModels = vehicleService.find(search);
        List<Vehicle> vehicleInputs = new ArrayList<>();
        for (VehicleModel vehicleModel : vehicleModels) {
            Vehicle vehicleInput = VehicleModel.getVehicleInput(vehicleModel);
            vehicleInputs.add(vehicleInput);
        }
        return vehicleInputs;
    }

//    private List<Vehicle> getListVehicles(List<Depot> depotInputs, ProblemAssumption problemAssumption) {
//        List<VehicleModel> vehicleModels = problemAssumption.getVehicles();
//        List<Vehicle> vehicleInputs = new ArrayList<>();
//        for (VehicleModel vehicleModel : vehicleModels) {
//            Vehicle vehicleInput = VehicleModel.getVehicleInput(vehicleModel);
//            vehicleInput.setDepot(Depot.getById(vehicleModel.getDepot().getId().intValue(), depotInputs));
//            vehicleInputs.add(vehicleInput);
//        }
//        return vehicleInputs;
//    }

    private OptimizationScenario loadProblem(ProblemAssumption problem) {
        OptimizationScenario optimizationScenario = new OptimizationScenario();
        List<Depot> depotInputs = getListDepots();
        List<Customer> customerInputs = getListCustomers();
        List<Vehicle> vehicleInputs = getListVehicles();
        List<Request> requests = getListRequests(customerInputs, depotInputs, vehicleInputs, problem.getCustomers());
        if (problem.getIsExcludeProduct() != true) {
            for (Request request : requests) {
                request.setExcludingRequests(new ArrayList<>());
                request.setExcludingRequestIds(new ArrayList<>());
                request.setExcludeVehicles(new ArrayList<>());
            }
        } else {
            for (Request request : requests) {
                request.setExcludingRequests(Request.getListRequestById(request.getExcludingRequestIds(), requests));
            }   
        }
        optimizationScenario.setDepots(depotInputs);
        optimizationScenario.setCustomers(customerInputs);
        optimizationScenario.setRequests(requests);
        optimizationScenario.setVehicles(vehicleInputs);
        return optimizationScenario;
    }

//    private OptimizationScenario loadProblem(ProblemAssumption problemAssumption) {
//        OptimizationScenario optimizationScenario = new OptimizationScenario();
//        List<DepotInput> depotInputs = getListDepots(problemAssumption);
//        List<CustomerInput> customerInputs = getListCustomers(problemAssumption);
//        List<VehicleInput> vehicleInputs = getListVehicles(depotInputs, problemAssumption);
//        List<Request> requests = getListRequests(customerInputs, depotInputs, vehicleInputs, problemAssumption.getCustomers());
//        for (Request request : requests) {
//            request.setExcludingRequests(Request.getListRequestById(request.getExcludingRequestIds(), requests));
//        }
//        optimizationScenario.setDepots(depotInputs);
//        optimizationScenario.setCustomers(customerInputs);
//        optimizationScenario.setRequests(requests);
//        optimizationScenario.setVehicles(vehicleInputs);
//        return optimizationScenario;
//    }

    private ProblemAssumption createProblemAssumption() {
        ProblemAssumption problemAssumption = new ProblemAssumption();
        List<CustomerModel> customerModels = customerService.find(new CustomerSearch());
        for (CustomerModel customerModel : customerModels) {
            customerModel.setNodeType(NodeType.CUSTOMER);
        }
        List<DepotModel> depotModels = depotService.find(new DepotSearch());
        for (DepotModel depotModel : depotModels) {
            depotModel.setNodeType(NodeType.DEPOT);
        }
        List<VehicleModel> vehicleModels = vehicleService.find(new VehicleSearch());
        List<OrderModel> orderModels = orderService.find(new OrderSearch());

        problemAssumption.getNodes().addAll(customerModels);
        problemAssumption.getNodes().addAll(depotModels);
        problemAssumption.setVehicles(vehicleModels);
        problemAssumption.setOrders(orderModels);
        problemAssumption.setCustomers(customerModels);
        problemAssumption.setDepots(depotModels);
        return problemAssumption;
    }


    private ProblemAssumption createProblemAssumption(ProblemAssumption problem) {
        ProblemAssumption problemAssumption = new ProblemAssumption();
        List<CustomerModel> customerModels = problem.getCustomers();
        for (CustomerModel customerModel : customerModels) {
            customerModel.setNodeType(NodeType.CUSTOMER);
        }
        List<DepotModel> depotModels = problem.getDepots();
        for (DepotModel depotModel : depotModels) {
            depotModel.setNodeType(NodeType.DEPOT);
        }
        List<VehicleModel> vehicleModels = problem.getVehicles();
        List<OrderModel> orderModels = problem.getOrders();

        problemAssumption.getNodes().addAll(customerModels);
        problemAssumption.getNodes().addAll(depotModels);
        problemAssumption.setVehicles(vehicleModels);
        problemAssumption.setOrders(orderModels);
        problemAssumption.setCustomers(customerModels);
        problemAssumption.setDepots(depotModels);
        return problemAssumption;
    }

    public List<Solution> run(ProblemAssumption problem) {
        logger.info("Create Problem");
        OptimizationScenario optimizationScenario = loadProblem(problem);
        ProblemAssumption.setParameters(optimizationScenario, problem);
        ProblemAssumption problemAssumption = createProblemAssumption();
        logger.info("Run Algorithm");
        List<Individual> rawSolutions = MainAlgorithm.runAlgorithm(optimizationScenario);
        List<Solution> solutions = new ArrayList<>();
        int solutionNumber = rawSolutions.size() > 3 ? 3 : rawSolutions.size();
        for (int i = 0; i < solutionNumber; i++) {
            Individual rawSolution = rawSolutions.get(i);
            solutions.add(Solution.createSolutionWithJourney(rawSolution, problemAssumption));
        }
        for (Solution solution : solutions) {
            solution.deleteCorrelations();
        }
        return solutions;
    }

    public List<Solution> runAlgorithm(SolutionDTO solutionDTO) {
        OptimizationScenario optimizationScenario = solutionDTO.getOptimizationScenario();
        ProblemAssumption problemAssumption = solutionDTO.getProblemAssumption();
        logger.info("Run Algorithm");
        List<Individual> rawSolutions = MainAlgorithm.runAlgorithm(optimizationScenario);
        List<Solution> solutions = new ArrayList<>();
        int solutionNumber = rawSolutions.size() > 3 ? 3 : rawSolutions.size();
        for (int i = 0; i < solutionNumber; i++) {
            Individual rawSolution = rawSolutions.get(i);
            solutions.add(Solution.createSolutionWithJourney(rawSolution, problemAssumption));
        }
        for (Solution solution : solutions) {
            solution.deleteCorrelations();
        }
        return solutions;
    }

    public Solution changeOrderToJourney(SolutionDTO updateSolution) {
        OptimizationScenario optimizationScenario = loadProblem(updateSolution.getProblemAssumption());
        ProblemAssumption.setParameters(optimizationScenario, updateSolution.getProblemAssumption());
        ProblemAssumption problemAssumption = createProblemAssumption();
        Solution solution = updateSolution.getSolution();
        updateSolution.setProblemAssumption(problemAssumption);
        solution = Solution.updateJourneys(updateSolution, optimizationScenario);
        return solution;
    }

//    public SolutionDTO initProblem(ProblemAssumption problem) {
//        logger.info("Create Problem");
//        SolutionDTO solutionDTO = new SolutionDTO();
//        OptimizationScenario optimizationScenario = loadProblem(problem.getCustomers());
//        ProblemAssumption.setParameters(optimizationScenario, problem);
//        ProblemAssumption problemAssumption = createProblemAssumption();
////        solutionDTO.setProblemAssumption(problemAssumption);
//        solutionDTO.setOptimizationScenario(optimizationScenario);
//        return solutionDTO;
//    }

}
