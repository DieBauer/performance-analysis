package performanceanalysis.server.messages

import akka.actor.ActorRef
import performanceanalysis.server.Protocol.ValueType

object LogMessages {

  /**
    * Used by LogReceiverActor towards LogReceiver to request processing of single log of a component
    */
  case class SubmitLog(componentId: String, logLine: String)

  /**
    * Used by LogReceiverActor to signal log submitted
    */
  case object LogSubmitted


  /**
    * Used by AdministratorActor towards LogParserActor to request its details
    */
  case object RequestDetails

  /**
    * Used by LogParserActor towards AdministratorActor to return its details
    */
  case class Details(metrics: List[Metric])

  /**
    * Used by Administrator towards LogReceiver to notify it of a new LogReceiver actor
    */
  case class RegisterNewLogParser(componentId: String, actor: ActorRef, dateFormat: Option[String])

  /**
    * Used to register a metric in the LogParserActor
    */

  case class Metric(metricKey: String, regex: String, valueType: ValueType = ValueType(classOf[String]))

  /**
    * Used to register a metric in the AdministratorParserActor
    */
  case class RegisterMetric(componentId: String, metric: Metric)

  /**
    * Used by LogParserActor to signal a metric was registered
    */
  case class MetricRegistered(metric: Metric)

  /**
    * Used by LogParserActor to indicated that requested metrics is not registered.
    */
  case class MetricNotFound(componentId: String, metricKey: String)

  /**
    * Used by AdministratorActor towards LogParserActor to request its log lines
    */
  case object RequestComponentLogLines

  /**
    * Used by LogParserActor towards AdministratorActor to return its log lines
    */
  case class ComponentLogLines(logLines: List[String])


  /**
    * Used by AdministratorActor towards LogParserActor to request its parsed log lines
    */
  case class RequestParsedLogLines(metricKey: String)

  /**
    * Used by the Administrator to request parsed logLines for a component and metricKey
    */
  case class GetParsedLogLines(componentId: String, metricKey: String)

  /**
    * Used by the Administrator to request all posted logLines for a component
    */
  case class GetComponentLogLines(componentId: String)


}
