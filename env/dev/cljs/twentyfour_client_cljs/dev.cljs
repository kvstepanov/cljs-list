(ns ^:figwheel-no-load twentyfour-client-cljs.dev
  (:require
    [twentyfour-client-cljs.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
