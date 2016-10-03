(ns leiningen.eb.beanstalk
  (:require [clojure.java.io :as io])
  (:import 
    com.amazonaws.auth.DefaultAWSCredentialsProviderChain
    com.amazonaws.regions.Regions
    com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient
    com.amazonaws.services.elasticbeanstalk.model.CreateApplicationVersionRequest
    com.amazonaws.services.elasticbeanstalk.model.DescribeEnvironmentsRequest
    com.amazonaws.services.elasticbeanstalk.model.DescribeApplicationVersionsRequest
    com.amazonaws.services.elasticbeanstalk.model.S3Location
    ))

(defn- region [project]
  """
  region returns the default region to use from the standard AWS_DEFAULT_REGION env var.
  """
  (let [aws-region (or (System/getenv "AWS_DEFAULT_REGION") "us-east-1")]
    (Regions/fromName aws-region)))

(defn- beanstalk-client [project]
  """
  beanstalk-client builds the client connector for an AWS beanstalk end-point.
  """
  (doto (AWSElasticBeanstalkClient. (DefaultAWSCredentialsProviderChain.))
    (.withRegion (region project))))

(defn- env-request [project]
  (doto (DescribeEnvironmentsRequest.)
    (.setApplicationName (:name project))))

(defn- ver-request [project]
  (doto (DescribeApplicationVersionsRequest.)
    (.setApplicationName (:name project))))

(defn- source-bundle [b k]
  (S3Location. b k))

(defn- create-ver-request [project label bucket s3key]
  (doto (CreateApplicationVersionRequest. (:name project) label)
    (.setSourceBundle (source-bundle bucket s3key))))

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

(defn get-bucket [project]
  (->> (first (get-versions project))
      .getSourceBundle
      .getS3Bucket))

(defn set-label [project label bucket s3key]
  (-> (beanstalk-client project)
      (.createApplicationVersion (create-ver-request project label bucket s3key))))
