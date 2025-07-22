(ns render
  (:require [scicloj.clay.v2.api :as clay]))


(clay/make! {:render true
             :source-path "notebooks/index.clj"
             :format [:quarto :html]
             :run-quarto false
             :base-target-path "site"
             :hide-code true})

