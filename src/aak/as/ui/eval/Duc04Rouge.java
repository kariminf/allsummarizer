package aak.as.ui.eval;

import java.io.File;
import java.io.IOException;
import java.util.List;
import aak.as.tools.Calculus;
import  aak.as.tools.FileManager;


public class Duc04Rouge {

	private static String duc04folder = "duc04/src/";
	private static String rougefolder = "duc04/rouge/";
	private static String mdlfolder = "/media/kariminf/MyData/DEV/Summarization/ROUGE/duc04/MODEL/";
	
	private static String peerfolder = "PEER";
	private static String modelfolder = "../MODEL";//../MODEL
	
	private static String xmlfolder = "XML-ABS/";//XML-ABS/
	private static String outfolder = "OUT/ABS/";
	
	private static String [] features = {
		"TFU",
		"TFB",
		"Pos",
		"RLeng",
		"PLeng",
	};
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File dir = new File(duc04folder);
		File [] topics = dir.listFiles();
		
		String[] mfiles = new File(mdlfolder).list();
		System.out.println(mfiles.length);
		
		String perlscript = "#!/usr/bin/perl -w \n";
	    perlscript += "use Cwd;\n";
	    perlscript += "$curdir=getcwd;\n";
	    perlscript += "$ROUGE=\"perl ../../ROUGE-1.5.5.pl\";\n";
	    perlscript += "chdir(\"duc04/AS2014\");\n\n";
	    
		
		
		// number of combinations
		for (int combNbr=1; combNbr <= features.length; combNbr++){
			List<List<Integer>> comb = 
					Calculus.getCombinations(features.length, combNbr);	

			//different combinations for a given number
			for (List<Integer> oneComb : comb){
				String combStr = "";
				for (int index: oneComb)
					combStr += features[index] + "-";
				combStr = combStr.substring(0, combStr.length()-1);
				
				String xmlcontent = "<ROUGE-EVAL version=\"1.0\">\n";
				
				for(File topic : topics){ // files
					
					
					String topicname = topic.getName();
					

					xmlcontent += "<EVAL ID=\""+topicname+"\">\n";
					
					xmlcontent += "<PEER-ROOT>\n";
					xmlcontent += peerfolder + "\n";
					xmlcontent += "</PEER-ROOT>\n";
					xmlcontent += "<MODEL-ROOT>\n";
					xmlcontent += modelfolder + "\n";
					xmlcontent += "</MODEL-ROOT>\n";
					xmlcontent += "<INPUT-FORMAT TYPE=\"SPL\">\n";
					xmlcontent += "</INPUT-FORMAT>\n";
					xmlcontent += "<PEERS>\n";

					for(int th = 0; th <= 25; th++)
						xmlcontent += "<P ID=\""+ th +"\">" + combStr + "/" + topicname + "/" + th + ".asz</P>\n";

					xmlcontent += "</PEERS>\n";
					xmlcontent += "<MODELS>\n";
					String Topicname = topicname.toUpperCase();
					Topicname = Topicname.substring(0, Topicname.length()-1);
					System.out.println(Topicname);
					int mdlnum = 1;
					for(String mfile: mfiles){
						System.out.println(mfile);
						if (mfile.startsWith(Topicname)){
							xmlcontent += "<M ID=\"M"+mdlnum+"\">" + mfile + "</M>\n";
							mdlnum++;
						}}
					xmlcontent += "</MODELS>\n";
					xmlcontent += "</EVAL>\n";

				} // files
				
				xmlcontent += "</ROUGE-EVAL>";
				
				//combStr = numeval + "-" + combStr;
				
				try {
					FileManager.saveFile(rougefolder + combStr + ".xml", xmlcontent);
				} catch (IOException e) {
					e.printStackTrace();
				}	
				
				perlscript += "$cmd=\"$ROUGE -e ../../data ";
				perlscript += " -a -x -c 95 -r 1000 -n 2 -f A -p 0.5 -t 0 -d ";
				perlscript += xmlfolder + combStr + ".xml" + " > ";
				perlscript += outfolder + combStr + ".out" + "\";\n";
				perlscript += "print $cmd,\"\\n\";\n";
				perlscript += "system($cmd);\n\n";
				
			}//different combinations for a given number
		}// number of combinations
		
		perlscript += "chdir($curdir);";
		try {
			FileManager.saveFile(rougefolder + "cmplg2014" + ".pl", perlscript);
		} catch (IOException e) {
			e.printStackTrace();
		}	

	} //main function

}
