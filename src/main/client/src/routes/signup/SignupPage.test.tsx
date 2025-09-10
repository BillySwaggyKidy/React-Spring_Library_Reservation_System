import { screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import userEvent from '@testing-library/user-event';
import { Route, Routes } from 'react-router-dom';
import { renderWithProvider } from '@/src/test/test-util';
import SignupPage from './SignupPage';
import LoginPage from '../login/LoginPage';

describe('Signup Form', () => {
    it('try to create a new account', async () => {
        renderWithProvider(
            <Routes>
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/login" element={<LoginPage />} />
            </Routes>,{route:'/signup'}
        );

        const user = userEvent.setup();

        await user.type(screen.getByLabelText(/Username/i), 'Bob');
        await user.type(screen.getByLabelText(/Email/i), 'bob@gmail.com');
        await user.type(screen.getByLabelText(/^Password$/i), 'godgod');
        await user.type(screen.getByLabelText(/^Confirm password$/i), 'godgod');
        await user.click(screen.getByDisplayValue(/Create new account/i));

        expect((await screen.findAllByText(/Login/i)).length).greaterThan(0);
    });

    it('try to create a new account without filling the confirm password', async () => {
        renderWithProvider(
            <Routes>
                <Route path="/signup" element={<SignupPage />} />
                <Route path="/login" element={<LoginPage />} />  
            </Routes>,{route:'/signup'}
        );

        const user = userEvent.setup();

        await user.type(screen.getByLabelText(/Username/i), 'Bob');
        await user.type(screen.getByLabelText(/Email/i), 'bob@gmail.com');
        await user.type(screen.getByLabelText(/^Password$/i), 'godgod');
        await user.click(screen.getByDisplayValue(/Create new account/i));

        expect(await screen.findByText(/Please confirm your password/i)).toBeInTheDocument();
    });
})