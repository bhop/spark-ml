package com.github.bhop.sparkml.naivebayes

import com.github.bhop.sparkml.common.Resources
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.SparkSession

object SpamFilter extends App with LazyLogging {

  val spark = SparkSession.builder()
    .appName("Spam Filter (Naive Bayes)")
    .master("local[*]")
    .getOrCreate()

  import spark.implicits._

  val rawData = spark
  .read
  .textFile(Resources.resourcePath("data/sms.csv"))
  .map(toMessage)

  val Array(trainingData, testData) = rawData.randomSplit(Array(0.7, 0.3))

  val pipeline = new Pipeline()
    .setStages(Array(
      new Tokenizer().setInputCol("message").setOutputCol("words"),
      new HashingTF().setInputCol("words").setOutputCol("frequency"),
      new IDF().setInputCol("frequency").setOutputCol("features"),
      new NaiveBayes()
    ))

  val naiveBayesModel = pipeline.fit(trainingData)

  val prediction = naiveBayesModel.transform(testData)

  prediction.show()

  val evaluator = new MulticlassClassificationEvaluator()
    .setLabelCol("label")
    .setPredictionCol("prediction")
    .setMetricName("accuracy")

  val accuracy = evaluator.evaluate(prediction)
  logger.info("Test set accuracy = " + accuracy)

  spark.stop()

  private def toMessage(raw: String): Message = {
    val split = raw.split(',')
    Message(
      label = if (split.head == "spam") 1.0 else 0.0,
      message = split.tail.mkString("")
    )
  }

  case class Message(label: Double, message: String)
}

