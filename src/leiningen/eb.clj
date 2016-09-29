(ns leiningen.eb
  (:require [leiningen.eb.beanstalk :as beanstalk])
  (:use [leiningen.help :only [help-for]]))

; significant inspiration taken from https://github.com/weavejester/lein-beanstalk

(defn info
  "Prints details about this projects deployment."
  ([project]
   (println (beanstalk/get-applications project))
   (println "info" (:name project)))

  ([project env-name]
   (println "info" (:name project) env-name)))

(defn eb
  "Manage AWS Elastic Beanstalk service."
  {:help-argslist '([info])
   :subtasks [#'info]}

  ([project]
   (println (help-for "eb")))

  ([project subtask & args]
   (case subtask
     "info"    (apply info project args)
     (println (help-for "eb")))))
