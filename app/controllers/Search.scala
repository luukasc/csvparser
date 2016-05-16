package controllers
 
import models.Result
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.data._
import play.api.data.Forms._
import scala.collection.mutable.ListBuffer
import java.io.File
 
class Search extends Controller {
 
  // Simple action - return search results as Json
  def perform(term:String) = Action {
    val m = Result.find(term)
    Ok(Json.toJson(m))
  }
}