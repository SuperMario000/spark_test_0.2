package main.scala.class1.base

/**
  * Created by lsc on 2017/7/7.
  */

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object LogResult{
  def main(args: Array[String]) {
    if (args.length == 0) {
      System.err.println("Usage: SogouResult <file1> <file2>")
      System.exit(1)
    }

    val conf = new SparkConf().setAppName("logResult").setMaster("local")
    val sc = new SparkContext(conf)

    //session查询次数排行榜
    //val rdd1 = sc.textFile(args(0)).map(_.split("\t")).filter(_.length==6)
    //val rdd2=rdd1.map(x=>(x(1),1)).reduceByKey(_+_).map(x=>(x._2,x._1)).sortByKey(false).map(x=>(x._2,x._1))
    var rdd = sc.textFile(args(0));
    val rdd1 = rdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _) // 文件按空格拆分，统计单词次数
    println("====1===="+rdd.flatMap(_.split(" ")).collect().mkString(","))
    println("====2===="+rdd.flatMap(_.split(" ")).map((_, 1)).collect().mkString(","))
    println("====3===="+rdd1.collect().mkString(","))
    val rdd2 = rdd1.map(x => (x._2, x._1)).sortByKey(ascending = false).map(x => (x._2, x._1)) // 按出现次数由高到低排序
    println("====4===="+rdd1.map(x => (x._2, x._1)).collect().mkString(","))
    println("====5===="+rdd1.map(x => (x._2, x._1)).sortByKey(ascending = false).collect().mkString(","))
    println("====6===="+rdd2.collect().mkString(","))
    rdd2.saveAsTextFile(args(1))
    sc.stop()
  }
}