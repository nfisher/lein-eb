(ns leiningen.eb.beanstalk
  (:import 
    com.amazonaws.auth.EnvironmentVariableCredentialsProvider
    com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClientBuilder))

(defn- region [project]
  """
  region returns the default region to use from the standard AWS_DEFAULT_REGION env var.
  """
  (let [aws-region (System/getenv "AWS_DEFAULT_REGION")]
    aws-region))

(defn- beanstalk-client [project]
  """
  beanstalk-client builds the client connector for an AWS beanstalk end-point.
  """
  (-> (AWSElasticBeanstalkClientBuilder/standard)
      (.withCredentials (EnvironmentVariableCredentialsProvider.))
      (.withRegion (region project))
      (.build)))

(defn get-applications [project]
  (->> (beanstalk-client project)
       .describeApplications
       .getApplications))
