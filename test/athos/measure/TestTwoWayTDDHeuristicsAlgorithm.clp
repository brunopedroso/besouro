;;
;; Tests two-way TDD conformant heuristic algorithm.
;;
;; version : $Id$
;; author  : Hongbing Kou
;;

(require* TwoWayTDDHeuristicAlgorithm)
(require* EpisodeTDDConformance)

;; Pattern 1
(deffacts test-pattern-1
  (EpisodeTDDConformance (index 1) (category "test-first"))
  (EpisodeTDDConformance (index 2) (category "test-first"))
  (EpisodeTDDConformance (index 3) (category "test-first"))
  (EpisodeTDDConformance (index 4) (category "test-first"))
  (EpisodeTDDConformance (index 5) (category "test-first"))  
  (EpisodeTDDConformance (index 6) (category "test-first"))  
)

(reset)
(run)

(printout t crlf "%%% Testing pattern 1 " crlf)
(printout t (test-tdd-conformance 1 "True") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "True") crlf crlf)
(printout t (test-tdd-conformance 5 "True") crlf crlf)
(printout t (test-tdd-conformance 6 "True") crlf crlf)
(undeffacts test-pattern-1)

;; Pattern 2
(deffacts test-pattern-2
  (EpisodeTDDConformance (index 1) (category "test-first"))
  (EpisodeTDDConformance (index 2) (category "test-first"))
  (EpisodeTDDConformance (index 3) (category "test-first"))
  (EpisodeTDDConformance (index 4) (category "test-last"))
  (EpisodeTDDConformance (index 5) (category "test-last"))  
  (EpisodeTDDConformance (index 6) (category "test-first"))  
)

(reset)
(run)

(printout t crlf "%%% Testing pattern 2 " crlf)
(printout t (test-tdd-conformance 1 "True") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "False") crlf crlf)
(printout t (test-tdd-conformance 5 "False") crlf crlf)
(printout t (test-tdd-conformance 6 "True") crlf crlf)
(undeffacts test-pattern-2)


;; Pattern 3
(deffacts test-pattern-3
  (EpisodeTDDConformance (index 1) (category "test-first"))
  (EpisodeTDDConformance (index 2) (category "refactoring"))
  (EpisodeTDDConformance (index 3) (category "refactoring"))
  (EpisodeTDDConformance (index 4) (category "test-last"))
  (EpisodeTDDConformance (index 5) (category "test-last"))  
  (EpisodeTDDConformance (index 6) (category "refactoring"))  
)

(reset)
(run)

(printout t crlf "%%% Testing pattern 3 " crlf)
(printout t (test-tdd-conformance 1 "True") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "False") crlf crlf)
(printout t (test-tdd-conformance 5 "False") crlf crlf)
(printout t (test-tdd-conformance 6 "False") crlf crlf)
(undeffacts test-pattern-3)


;; Pattern 4
(deffacts test-pattern-4
  (EpisodeTDDConformance (index 1) (category "test-first"))
  (EpisodeTDDConformance (index 2) (category "test-addition"))
  (EpisodeTDDConformance (index 3) (category "test-addition"))
  (EpisodeTDDConformance (index 4) (category "refactoring"))
  (EpisodeTDDConformance (index 5) (category "test-last"))
  (EpisodeTDDConformance (index 6) (category "test-last"))  
  (EpisodeTDDConformance (index 7) (category "refactoring"))
  (EpisodeTDDConformance (index 8) (category "refactoring"))      
)

(reset)
(run)

(printout t crlf "%%% Testing pattern 4 " crlf)
(printout t (test-tdd-conformance 1 "True") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "True") crlf crlf)
(printout t (test-tdd-conformance 5 "False") crlf crlf)
(printout t (test-tdd-conformance 6 "False") crlf crlf)
(printout t (test-tdd-conformance 7 "False") crlf crlf)
(printout t (test-tdd-conformance 8 "False") crlf crlf)
(undeffacts test-pattern-4)

;; Pattern 5
(deffacts test-pattern-5
  (EpisodeTDDConformance (index 1) (category "test-first"))
  (EpisodeTDDConformance (index 2) (category "refactoring"))
  (EpisodeTDDConformance (index 3) (category "refactoring"))
  (EpisodeTDDConformance (index 4) (category "refactoring"))
  (EpisodeTDDConformance (index 5) (category "test-last"))
  (EpisodeTDDConformance (index 6) (category "refactoring"))  
  (EpisodeTDDConformance (index 7) (category "refactoring"))
  (EpisodeTDDConformance (index 8) (category "test-first"))      
  (EpisodeTDDConformance (index 9) (category "refactoring"))      
  (EpisodeTDDConformance (index 10) (category "refactoring"))      
)

(reset)
(run)

(printout t crlf "%%% Testing pattern 5 " crlf)
(printout t (test-tdd-conformance 1 "True") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "True") crlf crlf)
(printout t (test-tdd-conformance 5 "False") crlf crlf)
(printout t (test-tdd-conformance 6 "True") crlf crlf)
(printout t (test-tdd-conformance 7 "True") crlf crlf)
(printout t (test-tdd-conformance 8 "True") crlf crlf)
(printout t (test-tdd-conformance 9 "True") crlf crlf)
(printout t (test-tdd-conformance 10 "True") crlf crlf)
(undeffacts test-pattern-5)

;; Pattern 6
(deffacts test-pattern-6
  (EpisodeTDDConformance (index 1) (category "test-first"))
  (EpisodeTDDConformance (index 2) (category "test-addition"))
  (EpisodeTDDConformance (index 3) (category "test-addition"))
  (EpisodeTDDConformance (index 4) (category "test-addition"))
  (EpisodeTDDConformance (index 5) (category "test-last"))
  (EpisodeTDDConformance (index 6) (category "test-addition"))  
  (EpisodeTDDConformance (index 7) (category "test-addition"))
  (EpisodeTDDConformance (index 8) (category "test-first"))      
  (EpisodeTDDConformance (index 9) (category "test-addition"))      
  (EpisodeTDDConformance (index 10) (category "test-addition"))      
)

(reset)
(run)

(printout t crlf "%%% Testing pattern 6 " crlf)
(printout t (test-tdd-conformance 1 "True") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "True") crlf crlf)
(printout t (test-tdd-conformance 5 "False") crlf crlf)
(printout t (test-tdd-conformance 6 "True") crlf crlf)
(printout t (test-tdd-conformance 7 "True") crlf crlf)
(printout t (test-tdd-conformance 8 "True") crlf crlf)
(printout t (test-tdd-conformance 9 "True") crlf crlf)
(printout t (test-tdd-conformance 10 "True") crlf crlf)
(undeffacts test-pattern-6)

;; Pattern 7
(deffacts test-pattern-7
  (EpisodeTDDConformance (index 1) (category "test-first"))
  (EpisodeTDDConformance (index 2) (category "test-addition"))
  (EpisodeTDDConformance (index 3) (category "regression"))
  (EpisodeTDDConformance (index 4) (category "test-addition"))
  (EpisodeTDDConformance (index 5) (category "test-last"))
  (EpisodeTDDConformance (index 6) (category "test-addition"))  
  (EpisodeTDDConformance (index 7) (category "regression"))
  (EpisodeTDDConformance (index 8) (category "test-first"))      
  (EpisodeTDDConformance (index 9) (category "test-addition"))      
  (EpisodeTDDConformance (index 10) (category "test-last"))      
)

(reset)
(run)

(printout t crlf "%%% Testing pattern 7 " crlf)
(printout t (test-tdd-conformance 1 "True") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "True") crlf crlf)
(printout t (test-tdd-conformance 5 "False") crlf crlf)
(printout t (test-tdd-conformance 6 "True") crlf crlf)
(printout t (test-tdd-conformance 7 "True") crlf crlf)
(printout t (test-tdd-conformance 8 "True") crlf crlf)
(printout t (test-tdd-conformance 9 "True") crlf crlf)
(printout t (test-tdd-conformance 10 "False") crlf crlf)
(undeffacts test-pattern-7)


;; Pattern 8 includes production
(deffacts test-pattern-8
  (EpisodeTDDConformance (index 1) (category "test-last"))
  (EpisodeTDDConformance (index 2) (category "refactoring"))
  (EpisodeTDDConformance (index 3) (category "production") (subtype "1"))
  (EpisodeTDDConformance (index 4) (category "production") (subtype "2"))
  (EpisodeTDDConformance (index 5) (category "test-first"))  
  (EpisodeTDDConformance (index 6) (category "refactoring"))  
  (EpisodeTDDConformance (index 7) (category "production") (subtype "2"))  
  (EpisodeTDDConformance (index 8) (category "test-last"))  
  (EpisodeTDDConformance (index 9) (category "production") (subtype "1"))  
  (EpisodeTDDConformance (index 10) (category "test-last"))  
)
(reset)
(run)
(printout t crlf "%%% Testing pattern 8 " crlf)
(printout t (test-tdd-conformance 1 "False") crlf crlf)
(printout t (test-tdd-conformance 2 "True") crlf crlf)
(printout t (test-tdd-conformance 3 "True") crlf crlf)
(printout t (test-tdd-conformance 4 "True") crlf crlf)
(printout t (test-tdd-conformance 5 "True") crlf crlf)
(printout t (test-tdd-conformance 6 "True") crlf crlf)
(printout t (test-tdd-conformance 7 "True") crlf crlf)
(printout t (test-tdd-conformance 8 "False") crlf crlf)
(printout t (test-tdd-conformance 9 "False") crlf crlf)
(printout t (test-tdd-conformance 10 "False") crlf crlf)
(undeffacts test-pattern-8)
