import { http } from 'msw';
import { mockBooks } from './data/books';
import { accountType } from '@/src/types/user';
import { PagedResponse } from '@/src/types/pageResponse';
import { bookType } from '@/src/types/book';

type UserRequestBody = {
  username: string,
  password: string
}

type NewAccountRequestBody = {
  username: string,
  email: string,
  password: string
}


export const handlers = [
  http.get('http://localhost:8001/auth/ping', () => {
    return new Response(JSON.stringify({
      authenticated: true,
      username: "Bob",
      role: "ADMIN"
    }), {
      status: 200,
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }),
    http.post('http://localhost:8001/auth/signup', async ({ request }) => {
    const body = (await request.json()) as NewAccountRequestBody;
    const {username, email, password} = body;
    if (username === 'Bob' && email === "bob@gmail.com" && password === 'godgod') {
        const account: accountType = {
            username: "Bob",
            role: "ADMIN"
        } 
        return new Response(JSON.stringify(account), {
            headers: {
            'Content-Type': 'application/json',
            },
            status: 200
        });
    }
    return new Response(JSON.stringify({ username: null }), {status: 401});
  }),
  http.post('http://localhost:8001/auth/login', async ({ request }) => {
    const body = (await request.json()) as UserRequestBody;
    const {username, password} = body;
    if (username === 'Bob' && password === 'godgod') {
        const account: accountType = {
            username: "Bob",
            role: "ADMIN"
        } 
        return new Response(JSON.stringify(account), {
            headers: {
            'Content-Type': 'application/json',
            },
            status: 200
        });
    }
    return new Response(JSON.stringify({ username: null }), {status: 401});
  }),

  http.get('http://localhost:8001/api/books', ({ request }) => {
    const url = new URL(request.url)
 
    const title = url.searchParams.get('title');
    const author = url.searchParams.get('author');
    const genres = url.searchParams.get('genres');
    const reserved = url.searchParams.get('isReserved');
    
    let bookFiltered = mockBooks;

    if (title) {
      bookFiltered = bookFiltered.filter(book =>
        book.title.toLowerCase().includes(title)
      );
    }

    if (genres) {
        bookFiltered = bookFiltered.filter(book =>
          book.genres.some(g => g.toLowerCase() === genres)
        );
    }

    if (author) {
      bookFiltered = bookFiltered.filter(book =>
        book.author.toLowerCase().includes(author)
      );
    }

    if (reserved !== null) {
      const isReserved = reserved === 'true';
      bookFiltered = bookFiltered.filter(book => book.reserved === isReserved);
    }

    const responseObj: PagedResponse<bookType> = {
      content: bookFiltered,
      page: 1,
      sizePerPage: 10,
      totalPages: 5,
      totalElements: 50
    };

    return new Response(JSON.stringify(responseObj), {
        headers: {
          'Content-Type': 'application/json',
        },
        status: 200
      });
    }),
];