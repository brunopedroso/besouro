;;
;; Defines a template to for TDDConformant evalaution
;;
;;

(provide EpisodeTDDConformance)

(deftemplate EpisodeTDDConformance
    "TDD Conformance template of episode"
    (slot index) ; Index of an episode
    (slot category) ; Episode category classified by classification rules.
    (slot subtype (default "1")) ; Episode category subtype
    (slot isTDD (default "False")) ; A toggle variable to tell whether an episode is TDD or not.
    (slot explanation (default "")) ;; Explanation of why it is classified as TDD or non-TDD.
)

;
; Query the tdd conformance of an episode
(defquery episode-tdd-conformance-query-by-index
  (declare (variables ?idx))  
  (EpisodeTDDConformance (index ?idx) (isTDD ?isTDD) (explanation ?exp))
)

;;
;; Implements a unit test schema to test conformance of an episode.
;;
(deffunction test-tdd-conformance (?idx ?expected)
    (bind ?result (run-query* episode-tdd-conformance-query-by-index ?idx))
    (if (not (?result next)) then 
     	(printout t "????? TDD conformance evaluation failed" crlf)
        (return FALSE)
     )
    
    (bind ?isTDD (?result getString isTDD))
    (bind ?exp (?result getString exp))
    
    (if (not (eq ?expected ?isTDD)) then 
        (printout t "????? Episode " ?idx ": expected TDD conformance result is '" ?expected 
                                          "' but was '" ?isTDD "'" crlf)
        (return ?exp)
     )
        
    (printout t "***** Episode " ?idx ": TDD conformance is '" ?isTDD  "'" crlf)
    (return ?exp)
)