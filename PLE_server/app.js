const express = require('express');
const app = express();
const hbase = require('hbase-rpc-client');


client = hbase({
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
 let pos = "x"+x+"y"+y;
 
  get = new hbase.Get(pos);
  const data = client.get('ourdb', get,(err, res) => {
 if (res === null || res === undefined ) {
 result.sendFile(__dirname+"/nothing.png")
 }else{
 val = res.columns[0].value;
 let data = new Buffer(val, 'base64');
 result.contentType('image/png');
 result.send(data);
 }
 })
});

app.listen(4350, function () {
 console.log('Listening on port ' + 4350);
});
