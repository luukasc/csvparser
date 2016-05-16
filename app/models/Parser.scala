package models

import scala.io.BufferedSource

object Parser {
  var name = ""
  
  def start(fileName: String): CSV = {
    name = fileName
    val file: BufferedSource = scala.io.Source.fromFile("/Users/luukascastren/Dropbox/Koodaus/CSVParser/" + fileName)
    val summary = toOutput(file)
    val output = printOutput(summary)
    return output
  }
  
	// Handles the CSV file
	def toOutput(bufferedSource: BufferedSource): Array[List[String]] = {
		var totalSumOfPoints: Double = 0.0
		var passedCourses: List[String] = List[String]()
		var failedCourses: List[String] = List[String]()
		
		try {
		for (line <- bufferedSource.getLines.drop(1)) {
			val cols = line.split(",").map(_.trim)
			
			// Sum of points
			if (cols.size >= 3) {	// A very simple toggle switch to prevent indexOutOfBoundsException
		   		if (!cols(2).isEmpty) {
		   			val currentPoints = makeDouble(cols(2))
		    		totalSumOfPoints += currentPoints.get
				}
			}	

			// Courses
			if (cols.size == 4) {	// A very simple toggle switch to prevent indexOutOfBoundsException
				if (!cols(1).isEmpty && !cols(3).isEmpty) {
		    		// Passed courses
		    		if (cols(3).equals("PASS")) {
		    			passedCourses = cols(1) :: passedCourses
		    		}
		    		// Failed courses
		    		else if (cols(3).equals("FAIL")) {
		    			failedCourses = cols(1) :: failedCourses
		    		}
				} 
			}
		}
		} catch {
			case e: Exception => Array(List("Error"))
		}

	  	// Remove duplicates, sort and return
	  	Array(List(totalSumOfPoints.toString), passedCourses.toSet.toList.sorted, failedCourses.toSet.toList.sorted)
	}

	// Print the result
	def printOutput(result: Array[List[String]]) = {
	  new CSV(name, result(0)(0), result(1).mkString(", "), result(2).mkString(", "))
	}

	// Checks that there is right info on column nr. 3 = string that can be changed to Double
	def makeDouble(points: String): Option[Double] = {
		try {
			Some(points.toDouble)
		} catch {
			case e: Exception => Option(0.0)
		}
	}
}