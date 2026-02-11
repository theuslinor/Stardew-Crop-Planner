/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'stardew-gold': '#ffcc00',
        'stardew-brown': '#5c3a21',
        'stardew-panel': '#f4d19b',
      }
    },
  },
  plugins: [],
}
