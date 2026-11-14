const fs = require('fs'), p = require('path');
const L = [
  ['jszip/dist/jszip.min.js', 'jszip.min.js'],
  ['html2canvas/dist/html2canvas.min.js', 'html2canvas.min.js'],
  ['jspdf/dist/jspdf.umd.min.js', 'jspdf.umd.min.js']
];
fs.mkdirSync('www/libs', { recursive: true });
fs.copyFileSync('index.html', 'www/index.html');
L.forEach(([s, d]) => fs.copyFileSync(p.join('node_modules', s), p.join('www/libs', d)));
console.log('Page et outils copiés dans www');
