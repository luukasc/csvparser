package controllers


import jp.t2v.lab.play2.auth.AuthElement

import play.api._
import play.api.mvc._
import play.twirl.api.Html
import jp.t2v.lab.play2.stackc.{RequestAttributeKey, RequestWithAttributes, StackableController}

import models._

class Messages extends Controller with AuthElement with AuthConfigImpl {
   // The `StackAction` method
  //    takes `(AuthorityKey, Authority)` as the first argument and
  //    a function signature `RequestWithAttributes[AnyContent] => Result` as the second argument and
  //    returns an `Action`

  // The `loggedIn` method
  //     returns current logged in user

  def main = StackAction(AuthorityKey -> Role.NormalUser) { implicit request =>
    val user = loggedIn
    val title = "message main"
    Ok(views.html.message_main(title))
  }

  def list = StackAction(AuthorityKey -> Role.NormalUser) { implicit request =>
    val user = loggedIn
    val title = "all messages"
    Ok(views.html.message_list(title))
  }

  def detail(id: Int) = StackAction(AuthorityKey -> Role.NormalUser) { implicit request =>
    val user = loggedIn
    val title = "messages detail "
    Ok(views.html.message_detail(title + id))
  }

  // Only Administrator can execute this action.
  def write = StackAction(AuthorityKey -> Role.Administrator) { implicit request =>
    val user = loggedIn
    val title = "write message"
    Ok(views.html.message_write(title))
  }

  object Messages extends Messages
}