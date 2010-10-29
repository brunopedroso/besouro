;;
;; Implements two-way TDD conformant heuristic algorithm.
;;
;; version : $Id$
;; authors  : Hongbing Kou and Bruno Pedroso
;;

(require* EpisodeTDDConformance)

(provide BesouroTDDConformanceAlgorithm)

;;
;; An episode is TDD conformant if it is a primitive test-first episode.
;;
(defrule TDD-Rule-1
   (declare (salience 100))
   ?simpleTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "test-first")) (isTDD ?class&:(eq ?class "False")))   
  =>
   (retract ?simpleTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "Tests were written before production code.")
        )
    )
)

;;
;; Refactorings are TDD Conformant.
;;
(defrule TDD-Rule-2
   (declare (salience 100))
   ?simpleTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "refactoring")) (isTDD ?class&:(eq ?class "False")))   
  =>
   (retract ?simpleTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "Changed design without changing behaviour.")
        )
    )
)

;;
;; TestAdditions are TDD Conformant.
;;
(defrule TDD-Rule-3
   (declare (salience 100))
   ?simpleTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "test-addition")) (isTDD ?class&:(eq ?class "False")))   
  =>
   (retract ?simpleTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "Added a test for an already solved case.")
        )
    )
)

;;
;; Regressions are TDD Conformant.
;;
(defrule TDD-Rule-4
   (declare (salience 100))
   ?simpleTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "regression")) (isTDD ?class&:(eq ?class "False")))   
  =>
   (retract ?simpleTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "Runing tests.")
        )
    )
)



;;
;; Test-last episode is not TDD conformant.
;;
(defrule NonTDD-Rule-1
   (declare (salience 20))
   ?simpleNonTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "test-last")) (explanation ?exp&:(eq ?exp "")))
  =>
   (retract ?simpleNonTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "False")
            (explanation "Tests were added after production code.")
        )
    )
)

;;
;; Production episode is not TDD conformant.
;;
(defrule NonTDD-Rule-2
   (declare (salience 20))
   ?simpleNonTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "production")) (explanation ?exp&:(eq ?exp "")))
  =>
   (retract ?simpleNonTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "False")
            (explanation "Tests were added after production code.")
        )
    )
)


;;
;; Unknown episode is not TDD conformant.
;;
(defrule NonTDD-Rule-3
   (declare (salience 20))
   ?simpleNonTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "unknown")) (explanation ?exp&:(eq ?exp "")))   
  =>
   (retract ?simpleNonTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "False") 
            (explanation "Development pattern can not be understood.")
        )
    )
)
