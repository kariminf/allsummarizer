# AllSummarizer

[![Project](https://img.shields.io/badge/Project-AllSummarizer-4B0082.svg)](https://kariminf.github.io/as.web)
[![Type](https://img.shields.io/badge/Type-Research-4B0082.svg)](https://github.com/kariminf/AllSummarizer)
[![License](https://img.shields.io/github/license/kariminf/jslingua.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![GitHub release](https://img.shields.io/github/release/kariminf/allsummarizer.svg)](https://github.com/kariminf/AllSummarizer/releases)
[![Github All Releases](https://img.shields.io/github/downloads/kariminf/allsummarizer/total.svg)](https://github.com/kariminf/AllSummarizer/releases)
<!---
[![Travis](https://img.shields.io/travis/kariminf/AllSummarizer.svg)](https://travis-ci.org/kariminf/AllSummarizer)
[![codecov](https://img.shields.io/codecov/c/github/kariminf/AllSummarizer.svg)](https://codecov.io/gh/kariminf/AllSummarizer)
[![jitpack](https://jitpack.io/v/kariminf/AllSummarizer.svg)](https://jitpack.io/#kariminf/AllSummarizer)
 -->

A research project implementation for automatic text summarization.
AllSummarizer uses an extractive method to generate the summary ;
Each sentence is scored based on some criteria, reordered, then if it scores among the first ones it will be included in the summary.

For more documentation [check this]((https://kariminf.github.io/as.web))

You can find more about the method in the paper:
```TeX
@inproceedings {13-aries-al,
	author = {Aries, Abdelkrime and Oufaida, Houda and Nouali, Omar},
	title = {Using clustering and a modified classification algorithm for automatic text summarization},
	series = {Proc. SPIE},
	volume = {8658},
	number = {},
	pages = {865811-865811-9},
	year = {2013},
	doi = {10.1117/12.2004001},
	URL = { http://dx.doi.org/10.1117/12.2004001}
}
```

Also, the participation of the system at [MultiLing 2015](http://multiling.iit.demokritos.gr/pages/revision/200) workshop:

```TeX
@Inbook{15-aries-al,
  author = {Aries, Abdelkrime
            and Zegour, Eddine Djamel
            and Hidouci, Walid Khaled},
  chapter = {AllSummarizer system at MultiLing 2015: Multilingual single and multi-document summarization},
  title = {Proceedings of the 16th Annual Meeting of the Special Interest Group on Discourse and Dialogue},
  year = {2015},
  publisher = {Association for Computational Linguistics},
  pages = {237--244},
  location = {Prague, Czech Republic},
  url = {http://aclweb.org/anthology/W15-4634}
}
```

## Dependencies:
This project is dependent to other projects:
* [KToolJa](https://github.com/kariminf/k-toolja): for file management and plugins
* [LangPi](https://github.com/kariminf/langpi): for text preprocessing; which depends on other libraries

Preprocessing plugins are in the folder: "preProcess".
For Hebrew and Tai preprocessing tools, check [LangPi releases](https://github.com/kariminf/langpi/releases/tag/v1.0.0).
Those two plugins are not Apache2 licensed.

## Command line usage
To execute from command line:
* Jar file: java -jar <jar_name> options
* Class: java kariminf.as.ui.MonoDoc options

### input/output options:
* -i <input_file>: it must be a file or a folder if it is multidocument or variant inputs
* -o <output_file>: it must be a file or a folder if it is multidocument or there is multiple output lengths, feature combinations or thresholds
* -v: variant inputs; a folder that contains files or folders to be summarized.

### summary options:
sumary unit:
* -b: we use Bytes to specify the summary size.
* -c: we use characters to specify the summary size.
* -w: we use words to specify the summary size.
* -s: we use sentences to specify the summary size.

sumary length:
* -n <number>: defines the number of units to be extracted.
* -r <ratio>: ratio from 1 to 100% defines the percentage of units to be extracted.
you can specify more than one length, by separating the lengths with semicolons

### summarizer options:
* -f <features>: the features used to score the sentences.
the features are separated by commas; for example: tfu,pos
for multiple combinations, we use semicolons; for example: tfu,pos;tfb,len
* -t <threshold>: a number from 0 to 100 to specify the threshold of clustering.
for multiple thresholds, we use semicolons; for example: 5;50

To get help, use -h

## Examples of command line
Suppose we have a folder for inputs called "exp":
```
exp
├── multi
│   ├── M001
│   │   ├── M0010.english
│   │   ├── M0011.english
│   │   └── M0012.english
│   └── M002
│       ├── M0020.english
│       ├── M0021.english
│       └── M0022.english
└── single
    ├── doc1.txt
    └── doc2.txt
```

### single document examples:
the command:
```
-i "exp/single" -o "exp/output" -l en -t "5-15:5" -n "100;200" -c -f "tfu,pos;tfb,rleng" -v
```
gives these files:
```
doc1.txt_0.05_Pos-TFU_100c.txt    doc1.txt_0.1_Pos-TFU_100c.txt     doc2.txt_0.15_Pos-TFU_100c.txt
doc1.txt_0.05_Pos-TFU_200c.txt    doc1.txt_0.1_Pos-TFU_200c.txt     doc2.txt_0.15_Pos-TFU_200c.txt
doc1.txt_0.05_RLeng-TFB_100c.txt  doc1.txt_0.1_RLeng-TFB_100c.txt   doc2.txt_0.15_RLeng-TFB_100c.txt
doc1.txt_0.05_RLeng-TFB_200c.txt  doc1.txt_0.1_RLeng-TFB_200c.txt   doc2.txt_0.15_RLeng-TFB_200c.txt
doc1.txt_0.15_Pos-TFU_100c.txt    doc2.txt_0.05_Pos-TFU_100c.txt    doc2.txt_0.1_Pos-TFU_100c.txt
doc1.txt_0.15_Pos-TFU_200c.txt    doc2.txt_0.05_Pos-TFU_200c.txt    doc2.txt_0.1_Pos-TFU_200c.txt
doc1.txt_0.15_RLeng-TFB_100c.txt  doc2.txt_0.05_RLeng-TFB_100c.txt  doc2.txt_0.1_RLeng-TFB_100c.txt
doc1.txt_0.15_RLeng-TFB_200c.txt  doc2.txt_0.05_RLeng-TFB_200c.txt  doc2.txt_0.1_RLeng-TFB_200c.txt
```

the command:
```
-i "exp/single/doc1.txt" -o "exp/output" -l en -t 5 -r "5;10" -c -f "tfu,pos"
```
gives these files:
```
doc1.txt_0.05_Pos-TFU_10%c.txt  doc1.txt_0.05_Pos-TFU_5%c.txt
```

### multi-document examples:
the command:
```
-i "exp/multi" -o "exp/output" -l en -t 5 -r "5;10" -c -f "tfu,pos" -v -m
```
gives these files:
```
M001_0.05_Pos-TFU_10%c.txt  M001_0.05_Pos-TFU_5%c.txt  
M002_0.05_Pos-TFU_10%c.txt  M002_0.05_Pos-TFU_5%c.txt
```

## License

Copyright (C) 2012-2017 Abdelkrime Aries

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
