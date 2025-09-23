import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10, // виртуальные пользователи
    duration: '10s', // секунды теста
};

export default function () {
    // 1. Логинимся
    const loginRes = http.post(
        'http://vehiclepark-core:8888/process_login',
        {
            username: 'Ivan',
            password: '12345',
        },
        {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            redirects: 0, // чтобы ловить 302
        }
    );

    check(loginRes, {
        'login redirect 302': (r) => r.status === 302,
        'redirect to enterprises': (r) =>
            r.headers['Location'] === 'http://vehiclepark-core:8888/managers/enterprises',
    });


    // 2. Достаём куки
    const cookies = loginRes.cookies;

    // 3. Проверяем доступ к защищённой странице
    const res = http.get('http://vehiclepark-core:8888/managers/enterprises', {
        cookies,
    });

    check(res, { 'page loaded': (r) => r.status === 200 });

    sleep(1);
}
