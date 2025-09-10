import { bookDetailsType } from "@/src/types/book";


export const mockBooks: bookDetailsType[] = [
  {
    id: 1,
    title: 'The Lost City',
    bookCoverUrl: '/covers/lost-city.jpg',
    authorName: 'Billy Bob',
    authorId: 101,
    description: 'An epic adventure into an ancient city lost to time.',
    genres: ['Adventure', 'Fantasy'],
    publishDate: new Date('2019-01-12'),
    volume: 1,
    totalPages: 320,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 2,
    title: 'Ocean of Dreams',
    bookCoverUrl: '/covers/ocean-dreams.jpg',
    authorName: 'John Doe',
    authorId: 102,
    description: 'A journey across mystical seas filled with wonder.',
    genres: ['Fantasy', 'Drama'],
    publishDate: new Date('2020-03-22'),
    volume: 1,
    totalPages: 280,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 3,
    title: 'Tech Future',
    bookCoverUrl: '/covers/tech-future.jpg',
    authorName: 'Eva Chang',
    authorId: 103,
    description: 'Exploring the possibilities and dangers of future technology.',
    genres: ['Science Fiction', 'Technology'],
    publishDate: new Date('2021-06-10'),
    volume: 1,
    totalPages: 350,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 4,
    title: 'Heart of the Jungle',
    bookCoverUrl: '/covers/jungle-heart.jpg',
    authorName: 'Mike Johnson',
    authorId: 104,
    description: 'A thrilling survival tale set deep in the Amazon rainforest.',
    genres: ['Adventure', 'Survival'],
    publishDate: new Date('2018-09-18'),
    volume: 1,
    totalPages: 295,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 5,
    title: 'Mathematical Wonders',
    bookCoverUrl: '/covers/math-wonders.jpg',
    authorName: 'Marie Curie',
    authorId: 105,
    description: 'Unlocking the mysteries of mathematics through engaging stories.',
    genres: ['Non-fiction', 'Education'],
    publishDate: new Date('2017-11-05'),
    volume: 1,
    totalPages: 400,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 6,
    title: 'Cooking with Love',
    bookCoverUrl: '/covers/cooking-love.jpg',
    authorName: 'Gordon Ramsay',
    authorId: 106,
    description: 'Recipes and stories from a life spent in the kitchen.',
    genres: ['Cooking', 'Lifestyle'],
    publishDate: new Date('2020-05-15'),
    volume: 1,
    totalPages: 220,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 7,
    title: 'Mystery of the Manor',
    bookCoverUrl: '/covers/manor-mystery.jpg',
    authorName: 'Agatha Brown',
    authorId: 107,
    description: 'A chilling mystery unfolds in an old countryside manor.',
    genres: ['Mystery', 'Thriller'],
    publishDate: new Date('2019-10-30'),
    volume: 1,
    totalPages: 310,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 8,
    title: 'Stars Above',
    bookCoverUrl: '/covers/stars-above.jpg',
    authorName: 'Neil Armstrong',
    authorId: 108,
    description: 'An astronaut’s reflection on life beyond Earth.',
    genres: ['Biography', 'Science'],
    publishDate: new Date('2016-07-20'),
    volume: 1,
    totalPages: 270,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 9,
    title: 'Neo & The Matrix',
    bookCoverUrl: '/covers/matrix.jpg',
    authorName: 'The Wachowskis',
    authorId: 109,
    description: 'A deep dive into the story of Neo and the Matrix universe.',
    genres: ['Science Fiction', 'Action'],
    publishDate: new Date('1999-03-31'),
    volume: 1,
    totalPages: 330,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  },
  {
    id: 10,
    title: 'Farming 101',
    bookCoverUrl: '/covers/farming.jpg',
    authorName: 'Farmer Joe',
    authorId: 110,
    description: 'The beginner’s guide to sustainable and modern farming.',
    genres: ['Non-fiction', 'Agriculture'],
    publishDate: new Date('2015-04-12'),
    volume: 1,
    totalPages: 260,
    status: {
      available: true,
      condition: 'NEW',
      beAvailableAt: new Date('2019-11-05')
    }
  }
];
