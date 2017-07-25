var LANGUAGE = "english";
var SENTENCES_COUNT = 30;

var MODEL = "model/en";
var SIZE_FILE = "src/target-length/en.txt";

var sizes = {};

var fs = require('fs');

var lines = fs.readFileSync(SIZE_FILE, 'utf-8').split('\n');
for (var i in lines){
  if(lines[i].trim().length < 1) break;
  var parts = lines[i].split(",");
  sizes[parts[0]] = parts[1];
}

var lexrank = require('lexrank');

for (var eval in sizes){
  var txt_path = "src/body/text/en/" + eval;
  console.log(txt_path);
  var originalText = fs.readFileSync(txt_path, 'utf-8');
  var topLines = lexrank.summarize(originalText, 30, function (err, toplines, text) {
    if (err) {
      console.log(err);
    }

    var summary = "";
    var size = 0;
    var maxSize = sizes[eval];

    for (var i=0; i< toplines.length; i++){
      size = size + toplines[i].text.length;
      if (size > maxSize) break;
      summary = summary + toplines[i].text + "\n"
    }

    var outfile = "baselines/linanqiu-lexrank/en/" + eval.slice(0, -9) + ".txt";

    fs.writeFile(outfile, summary, function(err) {
      if(err) {
        return console.log(err);
      }
      console.log("The file was saved!");
    });

  });
}
