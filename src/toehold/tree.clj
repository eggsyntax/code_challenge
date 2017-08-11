(ns toehold.tree
  (:require [clojure.zip :as z]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as string]
            [toehold.core :as c :refer :all]))


(defrecord node [content children])

;; Contentful-node zipper largely borrowed from
;; http://grokbase.com/p/gg/clojure/12ag6cjnch/how-to-represent-trees-for-use-with-zippers
(defn content-zipper
  "vector-zip and seq-zip assume that branch nodes don't have content. This version is like vector-zip, but assumes all nodes can have content."
  [root]
  (z/zipper (comp coll? :children)
            :children
            (fn [nd children]
              (assoc nd :children children))
            root))

(defn content [loc] (:content (first loc)))

(defn node-str
  "Return the attractively formatted contents of a node"
  [loc]
  (when-let [node (z/node loc)]
    (str "> " (:content node))))

(defn print-tree [loc]
  (when-not (z/end? loc)
    (do (when (z/branch? loc)
          (pprint (str (string/join "" (repeat (count (z/path loc)) " "))
                       (node-str loc))))
        (recur (z/next loc)))))

(defn node-path "Return the simple path of nodes up to and including this location, including the location"
  [loc]
  (conj (z/path loc) (z/node loc)))

;; CHALLENGE 2: Write a function to build a tree of all possible games. Explain
;; why or why not it uses content-zipper (above).

;; CHALLENGE 3: Is it possible to rewrite build-tree so that it's significantly
;; more efficient in time and/or space? If so, what strategies do you see for
;; that? Implement one.

;; CHALLENGE 4: write code to answer some of the following questions:
;; 1. What percentage of 100000 random games have no win?
;; 2. Given a partial game in a particular state, which player if any has
;;   a guaranteed win if they play optimally?
;; 3. Under what conditions is player 2 (O) guaranteed a win?
;; 4. Can X get a win if they blow 1st move?
