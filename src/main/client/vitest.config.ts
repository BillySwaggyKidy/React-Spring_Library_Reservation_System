import { defineConfig } from 'vitest/config'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite';
import svgr from 'vite-plugin-svgr';
import path from 'path'

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    svgr()
  ],
  test: {
    globals: true,                 
    environment: 'jsdom',
    setupFiles: './src/test/setupTests.tsx',
    include: ['src/**/*.test.{ts,tsx}'],
    css: true,
    alias: {
      '@': path.resolve(__dirname, './'),
    },
  },
})