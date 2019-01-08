(ns todo-list.core
  (:require
   [cljs.loader :as loader]
   [reagent.core :as r]))

(def settings {:title "Todo List"})


;; Define state with some initial values
(def app-state
  (r/atom
   {:count 2
    :todolist
    [{:id 0 :text "Start learning mindcontrol" :finished true}
     {:id 1 :text "Read a book 'Debugging JS in IE11 without pain'" :finished false}
     {:id 2 :text "Become invisible for a while" :finished false}]}))


(def todo-text (r/atom ""))


;; Update todolist with provided function and args
(defn update-todolist [f & args]
  (apply swap! app-state update-in [:todolist] f args))


(defn add-todo [todo]
  (update-todolist conj todo))


(defn toggle-todo [todo]
  (swap! app-state update-in [:todolist (:id todo) :finished] not))


(defn next-id []
  (swap! app-state update-in [:count] inc))


(defn handle-add-event []
  (do
    (if (not (empty? @todo-text))
      (add-todo (hash-map :id (:count (next-id)) :text @todo-text)))
    (reset! todo-text "")))


;; Components
(defn page-header [title]
  [:header
   [:h1 title]])


(defn new-item []
  [:div
   [:input {:type "text"
            :placeholder "Type your task"
            :value @todo-text
            :on-change #(reset! todo-text (-> % .-target .-value))
            :on-key-press (fn [e]
                            (if (= 13 (.-charCode e))
                              (handle-add-event)))}]
   [:button {:on-click handle-add-event} "Add"]])


(defn item [todo]
  [:div {:key (:id todo)}
   [:span {:class "item-text"} (:text todo)]
   [:i {:class (str "ti-check " (if (:finished todo) "checked" "unchecked"))
        :on-click #(toggle-todo todo)}]])


(defn items-list []
  [:div
   (for [todo (:todolist @app-state)]
     (item todo))])


(defn todo-app []
  [:div.wrapper
   [page-header (get settings :title)]
   [new-item]
   [:hr]
   [items-list]])


(defn mount-root []
  (r/render [todo-app] (.getElementById js/document "app")))


(defn init! []
  (mount-root))