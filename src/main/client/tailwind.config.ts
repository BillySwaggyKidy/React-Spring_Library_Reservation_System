/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'white_wood_BG': "url('/background/white_wood_bg.jpg')",
        'login_BG': "url('/background/login_bg.jpg')",
      }
    },
  },
  plugins: [],
}

