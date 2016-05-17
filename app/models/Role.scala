package models

sealed trait Role 

object Role {
  
  case object Administrator extends Role
  case object NormalUser extends Role

  def stringToRole(value: String): Role = value match {
    case "Administrator" => Administrator
    case "NormalUser" => NormalUser
    case _ => throw new IllegalArgumentException()
  }
  
  def roleToString(value: Role): String = value match {
    case Administrator => "Administrator"
    case NormalUser => "NormalUser"
    case _ => throw new IllegalArgumentException()
  }
  
}