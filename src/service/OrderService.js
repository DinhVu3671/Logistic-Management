import service from './api';

class OrderService {

    search(search) {
        return service.post("/orders/search", search);
    }

    get(id) {
        return service.get(`/orders/${id}`);
    }

    create(data) {
        return service.post("/orders", data);
    }

    update(id, data) {
        return service.put(`/orders/${id}`, data);
    }

    delete(id) {
        return service.delete(`/orders/${id}`);
    }

    uploadFileExcel(objectFile) {
        return service.put(`/orders/upload-file`, objectFile, {
            headers: {
                'Content-type': 'multipart/form-data'
            }
        });
    }

    createOrderData() {
        return service.post("/orders/create-order-data");
    }

}

export default new OrderService();
