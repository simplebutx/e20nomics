import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from "path";

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
})


// 5173에서 8080으로 직접 전송하면 CORS발생하므로
// proxy가 요청을 가로채서 대신 전달
// 브라우저는 5173->5173 으로 착각