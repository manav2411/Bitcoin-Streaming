package coinyser
import org.apache.spark.sql.{Dataset, SparkSession}

import org.apache.spark.sql.functions._


object SparkStreaming extends App {

  implicit val spark: SparkSession = SparkSession.builder.master("local[*]").appName("coinyser").getOrCreate()
  import spark.implicits._
  val z = new {
    def show[A](ds: Dataset[A]): Unit = ds.show(false)
  }

  case class Transaction(timestamp: java.sql.Timestamp,
                         date: String,
                         tid: Int,
                         price: Double,
                         sell: Boolean,
                         amount: Double)
  val schema = Seq.empty[Transaction].toDS().schema

  val dfStream = {
    spark.readStream.format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("startingoffsets", "latest")
      .option("subscribe", "transactions")
      .load()
      .select(
        from_json(col("value").cast("string"), schema)
          .alias("v")).select("v.*").as[Transaction]

  }
  val query = {
    dfStream
      .writeStream
      .format("memory")
      .queryName("transactionsStream")
      .outputMode("append")
      .start()
  }
  z.show(spark.table("transactionsStream"))
}
