package models

import java.io._
import scala.io.BufferedSource

case class Users(username: String, password: String) {
  
  
  object Users {
    
    val filePath: String = "/Users/luukascastren/Dropbox/Koodaus/CSVParser/users.txt"
   
    
    def main(user: Users): Boolean = {
      println("main")
     var result: Boolean = false
    
      if (checkForDuplicates(user)) {
        println("main: checkForDuplicates true")
        if (authenticate(user)) { 
          println("main: checkForDuplicates true: authenticate true")
          result = true 
          } else { 
            println("main: checkForDuplicates true: authenticate false")
            result = false 
            }
        } else {
          println("main: checkForDuplicates false")
          saveUser(user)
          result = true}
     result
    }
    
    
    def saveUser(user: Users) = {
      val bw = new BufferedWriter(new FileWriter(filePath, true))
      bw.write(user.username + ";" + user.password  + "\n")
      bw.close()   
    }
    
    def authenticate(user: Users): Boolean = {
      var result: Boolean = false
      if (scala.io.Source.fromFile(filePath).isEmpty) return true
      for (line <- scala.io.Source.fromFile(filePath).getLines()) {
        if (line.takeWhile(_ != ";") == (user.username + ";" + user.password)) { 
          result = true
          return result
          } else { result = false }
      }
      result
    }

    def checkForDuplicates(user: Users): Boolean = {
      var result: Boolean = false
      if (scala.io.Source.fromFile(filePath).isEmpty) return true
      for (line <- scala.io.Source.fromFile(filePath).getLines()) {
        if (line.split(";")(0) == user.username) { 
          result = true
          return result
          } 
          else { result = false}
      }
      result
    }
    
  }
}