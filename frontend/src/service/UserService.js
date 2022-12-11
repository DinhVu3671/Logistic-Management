import service from './api';

class UserService {

    login(user) {
        return service.post("/sign-in", user);
    }

    logout(user) {
        return service.post("/sign-out", user);
    }

    signinByGmail(user) {
        return service.post("/sign-up-by-gmail", user);
    }

    getUserSession(user) {
        return service.post("/get-current-user", user);
    }
}

export default new UserService();
