;;
;; Defines episode categorization and query.
;;
;; @author  : Hongbing Kou
;; @version : $Id$

;; 
;; Episode classification
;; 
(provide Episode)

(deftemplate episode
    (slot category)
    (slot type)
    (slot explanation (default ""))
)

;
; Query episode classification and its explanation
(defquery episode-classification-query
  (episode (category ?cat) (type ?tp) (explanation ?exp))
)

