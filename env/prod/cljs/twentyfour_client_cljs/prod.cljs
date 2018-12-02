(ns twentyfour-client-cljs.prod
  (:require
    [twentyfour-client-cljs.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
