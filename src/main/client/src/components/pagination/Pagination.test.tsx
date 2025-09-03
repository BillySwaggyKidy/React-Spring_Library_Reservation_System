import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import Pagination from './Pagination';
import userEvent from '@testing-library/user-event';

// this function does nothing, only for the tests
const dummyFunction = (page: number) => {
    page = 0;
    return page;
}

describe('Pagination', () => {
  it('display pagination without previous button because we are at the beginning', async () => {
    render(<Pagination totalPages={10} currentPage={0} handlePageRequest={dummyFunction}/>);

    const nextButton = await screen.findByText(/next/i);

    expect(screen.queryByText(/previous/i)).not.toBeInTheDocument();
    expect(await screen.findByText(/^1$/i)).toBeInTheDocument();
    expect(await screen.findByText(/^2$/i)).toBeInTheDocument();
    expect(await screen.findByText(/^3$/i)).toBeInTheDocument();
    expect(await screen.findByText(/\.{3}/i)).toBeInTheDocument();
    expect(nextButton).toBeInTheDocument();
  });

  it('navigate using the incr button', async () => {
    render(<Pagination totalPages={10} currentPage={0} handlePageRequest={dummyFunction}/>);

    const nextButton = await screen.findByText(/next/i);
    const user = userEvent.setup();

    await user.click(nextButton);
    const previousButton = await screen.findByText(/previous/i);
    expect(previousButton).toBeInTheDocument();
    await user.click(previousButton);
    expect(await screen.findByText(/^1$/i)).toBeInTheDocument();
    expect(previousButton).not.toBeInTheDocument();
  });

  it('going to the last page', async () => {
    render(<Pagination totalPages={10} currentPage={0} handlePageRequest={dummyFunction}/>);

    const lastPage = await screen.findByText(/10/i);
    const user = userEvent.setup();

    await user.click(lastPage);
    expect(screen.queryByText(/\.{3}/i)).not.toBeInTheDocument();
    expect(screen.queryByText(/next/i)).not.toBeInTheDocument();
  })
});