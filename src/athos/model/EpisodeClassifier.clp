;;
;; Classifies Test-Driven Development, Refactoring and Test-Last Development style development
;; with actions in one episode which with with test-pass.
;;
;; @author  : Hongbing Kou
;; @version : $Id: TestPassEpisodeClassifier.clp 281 2005-11-10 22:25:19Z hongbing $

;;
;; Test creation -> Compilation error -> Code -> Test Fail -> code -> Test Pass iteration
;;

;; test-first:
;;  1   Complete test-first iteration
;;  2   Test-first iteration without test failure
;;  3   Test-first iteration without compilation error
;;  4   Test-first without compilation and failing test.
;; 
;; refactoring:
;;  1A   Refactoring on test code only
;;  1B   Refactoring on test code only
;;  2A   Refactoring on production code only 
;;  2B   Refactoring on production code only 
;;  4   Refactoring on production code or test code
;;
;; test-last:
;;  1   Production code and test code are edited in sequence, and there is no test failure.
;;  2   Production code and test code are edited in sequence, and there is test failure. 
;;
;; test-addition
;;  1   No production code, add a new test and it passes at the end.
;;  2   No production code, and a new test, it fails then passes
;;
;; regression:
;;  1   There are some code tweaking but no concrete work.
;;  2   Test environment configuration problem solving.
;;
;; production:
;;  1   Without method and statement increases, or there are some small code addition 
;;  2   With method and statement increase, but file size change is not significant 
;;  3   With method increase, and file byte change is significant.

;; Requires the template definition.
(require* JessActionTemplates)
(require* Episode)

(provide TestPassEpisodeClassifier)

;; If there is new test class or method is created.
(defrule Test-Method-Creation
    "Test class or method creation check."
    (UnaryRefactorAction (index ?i) (file ?f) (operation ?o&:(eq ?o "ADD")) (type ?t) (data ?d))
    (test (or (eq ?t "CLASS") (eq ?t "METHOD")))
    (UnitTestAction (index ?u) (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))
    (test (eq ?f ?ufile))    
    (test (< ?i ?u))
    =>
    (assert (there-is-test-creation ?i))
)

;; If there is new assertion statement(s) is added. 
(defrule Test-Statement-Creation
    "Test class or method creation check."
    (UnitTestEditAction (index ?i) (file ?testEditFile)
                        (assertionChange ?assertionChange&:(> ?assertionChange 0)))
    (UnitTestAction (index ?u) (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))
    (test (eq ?testEditFile ?ufile))    
    =>
    (assert (there-is-test-creation ?i))
)

;; There is work on production to make compilation pass.
(defrule TestCompilationError-then-Production
    "There is work on production work to make build success"
    (CompilationAction (index ?c) (file ?cfile))
    (ProductionEditAction (index ?p) (file ?pfile))
    (test (< ?c ?p))
    (test (not (eq ?cfile ?pfile)))
    (UnitTestAction (index ?u) (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))
    (test (eq ?cfile ?ufile))
    (test (< ?p ?u))
   =>
    (assert (test-compilation-error-then-production ?c ?p))
)

;; Failed unit test invocation and work around on production code afterward
;; which is a classical Test-Driven scenario.
(defrule Unittest-then-Production
    "There is failed unit test and editing work on production"
    (UnitTestAction (index ?u) (file ?ufile) (errmsg ?msg&:(not (eq ?msg nil))))
    (ProductionEditAction (index ?p) (file ?pfile))
    (test (< ?u ?p))
    (UnitTestAction (file ?ufile2) (errmsg ?msg2&:(eq ?msg2 nil)))
    (test (eq ?ufile ?ufile2))
   =>
    (assert (unittest-then-production ?u ?p))
)

;; Type 1 test-first iteration
(defrule Test-Driven-Classifier-Type1
    (declare (salience -100))
    (there-is-test-creation ?a)
    (test-compilation-error-then-production ?b ?c)
    (unittest-then-production ?d ?e)
    (test (and (< ?a ?b) (< ?a ?d)))
    =>
    (assert 
        (episode 
            (category test-first) 
            (type 1)
            (explanation (str-cat ?a "," ?b "," ?c "," ?d "," ?e))    
        )
    )
)

;; Type 2 test-first iteration
(defrule Test-Driven-Classifier-Type2
    (declare (salience -100))
    (there-is-test-creation ?a)
    (test-compilation-error-then-production ?b ?c)
    (not (exists (unittest-then-production ?d ?e)))
    (test (< ?a ?b))
    =>
    (assert (episode 
               (category test-first) 
               (type 2)
               (explanation (str-cat ?a "," ?b "," ?c))    
             )
     )
)

;; Type 3 test-first iteration
(defrule Test-Driven-Classifier-Type3
    (declare (salience -100))
    (there-is-test-creation ?a)
    (not (test-compilation-error-then-production ?b ?c))
    (unittest-then-production ?d ?e)
    (test (< ?a ?d))
    =>
    (assert (episode 
                (category test-first) 
                (type 3)
                (explanation (str-cat ?a "," ?d "," ?e))    
             )
    )
)

;; Type 4 test-first iteration
(defrule Test-Driven-Classifier-Type4
    (declare (salience -100))
    (there-is-test-creation ?a)
    (ProductionEditAction (index ?p))
    (not (test-compilation-error-then-production ?b ?c))
    (not (unittest-then-production ?d ?e))
    (test (< ?a ?p))
    =>
    (assert (episode 
               (category test-first) 
               (type 4)
               (explanation (str-cat ?a "," ?p))    
             )
     )
)

;;
;; Refactoring on test code with substantial edit
;;
(defrule Refactoring-1-Classifier-KindA
    (declare (salience -100))
    (not (there-is-test-creation ?i))
    (UnitTestEditAction (index ?e) (file ?file1))
    (not (ProductionEditAction))
    (UnitTestAction (index ?u) (file ?file2) (errmsg ?msg&:(eq ?msg nil)))
    (test (eq ?file1 ?file2))
    (test (< ?e ?u))
   =>
    (assert (episode 
              (category refactoring) 
              (type 1A)
              (explanation (str-cat ?e "," ?file1))
             )
     )
)

;;
;; Refactoring on test code without substantial edit but unary/binary refactoring
;;
(defrule Refactoring-1-Classifier-KindB
    (declare (salience -100))
    (not (there-is-test-creation ?i))
    (not (UnitTestEditAction))
    (not (ProductionEditAction))
    (or 
        (UnaryRefactorAction (index ?r) (file ?file1))
        (BinaryRefactorAction (index ?r) (file ?file1))
     )
    (UnitTestAction (index ?u) (file ?file2) (errmsg ?msg&:(eq ?msg nil)))
    (test (eq ?file1 ?file2))
    (test (< ?r ?u))
   =>
    (assert (episode 
                (category refactoring) 
                (type 1B)
                (explanation (str-cat ?r "," ?file1))
             )
     )
)

;;
;; Refactoring on production code with edit.
;; 
(defrule Refactoring-2-Classifier-KindA
    (declare (salience -100))
    (not (there-is-test-creation ?i))
    (or 
        (ProductionEditAction (index ?t) (file ?pfile) (methodChange ?mc&:(< ?mc 0)) (statementChange ?sc) (file ?pfile))
        (ProductionEditAction (index ?t) (file ?pfile) (methodChange ?mc) (statementChange ?sc&:(<= ?sc 0)) (file ?pfile))
    )
    (not (UnitTestEditAction))
    (UnitTestAction (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))    
   =>
    (assert (episode 
              (category refactoring) 
              (type 2A)
              (explanation ?pfile)
             )
     )
)

;;
;; Refactoring on production code without substantial edit.
;; 
(defrule Refactoring-2-Classifier-KindB
    (declare (salience -100))
    (not (there-is-test-creation ?i))
    (not (ProductionEditAction))
    (not (UnitTestEditAction))
    (or 
        (UnaryRefactorAction (index ?p) (file ?pfile))
        (BinaryRefactorAction (index ?p) (file ?pfile))
    )
    (UnitTestAction (index ?u) (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))    
    (test (< ?p ?u))
   =>
    (assert (episode 
               (category refactoring) 
               (type 2B)
               (explanation (str-cat ?p "," ?pfile))
            )
    )
)

;;
;; Production refactoring and test code refactoring (misc).
;;
(defrule Refactoring-3-Classifier
    (declare (salience -100))
    (not (there-is-test-creation ?i))
    (or 
        (ProductionEditAction (index ?p) (file ?pfile))
        (UnaryRefactorAction  (index ?p) (file ?pfile))
        (BinaryRefactorAction (index ?p) (file ?pfile))    
    )
    (or 
        (UnitTestEditAction   (index ?u) (file ?ufile))
        (UnaryRefactorAction  (index ?u) (file ?ufile))
        (BinaryRefactorAction (index ?u) (file ?ufile))            
    )
    
    (UnitTestAction (file ?tfile) (errmsg ?msg&:(eq ?msg nil)))    
    (test (eq ?ufile ?tfile))
   =>
    (assert (episode 
              (category refactoring) 
              (type 3)
              (explanation (str-cat ?p "," ?pfile "," ?u "," ?ufile)) 
             )
     )
)

;;
;; Test-last is the test creation after substantial production edit.
;;
(defrule Test-Last-1-Classifier
   (declare (salience -100))
   (ProductionEditAction (index ?p) (file ?pfile))
   (there-is-test-creation ?a)
   (test (< ?p ?a))
   (UnitTestAction (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))
  =>
   (assert (episode 
               (category test-last) 
               (type 1)
               (explanation (str-cat ?a "," ?p)) 
            )
    )
)

;;
;; Test-addition if that there is no production but test addition that passes without failing.
;;
(defrule Test-Addition-1-Classifier
   (declare (salience -100))
   (not (ProductionEditAction))
   (there-is-test-creation ?a)
   (UnitTestAction (index ?u) (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))
   (test (< ?a ?u))
  =>
   (assert (episode 
              (category test-addition) 
              (type 1)
              (explanation ?a)    
            )
   )
)

;;
;; Test-addition if that there is no production but test addition that fails first.
;;
(defrule Test-Addition-2-Classifier
   (declare (salience -100))
   (not (ProductionEditAction))
   (there-is-test-creation ?a)
   (UnitTestAction (index ?u1) (file ?ufile1) (errmsg ?msg1&:(not (eq ?msg1 nil))))
   (UnitTestAction (index ?u2) (file ?ufile2) (errmsg ?msg2&:(eq ?msg2 nil)))
   (test (and (< ?a ?u1) (< ?u1 ?u2) (eq ?ufile1 ?ufile2)))
  =>
   (assert (episode 
              (category test-addition) 
              (type 2)
              (explanation (str-cat ?a "," ?u1))    
            )
    )
)

;;
;; Regression test without fail first
;;
(defrule Regression-Classifier-1
   (declare (salience -100))
   (not (there-is-test-creation ?a))
   (not (ProductionEditAction))
   (not (UnitTestEditAction))
   (not (UnitTestAction (errmsg ?msg1&:(not (eq ?msg1 nil)))))
   (UnitTestAction (errmsg ?msg2&:(eq ?msg2 nil)))
  =>
   (assert (episode 
              (category regression) 
              (type 1)
              (explanation "")
            )
    )
)

;;
;; Regression test with failing tests or compilation error first.
;;
(defrule Regression-Classifier-2
   (declare (salience -100))
   (not (there-is-test-creation ?a))
   (not (ProductionEditAction))
   (not (UnitTestEditAction))
   (or 
        (UnitTestAction (errmsg ?msg1&:(not (eq ?msg1 nil))))
        (CompilationAction (message ?msg2&:(not (eq ?msg2 nil))))
    )
   (UnitTestAction (errmsg ?msg3&:(eq ?msg3 nil)))
  =>
   (assert (episode 
              (category regression) 
              (type 2)
              (explanation "")
            )
   )
)

;;
;; Production code edit without method increase but statement change, likely to be
;; a refactoring but we classify it as production type 1.
;; 
(defrule Production-1-Classifier
    (declare (salience -100))
    (not (there-is-test-creation))
    (ProductionEditAction (index ?t) (file ?pfile) (methodChange ?mc&:(= ?mc 0)) (statementChange ?sc&:(> ?sc 0)))
    (not (UnitTestEditAction))
    (UnitTestAction (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))    
   =>
    (assert (episode 
                (category production) 
                (type 1)
                (explanation  ?pfile)
            )
     )
)

;;
;; Production code edit with method increase but statement decrease or file size decrease
;; 
(defrule Production-2-Classifier
    (declare (salience -100))
    (not (there-is-test-creation ?i))
    (ProductionEditAction (index ?t) (methodChange ?mc&:(> ?mc 0)) (byteChange ?bc&:(<= ?bc 100)) (file ?pfile))
    (not (UnitTestEditAction))
    (UnitTestAction (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))    
   =>
    (assert (episode 
                (category production) 
                (type 2)
                (explanation ?pfile)
            )
    )
)

;;
;; Production code edit with method increase, and statement increase, and file size increase
;; 
(defrule Production-3-Classifier
    (declare (salience -100))
    (not (there-is-test-creation ?i))
    (ProductionEditAction (index ?t) (file ?pfile) (methodChange ?mc&:(> ?mc 0)) (byteChange ?bc&:(> ?bc 100)))
    (not (UnitTestEditAction))
    (UnitTestAction (file ?ufile) (errmsg ?msg&:(eq ?msg nil)))    
   =>
    (assert (episode 
                (category production) 
                (type 3)
                (explanation ?pfile)
             )
    )
)

;(reset)
;(run)
;(facts)