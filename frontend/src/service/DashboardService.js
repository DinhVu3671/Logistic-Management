import service from './api';

class DashboardService {
    orderItemStat(dateRange) {
        return service.post("/dashboard/order-item-stat", dateRange);
    }
}

export default new DashboardService();
