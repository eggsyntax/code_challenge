(ns toehold.core-test
  (:require [clojure.test :refer :all]
            [toehold.core :refer :all]))

;; Partial game example
(def x1 [[1 1 :x] [1 2 :o] [2 2 :x] [2 0 :o]])
(def b1 (board-from x1))

;; Completed game example (no win)
(def x2 [[0 2 :x] [0 0 :o] [0 1 :x] [2 1 :o] [1 1 :x]
         [1 2 :o] [2 2 :x] [2 0 :o] [1 0 :x]])
(def b2 (board-from x2))

;; Won game example
(def x3 [[2 0 :x] [2 1 :o] [0 2 :x] [1 2 :o] [1 1 :x]])
(def b3 (board-from x3))

(deftest available-moves-test
  (testing "Available moves"
    (are [x y] [= x y]
      #{[0 0] [0 1] [0 2] [1 0] [2 1]} (set (available-moves b1))
      #{} (set (available-moves b2))
      #{[0 0] [0 1] [1 0] [2 2]} (set (available-moves b3))
      )))

(deftest full-test
  (are [x y] (= x y)
    false (full? x1)
    true  (full? x2)))

(deftest win-test
  (are [x y] (= x y)
    nil (win? x1)
    nil (win? x2)
    :x (win? x3)))
