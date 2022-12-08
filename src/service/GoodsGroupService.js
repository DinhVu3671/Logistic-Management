import service from './api';

class GoodsGroupService {

    search(search) {
        return service.post("/goods-groups/search", search);
    }

    get(id) {
        return service.get(`/goods-groups/${id}`);
    }

    create(data) {
        return service.post("/goods-groups", data);
    }

    update(id, data) {
        return service.put(`/goods-groups/${id}`, data);
    }

    delete(id) {
        return service.delete(`/goods-groups/${id}`);
    }

}

export default new GoodsGroupService();
