;;
;; Implements one-way TDD conformant heuristic algorithm.
;;
;; version : $Id$
;; author  : Hongbing Kou
;;

(require* EpisodeTDDConformance)

(provide OneWayTDDHeuristicAlgorithm)

;;
;; An episode is TDD conformant if it is a primitive test-first episode.
;;
(defrule TDD-Rule-1
   (declare (salience -100))
   ?simpleTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "test-first")) (isTDD ?class&:(eq ?class "False")))   
  =>
   (retract ?simpleTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation (str-cat "Episode '" ?cat "' is automatically TDD conformant."))
         )
   )         
)

;;
;; A refactoring episode is TDD conformant if it follows another TDD episode.
;;
(defrule TDD-Rule-2
   (declare (salience -50))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?refactorTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "refactoring")) (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?refactorTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation (str-cat "Episode '" ?cat "' follows a TDD conformant episode."))
        )
    )
)

;; A test-addition episode is TDD conformant if it follows another TDD episode.
(defrule TDD-Rule-3
   (declare (salience -50))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?test-additionTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "test-addition")) (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?test-additionTDD)
   (assert 
       (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation (str-cat "Episode '" ?cat "' follows a TDD conformant episode."))
        )
   )
)

;; A regression episode is TDD conformant if it follows another TDD episode.
(defrule TDD-Rule-4
   (declare (salience -50))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?regressionTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "regression")) (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?regressionTDD)
   (assert 
       (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation (str-cat "Episode '" ?cat "' follows a TDD conformant episode."))
       ) 
   )
)

;; A type 1 and 2 of production is TDD conformant if it follows another TDD episode.
(defrule TDD-Rule-5
   (declare (salience -50))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?prodTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "production")) 
                         (subtype ?st&:(or (eq ?st "1") (eq ?st "2")))
                         (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?prodTDD )
   (assert 
       (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation (str-cat "Episode '" ?cat "' follows a TDD conformant episode."))
        )
   )
)