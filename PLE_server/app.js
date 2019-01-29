const express = require('express');
const app = express();
const port = 4350;
const HBase = require('hbase-rpc-client');
const dbName = 'one';

client = HBase({
   zookeeperHosts: ['young'],
   zookeeperRoot: ['/hbase']
  })

app.set('views', "./ejs/");
app.set('view engine', 'ejs');

app.get('/', function (req, res) { 
 res.render('index');
});

app.get('/tile/:z/:x/:y', function (req, result) {
 let x = req.params.x;
 let y = req.params.y;
 let z = 1;

 console.log("coordonnés "+ x +" "+ y+" "+z);
 let index = "zoom" + z + "x" + x +"y"+ y;
 
  get = new HBase.Get(index);
  const data = client.get(dbName, get,(err, res) => {
 //console.log(res.columns[0].value);
 if (res === null || res === undefined ) {
 result.sendFile(__dirname+"/water.png")
 }
 else {
 val = res.columns[0].value;
 let data = new Buffer(val, 'base64');
 result.contentType('image/png');
 // result.setHeader('Content-Type', 'image/png');
 result.send(data);
 }
 })
});

app.listen(port, function () {
 console.log('Listening on port ' + port);
});