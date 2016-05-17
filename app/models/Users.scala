package models

import java.io._
import scala.io.BufferedSource
import scala.util.Random

case class Users(id: String, email: String, password: String, name: String, role: Role)

  object Users {
    
    val filePath = models.Utils.filePath
      
    def apply(name: String, email: String, password: String) = {
      println("Users: apply called")
      createUser(name, email, password)
    }
    
    def unapply(user: Option[Users]): Option[(String,String,String)]  = {
      println("Users: unapply called")
     try {
       Option(user.get.name, user.get.email, user.get.password)
     } catch {
       case e: Exception => Option(null)
     }
    }
  
    def createUser(name: String, email: String, password: String): Option[Users] = {
      println("Users: createUser called")
      if (checkForDuplicates(email)) Option(null) 
      else { 
        val result = new Users(createId, email, password, name, Role.stringToRole("NormalUser"))
        writeUserToFile(result)
        Option(result)
      }
    }
    
    // TODO: Make better
    def createId: String = {
      Utils.idGenerator += 1
      (Utils.idGenerator + 1).toString
    }
    
    def authenticate(email: String, password: String): Option[Users] = {
      findByEmail(email).filter( user => password == user.password)
    }
  
    def findByEmail(email: String): Option[Users] = {
      try {
        Option(findAll().toArray.filter(user => user.email  == email)(0))
      } catch {
        case e: Exception => Option(null)
      }
    }
  
    def findById(id: String): Option[Users] = {
      Option(findAll().toArray.filter(user => user.id  == id)(0))
    }
  
    def findAll(): Seq[Users] = {
      var listOfAll: List[Users] = List()
      
      for (line <- scala.io.Source.fromFile(filePath).getLines.drop(1)) {
        val column = line.split(",").map(_.trim)
          listOfAll = new Users(column(0), column(1), column(2), column(3), models.Role.stringToRole(column(4))) :: listOfAll
      }
      listOfAll.toSeq
    }

    def writeUserToFile(users: Users) {
        val bw = new BufferedWriter(new FileWriter(filePath, true))
        bw.write(users.id.toString() + "," + users.email.toString() + ","+ users.password.toString + "," + users.name.toString() + "," + models.Role.roleToString(users.role) + "\n")
        bw.close()  
    }
    
    def checkForDuplicates(email: String): Boolean = {
      println("Users: checkForDuplicates called")
      if (findByEmail(email).getOrElse(false) == false) {
        println("Users: checkForDuplicates called: 1: There were no duplicates!")
        false
      }
      else {
        println("Users: checkForDuplicates called: 2: There was a duplicate!")
        true
      }
    }
    
  }


