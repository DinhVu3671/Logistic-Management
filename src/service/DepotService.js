import service from './api';

class DepotService {

    search(search) {
        return service.post("/depots/search", search);
    }

    get(id) {
        return service.get(`/depots/${id}`);
    }

    create(data) {
        return service.post("/depots", data);
    }

    update(id, data) {
        return service.put(`/depots/${id}`, data);
    }

    delete(id) {
        return service.delete(`/depots/${id}`);
    }

    getDepots(){
        return service.post(`/depots/get-depots`);
    }

    uploadFileExcel(objectFile) {
        return service.put(`/depots/upload-file`, objectFile, {
            headers: {
                'Content-type': 'multipart/form-data'
            }
        });
    }

}

export default new DepotService();
