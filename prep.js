const fs = require('fs'), p = require('path');
const L = [
  [['jszip/dist/jszip.min.js'], 'jszip.min.js'],
  [['html2canvas/dist/html2canvas.min.js'], 'html2canvas.min.js'],
  [['jspdf/dist/jspdf.umd.min.js'], 'jspdf.umd.min.js'],
  [['mammoth/mammoth.browser.min.js'], 'mammoth.browser.min.js'],
  [['pdfjs-dist/build/pdf.min.js'], 'pdf.min.js'],
  [['pdfjs-dist/build/pdf.worker.min.js'], 'pdf.worker.min.js'],
  [['qrcode-generator/qrcode.js', 'qrcode-generator/dist/qrcode.js'], 'qrcode.js']
];
fs.mkdirSync('www/libs', { recursive: true });
fs.copyFileSync('index.html', 'www/index.html');
let manque = 0;
L.forEach(([srcs, d]) => {
  const f = srcs.find(x => fs.existsSync(p.join('node_modules', x)));
  if (f) fs.copyFileSync(p.join('node_modules', f), p.join('www/libs', d));
  else { manque++; console.log('ATTENTION : fichier introuvable pour', d); }
});
console.log('Page et outils copiés dans www' + (manque ? ' (' + manque + ' manquant(s))' : ''));
