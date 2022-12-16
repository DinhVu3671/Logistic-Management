import service from './api';

class DashboardService {
    orderItemStat(dateRange) {
        return service.post("/dashboard/order-item-stat", dateRange);
    }
    sales(year) {
        return service.get(`/dashboard/sales/${year}`)
    }
}

export default new DashboardService();
