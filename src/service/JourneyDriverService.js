import service from './api';

class JourneyDriverService {

    tracking(search) {
        return service.post(`journey-driver/tracking`, search);
    }

}

export default new JourneyDriverService();
