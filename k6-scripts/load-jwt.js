import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 50,          // виртуальные пользователи
    duration: '30s',  // секунды теста
};

export default function () {
    // 1. Авторизация
    const loginRes = http.post('http://vehiclepark-core:8888/api/auth/login', JSON.stringify({
        username: 'Ivan',
        password: '12345'
    }), {
        headers: { 'Content-Type': 'application/json' }
    });

    check(loginRes, { 'login success': (r) => r.status === 200 });

    const token = loginRes.json('jwt-token');

    // 2. Запрос к защищённому эндпоинту
    const res = http.get('http://vehiclepark-core:8888/api/managers/1/vehicles', {
        headers: { Authorization: `Bearer ${token}` }
    });

    check(res, { 'status 200': (r) => r.status === 200 });

    sleep(1); // пауза
}
