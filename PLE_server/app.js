var express = require("express");
var path = require("path");
var app = express();
const hbase = require('hbase');
//const HBase = require('hbase-client');
const HBase = require('hbase-rpc-client');
const { createImageData, Canvas } = require('canvas');

//var client = hbase({ host: 'young', port: 16000 });
client = HBase({
  zookeeperHosts: ['ripoux'],
  zookeeperRoot: ['/hbase']
})

app.use( express.static( "./ejs" ) );
app.set('view engine', 'ejs');

app.get('/', function (req, res) {
    res.render(path.resolve('ejs/index.ejs'));
});

/*app.get('/page', function (req, res) {
  console.log("getting");
  const data = client.getScanner('achemoune');
  var s = data.toArray((err, res) => {
    //console.log(res[0].columns[0].value);
    //let json = JSON.stringify(res[0].columns[0].value);
    //console.log(json);
  });
  res.render(path.resolve('ejs/page.ejs'));
});*/

app.get('/canvas', function (req, result) {
  /*let x = req.params.x;
  let y = req.params.y;
  let z = req.params.z;
  console.log('x : ' + x + ', y : ' + y + ', z : ' + z);
  let rowkey = "X" + x + "Y" + y;
  console.log('après envoi : ' + rowkey);*/
  
  const data = client.getScanner('ourdb2');
  var s = data.toArray((err, res) => {

    let length = 1201;
    let arraySize = length * length * 4;
    let imgData = createImageData(new Uint8ClampedArray(arraySize), length);
    let canvas = new Canvas(1201*5, 1201*5);
    let ctx = canvas.getContext('2d');

    console.log(imgData.data.length);
    
    if (err !== null) {
      let i;
      for (i = 0; i < imgData.data.length; i+=4) {
        imgData.data[i] = 200;
        imgData.data[i+1] = 255;
        imgData.data[i+2] = 255;
        imgData.data[i+3] = 255;
      }

    } else {

      for(let i=0; i<res.length; i++){
        console.log(JSON.stringify(res[i].columns[1].value));
        console.log("============");
        let x = parseInt(JSON.stringify(res[i].columns[1].value).split('[')[1].split(']')[0].split(',')[0]) + 180;
        let y = 90 - parseInt(JSON.stringify(res[i].columns[1].value).split('[')[1].split(']')[0].split(',')[1]);

        console.log("x = "+x);
        console.log("y = "+y);

        console.log("iterati------"+i);
        
        let str1 = JSON.stringify(res[1].columns[0].value);
        var heightValues = str1.split('[')[1].split(']')[0].split(',');

        for(let i=0; i < heightValues.length; i+=4){
          
          let cl = getColor(heightValues[i]);

          imgData.data[i] = cl.r;
          imgData.data[i+1] = cl.g;
          imgData.data[i+2] = cl.b;
          imgData.data[i+3] = cl.a;
          
        }
        ctx.putImageData(imgData, x*300, y*300);
      }
    }
    result.setHeader('Content-Type', 'image/png');
    canvas.pngStream().pipe(result);
  });
});


app.listen(4372);

console.log("Running at Port 4372");

function getColor(value) {
  let result = {
    r: 0,
    g: 0,
    b: 0
  };

  if(value < 256) {result.r = 255; result.g = 255; result.b = 255;}
  if(value < 200) {result.r = 255; result.g = 255; result.b = 102;}
  if(value < 150) {result.r = 153; result.g = 255; result.b = 51;}
  if(value < 100) {result.r = 0; result.g = 153; result.b = 0;}
  if(value < 50) {result.r = 0; result.g = 102; result.b = 0;}
  if(value < 25) {result.r = 0; result.g = 255; result.b = 255;}

  return result;
}
