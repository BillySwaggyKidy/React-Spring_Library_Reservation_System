import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import BookSearch from './bookSearch/BookSearch';

describe('Dashboard', () => {
  it('should display book items as a list', async () => {
    render(<BookSearch />);

    const items = await screen.findAllByTestId("bookCard");

    expect(items.length).greaterThan(0);
  });
});