package models

import com.typesafe.config.ConfigFactory

/**
  * Created by Administrator on 09/08/2016.
  */
object RabbitConfig {
  var RABBITMQ_HOST = ConfigFactory.load().getString("rabbitmq.host")
  val RABBITMQ_QUEUE = ConfigFactory.load().getString("rabbitmq.queue")
  val RABBITMQ_EXCHANGEE = ConfigFactory.load().getString("rabbitmq.exchange")
  var USER_NAME = ConfigFactory.load().getString("rabbitmq.username")
  var USER_PASSWORD = ConfigFactory.load().getString("rabbitmq.password")
}
