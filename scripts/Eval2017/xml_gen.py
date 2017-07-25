# -*- coding: utf-8 -*-


import sys
import os
dir_path = os.path.dirname(os.path.realpath(__file__))

SIZE_FILE = "src/target-length/en.txt"

evals = []

def extract(summarizer, maxSize):
    summary = u""
    size = 0
    for sentence in summarizer(parser.document, SENTENCES_COUNT):
        size = size + len(unicode(sentence))
        if size > maxSize:
            break;
        summary = summary + unicode(sentence) + "\n"
    return summary

def readTextFile(path):
    txt_file = open(path, 'r')
    text = u""
    while 1:
        line = txt_file.readline()
        if line == "":
            break;
        text = text + line + "\n"
    return text

if __name__ == "__main__":
    reload(sys)
    sys.setdefaultencoding('utf8')
    """
    nltk.data.path.append('/home/kariminf/Data/NLTK/')



    for sentence in summarizer(parser.document, SENTENCES_COUNT):
        print(sentence)
    """

    file = open(SIZE_FILE, 'r')
    while 1:
        line = file.readline()
        if line == '':
			break;
        parts = line.split(",")
        evals.append(parts[0])
    file.close()

    xmlcontent = "<ROUGE-EVAL version=\"1.0\">\n"

    peers = os.listdir('baselines/')

    for eval in evals:
        xmlcontent += "<EVAL ID=\"" + eval[:-9] + "\">\n"
        xmlcontent += "<PEER-ROOT>\n"
        xmlcontent += dir_path + "/baselines\n"
        xmlcontent += "</PEER-ROOT>\n"
        xmlcontent += "<MODEL-ROOT>\n"
        xmlcontent += dir_path + "/model/en\n"
        xmlcontent += "</MODEL-ROOT>\n"
        xmlcontent += "<INPUT-FORMAT TYPE=\"SPL\">\n"
        xmlcontent += "</INPUT-FORMAT>\n"
        xmlcontent += "<PEERS>\n"
        for peer in peers:
            xmlcontent += "<P ID=\""+ peer + "\">" + peer + "/en/" + eval[:-9] + ".txt</P>\n"
        xmlcontent += "</PEERS>\n";
        xmlcontent += "<MODELS>\n";
        xmlcontent += "<M ID=\"M1\">" + eval[:-9] + "_summary.txt</M>\n";
        xmlcontent += "</MODELS>\n";
        xmlcontent += "</EVAL>\n";

    xmlcontent += "</ROUGE-EVAL>\n"

    fout = open("train.xml", "w")
    fout.write(xmlcontent)
    fout.close()
