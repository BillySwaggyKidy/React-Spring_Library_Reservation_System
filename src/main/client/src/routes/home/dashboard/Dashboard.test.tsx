import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import BookSearch from './bookSearch/BookSearch';
import { Routes, Route, MemoryRouter } from 'react-router-dom';

describe('Dashboard', () => {
  it('should display book items as a list', async () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <Routes>
          <Route path="/" element={<BookSearch />} />
        </Routes>
      </MemoryRouter>
    );

    const items = await screen.findAllByTestId("bookCard");

    expect(items.length).greaterThan(0);
  });
});