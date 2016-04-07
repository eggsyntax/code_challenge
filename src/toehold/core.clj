(ns toehold.core
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.arithmetic :as la]
            [clojure.pprint :refer [pprint]]
            [toehold.utils :refer [inspect]])
  (:gen-class))

(def players #{:x :o})

;; We represent a board-state as a 3x3 matrix of :x, :o, or :_ (blank).
;; nil for blank would be slightly easier to deal with, but would decrease
;; readability on print
(def empty-board [[:_ :_ :_] [:_ :_ :_] [:_ :_ :_]])

;; Moves are a sequence of [x y p], where player is either :x or :o, and
;; x, y indicates the column and row of the move.

(defn occupied? [board x y ]
  (not= ((board x) y) :_))

;; CHALLENGE 1: Implement this function. See toehold.core-test/available-moves-test
(defn available-moves [board]
  "Return all empty positions as [x y]"
  ;; TODO note that project is unrunnable until this function is implemented
  )

(defn move [board [x y val]]
  (if (occupied? board x y)
    (throw (Exception. (str "Can't move to occupied space: " board x y val)))
    (assoc board x (assoc (board x) y val))))

(defn after [mvs board] (reduce move board mvs))

(defn board-from [mvs] (after mvs empty-board))

(defn cur-player  [moves] ([:x :o] (mod (count moves) 2)))
(defn last-player [moves] ([:o :x] (mod (count moves) 2)))

(defn- rand-player [] (rand-nth players))

(defn- rand-valid-move [moves & [player]]
  (let [board (board-from moves) ; memoize?
        avl-moves (available-moves board)]
    ;; (println "moves:" moves "(" (cur-player moves) ")")
    ;; (println "board:" (board-from moves))
    (assert (seq avl-moves) ; Make sure board's not full
            (str "No valid moves left on " board))
    (println "available moves:" avl-moves)
    (conj moves
          (conj (rand-nth avl-moves)
                (or player (cur-player moves))))))

(defn full? [moves]
  (>= (count moves) 9))

(defn cols [b]
  (for [col-i (range 3)]
    (mapv #(nth % col-i) b)))

;; Trivial, but handy to have the matching call
(defn rows [b] b)

(defn call
  "Given a seq containing a fn and some args, apply the fn to the args"
  [& args]
  (apply (first args) (rest args)))

(defn diags [b]
  "(mapv call b (range 3)) returns [((b 0) 0), ((b 1) 1), ((b 2) 2)]. Then
we do the same thing but for [2 1 0]."
  (vector (mapv call b (range 3))
          (mapv call b (reverse (range 3)))))

(defn triplets [b]
  "Return all triplets of b that could qualify as a win"
  (concat (rows b) (cols b) (diags b)))

(defn- check-triplet [triplet]
  (let [one (players (first triplet))] ;call players to restrict to :x :o
    (when (every? #(= % one) triplet)
      one)))

(defn win?
  "Given a list of moves, return the winning player (or nil if none)"
  [moves]
  (first (keep check-triplet (triplets (board-from moves)))))

(defn full-or-win? [moves]
  (or (full? moves) (win? moves)))

(defn rand-game
  "Return a game consisting of a sequence of valid moves ending in a win
  or a full board"
  []
  (first (drop-while (comp not full-or-win?)
                     (iterate rand-valid-move []))))

(defn -main
  "Minimal usage example"
  [& args]
  (let [game (rand-game)]
    (println "moves: " game)
    (println "board at end of random game: " (board-from game))
    (println "winner: " (win? game))
    (println "final move: " (last game))))
