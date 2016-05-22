package performanceanalysis.logreceiver.alert

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.testkit.TestProbe
import performanceanalysis.base.ActorSpecBase
import performanceanalysis.server.Protocol._
import performanceanalysis.server.Server

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class AlertActionActorSpec(testSystem: ActorSystem) extends ActorSpecBase(testSystem) {
  val message: String = "log message"
  def this() = this(ActorSystem("AlertActionActorSpec"))

  "AlertActionActor" must {

    val alertActionActor = system.actorOf(Props[AlertActionActor])

    "send out an alert to a given endpoint when it receives an AlertingRuleViolated message " in {
      val testProbe = TestProbe("testProbe")
      import system.dispatcher
      val mockHttp = new MockHttp
      val host = Await.result(mockHttp.getServerAddress, 1 second).getHostString

      val url = s"http://$host/valid"
      testProbe.send(alertActionActor, AlertRuleViolated(url, message))
      // Here I would like to verify that the alertActionActor sent out a request. Is there a possibility to do so?
      testProbe.expectNoMsg()

    }
  }
}

class MockHttp extends Server {
  override protected def httpInterface: String = "localhost"

  override protected def httpPort: Int = 8082

  override protected def componentsRoute: Route = pathPrefix("valid") {
    post {
      entity(as[String]) { body =>
        if (body.isEmpty) {
          failWith(new IllegalArgumentException("message is not posted"))
        } else {
          complete("good request")
        }
      }
    }
  } ~ pathPrefix("invalid") {
    put {
      failWith(new RuntimeException("why did you post"))
    }
  }
}

object TestMockHttp extends App {
  //POST(http://localhost:8082/valid)
  val mockHttp = new MockHttp
}