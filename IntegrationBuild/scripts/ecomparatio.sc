import java.io._
import scala.io.Source
import edu.holycross.shot.cite._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.scm._
import edu.holycross.shot.ohco2._
import cats.syntax.either._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.parser.decode


/*
==============================
Set up for parsing JSON
==============================

Eventually Parameterize These Values!
(Data that is not in the JSON, but probably should be.)

*/

val jsonFilePath = "resources/PeriklesFinal.json"

// Documentation URL (for CEX file)
val documentationURL:String = "https://github.com/Eumaeus/ecomparatio-cite"

// Stuff for CTS Data
val textUrnBase:String = "urn:cts:greekLit:tlg0007.tlg012"
val citationScheme:String = "section/sentence"

val groupName:String = "Plutarch"
val workTitle:String = "Pericles"
val textLang:String = "grc"
val diffExemplarUrnComponent:String = "diffTokens"
val primarySeparator:String = "#"

// Stuff for CITE Collection
val collectionNS:String = "ecomp"
val collectionName:String = "ecompDiffs"
val collectionVersion:String = "demo"
val objectSelector:String = "diff" // will be part of the objectSelector
var objectSelectorIndex:Int = 0 // as String, will be appended to objectSelector

// URNs: Collection-urn, and property-urns
val collectionUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}:")
val collectionUrnUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.urn:")
val collectionLabelUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.label:")
val collectionSeqUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.seq:")
val collectionBaseTextUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.base:")
val collectionOtherTextUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.other:")
val collectionLabelUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.label:")
val collectionDiffTypeUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.difftype:")
val collectionDistanceUrn:Cite2Urn = Cite2Urn(s"urn:cite2:${collectionNS}:${collectionName}.${collectionVersion}.distance:")



val levelOneCitationRegex:String = """[0-9]+\."""
val levelTwoCitationRegex:String = """\[[0-9]+\]"""

val cexFilePath:String = "resources/texts-cex.cex"
val fullCexFilePath:String = "resources/plutarchComparatio.cex"

val cexHeader:String = """
// A library demonstrating eComparatio data in CITE/CEX format
// Plutarch, Life of Perices, Greek.

#!cexversion
3.0

#!citelibrary
name#demo
urn#urn:cite2:cex:fufolio.2018_1:ecomparatioDemo
license#CC Share Alike.  For details, see <https://github.com/Eumaeus/ecomparatio-cite>.
"""

val cexCatalogHeader:String = """
#!ctscatalog
urn#citationScheme#groupName#workTitle#versionLabel#exemplarLabel#online#lang
"""


/*

==============================
Utility Methods for dealing with JSON
==============================

*/

def eitherListToListJson(le:Either[io.circe.Error,List[Json]]):List[Json] = {
	val lj:List[Json] = {
		le match {
			case Right(l) => l
			case _ => {
				println(s"-----\n${le}\n-----")
				throw new Exception("bad jason list")
			}
		}
	}
	lj
}
def eitherListToListString(le:Either[io.circe.Error,List[String]]):List[String] = {
	val ls:List[String] = {
		le match {
			case Right(l) => l
			case _ => {
				println(s"-----\n${le}\n-----")
				throw new Exception("bad jason list")
			}
		}
	}
	ls
}

/*
==============================
Do some URN construction from JSON data and parameterized values
==============================
*/

def textNameStringToVersionUrn(tn:List[String]):CtsUrn = {
	 val versionComponentString:String = tn(1).replace(" ","")
	 val versionUrn:CtsUrn = CtsUrn(s"${textUrnBase}.${versionComponentString}:")
	 versionUrn
}

def textNameStringToExemplarUrn(tn:List[String]):CtsUrn = {
	 val versionComponentString:String = tn(1).replace(" ","")
	 val exemplarUrn:CtsUrn = CtsUrn(s"${textUrnBase}.${versionComponentString}.${diffExemplarUrnComponent}:")
	 exemplarUrn
}


/*
==============================
Process JSON file into CEX
==============================
*/

val lines = Source.fromFile(jsonFilePath).getLines.toList
val tempString:String = lines(0)
val jsString:String = tempString.replace("""'""",""""""")
val doc: Json = parse(jsString).getOrElse(Json.Null)

// We need a cursor to get stuff
val cursor: HCursor = doc.hcursor

// Get text catalog
val textNamesEither = cursor.downField("textnames").as[List[Json]]
val textNamesListJson:List[Json] = eitherListToListJson(textNamesEither)
val textNamesList:List[List[String]] = textNamesListJson.map(tn => {
	val textNameEither = tn.as[List[String]]
	eitherListToListString(textNameEither)
})
var tempCexCatalog:String = s"${cexHeader}\n\n${cexCatalogHeader}"

// Get Vector of Map["version"->CtsUrn,"exemplar"->CtsUrn]
val corpusUrns:Vector[Map[String,CtsUrn]] = {
	textNamesList.map( tn =>{
	  	val vUrn = textNameStringToVersionUrn(tn)
	  	val eUrn = textNameStringToExemplarUrn(tn)
	  	val urnMap:Map[String,CtsUrn] = Map("version" -> vUrn, "exemplar" -> eUrn)
	  	urnMap
	} ).toVector
}

val tempCexCatalogEntries:String = textNamesList.map( tn => {
	 val versionComponentString:String = tn(1).replace(" ","")
	 val versionUrn:CtsUrn = CtsUrn(s"${textUrnBase}.${versionComponentString}:")
	 val versionCitationScheme:String = citationScheme
	 val exemplarUrn:CtsUrn = CtsUrn(s"${textUrnBase}.${versionComponentString}.${diffExemplarUrnComponent}:")
	 val exemplarCitationScheme:String = s"${versionCitationScheme}/token"
	 val versionLabel:String = s"${tn(1)} ${tn(2)} ${tn(3)}"
	 val exemplarLabel:String = s"tokenized for comparison"
	 var tempString:String = versionUrn.toString + '#'
	 tempString = tempString + versionCitationScheme + '#'
	 tempString = tempString +  groupName + '#' + workTitle + '#' + versionLabel + "##true#" + textLang + "\n"
	 tempString = tempString +  exemplarUrn.toString + '#'
	 tempString = tempString +  exemplarCitationScheme + '#'
	 tempString = tempString +  groupName + '#' + workTitle + '#' + versionLabel + '#'
	 tempString = tempString +  exemplarLabel + "#true#" + textLang
	 tempString
}).mkString("\n")
tempCexCatalog = tempCexCatalog + tempCexCatalogEntries
val cexCatalog:String = tempCexCatalog

// Get text tokens
val allTextListEither = cursor.downField("alltexts").as[List[Json]]
val allTextListJson:List[Json] = eitherListToListJson(allTextListEither)
val allTextList:List[(List[(String)],Int)] = allTextListJson.map(atl => {
	val textJsonEither = atl.as[List[String]]
	eitherListToListString(textJsonEither)
}).zipWithIndex

// We want, for each token, the string, a pseudo-citation, and the index
val pseudoCts:List[(List[(String,String,Int)],Int)] = {
	allTextList.map( tt => {
		 val workNum:Int = tt._2
		 val thisTokenList:List[String] = tt._1
		 val thisTripleList:List[(String,String,Int)] = {
		 	thisTokenList.zipWithIndex.map( t => {
		 	 	val newEntry = {
		 	 		(t._1,s"${workNum}:${t._2}",t._2)
		 	 	}
		 	 	newEntry
		 	})
		 }
		 (thisTripleList,workNum)
	})
}

/*

Notes for the Future:

	- This blindly captures a citation scheme specific to this JSON
	- Sections look like "1.", subsections look like "[1]"

*/
def ctsTextFromJson(pcts:List[(List[(String,String,Int)],Int)]):String = {
	val sb = new StringBuilder()
	sb.append(cexCatalog)
	sb.append("\n\n")
	sb.append("#!ctsdata\n")
	var levelOne:Int = 0
	var levelTwo:Int = 0
	var textContent:String = ""
	// Editions
	for (t <- pcts){
		levelOne = 0
		levelTwo = 0
		textContent = ""
		for (v <- t._1){
			val txt:String = v._1
			//val wrk:String = v._2.split(":")(0)
			val wrk:String = corpusUrns(v._2.split(":")(0).toInt)("version").toString
			val tok:String = v._2.split(":")(1)
		   val enum:Int = v._3
		   txt match {
		   	case t if t.matches(levelOneCitationRegex) => {
		   		if ( !(textContent.matches("""\s*"""))){
				   	sb.append(s"${wrk}${levelOne}.${levelTwo}${primarySeparator}${textContent}\n")
		   		}
			   	textContent = ""
			   	levelOne = levelOne + 1
			   	levelTwo = 0
		   	}
			   case t if t.matches(levelTwoCitationRegex) => {
		   		if ( !(textContent.matches("""\s*"""))){
				   	sb.append(s"${wrk}${levelOne}.${levelTwo}${primarySeparator}${textContent}\n")
				   }
			   	textContent = ""
		   		levelTwo = levelTwo + 1
			   }
				case t if t.matches("""\s*""") =>
				case _ => {
					textContent = textContent + " " + txt
				}
		   }
		}
	}
	// Analytical exemplars
	for (t <- pcts){
		levelOne = 0
		levelTwo = 0
		for (v <- t._1){
			val txt:String = v._1
			val wrk:String = corpusUrns(v._2.split(":")(0).toInt)("exemplar").toString
			val tok:String = v._2.split(":")(1)
		   val enum:Int = v._3
		   txt match {
		   	case t if t.matches("""[0-9]+\.""") => {
			   	levelOne = levelOne + 1
			   	levelTwo = 0
		   	}
			   case t if t.matches("""\[[0-9]+\]""") => {
		   		levelTwo = levelTwo + 1
			   }
				case t if t.matches(""" *""") =>
				case _ => {
					sb.append(s"${wrk}${levelOne}.${levelTwo}.${tok}${primarySeparator}${txt}\n")
				}
		   }
		}
	}
	val cexString:String = sb.toString
	cexString
}

// Here we actually make the CEX file
val ctsOnlyCEX = ctsTextFromJson(pseudoCts)

// Write it to disk
def writeCEX(cex:String):Unit = {
	val pw = new PrintWriter(new File(cexFilePath))
	pw.write(cex)
	pw.close
}

// 	THIS WILL BE OVERWRITTEN AFTER WE'VE ADDED COLLECTION DATA TO THE CEX, BELOW
writeCEX(ctsOnlyCEX)

// Test it by loading cexFilePath into a Cite Library
val cexData = Source.fromFile(cexFilePath).getLines.mkString("\n")
val library = CiteLibrary(cexData,"#",",")
val textRepo = library.textRepository.get

/*
==============================
Set up Collections
==============================
*/

val dataModelCEX:String = s"""
#!citeproperties
Property#Label#Type#Authority list
urn:cite2:cite:datamodels.v1.urn:#Data model#Cite2Urn#
urn:cite2:cite:datamodels.v1.label:#Label#String#
urn:cite2:cite:datamodels.v1.description:#Description#String#

#!citedata
urn#label#description
urn:cite2:cite:datamodels.v1:eComperatioDiff#eComparatioDiffference#Encoding of a difference operation based on eComparatio. See <${documentationURL}>.
urn:cite2:cite:datamodels.v1:commentarymodel#Commentary#Model text passages commenting on text passages


#!datamodels
Collection#Model#Label#Description
${collectionUrn}#urn:cite2:cite:datamodels.v1:eComperatioDiff#eComparatioDiffference#Encoding of a difference operation based on eComparatio. See <${documentationURL}>.
urn:cite2:cite:verbs.v1:#urn:cite2:cite:datamodels.v1:commentarymodel#Commentary Model#URN comments on URN. See documentation at <https://github.com/cite-architecture/commentary>.
"""

val collectionInventoryCEX:String = s"""
#!citecollections
URN#Description#Labelling property#Ordering property#License
${collectionUrn}#eComparatio Diffs#${collectionLabelUrn}#${collectionSeqUrn}#CC-attribution-share-alike

"""

val citePropertiesCEX:String = s"""
#!citeproperties
Property#Label#Type#Authority list
${collectionUrnUrn}#URN#Cite2Urn#
${collectionSeqUrn}#Sequence#Number#
${collectionBaseTextUrn}#Base Text#CtsUrn#
${collectionOtherTextUrn}#Other Text#CtsUrn#
${collectionLabelUrn}#Label#String#
${collectionDiffTypeUrn}#Different Type#String#
${collectionDistanceUrn}#Edit Distance#Number#

"""

val citeDataHeaderCEX:String =s"""#!citedata\nurn#seq#base#other#label#difftype#distance\n"""

// Get ready to write big Collection to disk… we'll stream this
val pw = new PrintWriter(new File(fullCexFilePath))

// write existing text cexString
pw.write(ctsOnlyCEX)

pw.append("\n")
pw.append(dataModelCEX)
pw.append(collectionInventoryCEX)
pw.append(citePropertiesCEX)
pw.append(citeDataHeaderCEX)
/* Make Collection Data */

// Get Comparatio Data
//   This is a nasty-complex little loop.

val relationsBuf:StringBuilder = new StringBuilder()
relationsBuf ++= "#!relations\n"

var citeData:String = "\n#!citedata\n"

val compListEither = cursor.downField("comparatio").as[List[Json]]
val compListJson:List[Json] = eitherListToListJson(compListEither)
for ((cj, i) <- compListJson.zipWithIndex){

	val otherTextUrn:CtsUrn = textNameStringToExemplarUrn(textNamesList(i))
	val otherIndex:Int = if (i == 0) 1 else 0
	val currentTextUrn:CtsUrn = textNameStringToExemplarUrn(textNamesList(otherIndex))

	//println(s"currentTextUrn: ${currentTextUrn}")
	//println(s"otherTextUrn: ${otherTextUrn}")

	val currentTextNodes:Vector[CitableNode] = textRepo.corpus.nodes.filter(_.urn.dropPassage == currentTextUrn)
	println(s"Got vector for current text: ${currentTextNodes.size}")
	val otherTextNodes:Vector[CitableNode] = textRepo.corpus.nodes.filter(_.urn.dropPassage == otherTextUrn)
	println(s"Got vector for other text: ${otherTextNodes.size}")

	val actualJson = cj.as[List[Json]].right.get(1).as[List[Json]].right.get(0).as[List[Json]]
	for (aj <- actualJson){
		for ((jo,count) <- aj.zipWithIndex){
			val field1:String = jo.toString.tail.dropRight(1).split(',')(0).trim
			val field2:String = jo.toString.tail.dropRight(1).split(',')(1).trim
			val field3:String = jo.toString.tail.dropRight(1).split(',')(2).trim.replaceAll(""""""","").trim
			val field4:Float = jo.toString.tail.dropRight(1).split(',')(3).trim.toFloat

			// get URNs for field1 and field2
			val field1Node:Option[CitableNode] = {
				val filteredNodes:Vector[CitableNode] = currentTextNodes.filter(n => {
					val passageComp:String = n.urn.passageComponent
					val tokenId:String = passageComp.split('.')(2)
					tokenId == field1
				})
				filteredNodes.size match {
					case n if (n == 0) => None
					case _ => Some(filteredNodes(0))
				}	
			}
			val field2Node:Option[CitableNode] = {
				val filteredNodes:Vector[CitableNode] = otherTextNodes.filter(n => {
					val passageComp:String = n.urn.passageComponent
					val tokenId:String = passageComp.split('.')(2)
					tokenId == field2
				})
				filteredNodes.size match {
					case n if (n == 0) => None
					case _ => Some(filteredNodes(0))
				}	
			}
			if ((field1Node != None) && (field2Node != None)){
				try {	
					val levDist:Float = field4.toFloat
					if (levDist < 1){
						relationsBuf ++= s"${field1Node.get.urn}#urn:cite2:cite:verbs.v1:commentsOn#${field2Node.get.urn}\n"
						print("+")
					} else {
						print(".")
					}
				} catch {
					case e:Exception => // do nothing
				}

				val indexCounter:Int = (i * compListJson.size) + count
				val thisLabelString:String = s"(${field1Node.get.urn.version}, ${field1Node.get.urn.passageComponent}) ${field1Node.get.text} :: ${field2Node.get.text} (${field2Node.get.urn.version}, ${field2Node.get.urn.passageComponent})"
				val collCexObjectString:String = s"""${collectionUrn}${indexCounter}#${indexCounter}#${field1Node.get.urn}#${field2Node.get.urn}#${thisLabelString}#${field3}#${field4}\n"""
				pw.append(collCexObjectString)
			}
			/*
			println("")
			println(s"${field1Node}")
			println(s"${field2Node}")
			*/

			/*
			println("")
			println(s"${i}: ${field1}")
			println(s"${i}: ${field2}")
			println(s"${i}: ${field3}")
			println(s"${i}: ${field4}")
			*/
		}
	}

}

pw.append(s"\n\n")
pw.append(relationsBuf)

pw.close
