package models

import akka.actor.{Actor, Props}
import com.rabbitmq.client.Channel
import org.joda.time.Seconds
import play.api.Logger
import play.api.libs.concurrent.Akka
import scala.concurrent.duration._
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Administrator on 09/08/2016.
  */
object RabbitMQSender {

  def startSending = {
    // create the connection
    val connection = RabbitMQConnection.getConnection()
    // create the channel we use to send
    val sendingChannel = connection.createChannel()
    // make sure the queue exists we want to send to
    sendingChannel.queueDeclare(RabbitConfig.RABBITMQ_QUEUE, false, false, false, null)

    Akka.system.scheduler.schedule(2.seconds, 1.seconds, Akka.system.actorOf(Props(
      new SendingActor(channel = sendingChannel,
        queue = RabbitConfig.RABBITMQ_QUEUE)))
      , "MSG to Queue")

    val callback1 = (x: String) => Logger.info("Recieved on queue callback 1: " + x)

    setupListener(connection.createChannel(), RabbitConfig.RABBITMQ_QUEUE, callback1)

    // create an actor that starts listening on the specified queue and passes the
    // received message to the provided callback
    val callback2 = (x: String) => Logger.info("Recieved on queue callback 2: " + x)

    // setup the listener that sends to a specific queue using the SendingActor
    setupListener(connection.createChannel(), RabbitConfig.RABBITMQ_QUEUE, callback2)
  }

  private def setupListener(receivingChannel: Channel, queue: String, f: (String) => Any) {
    Akka.system.scheduler.scheduleOnce(2 seconds,
      Akka.system.actorOf(Props(new ListeningActor(receivingChannel, queue, f))), "")
  }
}

class SendingActor(channel: Channel, queue: String) extends Actor {

  def receive = {
    case some: String => {
      val msg = some + " : " + System.currentTimeMillis
      channel.basicPublish("", queue, null, msg.getBytes())
      Logger.info(msg)
    }
    case _ =>

  }
}