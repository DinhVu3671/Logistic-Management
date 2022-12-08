import service from './api';

class VehicleService {

    search(search) {
        return service.post("/vehicles/search", search);
    }

    get(id) {
        return service.get(`/vehicles/${id}`);
    }

    create(data) {
        return service.post("/vehicles", data);
    }

    update(id, data) {
        return service.put(`/vehicles/${id}`, data);
    }

    updateVehicleProduct(idVehicle, id, data) {
        return service.put(`/vehicles/${idVehicle}/vehicle-product/${id}`, data);
    }

    delete(id) {
        return service.delete(`/vehicles/${id}`);
    }

    uploadFileExcel(objectFile) {
        return service.put(`/vehicles/upload-file`, objectFile, {
            headers: {
                'Content-type': 'multipart/form-data'
            }
        });
    }

}

export default new VehicleService();
