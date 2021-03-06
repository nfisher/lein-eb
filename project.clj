(let [version (or "0.0.0-SNAPSHOT" (System/getenv "VERSION"))]
  (defproject lein-eb version
    :description "Leiningen plugin to manage Amazon's Elastic Beanstalk"
    :url "https://github.com/nfisher/lein-eb"
    :dependencies [[com.amazonaws/aws-java-sdk-elasticbeanstalk "1.11.160" :exclusions [commons-logging]]
                   [com.amazonaws/aws-java-sdk-s3 "1.11.160" :exclusions [commons-logging]]]
    :min-lein-version "2.0.0"
    :eval-in-leiningen true))
