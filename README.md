# spark_test_0.2
1、分类和聚类的区别：

Classification (分类)，对于一个classifier，通常需要你告诉它“这个东西被分为某某类”这样一些例子，
理想情况下，一个 classifier 会从它得到的训练集中进行“学习”，从而具备对未知数据进行分类的能力，
这种提供训练数据的过程通常叫做supervised learning (监督学习)，

Clustering (聚类)，简单地说就是把相似的东西分到一组，
聚类的时候，我们并不关心某一类是什么，我们需要实现的目标只是把相似的东西聚到一起。
因此，一个聚类算法通常只需要知道如何计算相似度就可以开始工作了，
因此 clustering 通常并不需要使用训练数据进行学习，这在Machine Learning中被称作unsupervised learning (无监督学习).

2、常见的分类与聚类算法

所谓分类，简单来说，就是根据文本的特征或属性，划分到已有的类别中。
如在自然语言处理NLP中，我们经常提到的文本分类便就是一个分类问题，一般的模式分类方法都可用于文本分类研究。
常用的分类算法包括：
    决策树分类法，
    朴素贝叶斯分类算法(native Bayesian classifier)、
    基于支持向量机(SVM)的分类器，
    神经网络法，
    k-最近邻法(k-nearestneighbor，kNN)，
    模糊分类法
等等。

分类作为一种监督学习方法，要求必须事先明确知道各个类别的信息，并且断言所有待分类项都有一个类别与之对应。
但是很多时候上述条件得不到满足，尤其是在处理海量数据的时候，
如果通过预处理使得数据满足分类算法的要求，则代价非常大，这时候可以考虑使用聚类算法。

常用的聚类算法包括:
    K均值(K-mensclustering)聚类(最典型)
    属于划分法K中心点（K-MEDOIDS）算法、CLARANS算法；
    属于层次法的BIRCH算法、CURE算法、CHAMELEON算法等；
    基于密度的方法：DBSCAN算法、OPTICS算法、DENCLUE算法等；
    基于网格的方法：STING算法、CLIQUE算法、WAVE-CLUSTER算法；基于模型的方法)。
    
3.其他
各种机器学习的应用场景分别是什么
https://www.zhihu.com/question/26726794/answer/33813531
从决策树学习谈到贝叶斯分类算法、EM、HMM
http://blog.csdn.net/tarim/article/details/41345341