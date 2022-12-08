import axios from 'axios';

const api = axios.create({
    baseURL: `http://127.0.0.1:8080/`,
    // baseURL: `https://route-scheduler-be.azurewebsites.net/`,

});

// Add a request interceptor
api.interceptors.request.use(
    config => {
        const sessionId = localStorage.getItem('sessionId');
        if (sessionId) {
            config.headers['sessionId'] = sessionId;
        }
        return config;
    },
    error => {
        Promise.reject(error)
    });


// Add a 401 response interceptor
api.interceptors.response.use(function (response) {
    return response;
}, function (error) {
    if (401 === error.response.status) {
        const history = window.history;
        localStorage.clear();
        history.pushState(null, '', '/#');
        window.location.reload();
    } else {
        return Promise.reject(error);
    }
});


export default api;