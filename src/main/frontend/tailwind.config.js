/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // Можно настроить собственные оттенки для теплой цветовой гаммы
        "warm-brown": {
          50: "#FAF7F2",
          100: "#F5EEDA",
          200: "#EBD9B4",
          300: "#E0C28F",
          400: "#D5AB6A",
          500: "#C99444",
          600: "#AB7A36",
          700: "#8D6029",
          800: "#6F461C",
          900: "#512C0F",
        },
      },
    },
  },
  plugins: [],
}