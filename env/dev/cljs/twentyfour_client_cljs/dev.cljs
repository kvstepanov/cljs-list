(ns ^:figwheel-no-load todo-list.dev
  (:require
   [todo-list.core :as core]
   [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
