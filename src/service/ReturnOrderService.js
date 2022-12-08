import service from './api';

class ReturnOrderService {

    get(orderId) {
        return service.post(`/${orderId}/return-orders/get-by-order`, { id: orderId });
    }

    create(orderId, data) {
        return service.post(`/${orderId}/return-orders`, data);
    }

    update(orderId, id, data) {
        return service.put(`/${orderId}/return-orders/${id}`, data);
    }

    delete(orderId, id) {
        return service.delete(`/${orderId}/return-orders/${id}`);
    }

}

export default new ReturnOrderService();
