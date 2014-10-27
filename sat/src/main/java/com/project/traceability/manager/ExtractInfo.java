package com.project.traceability.manager;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.traceability.model.RequirementModel;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

public class ExtractInfo {
	private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";

	private final TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer
			.factory(new CoreLabelTokenFactory(), "invertible=true");

	private final static LexicalizedParser parser = LexicalizedParser
			.loadModel(PCG_MODEL);
	private static List<String> classNames = new ArrayList<String>();
	private static List<String> functionNames = new ArrayList<String>();

	
	private static List<String> attributeNames;
	private static List<String> behaviourList = new ArrayList<String>();

	public static void run(List<RequirementModel> requirementAretefactElements) {

		/*
		 * String a =
		 * "The system shall allow customers to withdraw money from their account if the new balance is greater than or equal to the overdraft balance"
		 * ; MaxentTagger tagge = new MaxentTagger(
		 * "./src/english-left3words-distsim.tagger"); String tagged =
		 * tagge.tagString(a); System.out.println(tagged);
		 */
		String str = null;
		for (int i = 0; i < 5/*requirementAretefactElements.size()*/; i++) {
			extactClass(requirementAretefactElements.get(i).getTitle(), requirementAretefactElements.get(i).getContent());
		
			Set classSet = new HashSet<String>(classNames);
			System.out.println(classSet);
			Set attributeSet = new HashSet<String>(attributeNames);
			System.out.println(attributeSet);
			Set functionSet = new HashSet<String>(functionNames);
			System.out.println(functionSet);
		}
		
		//Set classSet = new HashSet<String>(classNames);
		//System.out.println(classSet);
		
	}

	public static void extactClass(String title, String content) {
		
		attributeNames = new ArrayList<String>();

		String[] defaultWords = { "database", "record", "system", "company",
				"information", "organization", "detail" };

		classNames = ExtractInfo.getInfo(title); // title
		// case 1

		if (content.contains("such as")) {

			String[] splitSentence = content.split("such as");

			// send the splitSentence[0] to get the nouns
			classNames.addAll(ExtractInfo.getInfo(splitSentence[0]));

			for (int i = 0; i < defaultWords.length; i++) {
				if (classNames.contains(defaultWords[i])) {
					classNames.remove(defaultWords[i]);
					i--;
				}
			}
			
			// TODO : send the splitSentence[1] to get the nouns for select
			// attribute
			String[] attributeString = splitSentence[1].split(",");
			for(int i = 0; i < attributeString.length; i++){
				if(attributeString[i].contains("and")){
					String[] attr = attributeString[i].split("and");
					for(int j = 0; j < attr.length; j++)
						attributeNames.add(attr[j].toLowerCase());
				}
				else 
					attributeNames.add(attributeString[i].toLowerCase());
			}
			generateBehavior(attributeNames);

		}
	}

	public static void generateBehavior(List attList) {
		for (int i = 0; i < attList.size(); i++) {
			behaviourList.add("get" + attList.get(i));
			behaviourList.add("set" + attList.get(i));
		}
	}

	public static ArrayList<String> getInfo(String str) {

		// Parser parser = new Parser();
		Tree tree = parser.parse(str);

		List<Tree> leaves = tree.getLeaves();
		// Print words and Pos Tags
		for (int i = 0; i < leaves.size(); i++) {
			Tree leaf = leaves.get(i);
			Tree preLeaf = null;
			Tree preParent = null;
			if (i != 0)
				preLeaf = leaves.get(i - 1);
			// for (Tree leaf : leaves) {
			Tree parent = leaf.parent(tree);
			if (i != 0)
				preParent = preLeaf.parent(tree);
			/*System.out.print(leaf.label().value() + "-"
					+ parent.label().value() + " ");*/
			if (parent.label().value().equals("IN")) {
				leaves.remove(leaf);
				i--;
			}
			if (i != 0 && preParent.label().value().equals("JJ")
					&& parent.label().value().equals("NN")) {
				classNames.add(preLeaf.label().value().toLowerCase() + " "
						+ leaf.label().value().toLowerCase());
				// System.out.println("JN*****" + preLeaf.label().value() + " "
				// + leaf.label().value());
			} else if (i != 0 && preParent.label().value().equals("NN")
					&& parent.label().value().equals("NN")) {
				classNames.add(preLeaf.label().value().toLowerCase() + " "
						+ leaf.label().value().toLowerCase());
				// System.out.println("NN*****" + preLeaf.label().value() + " "
				// + leaf.label().value());
			} else if (parent.label().value().equals("NN")
					|| parent.label().value().equals("NNP")) {
				classNames.add(leaf.label().value().toLowerCase());
				// System.out.println("N*****" + leaf.label().value());
			} /*else if (parent.label().value().equals("VB")) {
				functionNames.add(leaf.label().value().toLowerCase());
				// System.out.println("N*****" + leaf.label().value());
			}*/
		}

		return (ArrayList<String>) classNames;
	}

	public Tree parse(String str) {
		List<CoreLabel> tokens = tokenize(str);
		Tree tree = parser.apply(tokens);
		return tree;
	}

	private List<CoreLabel> tokenize(String str) {
		Tokenizer<CoreLabel> tokenizer = tokenizerFactory
				.getTokenizer(new StringReader(str));
		return tokenizer.tokenize();
	}

}
