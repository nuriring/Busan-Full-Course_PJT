import axios from 'axios';

const client = axios.create({
  baseURL: 'http://127.0.0.1:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

client.interceptors.request.use((config) => {
  config.headers['access-token'] = localStorage.getItem('accessToken');
  return config;
});

export default client;