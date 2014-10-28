package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.List;

public class Extract_Identifiers {
	List<String> classList = new ArrayList();
	List<String> attributeList = new ArrayList();
	List<String> behaviourList = new ArrayList();
	
	void extactClass(String title, String content){
		
		String[] defaultWords = {"database", "record", "system", "company", "information", "organization", "detail"};
		//case 1
		if(content.contains("such as")){
			String[] splitSentence= content.split("such as");
			//TODO : send the splitSentence[0] to get the nouns
			for(int i=0;i<defaultWords.length;i++){
				if(classList.contains(defaultWords[i])){
					classList.remove(defaultWords[i]);
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
