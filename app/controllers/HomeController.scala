package controllers

import javax.inject._
import scala.collection.mutable._
import models.Parser._
import models.Users._
import models.CSV._
import play.api._
import play.api.mvc._
import play.api.libs.functional._
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json._
import play.api.libs.json.JsPath
import play.api.libs.json.Json.toJson
import play.api.libs.json.Reads
import play.api.libs.json.Reads._
import play.api.libs.json.Reads.StringReads
import play.api.libs.json.Reads.functorReads
import play.api.mvc.Action
import play.api.mvc.Controller

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action {
    val all = models.Result.all
    var arrayCSV: Buffer[models.CSV] = Buffer()
    
    for (i <- all) {
      arrayCSV += details(i)
    }
    
    Ok(views.html.index(arrayCSV.toArray))
  }

  def details(args: String) = {
    val CSV = models.Parser.start(args)
    CSV
  }
  
  def detail(args: String) = Action {
    implicit request => println(args)
    val CSV = models.Parser.start(args)
    Ok(views.html.details(args, CSV))
  }
  
  def login() = Action {
    Ok(views.html.login()).withNewSession
  }
  
  // Handles the username-password sent as JSON:
  def postLogin = Action(parse.json) { request =>
  
    // Creates a reader for the JSON - turns it into a LoginRequest
    implicit val loginRequest: Reads[LoginRequest] = Json.reads[LoginRequest]
  
    /*
     * Call validate and if ok we return valid=true and put username in session
     */
    request.body.validate[LoginRequest] match {
      case s: JsSuccess[LoginRequest] if (s.get.authenticate) => {
        Ok(toJson(Map("valid" -> true))).withSession("user" -> s.get.username)
      }
      // Not valid
      case _ => Ok(toJson(Map("valid" -> false)))
    }
  }
  
   def welcome =  Action { implicit request =>
    request.session.get("user").map {
      user =>
        {
          Redirect(routes.HomeController.index())
        }
    }.getOrElse(Redirect(routes.HomeController.login()))
  }

  
case class LoginRequest(username: String, password: String) {
    println("LoginRequest called with: " + username + " " + password)
    val user = new models.Users(username, password)
    def authenticate = user.Users.main(user)
}

def createUser() = Action {
  Ok(views.html.createUser())
}

}
