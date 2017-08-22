/*
 * This file is part of AllSummarizer project
 * 
 * Copyright 2015 Abdelkrime Aries <kariminfo0@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kariminf.testing;

import java.util.List;

import kariminf.as.preProcess.DynamicPreProcessor;
import kariminf.as.tools.Data;


public class PreProcess {

	private static String lang = "ar";
	
	private static String getStringLang (String lang){
		String res="";
		
		switch (lang){
		case "ar":
			res += " اسمي هو كريم، وأنا أدرس المعلوماتية في المدرسة العليا للإعلام الآلي، التي تقع في الجزائر العاصمة، لأتحصل على شهادة الماجستير. ";
			res += " بحثي في ESI عن التلخيص الآلي، والذي يشكل التقاطع بين IR و NLP . ";
			res += " في هذا البحث، الفكرة الرئيسية هي إيجاد الجمل المناسبة باستعمال تقنيات IR. ";
			res += " الميزات الإحصائية تمثل قوة IR في إيجاد مدى التناسب. ";
			res += " تقنيات AI تستعمل أيضا، مثل لوغارتمات التعلم لإنشاء نماذج لكل موضوع في النص المدخل";
			return res;
			
		case "cs":
			res += "Mé jméno je Karim, a studuji informatiku v ESI, která je v Alžíru, získat magisterský stupeň. ";
			res += " Můj výzkum ve ESI je o ATS, to je křižovatka mezi IR a NLP. ";
			res += " V tomto výzkumu, hlavní myšlenkou je najít relevantní věty pomocí infračervené techniky. ";
			res += " Statistické vlastnosti jsou síla IR najít relevantní. ";
			res += " AI techniky jsou používány, jako je učení algoritmů vytvořit modely pro každé téma ve vstupním textu.";
			return res;
			
		case "el":
			res += "Αφότου οι ΗΠΑ αύξησαν την προσφερόμενη χορηγία τους στα 350 εκατομμύρια αμερικανικά δολάρια, ο Ιάπωνας πρωθυπουργός Junichiro Koizumi ανακοίνωσε το Σάββατο, 1 Ιανουαρίου, τη δωρεά μισού δισεκατομμυρίου δολαρίων. ";
			res += " Η Κίνα έχει υποσχεθεί 60.5 εκατομμύρια δολάρια ΗΠΑ, τη μεγαλύτερη δωρεά ενός μεμονωμένου κράτους μετά την Ιαπωνία και τις ΗΠΑ, το Ηνωμένο Βασίλειο και τη Σουηδία.";
			res += " Η Νορβηγία αύξησε την προσφερόμενη χορηγία της στα 180 εκατομμύρια δολάρια ΗΠΑ.";
			res += " Παρά τις ενθαρρυντικές υποσχέσεις, ο επικεφαλής του Γραφείου Συντονισμού Ανθρωπιστικών Υποθέσεων των Ηνωμένων Εθνών στην Ινδονησία, Michael Elmquist, προειδοποίησε ότι η διαχείριση της εφοδιαστικής αλυσίδας για τη διασφάλιση των κεφαλαίων, την αγορά των αγαθών και την αποστολή τους στις πληγείσες περιοχές θα πάρει χρόνο, ενδεχομένως εβδομάδες.";
		    res += " Στο μεταξύ, ο επιβεβαιωμένος αριθμός των θυμάτων θα συνεχίσει να σκαρφαλώνει στα ύψη, όπως και οι θάνατοι λόγω αφυδάτωσης, ασθενειών και του λιμού.";
		    return res;
		    
		case "en":
			res += "My name is Karim, and I study informatics at ESI, which is at Algiers, to obtain Magister degree. ";
			res += "My research in ESI is about ATS, it is the intersection between IR and NLP. ";
			res += "In this research, the main idea is to find relevant sentences using IR technics. ";
			res += "The statistical features are the power of IR to find relevancy. ";
			res += "AI technics are used, such as learning algorithms to create models for each topic in the input text. ";
			return res;
			
		case "es":
			return res;
			
		case "fr":
			res += "Mon nom est Karim, et j'étude l'informatique à l'ESI, qui se situe à Alger, pour obtenir le diplôme de Magister. ";
			res += "Mon recherche à l'ESI est sur le RAT, qui est l'intersection entre la RI et le TALN. ";
			res += "Dans ce recherche, l'idée principale est de trouver les phrses pertinentes en utilisant les techniques de RI. ";
			res += "Les caractéristiques statistiques sont le point fort de RI pour trouver la pertinence. ";
			res += "Les techniques de l'IA sont utilisées, comme les algorithmes d'apprentissage afin de créer des models pour chaque thème dans le texte d'entrée. ";
			return res;
			
		case "he":
			res += "לאחר שארה\"ב הגדילה את המימון שהיא מעניקה ל- 350 מיליון דולר, ראש הממשלה של יפן, ג'אנקירו קויזומי, הכריז ביום שבת ה-1 בינואר ,על מתן תרומה בסך חצי מיליארד דולר. סין הבטיחה 60.5 מיליון דולר ותמוקם אחרי יפן, ארה\"ב, אנגליה ושוודיה בתרומתה הגדולה כאומה יחידה. נורבגיה הגדילה את תרומות המימון ל- 180 מיליון דולר.";
			res += "האומות המאוחדות הזהירו כי יתכנו עיכובים.";
			res += "למרות ההבטחות המעודדות, ראש משרד האו\"ם הראשי לתיאום הומניטרי באינדונזיה , מיכאל אלמכויסט, הזהיר כי הטיפול הלוגיסטי באבטחת התרומות, רכישת אספקה והעברתה אל האזורים הפגועים ייקח זמן, וייתכן אף שבועות. בינתיים, שיעור התמותה ימשיך לגדול, כמו כן מוות עקב התייבשות, מחלות ורעב.";
			return res;
			
		case "hi":
			res += "अचानक से, दुनिया के धनी देशों ने भूकंप/सुनामी से क्षतिग्रस्त क्षेत्रो में धन बाटने का कार्य शुरू कर दिया है. वादा किया हुआ धन पिछले 24 घंटे में दुगनी हो गई है लगभग 2 अरब अमरीकी डॉलर (USD) तक. ";
			res += "अमेरिका ने जब अपने दान को 350000000 अमेरिकेन  डालर किया तब जापानी प्रधानमंत्री कोइज़ुमी जुनिचिरो ने  शनिवार 1 जनवरी पर  डेढ़ अरब डॉलर दान की घोषणा की. चीन ने 60500000 अमरीकी डालर का वादा किया, जापान, अमेरिका और यूनाइटेड किंगडम के बाद, सबसे बड़ा दान के लिए. नॉर्वे ने अपना दान को 180000000 अमरीकी डालर किया.";
			res += "उत्साहजनक वादे के बावजूद, संयुक्त राष्ट्र कार्यालय इंडोनेशिया प्रमुख माइकल एल्म्क़ुइस्त में मानवीय मामलों के समन्वय के लिए चेतावनी दी है कि धन को सुरक्षित, आपूर्ति क्रय और उन्हें ज़खमी क्षेत्रों के लिए शिपिंग समय लगेगा, संभवतः सप्ताह की रसद. इस बीच में, मरने वालों की संख्या की पुष्टि की चढ़ाई करने के लिए जारी रहेगा, जैसा कि निर्जलीकरण, बीमारी और भुखमरी के कारण लोगों की मृत्यु हो सकती है.";
			return res;
			
		case "ro":
			return res;
			
		case "zh":
			res += "给当前幻灯片中所选对象设置一个操作，当单击此对象或鼠标移动到此对象的上方时执行该操作。";
			res += "弹出“幻灯片设计-动画方案”任务窗格，对当前幻灯片或整个演示文稿设置切换和动画效果。";
			res += "插入本地文件夹中的音频文件，在放映幻灯片时自动播放，当切换到下一张幻灯片时不会中断播放，一直循环播放到幻灯片结束放映。";
			return res;
		}
		
		return res;
		
	}
	
	public static void main(String[] args) {
		Data data = new Data();
		String input = getStringLang(lang);
		DynamicPreProcessor pp = new DynamicPreProcessor(lang);
		pp.setData(data);
		pp.addText(input);
		pp.preProcess();
		List<List<String>> l = data.getSentWords();
		
		int i=0;
		for (List<String> s: l){
			System.out.println(data.getNbrWords(i) + "=" + s.toString());
			i++;
		}

	}

}
