import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,                  // pour utiliser `describe`, `it`, `expect` sans import
    environment: 'jsdom',           // simulate un DOM pour React
    setupFiles: './src/test/setupTests.ts',  // fichier où tu importes jest-dom
    include: ['src/**/*.test.{ts,tsx}'], // où sont tes fichiers de tests
    css: true,                      // si tu utilises Tailwind
    alias: {
    '@': path.resolve(__dirname, './'),
  },
  },
})