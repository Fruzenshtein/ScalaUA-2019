package ua.sample.classic.sync

import scala.collection.mutable

object SampleA extends App {

  val userDB = mutable.Map.empty[UserId, User]
  val userDao = new UserDaoImpl(userDB)

  /**
    * Error: value flatMap is not a member of Unit
    * _ <- userDao.addUser(User("uid01", "Alex"))

  for {
    _ <- userDao.addUser(User("uid01", "Alex"))
    user <- userDao.getUser("uid01")
  } yield {
    println(user)
  }
  */

  userDao.addUser(User("uid01", "Alex"))
  val user = userDao.getUser("uid01")
  println(user)

}
