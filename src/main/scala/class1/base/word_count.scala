package main.scala.class1.base

/**
  * Created by lsc on 2017/8/1.
  */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object word_count {
  def main(args: Array[String]) {
    val inputFile =  "file:///D:/Workspaces/IdeaProjects/SCALA_PROJECTS/data/spark_test_0.1/wordcount/in/data.txt"
    val conf = new SparkConf().setAppName("WordCount").setMaster("local")
    val sc = new SparkContext(conf)
    val textFile = sc.textFile(inputFile)
    val wordCount = textFile.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey((a, b) => a + b).map(pair => (pair._2, pair._1)).sortByKey(false).map(pair => (pair._2, pair._1))
    wordCount.foreach(println)
  }
}
