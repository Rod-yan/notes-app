import axios from 'axios';
import { Note, NoteRequest, Category } from '../types';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
const TOKEN_KEY = 'token';
const USERNAME_KEY = 'username';
const BEARER_PREFIX = 'Bearer ';

const api = axios.create({
    baseURL: API_BASE_URL,
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem(TOKEN_KEY);
    if (token) {
        config.headers.Authorization = `${BEARER_PREFIX}${token}`;
    }
    return config;
});

export const authService = {
    login: async (request: any) => {
        const response = await api.post('/auth/login', request);
        if (response.data.token) {
            localStorage.setItem(TOKEN_KEY, response.data.token);
            localStorage.setItem(USERNAME_KEY, response.data.username);
        }
        return response.data;
    },
    logout: () => {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USERNAME_KEY);
    },
    isAuthenticated: () => {
        return !!localStorage.getItem(TOKEN_KEY);
    }
};

export const noteService = {
    getNotes: async (archived = false, category?: string) => {
        const response = await api.get<Note[]>('/notes', {
            params: { archived, category }
        });
        return response.data;
    },
    createNote: async (note: NoteRequest) => {
        const response = await api.post<Note>('/notes', note);
        return response.data;
    },
    updateNote: async (id: number, note: NoteRequest) => {
        const response = await api.put<Note>(`/notes/${id}`, note);
        return response.data;
    },
    deleteNote: async (id: number) => {
        await api.delete(`/notes/${id}`);
    },
    archiveNote: async (id: number) => {
        const response = await api.post<Note>(`/notes/${id}/archive`);
        return response.data;
    },
    unarchiveNote: async (id: number) => {
        const response = await api.post<Note>(`/notes/${id}/unarchive`);
        return response.data;
    }
};

export const categoryService = {
    getAllCategories: async () => {
        const response = await api.get<Category[]>('/categories');
        return response.data;
    }
};
