(let [version (or "0.0.0-SNAPSHOT" (System/getenv "VERSION"))]
  (defproject lein-eb version
    :description "Leiningen plugin to manage Amazon's Elastic Beanstalk"
    :url "https://github.com/nfisher/lein-eb"
    :dependencies [[com.amazonaws/aws-java-sdk-elasticbeanstalk "1.11.38" :exclusions [commons-logging]]
                   [com.fasterxml.jackson.core/jackson-annotations "2.6.0"]
                   [com.fasterxml.jackson.core/jackson-core "2.6.6"]
                   [com.fasterxml.jackson.core/jackson-databind "2.6.6"]]
    :min-lein-version "2.0.0"
    :eval-in-leiningen true))
