const express = require('express');

const app = express();

var WebHDFS = require('webhdfs');

var hdfs = WebHDFS.createClient(/*{user: 'webuser', host: 'localhost', port: 9000, path: 'hdfs://localhost:9000'}*/);

var remoteFileStream = hdfs.createReadStream('hdfs://localhost:9000/data/Output.png');

app.get('/', function(req, res) {
    res.sendfile('./index.html');
});

app.use(function(req, res, next){
  res.status(404).send('Page introuvable !');
});

app.listen(3000, function() {
  console.log('PLE app listening on port 3000!');
});
