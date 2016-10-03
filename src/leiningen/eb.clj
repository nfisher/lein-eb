(ns leiningen.eb
  (:require [leiningen.eb.beanstalk :as beanstalk])
  (:use [leiningen.help :only [help-for]]))

; significant inspiration taken from https://github.com/weavejester/lein-beanstalk

(defn info
  "Prints details about this projects deployment."
  ([project]
   (let [app (beanstalk/get-application project)
         envs (beanstalk/get-environments project)
         vers (beanstalk/get-versions project)]
     (println "Application Name:" (.getApplicationName app))
     (println "  Created       :" (.toString (.getDateCreated app)))
     (println "  Updated       :" (.toString (.getDateUpdated app)))
     (println "")
     (println "Environments    :")
     (doseq [env envs]
       (println "  "
                (.getEnvironmentName env)
                (.getVersionLabel env)))
     (println "Versions        :")
     (doseq [ver vers]
       (println (.getVersionLabel ver) (.getS3Bucket (.getSourceBundle ver))))))

  ([project env-name]
   (println "info" (:name project) env-name)))


(defn publish
  "Publish asset to the applications S3 bucket."
  ([project]
   (println (help-for "eb")))
  ([project file label]
   ; retrieve bucket name.
   ; hash file.
   ; upload to S3.
   ; publish S3Location as version.
   (println file label)))

(defn eb
  "Manage AWS Elastic Beanstalk service."
  {:help-argslist '([info publish])
   :subtasks [#'info #'publish]}

  ([project]
   (println (help-for "eb")))

  ([project subtask & args]
   (case subtask
     "info"    (apply info project args)
     "publish" (apply publish project args)
     (println (help-for "eb")))))
