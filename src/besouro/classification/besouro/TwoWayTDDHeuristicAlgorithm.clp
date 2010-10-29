;;
;; Implements two-way TDD conformant heuristic algorithm.
;;
;; version : $Id$
;; author  : Hongbing Kou
;;

(require* EpisodeTDDConformance)

(provide TwoWayTDDHeuristicAlgorithm)

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
;; A refactoring episode is TDD conformant if it follows another TDD episode.
;;
(defrule TDD-Rule-2
   (declare (salience 80))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?refactorTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "refactoring")) (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?refactorTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation "This portion appears to be about refactoring and follows a TDD-conformant episode.")
        )
    )
)

;; A test-addition episode is TDD conformant if it follows another TDD episode.
(defrule TDD-Rule-3
   (declare (salience 80))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?test-additionTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "test-addition")) (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?test-additionTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation "Only new tests were added, following a TDD-conformant episode.")
        )
    )
)

;; A regression episode is TDD conformant if it follows another TDD episode.
(defrule TDD-Rule-4
   (declare (salience 80))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?regressionTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "regression")) (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?regressionTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation "Only tests were run, following a TDD-conformant episode.")
        )
    )
)

;; A production subtype either 1 or 2 is TDD conformant if it follows another TDD episode.
(defrule TDD-Rule-5
   (declare (salience 80))
   (EpisodeTDDConformance (index ?i) (isTDD ?res&:(eq ?res "True")))   
   ?prodTDD <- (EpisodeTDDConformance (index ?j) (category ?cat&:(eq ?cat "production")) 
                        (subtype ?st&:(or (eq ?st "1") (eq ?st "2")))
                        (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?prodTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?j) (category ?cat) (isTDD "True")
            (explanation "This portion of development appears to be production, which is is likely to be a refactoring and it follows a TDD-conformant episode.")
        )
    )
)


;; A refactoring episode that precedes a TDD episode should also be a TDD episode 
;; although it tails a test-last episode.
(defrule TDD-Rule-6
   (declare (salience 50))
   ?refactoringTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "refactoring")) (isTDD ?class&:(eq ?class "False")))
   (EpisodeTDDConformance (index ?j) (isTDD ?res&:(eq ?res "True")))   
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?refactoringTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "This portion appears to be about refactoring and precedes a TDD-conformant episode.")
        )
    )
)

;; A test-addition episode that precedes a TDD episode should also be a TDD episode 
;; although it tails a tests-last episode.
(defrule TDD-Rule-7
   (declare (salience 50))
   ?test-additionTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "test-addition")) (isTDD ?class&:(eq ?class "False")))
   (EpisodeTDDConformance (index ?j) (isTDD ?res&:(eq ?res "True")))   
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?test-additionTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "Only new tests were added, preceding a TDD-conformant episode.")
        )
    )
)

;; A regression episode that precedes a TDD episode should also be a TDD episode 
;; although it tails a tests-last episode.
(defrule TDD-Rule-8
   (declare (salience 50))
   ?regressionTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "regression")) (isTDD ?class&:(eq ?class "False")))
   (EpisodeTDDConformance (index ?j) (isTDD ?res&:(eq ?res "True")))   
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?regressionTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "Only tests were run, preceding a TDD-conformant episode.")
        )
    )
)

;; A production episode subtype 1 or 2 that precedes a TDD episode should also be a TDD episode 
;; although it tails a tests-last episode.
(defrule TDD-Rule-9
   (declare (salience 50))
   ?prodTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "production")) 
                         (subtype ?st&:(or (eq ?st "1") (eq ?st "2")))
                         (isTDD ?class&:(eq ?class "False")))
   (EpisodeTDDConformance (index ?j) (isTDD ?res&:(eq ?res "True")))   
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?prodTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "True")
            (explanation "This portion of development appears to be production, which is is likely to be a refactoring and it precedes a TDD-conformant episode.")
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
;; Production type 3 is TDD conformant.
;;
(defrule NonTDD-Rule-2
   (declare (salience 20))
   ?simpleNonTDD <- (EpisodeTDDConformance (index ?i) (category ?cat&:(eq ?cat "production")) 
                                           (subtype ?type&:(or (eq ?type "3") (eq ?type "3M"))) 
                                           (explanation ?exp&:(eq ?exp "")))   
  =>
   (retract ?simpleNonTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (subtype ?type) (isTDD "False") 
            (explanation "Too much production code was added in this portion.")
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

;;
;; A refactoring, regression, test-addition and production episode is not TDD conformance if it
;; does not follow a TDD conformant episode.
;;
(defrule NonTDD-Rule-4
   (declare (salience 10))
   (EpisodeTDDConformance (index ?j) (isTDD ?class&:(eq ?class "False")))
   ?simpleNonTDD <- (EpisodeTDDConformance (index ?i) 
        (category ?cat&:(or (eq ?cat "refactoring") (eq ?cat "regression") (eq ?cat "test-addition") (eq ?cat "production")))  
        (isTDD ?class&:(eq ?class "False"))
        (explanation ?exp&:(eq ?exp "")))
   (test (= (+ ?j 1) ?i ))
  =>
   (retract ?simpleNonTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "False")
            (explanation (str-cat "This portion of development appears to be `" ?cat "'; however, it does not follow or precede a TDD-conformant episode."))
        )
    )
)

;;
;; A refactoring, regression, test-addition and production episode is not TDD conformance if it
;; does not precede a TDD conformant episode.
;;
(defrule NonTDD-Rule-5
   (declare (salience 5))
   ?simpleNonTDD <- (EpisodeTDDConformance (index ?i) 
        (category ?cat&:(or (eq ?cat "refactoring") (eq ?cat "regression") (eq ?cat "test-addition") (eq ?cat "production")))  
        (isTDD ?class&:(eq ?class "False")) (explanation ?exp&:(eq ?exp "")))
   (EpisodeTDDConformance (index ?j) (isTDD ?class&:(eq ?class "False")))
   (test (= (+ ?i 1) ?j))
  =>
   (retract ?simpleNonTDD)
   (assert 
        (EpisodeTDDConformance 
            (index ?i) (category ?cat) (isTDD "False")
            (explanation (str-cat "This portion of development appears to be `" ?cat "'; however, it does not precede or follow a TDD-conformant episode."))
        )
    )
)
