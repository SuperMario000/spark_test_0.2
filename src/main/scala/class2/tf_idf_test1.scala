package main.scala.class2

import org.apache.spark.{SparkContext,SparkConf}
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.SparseVector

/**
  * Created by lsc on 2017/7/24.
  */

object tf_idf_test1 {
  val stopwords = Set(
    "the","a","an","of","or","in","for","by","on","but", "is", "not", "with", "as", "was", "if",
    "they", "are", "this", "and", "it", "have", "from", "at", "my", "be", "that", "to","what","which"
  )

  val regexNum="[^0-9]*".r

  def tokenize(content:String):Seq[String]={
    content.split("\\W+")
      .map(_.toLowerCase)
      .filter(regexNum.pattern.matcher(_).matches) //filter the word included number
      .filterNot(stopwords.contains) //filter the stopped words
      .filter(_.length>2) //filter the word that it's length less than 2
      .toSeq
  }


  def main(args: Array[String]){
    val conf = new SparkConf().setAppName("naiveBayes").setMaster("local")
    val sc = new SparkContext(conf)
    val path="file:///D:/Workspaces/IdeaProjects/SCALA_PROJECTS/data/spark_test_0.1/tf_idf/20News_groups_small/*"
    val rdd=sc.wholeTextFiles(path)
    val titls=rdd.map(_._1)
    val documents= rdd.map(_._2).map(tokenize)
    val hashingTF = new HashingTF()
    val mapWords=documents.flatMap(x=>x).map(w=>(hashingTF.indexOf(w),w)).collect.toMap
    val tf=hashingTF.transform(documents)
    val bcWords=tf.context.broadcast(mapWords)
    tf.cache()
    val idf = new IDF(2).fit(tf)
    val tfidf: RDD[linalg.Vector] = idf.transform(tf)
    val r = tfidf.map{
      case SparseVector(size, indices, values)=>
        val words=indices.map(index=>bcWords.value.getOrElse(index,"null"))
        words.zip(values).sortBy(-_._2).take(20).toSeq
    }
    titls.zip(r).saveAsTextFile("file:///D:/Workspaces/IdeaProjects/SCALA_PROJECTS/data/spark_test_0.1/tf_idf/20News_groups_small_result_"+System.currentTimeMillis)
  }
}
