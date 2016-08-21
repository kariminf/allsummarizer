[![Hex.pm](https://img.shields.io/badge/Project-AllSummarizer-red.svg?style=plastic)](https://github.com/kariminf/AllSummarizer)
[![Hex.pm](https://img.shields.io/badge/License-Apache_2-red.svg?style=plastic)](https://github.com/kariminf/AllSummarizer/blob/master/LICENSE)
[![Hex.pm](https://img.shields.io/badge/Version-2.1.0-red.svg?style=plastic)](https://github.com/kariminf/AllSummarizer/releases)

AllSummarizer
=============
A research project implimentation for automatic text summarization.
AllSummarizer is considered as an extractive method;
Each sentence is scored based on some creteria, reorder the most scored ones then extract the first relevant ones.

You can find more about the method in the paper:

 | Using clustering and a modified classification algorithm for automatic text summarization
------------ | -------------
Authors: | Abdelkrime Aries (A), Houda Oufaida (A) and Omar Nouali (B)
Affiliation: | A: Ecole Nationale Supérieue d'Informatique (ESI),  Algeria; <br> B: Ctr. de recherche sur l'Information Scientifique et Technique (CERIST), Algeria
Booktitle: | Document Recognition and Retrieval XX. Proceedings of the SPIE, Volume 8658.
Date: | February 4, 2013
Address: | Burlingame, California, USA
Publisher: | SPIE
Link: | http://dx.doi.org/10.1117/12.2004001

Also, the participation of the system at [MultiLing 2015](http://multiling.iit.demokritos.gr/pages/revision/200) workshop:

 | AllSummarizer system at MultiLing 2015: Multilingual single and multi-document summarization
------------ | -------------
Authors: | Abdelkrime Aries, Djamel Eddine Zegour and Khaled Walid Hidouci
Affiliation: | Ecole Nationale Supérieue d'Informatique (ESI),  Algeria
Booktitle: | Proceedings of the SIGDIAL 2015 Conference, pages 237–244, , . 2015
Date: | September 2-4, 2015
Address: | Prague, Czech Republic
Publisher: | Association for Computational Linguistics
Link: | http://www.aclweb.org/anthology/W15-4634

# How it works?
TODO: add a brief description since there is a link to the paper

# Dependencies:
This project is dependent to other projects:
* [KToolJa](https://github.com/kariminf/KToolJa): for file management and plugins
* [LangPi](https://github.com/kariminf/LangPi): for text preprocessing; which depends on other libraries

Preprocessing plugins are in the folder: "preProcess".
For Hebrew and Tai preprocessing tools, check [LangPi releases](https://github.com/kariminf/LangPi/releases/tag/v1.0.0).
Those two plugins are not Apache2 licensed.

# Command line usage
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

# License
The code is released under Apache 2.0 license.
For more details about this license, check [LICENSE](./LICENSE) file
