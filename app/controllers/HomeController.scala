package controllers

import javax.inject._
import jp.t2v.lab.play2.auth.LoginLogout
import jp.t2v.lab.play2.auth.AuthElement
import models._

import scala.collection.mutable._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api._
import play.api.mvc._
import play.api.libs.functional._
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.data._
import play.api.data.Forms._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() extends Controller with AuthConfigImpl with LoginLogout with AuthElement {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = StackAction(AuthorityKey -> Role.NormalUser) { implicit request =>
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
  
  def detail(args: String) = StackAction(AuthorityKey -> Role.NormalUser) { implicit request =>
    val CSV = models.Parser.start(args)
    Ok(views.html.details(args, CSV))
  }

  def listDetails() = StackAction(AuthorityKey -> Role.NormalUser) { implicit request => 
    Ok(views.html.listDetails())
}

  /** Alter the login page action to suit your application. */
  def login = Action { implicit request =>
    Ok(views.html.login( loginForm ))
  }

    /** Your application's login form.  Alter it to fit your application */
  val loginForm = Form {
      mapping("email" -> email, "password" -> text)(Users.authenticate)(_.map(u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }
  
    /**
   * Return the `gotoLogoutSucceeded` method's result in the logout action.
   *
   * Since the `gotoLogoutSucceeded` returns `Future[Result]`,
   * you can add a procedure like the following.
   *
   *   gotoLogoutSucceeded.map(_.flashing(
   *     "success" -> "You've been logged out"
   *   ))
   */
  def logout = Action.async { implicit request =>
    // do something...
    gotoLogoutSucceeded.map(_.flashing(
        "success" -> Utils.exitSuccesText
        ).removingFromSession("rememberme"))
  }

    /**
   * Return the `gotoLoginSucceeded` method's result in the login action.
   *
   * Since the `gotoLoginSucceeded` returns `Future[Result]`,
   * you can add a procedure like the `gotoLogoutSucceeded`.
   */
  def authenticate = Action.async { implicit request =>
    println("HomeController: authenticate called")
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        println("Authenticate: Error: " + loginForm.data.map(println(_)))
        Future.successful(BadRequest(views.html.login(formWithErrors)))
      },
      user => {
        println("Authenticate: Success: " + loginForm.data.map(println(_)))
        gotoLoginSucceeded(user.get.id)
      }
    )
  }

  // CREATE NEW USER
  
  /** Your application's login form.  Alter it to fit your application 
  *  mapping("email" -> email, "password" -> text)(Users.authenticate)(_.map(u => (u.email, "")))
  *  .verifying("Invalid email or password", result => result.isDefined)
  * 
  */
  val createUserForm = Form {
    mapping(
        "name" -> text, 
        "email" -> email, 
        "password" -> text
        )(Users.apply)(Users.unapply).verifying("Email already on use!", result => result.isDefined)
  }
  
  // TODO: no idea
  def authenticateNew = Action.async { implicit request =>
    println("HomeController: authenticateNew called")
    
    createUserForm.bindFromRequest.fold(
      formWithErrors => {
        println("HomeController: createUserForm: formWithErrors")
        println(createUserForm.bindFromRequest.errors)
        Future.successful(BadRequest(views.html.createNewUser(formWithErrors)))
      },
      user => {
          gotoLoginSucceeded(user.get.id)
          }
    )
  }
    
  def createNewUser() = Action { implicit request =>
    Ok(views.html.createNewUser(createUserForm))
  }

  
  
}
