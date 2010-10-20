;;
;; Defines JESS fact definition template for development actions.
;;
;; version : $Id: JessActionTemplates.clp 774 2005-12-31 04:23:52Z hongbing $
;; author  : Hongbing Kou
;;

(provide JessActionTemplates)

;; Action template.
(deftemplate Action
    "Common parts of all actions"
    (slot index) ; Order of the action in the microprocess
    (slot file)  ; Object of file that this action is associated with.
)

;; Edit action
(deftemplate EditAction extends Action
    "File edit action"
    (slot duration (default 0)) ; Total edit time.
    (slot byteChange (default 0)) ; Number of bytes change
)

;; Production code edit action
(deftemplate ProductionEditAction extends EditAction
   "Java production code edit action"
   (slot methodChange (default 0))
   (slot statementChange (default 0))
)

;; Unit test edit action
(deftemplate UnitTestEditAction extends EditAction
    "Unit test edit action"
    (slot testChange (default 0))
    (slot assertionChange (default 0))
)

;; Unit test code invocation
(deftemplate UnitTestAction extends Action
    "Unit test execution"
    (slot errmsg (default nil))
)

;; Defines compilation error action
(deftemplate CompilationAction extends Action
    "Compilation error"
    (slot message)
)

;; Unary refactor action
(deftemplate UnaryRefactorAction extends Action
    "Changes on java structures"
    (slot operation)  ; add or delete
    (slot type)       ; class, field or method
    (slot data)       ; class, field or method name. 
)

;; Binary refactor action
(deftemplate BinaryRefactorAction extends Action
    "Rename a class, field or method."
    (slot operation)  ; Move or rename
    (slot type)       ; class, field or method
    (slot from)       ; Change from
    (slot to)         ; Change to
)

;; Buffer transition action
(deftemplate BufferTransAction extends Action
    "Buffer transition"
    (slot leavingFile)
)



;;;;;;;;;;;;
;; do we need these ones to measure TDD?


; Debug action
;;(deftemplate DebugAction extends Action
;;    "Code debugging"
;;    (slot operation) ; Debug operation
;;    (slot location)  ; Debugged location
;;)

;; Document edit action
;;(deftemplate DocumentEditAction extends EditAction
;;   "Documentation edit action"
;;)
