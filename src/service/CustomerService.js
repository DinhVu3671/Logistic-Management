import service from './api';

class CustomerService {

    search(search) {
        return service.post("/customers/search", search);
    }

    get(id) {
        return service.get(`/customers/${id}`);
    }

    create(data) {
        return service.post("/customers", data);
    }

    update(id, data) {
        return service.put(`/customers/${id}`, data);
    }

    delete(id) {
        return service.delete(`/customers/${id}`);
    }

    getClusteringCustomers() {
        return service.post("/customers/get-clustering-customers");
    }

    uploadFileExcel(objectFile) {
        return service.put(`/customers/upload-file`, objectFile, {
            headers: {
                'Content-type': 'multipart/form-data'
            }
        });
    }

    createCustomerData() {
        return service.post("/customers/create-customer-data");
    }

}

export default new CustomerService();
