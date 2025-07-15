import '@testing-library/jest-dom';
import { setupServer } from 'msw/node'
import { handlers } from './mocks/handlers';
import { beforeAll, afterEach, afterAll } from 'vitest';

const server = setupServer(...handlers);

beforeAll(() => {
  server.listen({
    onUnhandledRequest: 'error', // pour faire échouer si le MSW n'intercepte pas
  });

  server.events.on('request:start', (req) => {
    console.log('[MSW REQUEST]', req.request.method, req.request.url);
  });

  server.events.on('request:match', (req) => {
    console.log('[MSW MATCH]', req.request.url);
  });

  server.events.on('request:unhandled', (req) => {
    console.warn('[MSW ⚠️ UNHANDLED]', req.request.method, req.request.url);
  });
});

afterEach(() => server.resetHandlers());
afterAll(() => server.close());