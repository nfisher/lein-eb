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
    com.amazonaws.services.elasticbeanstalk.model.UpdateEnvironmentRequest
    ))

(defn- region [project]
  """
  region returns the default region to use from the standard AWS_DEFAULT_REGION env var.
  """
  (let [aws-region (or (System/getenv "AWS_DEFAULT_REGION") "eu-west-1")]
    (Regions/fromName aws-region)))

(def bc (atom nil))

(defn- beanstalk-client [project]
  """
  beanstalk-client builds the client connector for an AWS beanstalk end-point.
  """
  (let [c (AWSElasticBeanstalkClient. (DefaultAWSCredentialsProviderChain.))]
    (if (compare-and-set! bc nil c)
      (doto c
        (.withRegion (region project)))
      (deref bc))))

(defn- env-request [project]
  (doto (DescribeEnvironmentsRequest.)
    (.setApplicationName (:name project))))

(defn- ver-request [project]
  (doto (DescribeApplicationVersionsRequest.)
    (.setApplicationName (:name project))))

(defn- update-env-ver-request [project env version]
  (doto (UpdateEnvironmentRequest.)
    (.setApplicationName (:name project))
    (.setEnvironmentName env)
    (.setVersionLabel version)))

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

(defn update-env-version [project env version]
  (-> (beanstalk-client project)
      (.updateEnvironment (update-env-ver-request project env version))))
