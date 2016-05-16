package models
 
import java.io.File

 
case class Result(text: String, src: String)
 
// Finds files in the current dir. matching the given search term 
object Result {
   
  // Simple list of files in the current directory
  def all = {
    new File("/Users/luukascastren/Dropbox/Koodaus/CSVParser")
                .listFiles().map(_.getName()).filter((k: String) => k.contains(".csv"))
  }
              
 
  // Simple case-sensitive filter
  def find(term: String) = Result.all.filter(_.contains(term))
}