package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.List;

public class Extract_Identifiers {
	List<String> classList = new ArrayList<String>();
	List<String> attributeList = new ArrayList<String>();
	List<String> behaviourList = new ArrayList<String>();
	
	void extactClass(String title, String content){
		
		String[] defaultWords = {"database", "record", "system", "company", "information", "organization", "detail"};
		
		classList = ExtractInfo.getInfo(title);	//title
		//case 1
		
		if(content.contains("such as")) {
			
			String[] splitSentence= content.split("such as");
			
			
			//TODO : send the splitSentence[0] to get the nouns
			classList.addAll(ExtractInfo.getInfo(splitSentence[0]));
			
			for(int i = 0; i < defaultWords.length; i++) {
				if(classList.contains(defaultWords[i])) {
					classList.remove(defaultWords[i]);
					i--;
				}
			}
			for(int i=0;i<classList.size();i++){
				if(content.toLowerCase().contains(classList.get(i).toString().toLowerCase())){
					System.out.println("class name is:" +classList.get(i));
				}
			}
			
			//TODO : send the splitSentence[1] to get the nouns for select attribute
			
			generateBehavior(attributeList);
			
		}
	}
	
	
	void generateBehavior(List attList){
		for(int i=0;i<attList.size();i++){
			behaviourList.add("get"+attList.get(i));
			behaviourList.add("set"+attList.get(i));
		}
	}

}
