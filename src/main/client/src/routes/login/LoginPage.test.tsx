import { screen, waitFor } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import userEvent from '@testing-library/user-event';
import { Navigate, Route, Routes } from 'react-router-dom';
import LoginPage from './LoginPage';
import Home from '../home/Home';
import { renderWithProvider } from '@/src/test/test-util';
import Dashboard from '../home/dashboard/Dashboard';

describe('Login Form', () => {
  it('should allow user to login with in admin account', async () => {
    renderWithProvider(
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<Home />}>
            <Route index={true} element={<Navigate to="/books" replace />} />
            <Route path="books" element={<Dashboard />} />
          </Route>
        </Routes>,{route:'/login'}
    );

    const user = userEvent.setup();

    await user.type(screen.getByLabelText(/Username/i), 'Bob');
    await user.type(screen.getByLabelText(/Password/i), 'godgod');
    await user.click(screen.getByDisplayValue(/login/i));

    await waitFor(() => {
      expect(screen.getByText(/Bob/i)).toBeInTheDocument();
    });
  });

  it('login with unknow account', async () => {
    renderWithProvider(
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/" element={<Home />} />
        </Routes>,{route:'/login'}
    );
    const user = userEvent.setup();

    await user.type(screen.getByLabelText(/Username/i), 'tr');
    await user.type(screen.getByLabelText(/Password/i), 'srth');
    await user.click(screen.getByDisplayValue(/login/i));

    expect(await screen.findByText(/Login Failed/i)).toBeInTheDocument();
  });
});