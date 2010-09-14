;; Insert actions and test episode classifier algorithm.

;; Requires the dependant rules.
(require JessActionTemplates)
(require Episode)
(require TestPassEpisodeClassifier)

;;
;; Implements unit-test function to test the results.
;;
(deffunction test-classifier (?name ?type)
    (bind ?result (run-query* episode-classification-query))
    (if (not (?result next)) then 
     	(printout t "????? Episode cannot be classified" crlf)
        (return FALSE)
     )

    (bind ?rname (?result getString cat))
    (bind ?rtype (?result getString tp))
    (bind ?rexp (?result getString exp))
    (if (not (eq ?name ?rname)) then 
        (printout t "????? Expected category name " ?name " but was " ?rname crlf)
        (return FALSE)
     )
        
    (if (not (eq ?type ?rtype)) then 
        (printout t "????? Expected category type " ?type " but was " ?rtype crlf)
        (return FALSE)
            
     )
    
    (printout t "***** Episode " ?name " type " ?type " test passed" crlf)
    (return ?rexp)
)

;; Test episode test-first type 1.
(deffacts test-first-1-episode
    "Assert test-driven type 1 actions into working memory"
    (UnaryRefactorAction (index 1) (file TestTriangle.java) 
                         (operation "ADD") (type "METHOD") 
                         (data "void testEquilateral()"))    
    (UnitTestEditAction (index 2) (file TestTriangle.java) (duration 123))   
    (CompilationAction  (index 3) (file TestTriangle.java) ;;
                        (message "unknown type"))
    (ProductionEditAction (index 4) (file Triangle.java) (duration 200))
    (UnitTestAction       (index 5) (file TestTriangle.java) (errmsg "Failed to run test"))
    (ProductionEditAction (index 6) (file Triangle.java) (duration 100))
    (UnitTestAction       (index 7) (file TestTriangle.java))    
)

(reset)
(run)
(printout t (test-classifier "test-first" "1") crlf crlf)
(undeffacts test-first-1-episode)

;; Test episode test-first type 2.
(deffacts test-first-2-episode
    "Assert test-driven type 2 actions into working memory"
    (UnaryRefactorAction (index 1) (file TestTriangle.java) 
                         (operation "ADD") (type "METHOD") 
                         (data "void testEquilateral()"))    
    (UnitTestEditAction (index 2) (file TestTriangle.java) (duration 123))   
    (CompilationAction  (index 3) (file TestTriangle.java) ;;
                        (message "unknown type"))
    (ProductionEditAction (index 4) (file Triangle.java) (duration 200))
    (UnitTestAction       (index 5) (file TestTriangle.java))    
)

(reset)
(run)
(printout t (test-classifier "test-first" "2") crlf crlf)
(undeffacts test-first-2-episode)

;;
;; Test episode test-first type 3 with test-failure but compilation error.
;;
(deffacts test-first-3-episode
    "Assert test-driven type 3 actions into working memory"
    (UnaryRefactorAction (index 1) (file TestTriangle.java) 
                         (operation "ADD") (type "METHOD") 
                         (data "void testEquilateral()"))    
    (UnitTestEditAction (index 2) (file TestTriangle.java) (duration 123))   
    (ProductionEditAction (index 3) (file Triangle.java) (duration 200))
    (UnitTestAction       (index 4) (file TestTriangle.java) (errmsg "Failed to run test"))
    (ProductionEditAction (index 5) (file Triangle.java) (duration 100))
    (UnitTestAction       (index 6) (file TestTriangle.java))    
)

(reset)
(run)
(printout t (test-classifier "test-first" "3") crlf crlf)
(undeffacts test-first-3-episode)

;;
;; Test episode test-first type 4 without test-failure and compilation error.
;;
(deffacts test-first-4-episode
    "Assert test-driven type 4 actions into working memory"
    (UnaryRefactorAction (index 1) (file TestTriangle.java) 
                         (operation "ADD") (type "METHOD") 
                         (data "void testEquilateral()"))    
    (UnitTestEditAction (index 2) (file TestTriangle.java) (duration 123))   
    (ProductionEditAction (index 3) (file Triangle.java) (duration 200))
    (ProductionEditAction (index 4) (file Triangle.java) (duration 100))
    (UnitTestAction       (index 5) (file TestTriangle.java))    
)

(reset)
(run)
(printout t (test-classifier "test-first" "4") crlf crlf)
(undeffacts test-first-4-episode)

(deffacts refactoring-1-episodeA
   "Assert refactoring type 1 actions into working memory"   
   (UnitTestEditAction (index 1) (file TestTriangle.java) (duration 300))    
   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg "1 is not equal to 2"))        
   (UnitTestEditAction (index 3) (file TestTriangle.java) (duration 200))    
   (UnitTestAction (index 4) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "refactoring" "1A") crlf crlf)
(undeffacts refactoring-1-episodeA)

(deffacts refactoring-1-episodeB
   "Assert refactoring type 1 actions into working memory"   
   (UnaryRefactorAction (index 1) (file TestTriangle.java) (operation REMOVE) (type METHOD) (data "void delete"))
   (UnitTestAction (index 2) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "refactoring" "1B") crlf crlf)
(undeffacts refactoring-1-episodeB)

(deffacts refactoring-2-episodeA
   "Assert refactoring type 2 actions into working memory"   
   (ProductionEditAction (index 1) (file Triangle.java) (duration 200))    
   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
   (ProductionEditAction (index 3) (file Triangle.java) (duration 200))    
   (UnitTestAction (index 4) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "refactoring" "2A") crlf crlf)
(undeffacts refactoring-2-episodeA)

(deffacts refactoring-2-episodeB
   "Assert refactoring type 2 actions into working memory"   
   (UnaryRefactorAction  (index 1) (file Triangle.java) (operation ADD) (type METHOD) (data "void create()"))
   (UnitTestAction       (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))         
   (BinaryRefactorAction (index 3) (file Triangle.java) (operation RENAME) (type METHOD) (from "void create()") (to "void create(int)"))
   (UnitTestAction       (index 4) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "refactoring" "2B") crlf crlf)
(undeffacts refactoring-2-episodeB)


(deffacts refactoring-3-episodeA
   "Assert Validation type 3 actions into working memory"   
   (ProductionEditAction (index 1) (file Triangle.java) (duration 200))    
   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
   (UnitTestEditAction (index 3) (file TestTriangle.java) (duration 200))    
   (UnitTestAction (index 4) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "refactoring" "3") crlf crlf)
(undeffacts refactoring-3-episodeA)

(deffacts refactoring-3-episodeB
   "Assert refactoring type 3 actions into working memory"   
   (UnaryRefactorAction  (index 1) (file Triangle.java) (operation ADD) (type METHOD) (data "void create()"))
   (ProductionEditAction (index 2) (file Triangle.java) (duration 200))    
   (UnitTestAction       (index 3) (file TestTriangle.java) (errmsg  "Fix the test"))        
   (UnitTestEditAction   (index 4) (file TestTriangle.java) (duration 200))    
   (UnitTestAction       (index 5) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "refactoring" "3") crlf crlf)
(undeffacts refactoring-3-episodeB)

(deffacts Test-Last-1-episode
   "Assert Test-last type 1 actions into working memory"   
   (ProductionEditAction (index 1) (file Triangle.java) (duration 200))    
   (UnitTestEditAction   (index 2) (file TestTriangle.java) (duration 200) (assertionChange 3))     
   (UnitTestAction       (index 3) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "test-last" "1") crlf crlf)
(undeffacts Test-Last-1-episode)

(deffacts Test-Last-2-episode
   "Assert Test-last type 2 actions into working memory"   
   (ProductionEditAction (index 1) (file Triangle.java) (duration 200))    
   (UnitTestEditAction   (index 2) (file TestTriangle.java) (duration 200) (assertionChange 3))
   (UnitTestAction       (index 3) (file TestTriangle.java) (errmsg "1 is not equal to 2"))     
   (UnitTestEditAction   (index 4) (file TestTriangle.java) (duration 200))
   (UnitTestAction       (index 5) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "test-last" "1") crlf crlf)
(undeffacts Test-Last-2-episode)

(deffacts Test-Test-Addition-1-episode
   "Assert test-addition actions into working memory"   
   (UnitTestEditAction   (index 1) (file TestTriangle.java) (duration 200) (assertionChange 3))     
   (UnitTestAction       (index 2) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "test-addition" "1") crlf crlf)
(undeffacts Test-Test-Addition-1-episode)

(deffacts Test-Test-Addition-2-episode
   "Assert test-addition actions into working memory"   
   (UnitTestEditAction   (index 1) (file TestTriangle.java) (duration 200) (assertionChange 3))     
   (UnitTestAction       (index 2) (file TestTriangle.java) (errmsg "1 is not equal to 2"))     
   (UnitTestEditAction   (index 3) (file TestTriangle.java) (duration 400))       
   (UnitTestAction       (index 4) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "test-addition" "2") crlf crlf)
(undeffacts Test-Test-Addition-2-episode)


(deffacts Regression-1-episode
   "Assert Validation type 1 actions into working memory"
   (UnitTestAction       (index 1) (file TestRate.java))        
   (UnitTestAction       (index 2) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "regression" "1") crlf crlf)
(undeffacts Regression-1-episode)

(deffacts Regression-2-episode
   "Assert regression type 2 actions into working memory"   
   (CompilationAction    (index 1) (file TestTriangle.java) (message "Class ZorroStream not found"))        
   (UnitTestAction       (index 2) (file TestTriangle.java))
)
(reset)
(run)
(printout t (test-classifier "regression" "2") crlf crlf)
(undeffacts Regression-2-episode)

;; Production type 1 without method increase
(deffacts production-1-episode
   "Assert production type 1 actions into working memory" 
   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 0) (statementChange 14) (byteChange 210) (duration 200))    
   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
   (UnitTestAction (index 3) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "production" "1") crlf crlf)
(undeffacts production-1-episode)

;; Production type 2 with method increase but byte size decrease
(deffacts production-2-episode
   "Assert production type 2 actions into working memory"   
   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 2) (statementChange 2) (byteChange -10) (duration 200))    
   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
   (UnitTestAction (index 3) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "production" "2") crlf crlf)
(undeffacts production-2-episode)

;; Production type 2 with method increase but statement decrease
(deffacts production-2-episodeB
   "Assert production type 3 actions into working memory"   
   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 2) (statementChange 2) (byteChange 8) (duration 200))    
   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
   (UnitTestAction (index 3) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "production" "2") crlf crlf)
(undeffacts production-2-episodeB)

;; Production type 3 with method increase, and size increase and byte increase
(deffacts production-3-episode
   "Assert production type 3 actions into working memory"   
   (ProductionEditAction (index 1) (file Triangle.java) (methodChange 2) (statementChange 5) (byteChange 125) (duration 200))    
   (UnitTestAction (index 2) (file TestTriangle.java) (errmsg  "Fix the test"))        
   (UnitTestAction (index 3) (file TestTriangle.java))        
)
(reset)
(run)
(printout t (test-classifier "production" "3") crlf crlf)
(undeffacts production-3-episode)

;(reset)
;(run)
;(facts)

(deffacts test-first-1-episode-real
   "Test a episode in work."   
   (BufferTransAction (index 1) (leavingFile Frame.java))
   (UnitTestEditAction (index 2) (file TestFrame.java) (duration 0)  
                       (testChange 0) (assertionChange 1)) 
   (UnitTestAction  (index 3) (file TestFrame.java) (errmsg "Failed to run test"))              
   (BufferTransAction (index 4) (leavingFile TestFrame.java))
   (ProductionEditAction (index 5) (file Frame.java) (methodChange 0) (statementChange 0) (byteChange 33) (duration 0))    
   (BufferTransAction (index 6) (leavingFile Frame.java))
   (UnaryRefactorAction (index 7) (file TestFrame.java) 
                         (operation "ADD") (type "METHOD") (data "void testGame()"))    
   (UnitTestEditAction (index 8) (file TestFrame.java) (duration 34)  
                       (testChange 1) (assertionChange 0) (byteChange 131)) 
   (CompilationAction  (index 9) (file TestFrame.java) ;;
                        (message "BowlingGame cannot be resolved to a type"))
   (UnitTestEditAction (index 10) (file TestFrame.java) (duration 0)  
                       (testChange 0) (assertionChange 0) (byteChange 4)) 
   (UnaryRefactorAction (index 11) (file BowlingGame.java) 
                         (operation "ADD") (type "CLASS") (data "BowlingGame.java"))    
   (BufferTransAction (index 12) (leavingFile TestFrame.java)) 
   (ProductionEditAction (index 13) (file BowlingGame.java) (methodChange 0) (statementChange 0) (byteChange 167) (duration 9))    
   (UnaryRefactorAction (index 14) (file BowlingGame.java) 
                         (operation "ADD") (type "METHOD") (data "BowlingGame()"))    
   (ProductionEditAction (index 15) (file BowlingGame.java) (methodChange 1) (statementChange 0) (byteChange 82) (duration 23))    
   (BufferTransAction (index 16) (leavingFile BowlingGame.java)) 
   (UnitTestAction (index 17) (file TestFrame.java)) 
)

(reset)
(run)
(printout t (test-classifier "test-first" "1") crlf crlf)
(undeffacts test-first-1-episode-real)