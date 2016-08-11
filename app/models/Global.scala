package models

import play.api.mvc._
import play.api._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    RabbitMQSender.startSending
  }
}