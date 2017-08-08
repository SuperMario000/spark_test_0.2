package main.scala.class1.ml.regression

import breeze.linalg.sum
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by lsc on 2017/8/4.
  */

object decision_tree_test1 {

  def main(args: Array[String]) {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)

    val conf=new SparkConf().setAppName("regression").setMaster("local[1]")
    val sc=new SparkContext(conf)

    val records=sc.textFile("file:///D:/Workspaces/IdeaProjects/SCALA_PROJECTS/data/spark_test_0.1/regression/hour_noheader.csv").map(_.split(",")).cache()

    val mappings=for(i<-Range(2,10))yield get_mapping(records,i)

    val cat_len=sum(mappings.map(_.size))
    val num_len=records.first().slice(10,14).size
    val total_len=cat_len+num_len
    //decision tree data
    val data=records.map{record=>
      val features=record.slice(2,14).map(_.toDouble)
      val label=record(record.size-1).toDouble
      LabeledPoint(label,Vectors.dense(features))

    }.randomSplit(Array(0.7,0.3),11L)

    val categoricalFeaturesInfo = Map[Int, Int]()
    val tree_model=DecisionTree.trainRegressor(data(0),categoricalFeaturesInfo,"variance",5,32)
    //    val linear_model=LinearRegressionWithSGD.train(data,10,0.5)
    val true_vs_predicted=data(1).map(p=>(p.label,tree_model.predict(p.features)))
    //println( true_vs_predicted.take(5).toVector.toString())
    // MSE是均方误差，是用作最小二乘回归的损失函数，表示所有样本预测值和实际值平方差的平均值
    // RMSE是MSE的平方根
    val MSE=true_vs_predicted.map(value=>
    {
      (value._1-value._2)*(value._1-value._2)
    }).mean()
    // 平均绝对误差（MAE）：预测值与实际值的绝对值差的平均值
    val MAE=true_vs_predicted.map(value=>
    {
      math.abs(value._1-value._2)
    }).mean()
    // 均方根对数误差（RMSLE）：预测值和目标值进行对数变换后的RMSE.
    val RMSLE=true_vs_predicted.map(value=>
    {
      math.pow(math.log(value._1+1)-math.log(value._2+1),2)
    }).mean()

    println("-------MSE:"+MSE)
    println("-------MAE:"+MAE)
    println("-------RMSLE:"+RMSLE)
  }

  def get_mapping(rdd:RDD[Array[String]], idx:Int)=
  {
    rdd.map(filed=>filed(idx)).distinct().zipWithIndex().collectAsMap()
  }

}