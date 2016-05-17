package models

import java.io._
import scala.io.BufferedSource

case class Users(id: String, email: String, password: String, name: String, role: Role) 
  

  
  object Users {
  
    val filePath = models.Utils.filePath
  
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

    def create(users: Users) {
        val bw = new BufferedWriter(new FileWriter(filePath, true))
        bw.write(users.id + "," + users.email + "," + users.name + "," + users.role.toString)
        bw.close()  
    }
    
    
    
    
  }
