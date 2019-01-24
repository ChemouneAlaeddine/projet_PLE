var express = require("express");
var path = require("path");
var app = express();
app.use( express.static( "./ejs" ) );
app.set('view engine', 'ejs');

app.get('/', function (req, res) {
    res.render(path.resolve('ejs/index.ejs'));
  });
  app.get('/page', function (req, res) {
    res.render(path.resolve('ejs/page.ejs'));
  });

app.listen(8080);

console.log("Running at Port 8080");