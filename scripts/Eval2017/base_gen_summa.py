# -*- coding: utf-8 -*-

import sys, os

from summa.summarizer import summarize


LANGUAGE = "english"
SENTENCES_COUNT = 30

MODEL = "model/en"
SIZE_FILE = "src/target-length/en.txt"

sizes = dict()

def extract(sentences, maxSize):
    summary = u""
    size = 0
    for sentence in sentences:
        size = size + len(unicode(sentence))
        if size > maxSize:
            break;
        summary = summary + unicode(sentence) + "\n"
    if size == 0:
        return sentences[0] + "\n"
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
        sizes[parts[0]] = int(parts[1])
    file.close()
    

    #nltk.data.path.append('/home/kariminf/Data/NLTK/')
    for eval in sizes:
		txt_path = "src/body/text/en/" + eval
		print(txt_path)
		text = readTextFile(txt_path)
		sentences = summarize(text,language=LANGUAGE, split=True)
		summary = extract(sentences, sizes[eval])
		fout = open("baselines/summa_textrank/en/" + eval[:-9] + ".txt", "w")
		fout.write(summary)
		fout.close()
