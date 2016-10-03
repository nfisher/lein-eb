(ns leiningen.eb.s3
  (:require [clojure.java.io :as io])
  (:import 
    com.amazonaws.auth.DefaultAWSCredentialsProviderChain
    com.amazonaws.services.s3.transfer.TransferManager
    java.math.BigInteger
    java.security.MessageDigest))

(defn- hex [b]
  ; force sha hex to a positive integer.
  (format "%x" (BigInteger. 1 b)))

(defn- sha256sum [f]
  (with-open [is (io/input-stream (io/file f))]
    (let [size (.available is)
          buf (byte-array size)
          ; TODO: (NF 2016-10-03) This works but need to handle partial reads.
          n (.read is buf)]
      (-> (MessageDigest/getInstance "SHA-256")
          (.digest buf)
          hex))))

(defn gen-key [f]
  (str (sha256sum f) "-" (.getName f)))

(defn upload [bucket k f]
  (-> (TransferManager. (DefaultAWSCredentialsProviderChain.))
      (.upload bucket k f)
      (.waitForCompletion)))
