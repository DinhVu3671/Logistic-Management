import service from './api';

class SolutionRouteService {

    search(search) {
        return service.post("/routes/search", search);
    }

    get(id) {
        return service.get(`/routes/${id}`);
    }

    create(data) {
        return service.post("/routes", data);
    }

    update(id, data) {
        return service.put(`/routes/${id}`, data);
    }

    delete(id) {
        return service.delete(`/routes/${id}`);
    }

    getRoutes(problemAssumption) {
        return service.post(`routes/get-solutions`, problemAssumption);
    }

    changeOrderToJourney(data) {
        return service.post(`routes/change-order-journey`, data);
    }

    saveSolution(solutionRouting) {
        return service.post(`routes/save-solution`, solutionRouting);
    }

    searchSolution(search) {
        return service.post(`routes/search-solution`, search);
    }

    trackingRoute(search) {
        return service.post(`routes/tracking`, search);
    }

    selectSolution(solutionRouting) {
        return service.post(`routes/select-solution`, solutionRouting);
    }

    // initSolutions(solutionDTO) {
    //     return service.post(`routes/init`, solutionDTO);
    // }

    // initProblem(problemAssumption) {
    //     return service.post(`routes/init-problem`, problemAssumption);
    // }

}

export default new SolutionRouteService();
