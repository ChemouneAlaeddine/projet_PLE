var express = require("express");
var path = require("path");
var app = express();
const hbase = require('hbase');
//const HBase = require('hbase-client');
const HBase = require('hbase-rpc-client');
const { createImageData, Canvas } = require('canvas');

console.log("something");

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
  
  const data = client.getScanner('hello');
  var s = data.toArray((err, res) => {
    //console.log(json);

    let length = 1201;
    let arraySize = length * length /** 4*/;
    let imgData = createImageData(new Uint8ClampedArray(arraySize), length);
    //let imgData1 = createImageData(new Uint8ClampedArray(arraySize), length);
    let canvas = new Canvas(1201, 1201);
    let ctx = canvas.getContext('2d');
    console.log(imgData.data.length);
    if (err !== null) {
      let i;
      for (i = 0; i < imgData.data.length; i+=4/*i+=4*/) {
        imgData.data[i] = 200;
        imgData.data[i+1] = 255;
        imgData.data[i+2] = 255;
        imgData.data[i+3] = 255;
      }

    } else {
      console.log(res[0].columns[1].value);
      for(let i=0; i<2;i++){
        //console.log("iterati------"+i);
      let str = JSON.stringify(res[i].columns[0].value);
      
      var heightValues = str.split('[')[1].split(']')[0].split(',');
      let coord = JSON.stringify(res[i].columns[1].value);
      console.log(coord[0]);
      var x = str.split('[')[1].split(']')[0].split(',')[0];
      var y = str.split('[')[1].split(']')[0].split(',')[1];
      //console.log(x+"---"+y);
      for(let i=0; i < heightValues.length; i+=4){
        
        let cl = getColor(heightValues[i]);

        imgData.data[i] = cl.r;
        imgData.data[i+1] = cl.g;
        imgData.data[i+2] = cl.b;
        imgData.data[i+3] = cl.a;
        
      }
      ctx.putImageData(imgData, i*300, 0);
      }
     /* let str1 = JSON.stringify(res[1].columns[0].value);
      var heightValues1 = str1.split('[')[1].split(']')[0].split(',');
      
      for(let i=0; i < heightValues1.length; i+=4){
        
        let cl = getColor(heightValues1[i]);

        imgData1.data[i] = cl.r;
        imgData1.data[i+1] = cl.g;
        imgData1.data[i+2] = cl.b;
        imgData1.data[i+3] = cl.a;
      }*/
    }

    //ctx.putImageData(imgData, 0, 0);
    //ctx.putImageData(imgData1, 300, 0);
    result.setHeader('Content-Type', 'image/png');
    canvas.pngStream().pipe(result);
  });


});





app.listen(4374);

console.log("Running at Port 4371");









function getColor(hv) {
  let color = {
    r: 0,
    g: 0,
    b: 0,
    a: 0
  };
  if (hv == 0) {
    color.g = 255;
    color.r = 200;
    color.b = 255;
  } else if ((hv > 0) && (hv <= 64)) {
    color.r = hv * 3;
    color.g = 64 + hv * 2;
    color.b = 0;
  } else if (hv > 64 && hv <= 128) {
    color.r = 192 - (hv - 64);
    color.g = 192 - (hv - 64) * 2;
    color.b = 0;
  } else if (hv > 128 && hv <= 239) {
    color.r = hv;
    color.g = 63 + ((hv - 128) * 1.5);
    color.b = 0;
  } else if (hv > 239) {
    color.r = 255;
    color.g = 255;
    color.b = hv;
  }
  color.a = 255;
  return color;
}