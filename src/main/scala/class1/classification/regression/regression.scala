package main.scala.class1.classification.regression

import breeze.linalg.sum
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by lsc on 2017/8/2.
  * 线性回归
  * http://blog.csdn.net/u010824591/article/details/50374904#comments
  */
object regression {

  def main(args: Array[String]) {

    val conf=new SparkConf().setAppName("regression").setMaster("local[1]")
    val sc=new SparkContext(conf)

    val records=sc.textFile("file:///D:/soft-install/workspaces/ideaprojects/sparkprojects/data/spark_test_0.1/class2/regression/hour_noheader.csv").map(_.split(",")).cache()

    // Range(2,10) = Range(2, 3, 4, 5, 6, 7, 8, 9)
    val mappings=for(i<-Range(2,10))yield get_mapping(records,i)
    println("=======test2======"+get_mapping(records,2).mkString(","))
    println("=======mappings======"+mappings.mkString(","))
    val cat_len=sum(mappings.map(_.size))
    val num_len=records.first().slice(10,14).size

    //*****************************tmd*test*************************************
    val cat_vec1=Array.ofDim[Double](57)
    println("=======cat_vec1======"+cat_vec1.mkString(","))
    val record = records.first()
    println("=======record======"+record.mkString(","))
    var i=0
    var step=0
    for(filed<-record.slice(2,10))
    {

      val m=mappings(i)
      val idx=m(filed)
      cat_vec1(idx.toInt+step)=1.0
      i=i+1
      step=step+m.size
    }
    val num_vec1=record.slice(10,14).map(x=>x.toDouble)
    println("=======cat_vec1======"+cat_vec1.mkString(","))
    println("=======num_vec1======"+num_vec1.mkString(","))
    val features1=cat_vec1++num_vec1
    println("=======features1======"+features1.mkString(","))
    val label1=record(record.size-1).toInt
    println("=======label1======"+label1)
    //*****************************tmd*test*************************************


    val total_len=cat_len+num_len
    println("--------cat_len="+cat_len+"--------num_len="+num_len+"--------total_len="+total_len)
    //linear regression data 此部分代码最重要，主要用于产生训练数据集，按照前文所述处理类别特征和实数特征。
    val data=records.map{record=>

      val cat_vec=Array.ofDim[Double](cat_len)
      var i=0
      var step=0
      for(filed<-record.slice(2,10))
      {

        val m=mappings(i)
        val idx=m(filed)
        cat_vec(idx.toInt+step)=1.0
        i=i+1
        step=step+m.size
      }
      val num_vec=record.slice(10,14).map(x=>x.toDouble)

      val features=cat_vec++num_vec
      val label=record(record.size-1).toInt


      LabeledPoint(label,Vectors.dense(features))
    }

    // val categoricalFeaturesInfo = Map[Int, Int]()
    //val linear_model=DecisionTree.trainRegressor(data,categoricalFeaturesInfo,"variance",5,32)
    val linear_model=LinearRegressionWithSGD.train(data,10,0.5)
    val true_vs_predicted=data.map(p=>(p.label,linear_model.predict(p.features)))
    //输出前五个真实值与预测值
    println( true_vs_predicted.take(5).toVector.toString())
  }

  def get_mapping(rdd:RDD[Array[String]], idx:Int)=
  {
    rdd.map(filed=>filed(idx)).distinct().zipWithIndex().collectAsMap()
  }

}
