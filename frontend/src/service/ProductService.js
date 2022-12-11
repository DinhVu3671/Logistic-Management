import service from './api';

class ProductService {

    search(search) {
        return service.post("/products/search", search);
    }

    get(id) {
        return service.get(`/products/${id}`);
    }

    create(data) {
        return service.post("/products", data);
    }

    update(id, data) {
        return service.put(`/products/${id}`, data);
    }

    delete(id) {
        return service.delete(`/products/${id}`);
    }

    uploadFileExcel(objectFile) {
        return service.put(`/products/upload-file`, objectFile, {
            headers: {
                'Content-type': 'multipart/form-data'
            }
        });
    }

}

export default new ProductService();
