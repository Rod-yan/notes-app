import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import Login from './Login';
import { authService } from '../services/api';

vi.mock('../services/api', () => ({
  authService: {
    login: vi.fn(),
  },
}));

describe('Login', () => {
  it('calls onLoginSuccess on successful login', async () => {
    const mockLogin = authService.login as any;
    mockLogin.mockResolvedValueOnce({ token: '123', username: 'admin' });
    const onSuccess = vi.fn();

    render(<Login onLoginSuccess={onSuccess} />);

    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'admin' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'admin123' } });
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    expect(mockLogin).toHaveBeenCalledWith({ username: 'admin', password: 'admin123' });
    // Wait for async
    await vi.waitFor(() => expect(onSuccess).toHaveBeenCalled());
  });

  it('shows error on failed login', async () => {
    const mockLogin = authService.login as any;
    mockLogin.mockRejectedValueOnce({ response: { data: { message: 'Invalid credentials' } } });
    const onSuccess = vi.fn();

    render(<Login onLoginSuccess={onSuccess} />);

    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'admin' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'wrong' } });
    fireEvent.click(screen.getByRole('button', { name: /login/i }));

    await vi.waitFor(() => expect(screen.getByText(/Invalid credentials/i)).toBeDefined());
    expect(onSuccess).not.toHaveBeenCalled();
  });
});
