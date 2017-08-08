package main.scala.class1.base

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
/**
  * Created by lsc on 2017/7/21.
  */
object base_transformation {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("base_make").setMaster("local")
    val sc = new SparkContext(conf)

    val rddInt:RDD[Int] = sc.makeRDD(List(1,2,3,4,5,6,2,5,1))
    val rddStr:RDD[String] = sc.parallelize(Array("a","b","c","d","b","a"), 1)
    val rddFile:RDD[String] = sc.textFile("file:///D:/Workspaces/IdeaProjects/SCALA_PROJECTS/data/spark_test_0.1/in/data.txt", 1)

    val rdd01:RDD[Int] = sc.makeRDD(List(1,3,5,3))
    val rdd02:RDD[Int] = sc.makeRDD(List(2,4,5,1))

    /* map操作 */
    println("======map操作======")
    println(rddInt.map(x => x + 1).collect().mkString(","))
    println("======map操作======")
    /* filter操作 */
    println("======filter操作======")
    println(rddInt.filter(x => x > 4).collect().mkString(","))
    println("======filter操作======")
    /* flatMap操作 */
    println("======flatMap操作======")
    println(rddFile.flatMap { x => x.split(",") }.first())
    println("======flatMap操作======")
    /* distinct去重操作 */
    println("======distinct去重======")
    println(rddInt.distinct().collect().mkString(","))
    println(rddStr.distinct().collect().mkString(","))
    println("======distinct去重======")
    /* union操作 */
    println("======union操作======")
    println(rdd01.union(rdd02).collect().mkString(","))
    println("======union操作======")
    /* intersection操作 */
    println("======intersection操作======")
    println(rdd01.intersection(rdd02).collect().mkString(","))
    println("======intersection操作======")
    /* subtract操作 */
    println("======subtract操作======")
    println(rdd01.subtract(rdd02).collect().mkString(","))
    println("======subtract操作======")
    /* cartesian操作 */
    println("======cartesian操作======")
    println(rdd01.cartesian(rdd02).collect().mkString(","))
    println("======cartesian操作======")

    sc.stop()
  }
}
