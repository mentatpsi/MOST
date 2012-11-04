package edu.rutgers.MOST.data;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.List;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;


/* 
 * TODO Retrieval of Species and Reactions
 * TODO Main class for testing
 * TODO Reduce redundancies
 * 
 * Notes: 
 * 
 * There are several likely issues with the XML writing. Decided on first writing pseudocode
 * to get an idea of how an XML writer will work for MOST. Just need to gain a better familiarity
 * with the javax.xml library and will be able to correct the issues then.
 * 
 * Additionally, there are a lot of redundancies which can be handled by creating various 
 * super-classes. To be taken care of around testing to allow for changes to be more easily made.
 * 
 * 
 * More information about XML reading and writing can be found at:
 * http://www.vogella.com/articles/JavaXML/article.html
 * 
 * More information about attribute adding can be found at: 
 * http://www.java2s.com/Code/Java/JDK-6/UsingXMLEventFactorytocreatexmldocument.htm
 * 
 */


public class SBMLWriter {
	/* SBML Writer has two approaches, one is to create a SBML from scratch through querying, the other is to examine the current 
	 * SBML document and modify changes to flux and eventually KO status when GDBB optimize is implemented
	 * 
	 */
	public Connection dbCon;
	public Vector<ModelReaction> allReactions;
	
	
	
	public SBMLWriter(Connection con) {
		/*Writes SBML based on connection and queries
		 * will require 
		*/
		this.setConnection(con);
	}
	
	public void setConnection(Connection con) {
		this.dbCon = con;
	}
	
	public void setReactions(Vector<ModelReaction> reactions) {
		this.allReactions = reactions;
	}
	
	
	public boolean isFluxDifferent(String reactionId, String flux) {
		/*TODO*/
		return false;
	}
	
	public boolean isKODifferent(String reactionId, String KO) {
		/*TODO*/
		return false;
	}
	
	public void detectDifferences() {
		/* TODO This method will predominately be used in cases where a SBML document was loaded. 
		 * It will search for KO and Flux values which have changed as a result of optimization and simulation. 
		 * It will approach modification in several possible fashions, one of which is to create an iterable 
		 * to jump to the XML nodes and directly modify those. Writing the document upon completion. 
		 * 
		 */
	}
	
	
	public void addReaction(XMLEventWriter eventWriter, String reactionid) throws Exception{
		/* addReaction will query database or factory using reactionid to attain notes, reactants, products, 
		 * and stoichemetric properties creating nodes for each occurrence
		 */
		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		
		// Create reaction open tag
	    StartElement reactionStartElement = eventFactory.createStartElement("",
	        "", "reaction"); //TODO: Find way of adding attributes regarding id, name, and reversible
	    
	    
	    eventWriter.add(reactionStartElement);
	    eventWriter.add(end);
	    
	    // Write the different nodes
	    SpeciesRef ref = new SpeciesRef();
	    
	    // TODO Create Notes, ListofSpecies, List 
	    /*for ()
	    ref.setValues(specieId, stoic);
	    
	    createNode(eventWriter, "mode", "1");
	    createNode(eventWriter, "unit", "901");
	    createNode(eventWriter, "current", "0");
	    createNode(eventWriter, "interactive", "0");
	     */
	    
	    eventWriter.add(eventFactory.createEndElement("", "", "config"));
	}
	
	public class ListofSpecies {
		public ArrayList<Species> speciesList;
		public XMLEventWriter eventWriter;
		
		public void addSpecies(Species species1) {
			speciesList.add(species1);
		}
		
		public void addEventWriter(XMLEventWriter eventWriter) {
			this.eventWriter = eventWriter;
		}
		
		public void write() throws Exception {
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			
			for (Species spe: speciesList) {
				
				String[] keys = spe.getKeys();
				String[] values = spe.getValues();
				int len = keys.length;
				Attribute[] attributes = new Attribute[len];
				
				for (int i=0; i < len; i++) {
					attributes[i] = eventFactory.createAttribute(keys[i], values[i]);
				}
				
				List attributeList = Arrays.asList(attributes);
				List nsList = Arrays.asList();
				StartElement paramStartElement = eventFactory.createStartElement("", "", "parameter",
			            attributeList.iterator(), nsList.iterator());
				eventWriter.add(tab);
			    eventWriter.add(paramStartElement);
			    eventWriter.add(eventFactory.createEndElement("", "", "parameter"));
				
				eventWriter.add(end);
		}
		
		private void createNode(XMLEventWriter eventWriter, String name,
			      String value) throws XMLStreamException {

			    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			    XMLEvent end = eventFactory.createDTD("\n");
			    XMLEvent tab = eventFactory.createDTD("\t");
			    
			    // Create Start node
			    StartElement sElement = eventFactory.createStartElement("", "", name);
			    eventWriter.add(tab);
			    eventWriter.add(sElement);
			    
			    // Create Content
			    Characters characters = eventFactory.createCharacters(value);
			    eventWriter.add(characters);
			    
			    // Create End node
			    EndElement eElement = eventFactory.createEndElement("", "", name);
			    eventWriter.add(eElement);
			    eventWriter.add(end);
		}
	}
	
	public class SpeciesRef{
		/*class for easy implementation of inserting a species node
		 * 
		 * Example:
		 * <speciesReference species="M_ac_c" stoichiometry="1.000000"/>
		 * 
		 */
		public String species;
		public String stoic;
				
		public void setSpecies(String name) {
			this.species = name;
		}
		
		public void setStoic(String value) {
			this.stoic = value;
		}
		
		public String[] getKeys() {
			String[] keys = new String[2];
			keys[0] = "species";
			keys[1] = "stoichiometry";
			return keys;
		}
		
		public String[] getValues() {
			String[] values = new String[2];
			values[0] = species;
			values[1] = stoic;
			return values;
		}
		
	}
	
	public class Reactant extends SpeciesRef{
		
	}
	
	public class Product extends SpeciesRef{
		
	}
	
	
	public class Note {
		public ArrayList<String> notes;
		public XMLEventWriter eventWriter;
		
		public void setEventWriter(XMLEventWriter eventWriter){
			this.eventWriter = eventWriter;
		}
		public void add(String note) {
			notes.add(note);
		}
		
		public void write() throws Exception {
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			
			for (String note: notes) {
				
				
			    // Create Notes open tag
			    StartElement noteStartElement = eventFactory.createStartElement("",
			        "", "notes");
			    eventWriter.add(noteStartElement);
			    eventWriter.add(end);
			    
			    // Write the different nodes
			    createNode(eventWriter, "html:p", note);
			}
			
			    eventWriter.add(eventFactory.createEndElement("", "", "notes"));
			    eventWriter.add(end);
				
			
		}
		
		private void createNode(XMLEventWriter eventWriter, String name,
			      String value) throws XMLStreamException {

			    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			    XMLEvent end = eventFactory.createDTD("\n");
			    XMLEvent tab = eventFactory.createDTD("\t");
			    
			    // Create Start node
			    StartElement sElement = eventFactory.createStartElement("", "", name);
			    eventWriter.add(tab);
			    eventWriter.add(sElement);
			    
			    // Create Content
			    Characters characters = eventFactory.createCharacters(value);
			    eventWriter.add(characters);
			    
			    // Create End node
			    EndElement eElement = eventFactory.createEndElement("", "", name);
			    eventWriter.add(eElement);
			    eventWriter.add(end);

		}
		
	}
	public class Species{
		/* class for easy implementation of species node under listofSpecies 
		 * 
		 * Example:
		 * <species id="M_succ_b" name="M_Succinate_C4H4O4" 
		 * compartment="Extra_organism" charge="-2" boundaryCondition="true"/>
		 */
		
		public String id;
		public String name;
		public String compartment;
		public String charge;
		public String boundaryCond;
		
		public void setId(String id) {
			this.id = id;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setCompartment(String compart) {
			this.compartment = compart;
		}
		
		public void setCharge(String charge) {
			this.charge = charge;
		}
		
		public void setBoundary(String boundary) {
			this.boundaryCond = boundary;
		}
		
		public String[] getKeys() {
			String[] keys = new String[5];
			keys[0] = "id";
			keys[1] = "name";
			keys[2] = "compartment";
			keys[3] = "charge";
			keys[4] = "boundaryCondition";
			return keys;
		}
		
		public String[] getValues() {
			String[] values = new String[5];
			values[0] = this.id;
			values[1] = this.name;
			values[2] = this.compartment;
			values[3] = this.charge;
			values[4] = this.boundaryCond;
			return values;
		}
		
	}
	
	public class Parameter{
		/*Class for easy implementation of Parameter node under listofParamters
		 * 
		 * Example:
		 * <parameter id="LOWER_BOUND" value="-999999.000000" units="mmol_per_gDW_per_hr"/>
		 * */
		public String id;
		public String value;
		public String units;
		
		public void setId(String id) {
			this.id = id;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public void setUnits(String units) {
			this.units = units;
		}
		
		public String[] getKeys() {
			String keys[];
			if (this.units != null) {
				keys = new String[3];
				keys[0] = "id";
				keys[1] = this.value;
				keys[2] = this.units;
			}
			else {
				keys = new String[2];
				keys[0] = this.id;
				keys[1] = this.value;
			}
			return keys;
			
		}
		
		public String[] getValues() {
			String[] atr;
			if (this.units != null) {
				atr = new String[3];
				atr[0] = this.id;
				atr[1] = this.value;
				atr[2] = this.units;
			}
			else {
				atr = new String[2];
				atr[0] = this.id;
				atr[1] = this.value;
			}
					
			return atr;
		}
	}
	
	public class listOfParamters{
		public ArrayList<Parameter> parameters;
		public XMLEventWriter eventWriter;
		
		public void add(Parameter param) {
			this.parameters.add(param);
		}
		
		
		public void setEventWriter(XMLEventWriter eventWriter) {
			this.eventWriter = eventWriter;
		}
		
		public void write() throws Exception {
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			
			for (Parameter param : parameters) {
				
				String[] keys = param.getKeys();
				String[] values = param.getValues();
				int len = keys.length;
				Attribute[] attributes = new Attribute[len];
				
				for (int i=0; i < len; i++) {
					attributes[i] = eventFactory.createAttribute(keys[i], values[i]);
				}
				
				List attributeList = Arrays.asList(attributes);
				List nsList = Arrays.asList();
				StartElement paramStartElement = eventFactory.createStartElement("", "", "parameter",
			            attributeList.iterator(), nsList.iterator());
				eventWriter.add(tab);
			    eventWriter.add(paramStartElement);
			    eventWriter.add(eventFactory.createEndElement("", "", "parameter"));
				
				eventWriter.add(end);
				
			}
		}
	}
	
	public class ListOf {
		/*TODO Extend several classes with this superclass
		 * 
		 */
		public XMLEventWriter eventWriter;
		
		public void setEventWriter(XMLEventWriter eventWriter) {
			this.eventWriter = eventWriter;
		}
		
		private void createNode(XMLEventWriter eventWriter, String name,
			      String[] attributeKey, String[] attributeValue) throws XMLStreamException {

				assert attributeKey.length == attributeValue.length;
				
				int numAttributes = attributeKey.length;
				
			    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			    XMLEvent end = eventFactory.createDTD("\n");
			    XMLEvent tab = eventFactory.createDTD("\t");
			    
			    // Create Start node
			    StartElement sElement = eventFactory.createStartElement("", "", name);
			    eventWriter.add(tab);
			    eventWriter.add(sElement);
			    
			    // Create Content
			    for (int i=0; i < numAttributes ; i++) {
			    	Attribute atr = eventFactory.createAttribute(attributeKey[i], attributeValue[i]);
			    	
			    	eventWriter.add(atr);
			    			
			    }
			    
			    // Create End node
			    EndElement eElement = eventFactory.createEndElement("", "", name);
			    eventWriter.add(eElement);
			    eventWriter.add(end);

		}
		
	}
	public class ListOfReactants{
		public ArrayList<Reactant> reactants;
		public XMLEventWriter eventWriter;
		
		
		public void addReactant(Reactant reac) {
			reactants.add(reac);
		}
		
		public void setEventWriter(XMLEventWriter eventWriter) {
			this.eventWriter = eventWriter;
		}
		
		public void write() throws Exception{
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			
			for (Reactant reac: reactants) {
				
				String[] keys = reac.getKeys();
				String[] values = reac.getValues();
				int len = keys.length;
				Attribute[] attributes = new Attribute[len];
				
				for (int i=0; i < len; i++) {
					attributes[i] = eventFactory.createAttribute(keys[i], values[i]);
				}
				
				List attributeList = Arrays.asList(attributes);
				List nsList = Arrays.asList();
				StartElement paramStartElement = eventFactory.createStartElement("", "", "parameter",
			            attributeList.iterator(), nsList.iterator());
				eventWriter.add(tab);
			    eventWriter.add(paramStartElement);
			    eventWriter.add(eventFactory.createEndElement("", "", "parameter"));
				
				eventWriter.add(end);
			}
	}
	
	public class ListOfProducts{
		public ArrayList<Product> products;
		public XMLEventWriter eventWriter;
		
		public void addProduct(Product prod) {
			products.add(prod);
		}
		
		public void setEventWriter(XMLEventWriter eventWriter) {
			this.eventWriter = eventWriter;
		}
		
		public void write() throws Exception{
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			
			for (Product prod : products) {
				
				String[] keys = prod.getKeys();
				String[] values = prod.getValues();
				int len = keys.length;
				Attribute[] attributes = new Attribute[len];
				
				for (int i=0; i < len; i++) {
					attributes[i] = eventFactory.createAttribute(keys[i], values[i]);
				}
				
				List attributeList = Arrays.asList(attributes);
				List nsList = Arrays.asList();
				StartElement paramStartElement = eventFactory.createStartElement("", "", "parameter",
			            attributeList.iterator(), nsList.iterator());
				eventWriter.add(tab);
			    eventWriter.add(paramStartElement);
			    eventWriter.add(eventFactory.createEndElement("", "", "parameter"));
				
				eventWriter.add(end);
			}
		}
		
		private void createNode(XMLEventWriter eventWriter, String name,
			      String value) throws XMLStreamException {

			    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			    XMLEvent end = eventFactory.createDTD("\n");
			    XMLEvent tab = eventFactory.createDTD("\t");
			    
			    // Create Start node
			    StartElement sElement = eventFactory.createStartElement("", "", name);
			    eventWriter.add(tab);
			    eventWriter.add(sElement);
			    
			    // Create Content
			    Characters characters = eventFactory.createCharacters(value);
			    eventWriter.add(characters);
			    
			    // Create End node
			    EndElement eElement = eventFactory.createEndElement("", "", name);
			    eventWriter.add(eElement);
			    eventWriter.add(end);

		}
		
		
	}
	
	
	
	
	public class KineticLaw {
		public String xmlns;
		public XMLEventWriter eventWriter;
		
		public KineticLaw() {
			this.initalize();
		}
		
		public void setEventWriter(XMLEventWriter eventWriter) {
			this.eventWriter = eventWriter;
		}
		
		public void write() throws Exception {
			
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			
			// Create kineticLaw open tag
		    StartElement kineticStartElement = eventFactory.createStartElement("",
		        "", "kineticLaw");
		    
		    Attribute attribute = eventFactory.createAttribute("xmlns", this.xmlns);
		    List attributeList = Arrays.asList(attribute);
		    List nsList = Arrays.asList();
		    
		    		    
		    StartElement mathStartElement = eventFactory.createStartElement("", "", "math",
		            attributeList.iterator(), nsList.iterator());
		    
		    eventWriter.add(tab);
		    eventWriter.add(mathStartElement);
		    
		    createNode(this.eventWriter, "ci", "FLUX_VALUE");
		    
		    eventWriter.add(eventFactory.createEndElement("", "", "math"));
		    eventWriter.add(end);
		    
		    eventWriter.add(eventFactory.createEndElement("", "", "kineticLaw"));
		    eventWriter.add(end);
		}
		
		private void createNode(XMLEventWriter eventWriter, String name,
			      String value) throws XMLStreamException {

			    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			    XMLEvent end = eventFactory.createDTD("\n");
			    XMLEvent tab = eventFactory.createDTD("\t");
			    
			    // Create Start node
			    StartElement sElement = eventFactory.createStartElement("", "", name);
			    eventWriter.add(tab);
			    eventWriter.add(sElement);
			    
			    // Create Content
			    Characters characters = eventFactory.createCharacters(value);
			    eventWriter.add(characters);
			    
			    // Create End node
			    EndElement eElement = eventFactory.createEndElement("", "", name);
			    eventWriter.add(eElement);
			    eventWriter.add(end);

		}
		
		public void initalize() {
			this.xmlns = "http://www.w3.org/1998/Math/MathML";
		}
	}
	
	
			
	
	
	private void createNode(XMLEventWriter eventWriter, String name,
		      String[] attributeKey, String[] attributeValue) throws XMLStreamException {

			assert attributeKey.length == attributeValue.length;
			
			int numAttributes = attributeKey.length;
			
		    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		    XMLEvent end = eventFactory.createDTD("\n");
		    XMLEvent tab = eventFactory.createDTD("\t");
		    
		    // Create Start node
		    StartElement sElement = eventFactory.createStartElement("", "", name);
		    eventWriter.add(tab);
		    eventWriter.add(sElement);
		    
		    // Create Content
		    for (int i=0; i < numAttributes ; i++) {
		    	Attribute atr = eventFactory.createAttribute(attributeKey[i], attributeValue[i]);
		    	
		    	eventWriter.add(atr);
		    			
		    }
		    
		    // Create End node
		    EndElement eElement = eventFactory.createEndElement("", "", name);
		    eventWriter.add(eElement);
		    eventWriter.add(end);

	}
	
	public String UnitDefine() {
		/* UnitDefine should return string for the definition of mmol_per_gDW_per_hr
		 * Can eventually be designed for the adding of any additional unit definition
		 */
		return "";
				
	}
	
	public void setXMLStart() throws Exception {
		/* TODO Implement to produce beginning of XML Document
		 * <?xml version="1.0" encoding="UTF-8"?>
		 * <sbml xmlns="http://www.sbml.org/sbml/level2" level="2" version="1" 
		 * xmlns:html="http://www.w3.org/1999/xhtml">
		 * 
		 * ...
		 * 
		 * <listOfUnitDefinitions>
		 * <unitDefinition id="mmol_per_gDW_per_hr">
		 * <listOfUnits>
		 * <unit kind="mole" scale="-3"/>
		 * <unit kind="gram" exponent="-1"/>
		 * <unit kind="second" multiplier=".00027777" exponent="-1"/>
		 * </listOfUnits>
		 * </unitDefinition>
		 * </listOfUnitDefinitions>
		 * 
		 * Based off of http://www.vogella.com/articles/JavaXML/article.html */
		
		 // Create a XMLOutputFactory
	    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	    
	    String configFile = "";
		// Create XMLEventWriter
	    XMLEventWriter eventWriter = outputFactory
	        .createXMLEventWriter(new FileOutputStream(configFile));
	    
	    // Create an EventFactory
	    XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	    XMLEvent end = eventFactory.createDTD("\n");
	    
	    // Create and write Start Tag
	    StartDocument startDocument = eventFactory.createStartDocument();
	    eventWriter.add(startDocument);
	    
	    // Create config open tag
	    StartElement configStartElement = eventFactory.createStartElement("",
	        "", "config");
	    eventWriter.add(configStartElement);
	    eventWriter.add(end);
	    
	    // Write the different nodes
	    //createNode(eventWriter, "mode", "1");
	    //createNode(eventWriter, "unit", "901");
	    //createNode(eventWriter, "current", "0");
	    //createNode(eventWriter, "interactive", "0");

	    eventWriter.add(eventFactory.createEndElement("", "", "config"));
	    eventWriter.add(end);
	    eventWriter.add(eventFactory.createEndDocument());
	    eventWriter.close();
		
	}
}
}
	


	
	
	
	
	
	
	