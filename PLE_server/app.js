var express = require("express");
var path = require("path");
var app = express();
const hbase = require('hbase');
//const HBase = require('hbase-client');
const HBase = require('hbase-rpc-client');

console.log("something");

//var client = hbase({ host: 'young', port: 16000 });
client = HBase({
  zookeeperHosts: ['young'],
  zookeeperRoot: ['/hbase']
})

app.use( express.static( "./ejs" ) );
app.set('view engine', 'ejs');

app.get('/', function (req, res) {
    res.render(path.resolve('ejs/index.ejs'));
});

app.get('/page', function (req, res) {
  console.log("getting");
  const data = client.getScanner('achemoune');
  var s = data.toArray((err, res) => {
    console.log(res[0].columns[0].value);
    let json = JSON.stringify(res[0].columns[0].value);
    console.log(json);
  });
  res.render(path.resolve('ejs/page.ejs'));
});

app.listen(4371);

console.log("Running at Port 4371");