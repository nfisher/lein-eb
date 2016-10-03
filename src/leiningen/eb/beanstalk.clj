(ns leiningen.eb.beanstalk
  (:import 
    com.amazonaws.auth.EnvironmentVariableCredentialsProvider
    com.amazonaws.regions.Regions
    com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient
    com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentsRequest
    com.amazonaws.services.elasticbeanstalk.model.DescribeApplicationVersionsRequest
    ))

(defn- region [project]
  """
  region returns the default region to use from the standard AWS_DEFAULT_REGION env var.
  """
  (let [aws-region (System/getenv "AWS_DEFAULT_REGION")]
    (Regions/fromName aws-region)))

(defn- beanstalk-client [project]
  """
  beanstalk-client builds the client connector for an AWS beanstalk end-point.
  """
  (doto (AWSElasticBeanstalkClient. (EnvironmentVariableCredentialsProvider.))
    (.withRegion (region project))))

(defn- env-request [project]
  (doto (DescribeEnvironmentsRequest.)
    (.setApplicationName (:name project))))

(defn- ver-request [project]
  (doto (DescribeApplicationVersionsRequest.)
    (.setApplicationName (:name project))))

(defn get-environments [project]
  (-> (beanstalk-client project)
      (.describeEnvironments (env-request project))
      .getEnvironments))

(defn get-applications [project]
  (->> (beanstalk-client project)
       .describeApplications
       .getApplications))

(defn get-application [project]
  (first
    (->> (get-applications project)
         (filter #(= (.getApplicationName %) (:name project))))))

(defn get-versions [project]
  (-> (beanstalk-client project)
      (.describeApplicationVersions (ver-request project))
      (.getApplicationVersions)))
