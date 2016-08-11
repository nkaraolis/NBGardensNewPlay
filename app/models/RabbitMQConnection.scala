package models


import com.rabbitmq.client.{Connection, ConnectionFactory}
import models.RabbitConfig
/**
  * Created by Administrator on 09/08/2016.
  */
object RabbitMQConnection {

  private val connection: Connection = null;

  /**
    * Return a connection if one doesn't exist. Else create
    * a new one
    */
  def getConnection(): Connection = {
    connection match {
      case null => {
        val factory = new ConnectionFactory()
        factory.setHost(RabbitConfig.RABBITMQ_HOST)
        factory.setUsername(RabbitConfig.USER_NAME)
        factory.setPassword(RabbitConfig.USER_PASSWORD)
        factory.newConnection()
      }
      case _ => connection
    }
  }
}