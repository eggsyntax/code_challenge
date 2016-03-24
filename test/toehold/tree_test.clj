(ns toehold.tree-test
  (:require [toehold.tree :as ttree :refer :all]
            [toehold.core :as c :refer :all]
            [clojure.test :as t :refer :all]
            [clojure.zip :as z])
  (:import [toehold.tree node]))

(defn v->nodes "Build a simple tree of nodes from a nested vector"
  [vectr]
  (node. (first vectr) (mapv v->nodes (rest vectr))))

(def x1 [17 [3] [14 [23] [11 [8]]]])
(def nodes (v->nodes x1))
(def z1 (content-zipper nodes))

(print-tree z1)
(comment
  "> 17"
  " > 3"
  " > 14"
  "  > 23"
  "  > 11"
  "   > 8"
  )

;; Handy zipper shortcuts for quicker testing:
(def z-l z/left)
(def z-r z/right)
(def z-d z/down)
(def con content)

(deftest basic-zipper-navigation-test
  (are [x y] [= x y]
    3 (-> z1 z-d con)
    8 (-> z1 z-d z-r z-d z-r z-d con)))

(def content-map #(map :content %))

(deftest node-path-test
  (are [x y] [= x y]
    '(17 14 11 8) (-> z1 z-d z-r z-d z-r z-d node-path content-map)))
